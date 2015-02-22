package functions.inherent;

import primitives.Int;
import primitives.Seq;
import rules.Primitive;
import errors.TypeException;
import functions.Function;
import functions.SigTemplate;
import functions.UsageTemplate;

public class FuncGet extends Function {

	public FuncGet (Primitive[] prims) throws TypeException {
		super(prims);
	}
	
	public FuncGet () {
		super();
	}
	
	@Override
	public Primitive exec() {
		Seq seq = (Seq)args()[0];
		int indx = ((Int)args()[1]).toInt();
		return seq.get(indx);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) throw new TypeException("Must provide a sequence and an index to extract.");
		if (!(args[0] instanceof Seq)) throw new TypeException("First argument must be a Seq.");
		if (!(args[1] instanceof Int)) throw new TypeException("Second argument must be an Int.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Seq: the sequence to extract an element from/nInt: the index that you want.",
											   "The element at the specified index, or Kore if there isn't one.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq", "Int" }, new String[]{ "Primitive" });
	}

	@Override
	public String name() {
		return "get";
	}

	@Override
	public Class returnType() {
		return Primitive.class;
	}

}
