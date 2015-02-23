package wblang.functions.indigenous;

import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.SigTemplate;
import wblang.functions.UsageTemplate;
import wblang.primitives.HostPrim;
import wblang.primitives.Int;
import wblang.primitives.Kore;
import wblang.primitives.Seq;
import wblang.rules.Primitive;

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
		if (indx >= seq.size() || indx < 0) return Kore.kore;
		return seq.get(indx);
	}

	@Override
	public void verifyArguments(Primitive[] args) throws TypeException {
		if (args.length != 2) throw new TypeException("Must provide a sequence/host and an index to extract.");
		if (!(args[0] instanceof Seq) && !(args[0] instanceof HostPrim)) throw new TypeException("First argument must be a Seq or Host.");
		if (!(args[1] instanceof Int)) throw new TypeException("Second argument must be an Int.");
	}

	@Override
	public String usage() {
		return UsageTemplate.make(signature(), "Seq: the sequence to extract an element from/nInt: the index that you want.",
											   "The element at the specified index, or Kore if there isn't one.");
	}

	@Override
	public String signature() {
		return SigTemplate.make(name(), new String[]{ "Seq|Host", "Int" }, new String[]{ "Primitive" });
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
