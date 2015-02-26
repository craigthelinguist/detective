package clustering.feature.aggregation;

import java.util.List;

import vectors.Vector;
import vectors.Vectors;

public class ClosestPair {

	public static Vector[] closestPair (List<Vector> vectors) {
		
		if (vectors.size() < 2) return null;
		int dimensions = vectors.get(0).dimensions();
		
		
		Vector[] vSet = new Vector[vectors.size()];
		vSet[0] = vectors.get(0); vSet[1] = vectors.get(1);
		double deltaX = Vectors.distance(vSet[0], vSet[1]);
		Vector[] pair = { vSet[0], vSet[1] };
		Grid grid = new Grid(vSet, deltaX, dimensions);
		
		for (int i = 2; i < vectors.size(); i++) {
			
			// construct set i
			Vector current = vectors.get(i);
			vSet[i] = current;
			
			// get points in the neighbourhood of current
			List<Vector> neighbours = grid.neighbourhood(current);
			
			// get smallest distance in that neighbourhood
			double min = -1;
			Vector[] minPair = new Vector[2];
			for (Vector vector : neighbours) {
				double d = Vectors.distance(vector, current);
				if (min == -1 || d < min) {
					min = d;
					minPair = new Vector[]{ current, vector };
				}
			}
			
			// was that distance smaller than deltaX?
			if (min != -1 && min < deltaX) {
				deltaX = min;
				pair = minPair;
				grid = new Grid(vSet, deltaX, dimensions);
			}
			else grid.insert(current);
			
		}
		
		return pair;
	}
		
}
