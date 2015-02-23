package clustering.feature.assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vectors.Vector;

public class RandomAssignment implements AssignmentStrategy {

	private int numCentroids;
	
	public RandomAssignment(int amountOfCentroids) {
		this.numCentroids = amountOfCentroids;
	}
	
	@Override
	public List<Vector> assignCentroids(List<Vector> vectors) {
	
		if (numCentroids > vectors.size()) throw new IllegalStateException("Assigning more centroids than there are vectors.");
		
		// pick out some random vectors to use as centroids
		Set<Integer> indices = new HashSet<>();
		while (indices.size() < numCentroids) {
			int random = (int) (Math.random() * vectors.size());
			indices.add(random);
		}
		
		// return the centroids
		List<Vector> centroids = new ArrayList<>();
		for (Integer intt : indices) {
			centroids.add(vectors.get(intt));
		}
		return centroids;
	}

	public String toString() {
		return "RandomAssignment";
	}
	
}
