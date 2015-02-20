package functions;

import errors.TypeException;
import rules.Primitive;
import rules.Expression;

public abstract class Function extends Expression {

	private final Primitive[] arguments;
	
	public Function (Primitive[] args)
	throws TypeException {
		verifyArguments(args);
		this.arguments = args;
	}
	
	public Function () {
		this.arguments = null;
	}
	
	/**
	 * Return true if the given arguments are correct for this function, or false otherwise.
	 * @param args
	 * @return
	 */
	public abstract void verifyArguments (Primitive[] args)
	throws TypeException ;
	
	/**
	 * Return a string detailing how this function should be used.
	 * @return
	 */
	public abstract String usage ();

	public abstract String signature ();

	public abstract String name ();

	public abstract Class returnType ();
	
	public Primitive[] args () { return this.arguments; }
	
	@Override
	public String eval () {
		return exec().toString();
	}
	
}
