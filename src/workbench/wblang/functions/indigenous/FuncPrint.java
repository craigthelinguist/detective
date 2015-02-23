package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Kore;
import wblang.rules.Primitive;

public class FuncPrint extends Function {

	public FuncPrint (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncPrint () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Primitive p = args()[0];
		String eval = p.eval();
		System.out.println(eval);
		return Kore.kore;
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 1) throw new TypeException(name() + " takes exactly 1 argument.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Any primitive.", "Prints the primitive out to stdout." );
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Any" }, new String[]{ "" });
	}

	@Override
	public String name() {
		return "print";
	}

	@Override
	public Class returnType() {
		return Kore.class;
	}

	
	
}
