package wblang.functions.indigenous;

import java.util.ArrayList;
import java.util.List;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Int;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncSlice extends Function {

	public FuncSlice (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncSlice () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Seq seq = (Seq) args()[0];
		
		int startIndex = 0;
		int endIndex = seq.size();
		
		if (args().length == 3) {
			startIndex = ((Int)args()[1]).toInt();
			endIndex = ((Int)args()[2]).toInt();
		}
		
		List<Primitive> subSeq = new ArrayList<>(); 
		StringBuilder sb = new StringBuilder();
		for (int i = Math.max(0,startIndex); i < Math.min(endIndex, seq.size()); i++) {
			Primitive elem = seq.get(i);
			subSeq.add(elem);
		}
		
		return new Seq<Primitive>(subSeq);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException  {
		if (args.length != 1 && args.length != 3) {
			throw new TypeException(name() + " takes 1 or 3 arguments.");
		}
		if (!(args[0] instanceof Seq)){
			throw new TypeException("first argument should be a Seq.");
		}
		else if (args.length == 3) {
			if (!(args[1] instanceof Int) || !(args[2] instanceof Int)) {
				throw new TypeException("optional 2nd & 3rd args should be Ints.");
			}
		}
		else throw new TypeException("Takes 1 or 3 arguments, not " + args.length);
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "A sequence", "The elements in the sequence. If you specify the two optional Ints, then it will display the elements in the specified subarray.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq", "Int", "Int" }, new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "slice";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

	
	
}
