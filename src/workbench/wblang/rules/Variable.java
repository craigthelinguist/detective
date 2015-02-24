package wblang.rules;

import workbench.Interpreter;

public class Variable extends Expression {

	private String name;
	
	public Variable (String name) {
		this.name = name;
	}

	@Override
	public Primitive exec() {
		Expression expr = Interpreter.getBinding(name);
		return expr.exec();
	}

	@Override
	public String eval() {
		return exec().toString();
	}

}
