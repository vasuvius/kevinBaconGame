package cs10;

import java.util.*;
import java.io.*;

public class KevinBaconGame {
	GraphLibPS<String,Set<String>> actorsMoviesGraph;
	Graph<String,Set<String>> BFSactorsMoviesGraph;
	String center;
	
	
	public KevinBaconGame(String movies, String actors, String moviesActors ) {
		actorsMoviesGraph = new GraphLibPS<String,Set<String>>(movies, actors, moviesActors);

	}
	public ArrayList<String> getGraphVertices(){
		ArrayList<String> allActors = new ArrayList<String>();
		for(String actor:actorsMoviesGraph.getGraph().vertices()) {
			allActors.add(actor);
		}
		return allActors;
	}
	public ArrayList<String> getsubgraphVertices(){
		ArrayList<String> allActors = new ArrayList<String>();
		for(String actor:BFSactorsMoviesGraph.vertices()) {
			allActors.add(actor);
		}
		return allActors;
	}
	public Graph<String,Set<String>> makeCenter(String actorName) throws IOException {
		center = actorName;
		actorsMoviesGraph.makeGraph();
		BFSactorsMoviesGraph = GraphAnalysisPS.bfs(actorsMoviesGraph.getGraph(), center);
		
		return BFSactorsMoviesGraph;
	}
	
	public ArrayList<String> getPath(String actorName) throws NullPointerException{
		return GraphAnalysisPS.getPath(BFSactorsMoviesGraph, actorName);
		
	}
	public Set<String> findMissingVertices() throws NullPointerException{
		return GraphAnalysisPS.missingVertices(actorsMoviesGraph.getGraph(), BFSactorsMoviesGraph);
	}
	public double avgSeparationFromCenter() {
		return GraphAnalysisPS.averageSeparation(BFSactorsMoviesGraph, center);
	}
	public static void main(String[] args) {
		KevinBaconGame game = new KevinBaconGame("movies.txt", "movie-actors.txt", "actors.txt");
		try{
		game.makeCenter("Kevin Bacon");
		System.out.println(game.getGraphVertices());
		System.out.println(game.getsubgraphVertices());
		System.out.println(game.getPath("Robert De Niro"));
		
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}
