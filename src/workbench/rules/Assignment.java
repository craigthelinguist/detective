package rules;

import workbench.TestingREPL;

public class Assignment extends Expression {

	final private String variableName;
	final private Expression assignmentExpression;
	
	public Assignment (String name, Expression expr) {
		this.variableName = name;
		this.assignmentExpression = expr;
	}
	
	@Override
	public Primitive exec () {
		TestingREPL.assign(variableName, assignmentExpression);
		return assignmentExpression.exec();
	}
	
	@Override
	public String eval () {
		return assignmentExpression.exec().toString();
	}

	
}
