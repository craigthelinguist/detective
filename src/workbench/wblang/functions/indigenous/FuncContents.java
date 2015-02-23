package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
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
		Seq seq = (Seq)args()[0];
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < seq.size(); i++) {
			sb.append(seq.get(i).toString()+"\n");
		}
		return new Str(sb.toString());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1) throw new TypeException(name() + " takes one seq as its argument.");
		if (!(args[0] instanceof Seq)) throw new TypeException(name() + " takes one seq as its argument.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "A sequence, or Host", "Formatted strings of the contents in the sequence.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq" }, new String[]{ "Str" });
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
