package clustering;

import java.util.List;

import dns.Host;

public interface ClusterStrategy {

	public List<Cluster> cluster (List<Host> hosts);
	
}
