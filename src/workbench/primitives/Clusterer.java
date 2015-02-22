package primitives;

import java.util.List;

import clustering.Cluster;
import clustering.feature.FeatureClusterer;
import clustering.feature.aggregation.AggregateStrategy;
import clustering.feature.aggregation.BasicAggregate;
import clustering.feature.aggregation.VectorAggregate;
import clustering.feature.assignment.AssignmentStrategy;
import dns.Host;
import errors.TypeException;
import rules.Primitive;

public class Clusterer extends Primitive {

	private FeatureClusterer clusterer;
	
	public Clusterer (int subsetSize, int numClusters, AggregateStrategy aggregate,
					   AssignmentStrategy assignment) {
		clusterer = new FeatureClusterer(aggregate, assignment);
		clusterer.setSubsetSize(subsetSize);
		clusterer.setNumClusters(numClusters);
	}
	
	public List<Cluster> cluster (List<Host> hosts) {
		return clusterer.cluster(hosts);
	}
	
	@Override
	public Primitive exec () {
		return this;
	}
	
	@Override
	public String eval() {
		StringBuilder sb = new StringBuilder();
		sb.append("Subset size: " + clusterer.getSubsetSize() +"\n");
		sb.append("Max iterations: " + clusterer.getMaxIterations() +"\n");
		sb.append("Number of clusters: " + clusterer.getNumClusters() +"\n");
		sb.append("Aggregation: " + clusterer.getAggregationStrategy() +"\n");
		sb.append("Assignment: " + clusterer.getAssignmentStrategy());
		return sb.toString();
	}
	
}
