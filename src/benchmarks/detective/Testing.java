package detective;

import java.util.List;

import clustering.Cluster;
import clustering.Clusterer;
import clustering.feature.FeatureClustering;
import clustering.feature.aggregation.AggregateOptions;
import dns.Host;
import io.IO;

public class Testing {
	
	private static final String HOST_FILE = "mix-kebab1.txt";
	
	
	public static void TestIO ()
	throws Exception {
		System.out.println("Loading hosts...");
		List<Host> hosts = IO.loadHosts(HOST_FILE);
		System.out.println("Finished loading hosts.");
		for (Host host : hosts) {
			System.out.println("==============");
			System.out.println(host);
			System.out.println("---------");
			for (String query : host.getQueries()) {
				System.out.println(query);
			}
		}

		System.out.println(hosts.size() + " loaded.");
	}
	
	public static void ClusterTestFile (AggregateOptions aggType)
	throws Exception {
		
		FeatureClustering.setMaxIterations(4);
		FeatureClustering.setNumClusters(4);
		FeatureClustering.setSubsetSize(5);
		
		System.out.println("Loading hosts...");
		List<Host> hosts = IO.loadHosts(HOST_FILE);
		System.out.println("Finished loading hosts.");
		System.out.println("Clustering...");
		
		
		List<Cluster> clusters = Clusterer.clusterByFeatures(hosts, aggType);
		System.out.println("Finished clustering.");
		System.out.println("Saving clusters...");
		IO.saveCluster(clusters);
		System.out.println("All done.");
		
	}
	
	public static void main (String[] args)
	throws Exception {
		ClusterTestFile(AggregateOptions.VECTOR_AGGREGATE);
	}

	

}
