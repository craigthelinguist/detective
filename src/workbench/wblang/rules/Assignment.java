package wblang.rules;

import workbench.Interpreter;

public class Assignment extends Expression {

	final private String variableName;
	final private Expression assignmentExpression;
	
	public Assignment (String name, Expression expr) {
		this.variableName = name;
		this.assignmentExpression = expr;
	}
	
	@Override
	public Primitive exec () {
		Interpreter.assign(variableName, assignmentExpression);
		return assignmentExpression.exec();
	}
	
	@Override
	public String eval () {
		return assignmentExpression.exec().toString();
	}

	
}
