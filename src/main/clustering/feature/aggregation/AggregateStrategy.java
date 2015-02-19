package clustering.feature.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vectors.Vector;
import vectors.Vectors;
import dns.Host;

/**
 * An AggregationStrategy is a way of grouping domains into partitions of size SUBSET_SIZE.
 */
public interface AggregateStrategy {

	/**
	 * Take a bunch of hosts and partition their domain queries into blocks of size SUBSET_SIZE.
	 * Then map each block to a feature vector, and return the list of feature vectors.
	 * @param hosts: hosts that sent queries we're interested in.
	 * @param SUBSET_SIZE: size of the blocks to partition into.
	 * @return List<Vector>
	 */
	public List<Vector> aggregate (List<Host> hosts);

}
