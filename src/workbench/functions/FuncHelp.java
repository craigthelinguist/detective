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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class returnType() {
		return Str.class;
	}

}
