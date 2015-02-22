package clustering.feature.assignment;

import java.util.List;

import vectors.Vector;

public class PredefinedAssignment implements AssignmentStrategy {

	private List<Vector> centroids;
	
	public PredefinedAssignment (List<Vector> centroids) {
		this.centroids = centroids;
	}
	
	@Override
	public List<Vector> assignCentroids(List<Vector> vectors) {
		return this.centroids;
	}
	
	public String toString() {
		return "PredefinedAssignment";
	}
	
}
