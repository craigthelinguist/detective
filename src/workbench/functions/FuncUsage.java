package functions;

import primitives.Primitive;
import primitives.Str;
import errors.TypeException;

public class FuncUsage extends Function {

	public FuncUsage(Primitive[] args) throws TypeException {
		super(args);
	}
	
	public FuncUsage () {
		super();
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length == 1 && args[0] instanceof Str){
			String name = args[0].toString();
			if (FunctionFactory.funcs.containsKey(name)) return;
		}
		throw new TypeException("Type usage(arg), where arg is the name of the function you're interested in.");
	}

	@Override
	public String usage() {
		return "Don't do this, man.";
	}

	@Override
	public String signature() {
		return "usage :: (str) -> (str)";
	}

	@Override
	public String name() {
		return "usage";
	}

	@Override
	public Primitive eval() {
		Function f = FunctionFactory.make(args()[0].toString());
		return new Str(f.usage());
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

	
	
}
