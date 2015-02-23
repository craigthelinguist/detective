package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Int;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncContents extends Function {

	public FuncContents (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncContents () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Seq seq = (Seq) args()[0];
		int lim = args().length==2 ? ((Int)args()[1]).toInt() : seq.size();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object obj : seq) {
			if (i == lim) break;
			sb.append(obj.toString() + "\n");
			i++;
		}
		return new Str(sb.toString());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length == 1) {
			if (!(args[0] instanceof Seq)){
				throw new TypeException("first argument should be a Seq.");
			}
		}
		else if (args.length == 2) {
			if (!(args[1] instanceof Int)) {
				throw new TypeException("optional second argument should be an Int.");
			}
		}
		else throw new TypeException("Takes 1 or 2 arguments, not " + args.length);
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "A sequence", "The elements in the sequence. If you specify an Int in the optional second argument,\nthen only that amount of elements will be printed.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq", "Int" }, new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "contents";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

	
	
}
