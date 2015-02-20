package rules;

import java.util.List;

import primitives.Kore;

public class Block extends Expression implements Executable {

	List<Expression> exprs;
	
	public Block (List<Expression> exprs) {
		this.exprs = exprs;
	}
	
	@Override
	public Primitive exec() {
		Primitive rValue = Kore.kore;
		for (Expression expr : exprs) {
			rValue = expr.exec();
		}
		return Kore.kore;
	}

	@Override
	public String eval() {
		return Kore.kore.toString();
	}

}
