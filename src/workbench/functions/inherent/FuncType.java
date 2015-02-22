package functions.inherent;

import primitives.Str;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncType extends Function {

	public FuncType (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncType () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Primitive p = args()[0];
		return new Str(p.typeName());
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length!= 1) throw new TypeException("Must specify exactly one argument.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Any variable.", "The type of that variable.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Any" }, new String[]{ "Str" });
	}

	@Override
	public String name() {
		return "type";
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
