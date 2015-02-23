package wblang.primitives;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import clustering.Cluster;
import wblang.rules.Primitive;

public class ClusterPrim extends Seq {

	private Cluster cluster;
	
	private ClusterPrim (Cluster cluster, List<Str> strs) {
		super(strs);
		this.cluster = cluster;
	}
	
	public static ClusterPrim make (Cluster cluster) {
		List<Str> strs = new ArrayList<>();
		Collection<String> domains = cluster.getDomains();
		for (String s : domains) {
			strs.add(new Str(s));
		}
		return new ClusterPrim(cluster, strs);
	}
	
	public String typeName() {
		return "Cluster";
	}
	
	

}
