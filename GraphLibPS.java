package cs10;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GraphLibPS<V,E> {
	private String inputMovies;
	private String moviesActors;
	private String inputActors;
	private HashMap<String, Set<String>> movieActorsMap; //movies ---> actors
	private HashMap<String, Set<String>> actorMoviesMap; //actors ---> movies
	private Graph <String, Set<String>> AMgraph;

	public GraphLibPS(String movies, String moviesActors,String actors) {
		//might have to reconfigure if it is only allowed to take one string value
		this.inputMovies = movies;
		this.moviesActors = moviesActors;
		this.inputActors = actors;
		
		
	}
	
	//I need to create a graph of all points given the readin information from the tests.
	public void readin() throws IOException{
		BufferedReader movies = new BufferedReader(new FileReader(inputMovies)); // reads in movies
		HashMap<Integer, String> moviesMap = new HashMap<Integer, String>();
		
		String currentLine;
		while ((currentLine = movies.readLine()) !=null) { 
			String[] lineArray = currentLine.split("\\|");
			moviesMap.put(Integer.parseInt(lineArray[0]), lineArray[1]);//reads movie number a key, and movie title as value
			
		}
		
		movies.close();
		
		BufferedReader actors = new BufferedReader(new FileReader(inputActors)); // read in actors
		HashMap<Integer, String> actorsMap = new HashMap<Integer,String>();
		
		while ((currentLine = actors.readLine()) !=null) { 
			String[] lineArray = currentLine.split("\\|");
			actorsMap.put(Integer.parseInt(lineArray[0]), lineArray[1]);//reads actor number a key, and actor name as value
			
		}
		
		actors.close();
		
		BufferedReader movieActors = new BufferedReader(new FileReader(moviesActors));
		HashMap<String,Set<String>> movieActorMap = new HashMap<String, Set<String>>();//movie --->actors
		HashMap<String,Set<String>> actorMovieMap = new HashMap<String, Set<String>>();//actors ---->movies
		
		while ((currentLine = movieActors.readLine()) != null) {
			String[] lineArray = currentLine.split("\\|");
			
			String movieName = moviesMap.get(Integer.parseInt(lineArray[0]));//string movie name
			String actorName = actorsMap.get(Integer.parseInt(lineArray[1])); //string actor name
			
			if (movieActorMap.containsKey(movieName)) { // if the movie name is already in the list
				//System.out.println("movie is already in list");
				Set<String> actorList = movieActorMap.get(movieName); // get the old set of actors
				actorList.add(actorName); // add the new actor
				movieActorMap.put(movieName, actorList); // override the old data with the new set
			}
			if (!movieActorMap.containsKey(movieName)){
				//System.out.println("new movie");
				Set<String> actorList = new HashSet<String>(); // make a new actor list
				actorList.add(actorName); //add the first actor in
				movieActorMap.put(movieName, actorList);	//add the set in
			}
			if (actorMovieMap.containsKey(actorName)) {
				//System.out.println("actor is already in list");
				Set<String> movieList = actorMovieMap.get(actorName); // get the old set of movies that an actor has been in
				movieList.add(movieName); // add the new movies to the old set
				actorMovieMap.put(actorName, movieList); //update the movies associated with an actor
				
			}
			if (!actorMovieMap.containsKey(actorName)) {
				//System.out.println("new actor");
				Set<String> movieList = new HashSet<String>(); //make a new set of movies
				movieList.add(movieName); // add the movies name to the movies an actor has starred in
				actorMovieMap.put(actorName, movieList); // put the actor name as key, movielist as value
				
				}
			
		}
		actorMoviesMap = actorMovieMap;
		movieActorsMap = movieActorMap;
		
		
		movieActors.close();
	}
	public HashMap<String, Set<String>> getActorMovie(){
		return actorMoviesMap;
		
	}
	public HashMap<String, Set<String>> getMovieActor(){
		return movieActorsMap;
	}
	public Graph<String, Set<String>> getGraph(){
	return AMgraph;
	}
	public void makeGraph() throws IOException{
		//take one actor, go to its first movie. Find all actors in that movie and add a vertex
		//with a set containing that movie name. Go to next movie. Find all actors in that movie
		//if they are already an attached vertex, add the movie to the set attaching those two actors
		//otherwise, add it in as a vertex
		readin();
		Graph<String,Set<String>> AMsgraph = new AdjacencyMapGraph<String, Set<String>>();
		for (String actor1 : actorMoviesMap.keySet()){ // runs through all actors
			//System.out.println("in loop actor1:" + actor1);
			
			Set<String> allAppearances = actorMoviesMap.get(actor1);
			for (String movie : allAppearances) { // runs through all movies of an actor
				//System.out.println("looping appearances:" + movie);
				Set<String> coStarList = movieActorsMap.get(movie); // gets all actors that costarred in that movie
				for (String actor2 : coStarList) { // runs through all other actors in that movie
					//System.out.println("looping costars:" + actor2);
					
					if (actor1 != actor2) {
						
						//System.out.println("actor 1!= actor2");
						
						if (AMsgraph.hasVertex(actor1) && AMsgraph.hasVertex(actor2)) {
								
								if (AMsgraph.hasEdge(actor1, actor2)) {
									//System.out.println("add to edge");
									Set<String> movieConnection = AMsgraph.getLabel(actor1, actor2);
									movieConnection.add(movie);
									AMsgraph.insertUndirected(actor1, actor2, movieConnection);
								}
								else {
									Set<String> movieConnection = new HashSet<String>();
									//System.out.println("new edge");
									movieConnection.add(movie);
									AMsgraph.insertUndirected(actor1, actor2, movieConnection);
								}
								
						}
						else if (AMsgraph.hasVertex(actor1) && !AMsgraph.hasVertex(actor2)) {
							AMsgraph.insertVertex(actor2);
							Set<String> movieConnection = new HashSet<String>();
							//System.out.println("new edge");
							movieConnection.add(movie);
							AMsgraph.insertUndirected(actor1, actor2, movieConnection);
							
						}
						else if (!AMsgraph.hasVertex(actor1) && !AMsgraph.hasVertex(actor2)) {
							AMsgraph.insertVertex(actor1);
							AMsgraph.insertVertex(actor2);
							Set<String> movieConnection = new HashSet<String>();
							//System.out.println("new edge");
							movieConnection.add(movie);
							AMsgraph.insertUndirected(actor1, actor2, movieConnection);
						}
						
					}
				}
				
			}
			
		}
		AMgraph = AMsgraph;
	}
		
	
	public static void main(String[] args) {
		try {
		GraphLibPS<String,Set<String>> j = new GraphLibPS<String,Set<String>>("moviesTest.txt", "movie-actorsTest.txt", "actorsTest.txt");
		String s = "jeff";
		s.concat("hello");

		j.makeGraph();
		System.out.println(j.getGraph());
		System.out.println("first bfs\n");
		System.out.println(GraphAnalysisPS.bfs(j.getGraph(), "Kevin Bacon"));
		System.out.println("get path");
		System.out.println(GraphAnalysisPS.getPath(GraphAnalysisPS.bfs(j.getGraph(), "Kevin Bacon"), "Charlie"));
		System.out.println("missing vertices");
		System.out.println(GraphAnalysisPS.missingVertices(j.getGraph(), GraphAnalysisPS.bfs(j.getGraph(),"Kevin Bacon")));
		System.out.println("average sep");
		System.out.println(GraphAnalysisPS.averageSeparation(GraphAnalysisPS.bfs(j.getGraph(), "Kevin Bacon"), "Kevin Bacon"));
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}
