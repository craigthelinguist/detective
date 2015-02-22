package clustering.feature.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vectors.Vector;
import vectors.Vectors;
import dns.Host;

/**
 * BasicAggregate will sort the domains queried alphabetically and then group the first SUBSET_SIZE into one, the next SUBSET_SIZE
 * into two, and so on.
 */
public class BasicAggregate implements AggregateStrategy {
	
	private final int SUBSET_SIZE;
	
	public BasicAggregate (final int SUBSET_SIZE) {
		this.SUBSET_SIZE = SUBSET_SIZE;
	}
	
	@Override
	public List<Vector> aggregate (List<Host> hosts) {
		List<Vector> vectors = new ArrayList<>();
		for (Host host : hosts){
			for (List<String> subset : partitionQueries(host)) {
				Vector featureVector = Vectors.featureVector(host, subset);
				vectors.add(featureVector);
			}
		}
		return vectors;
	}
	
	/**
	 * Partition the queries sent by a host into lists of strings and return them.
	 * @param host: host that sent some queries we're interested in.
	 * @return a list of the lists of strings we've grouped together.
	 */
	private List<List<String>> partitionQueries (Host host) {
		// sort queries
		List<String> queries = host.getQueries();
		Collections.sort(queries);
	
		// current subset
		List<List<String>> subsets = new ArrayList<>();
		List<String> subset = new ArrayList<>();
		int i = 0;
		
		// the partition
		for (String query : queries) {
			if (i % SUBSET_SIZE == 0 && !subset.isEmpty()) {
				subsets.add(subset);
				subset = new ArrayList<String>();
			}
			subset.add(query);
			i++;
		}
		
		// left overs
		if (!subset.isEmpty()) subsets.add(subset);
		
		// return
		return subsets;
	}
	
	public String toString(){
		return "BasicAggregate";
	}
	
	
}
