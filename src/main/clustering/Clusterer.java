package clustering;

import java.util.List;

import clustering.feature.FeatureClustering;
import dns.Host;

public class Clusterer {

	public static List<Cluster> clusterByFeatures (List<Host> hosts) {
		return new FeatureClustering().cluster(hosts);
	}
	
}
