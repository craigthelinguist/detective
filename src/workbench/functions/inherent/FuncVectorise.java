package functions.inherent;

import java.util.ArrayList;
import java.util.List;

import primitives.Seq;
import primitives.Str;
import primitives.Vec;
import rules.Primitive;
import vectors.Vector;
import vectors.Vectors;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncVectorise extends Function {

	public FuncVectorise (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncVectorise () {
		super();
	}

	@Override
	public Primitive exec() {
		List<String> strings = new ArrayList<>();
		if (args()[0] instanceof Str) {
			strings.add(args()[0].toString());
		}
		else {
			Seq<Str> seq = (Seq<Str>)args()[0];
			for (Str s : seq) {
				strings.add(s.toString());
			}
		}
		Vector vector = Vectors.featureVector(null, strings);
		return new Vec(vector);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1) throw new TypeException(name() + " only takes one argument - the Str or Seq<Str> to vectorise.");
		if (!(args[0] instanceof Str) && !(args[0] instanceof Seq)){
			throw new TypeException(name() + "'s first argument should Str or Seq<Str>");
		}
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "The string - or sequence of strings - to turn into a feature vector.",
								  "A feature vector representing the strings you passed in.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str|Seq" }, new String[]{ "Vec" });
	}

	@Override
	public String name() {
		return "vectorise";
	}

	@Override
	public Class returnType() {
		return Vec.class;
	}
	
}
