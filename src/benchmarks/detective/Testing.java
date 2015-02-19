package detective;

import java.util.List;

import clustering.Cluster;
import clustering.feature.FeatureClusterer;
import clustering.feature.aggregation.AggregateStrategy;
import clustering.feature.aggregation.BasicAggregate;
import clustering.feature.aggregation.VectorAggregate;
import clustering.feature.assignment.AssignmentStrategy;
import clustering.feature.assignment.RandomAssignment;
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
	
	public static void ClusterTestFile ()
	throws Exception {
		
		FeatureClusterer.setMaxIterations(4);
		FeatureClusterer.setNumClusters(4);
		FeatureClusterer.setSubsetSize(5);
		
		System.out.println("Loading hosts...");
		List<Host> hosts = IO.loadHosts(HOST_FILE);
		System.out.println("Finished loading hosts.");
		System.out.println("Clustering...");
		
		
		final AggregateStrategy aggregate = new VectorAggregate(5);
		final AssignmentStrategy assign = new RandomAssignment(4);
		FeatureClusterer fc = new FeatureClusterer(aggregate, assign);
		List<Cluster> clusters = fc.cluster(hosts);
		
		System.out.println("Finished clustering.");
		System.out.println("Saving clusters...");
		IO.saveCluster(clusters);
		System.out.println("All done.");
		
	}
	
	public static void main (String[] args)
	throws Exception {
		ClusterTestFile();
	}

	

}
