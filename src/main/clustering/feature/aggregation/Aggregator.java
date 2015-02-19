package clustering.feature.aggregation;

import java.util.List;

import dns.Host;
import vectors.Vector;

/**
 * An aggregator takes as input a list of hosts and a SUBSET_SIZE. The queries sent by those hosts are partitioned into blocks of
 * size SUBSET_SIZE, and those partitions are mapped to feature vectors. The feature vectors are then returned.
 * 
 * There are different strategies for how you partition queries.
 *
 */
public class Aggregator {
	
	private Aggregator () {}
	
	public static List<Vector> basicAggregate (List<Host> hosts, final int SUBSET_SIZE) {
		AggregationStrategy strategy = new BasicAggregate();
		return strategy.aggregate(hosts, SUBSET_SIZE);
	}
	
	public static List<Vector> entropyAggregate (List<Host> hosts, final int SUBSET_SIZE) {
		AggregationStrategy strategy = new EntropyAggregate();
		return strategy.aggregate(hosts, SUBSET_SIZE);
	}
	
}
