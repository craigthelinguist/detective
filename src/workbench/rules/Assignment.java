package rules;

import workbench.TestingREPL;

public class Assignment extends Expression {

	final private String name;
	final private Expression expr;
	
	public Assignment (String name, Expression expr) {
		this.name = name;
		this.expr = expr;
	}
	
	@Override
	public Primitive exec () {
		TestingREPL.assign(name, expr);
		return expr.exec();
	}
	
	@Override
	public String eval () {
		return expr.exec().toString();
	}

	
}
