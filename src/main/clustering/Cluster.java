package clustering;

import java.util.Collection;
import java.util.List;

public class Cluster {

	private List<String> domains;

	public Cluster (List<String> domains) {
		this.domains = domains;
	}

	/**
	 * Return the number of domains in this cluster.
	 * @return int
	 */
	public int size () {
		return this.domains.size();
	}
	
	/**
	 * Return an iterable view of the domains in this cluster.
	 * @return Iterable
	 */
	public Collection<String> getDomains () {
		return domains;
	}
	
}
