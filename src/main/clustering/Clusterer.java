package clustering;

import java.util.List;

import clustering.feature.FeatureClustering;
import clustering.feature.aggregation.AggregateOptions;
import dns.Host;

public class Clusterer {

	public static List<Cluster> clusterByFeatures (List<Host> hosts, AggregateOptions option) {
		return new FeatureClustering(option).cluster(hosts);
	}
	
}
