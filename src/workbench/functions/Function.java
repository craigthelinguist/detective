package functions;

import errors.TypeException;
import primitives.Primitive;

public abstract class Function {

	private final Primitive[] args;
	
	public Function (Primitive[] args)
	throws TypeException {
		verifyArguments(args);
		this.args = args;
	}
	
	protected Function () {
		this.args = null;
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
	
	public abstract Primitive eval ();

	public abstract Class returnType ();
	
	public Primitive[] args () { return this.args; }
	
}
