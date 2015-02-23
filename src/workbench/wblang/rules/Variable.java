package wblang.rules;

import workbench.TestingREPL;

public class Variable extends Expression {

	private String name;
	
	public Variable (String name) {
		this.name = name;
	}

	@Override
	public Primitive exec() {
		Expression expr = TestingREPL.getBinding(name);
		return expr.exec();
	}

	@Override
	public String eval() {
		return exec().toString();
	}

}
