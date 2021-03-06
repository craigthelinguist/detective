package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import clustering.feature.aggregation.AggregateStrategy;
import clustering.feature.aggregation.BasicAggregate;
import clustering.feature.aggregation.VectorAggregate;
import clustering.feature.assignment.AssignmentStrategy;
import clustering.feature.assignment.PredefinedAssignment;
import clustering.feature.assignment.RandomAssignment;
import vectors.Vector;
import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Clusterer;
import wblang.primitives.Int;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.primitives.Vec;
import wblang.rules.Primitive;

public class FuncClusterer extends Function {

	private static List<Str> validAggregateStrats = Arrays.asList(new Str[]{ new Str("vector"), new Str("basic") });
	private static List<Str> validAssignStrats = Arrays.asList(new Str[]{ new Str("random"), new Str("predefined") });

	public FuncClusterer (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncClusterer () {
		super();
	}
	@Override
	public Primitive exec() {
		
		int subsetSize = ((Int)args()[0]).toInt();
		int numClusters = ((Int)args()[1]).toInt();
		String aggregName = args()[2].toString();
		
		AggregateStrategy aggreg = null;
		AssignmentStrategy assign = null;
		
		if (aggregName.equals("vector")) aggreg = new VectorAggregate(subsetSize);
		else if (aggregName.equals("basic")) aggreg = new BasicAggregate(subsetSize);
		
		if (args()[3].toString().equals("random")) assign = new RandomAssignment(numClusters);
		else {
			Seq<Vec> vects = (Seq<Vec>)args()[3];
			List<Vector> vectors = new ArrayList<>();
			for (Vec vec : vects) {
				vectors.add(vec.asVector());
			}
			assign = new PredefinedAssignment(vectors);
		}
				
		return new Clusterer(subsetSize, numClusters, aggreg, assign);
				
	}
	
	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 4) throw new TypeException("Function clusterer takes four arguments, not " + args.length);
		if (!(args[0] instanceof Int)) throw new TypeException("Clusterer's first argument is subset_size, which should be Int.");
		if (!(args[1] instanceof Int)) throw new TypeException("Clusterer's second argument is num_clusters, which should be of type Int.");
		if (!(args[2] instanceof Str) || !validAggregateStrats.contains(args[2])){
			throw new TypeException("Clusterer's third argument, aggregation_strategy, should be \"vector\" or \"basic\")");
		}
		
		if (!(args[3] instanceof Str) && !(args[3] instanceof Seq)) {
			throw new TypeException("Clusterer's fourth argument, assignment_Strategy, should be \"random\" or a sequence of vectors.");
		}
		
		if (args[3] instanceof Str && !(args[3].toString().equals("random"))) {
			throw new TypeException("Clusterer's fourth argument, assignment_Strategy, should be \"random\" or a sequence of vectors.");
		}
		
		if (args[3] instanceof Seq) {			
			Seq<Primitive> seq = (Seq<Primitive>)args[3];
			for (Primitive prim : seq) {
				if (!(prim instanceof Vec)) throw new TypeException("Clusterer's fourth argument should be \"random\" or seq<vec>");
			}
		}
		
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(),
								  "Arg 1: subset size\n" + 
								  "Arg 2: Number of clusters\n" +
								  "Arg 3: aggregation strategy (\"basic\" or \"vector\"\n" +
								  "Arg 4: assignment strategy (\"random\" or List<Str>)",
								  "A Clusterer object.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(),
								new String[]{ "Int", "Int", "Str", "Str|List" },
								new String[]{ "Clusterer" });
	}

	@Override
	public String name() {
		return "clusterer";
	}

	@Override
	public Class returnType() {
		return Clusterer.class;
	}

}
