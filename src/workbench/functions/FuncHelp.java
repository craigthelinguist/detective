package functions;

import primitives.Primitive;
import primitives.Str;
import errors.TypeException;

public class FuncHelp extends Function {

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String usage() {
		return "Don't be lame.";
	}

	@Override
	public String signature() {
		return "Help :: () -> (Str)";
	}

	@Override
	public String name() {
		return "Help";
	}

	@Override
	public Primitive eval() {
		
		String s = "List of commands: \n";
		
		for (String str : FunctionFactory.funcs.keySet()) {
			Function f = FunctionFactory.make(str);
			s += f.usage() + "\n";
		}
		return new Str(s);
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
