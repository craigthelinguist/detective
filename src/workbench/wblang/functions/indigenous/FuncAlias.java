package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.FunctionFactory;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.Kore;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class FuncAlias extends Function {

	public FuncAlias (Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncAlias () {
		super();
	}
	
	@Override
	public Primitive exec() {
		String alias = args()[0].toString();
		String fName = args()[1].toString();
		FunctionFactory.addAlias(alias, fName);
		return Kore.kore;
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) throw new TypeException("alias(str,str) takes two arguments, not " + args.length);
		Primitive p1 = args[0];
		Primitive p2 = args[1];
		if (!((p1 instanceof Str) && (p2 instanceof Str))) {
			throw new TypeException("alias(str,str) two arguments must be of type Str.");
		}
		
		String s1 = ((Str)p1).toString();
		String s2 = ((Str)p2).toString();
		
		if (FunctionFactory.funcs.containsKey(s1)) {
			throw new TypeException("Cannot use " + s1 + " as an alias because that is the name of an existing fucntion.");
		}
		else if (!FunctionFactory.funcs.containsKey(s2)) {
			throw new TypeException("Aliasing unknown function " + s2);
		}		
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(),
				"First argument is the alias to use.\nSecond argument is the name of the aliased function.",
				"Associates the alias with the given function.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Str", "Str" }, new String[0]);
	}

	@Override
	public String name() {
		return "alias";
	}

	@Override
	public Class returnType() {
		return Kore.class;
	}

}
