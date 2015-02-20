package rules;

public abstract class Primitive extends Expression {
	
	@Override
	public String eval () {
		return toString();
	}
	
	@Override
	public Primitive exec () {
		return this;
	}
	
}
