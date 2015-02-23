package wblang.primitives;

import clustering.Cluster;
import wblang.rules.Primitive;

public class ClusterPrim extends Primitive {

	private Cluster cluster;
	
	public ClusterPrim(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public String typeName() {
		return "Cluster";
	}
	
	

}
