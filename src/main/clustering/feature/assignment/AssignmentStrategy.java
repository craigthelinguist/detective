package clustering.feature.assignment;

import java.util.List;

import vectors.Vector;

/**
 * AssignmentStrategy is an interface that improves modularity of how you assign the initial centroids for clustering.
 * @author aaroncraig
 *
 */
public interface AssignmentStrategy {

	public List<Vector> assignCentroids (List<Vector> vectors);
	
}
