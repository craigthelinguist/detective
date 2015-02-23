package wblang.rules;

import java.util.List;

import wblang.primitives.Kore;

public class Block extends Expression implements Executable {

	private List<Expression> blockExpressions;
	
	public Block (List<Expression> exprs) {
		this.blockExpressions = exprs;
	}
	
	@Override
	public Primitive exec() {
		Primitive rValue = Kore.kore;
		for (Expression expr : blockExpressions) {
			rValue = expr.exec();
		}
		return Kore.kore;
	}

	@Override
	public String eval() {
		return Kore.kore.toString();
	}

}
