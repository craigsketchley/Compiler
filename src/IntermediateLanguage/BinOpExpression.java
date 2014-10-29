package IntermediateLanguage;

/* This is likely to change later into an interface (implemented by different operations */

public class BinOpExpression implements Expression {
	
	public String op;
	public Expression lhs;
	public Expression rhs;
	
	BinOpExpression(String op, Expression lhs, Expression rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

}
