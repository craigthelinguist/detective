 package functions.inherent;

import java.util.ArrayList;
import java.util.List;

import clustering.Cluster;
import dns.Host;
import primitives.ClusterPrim;
import primitives.Clusterer;
import primitives.HostPrim;
import primitives.Seq;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncCluster extends Function {
	
	public FuncCluster (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncCluster () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Clusterer cl = (Clusterer)args()[0];
		List<HostPrim> hostPrims = ((Seq<HostPrim>)args()[1]).asList();
		List<Host> hosts = new ArrayList<>();
		for (HostPrim hp : hostPrims) {
			hosts.add(hp.getHost());
		}
		List<Cluster> clusters = cl.cluster(hosts);
		List<ClusterPrim> clusterPrims = new ArrayList<ClusterPrim>();
		for (Cluster cluster : clusters) {
			clusterPrims.add(new ClusterPrim(cluster));
		}
		Seq<ClusterPrim> seqClusterPrims = new Seq<>(clusterPrims);
		return seqClusterPrims;
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) {
			throw new TypeException("The function cluster takes two arguments.");
		}
		else if (!(args[0] instanceof Clusterer)) {
			throw new TypeException("cluster's first argument should be the clusterer.");
		}
		else if (!(args[1] instanceof Seq)) {
			throw new TypeException("cluster's second argument should be the seq of hosts to cluster.");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(),
				"The clusterer object that should perform the clustering.\nInstantiate with clusterer() function.",
				"The result of clustering, which is a sequence of strs. Each sequence is a cluster.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Clusterer" }, new String[]{ "Seq<Cluster>" });
	}

	@Override
	public String name() {
		return "cluster";
	}

	@Override
	public Class returnType() {
		return Seq.class;
	}

}
