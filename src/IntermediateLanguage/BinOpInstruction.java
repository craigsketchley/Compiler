package IntermediateLanguage;

/* This is likely to change later into an interface (implemented by different operations */

public class BinOpInstruction implements Instruction {
	
	public String op;
	public String dest;
	public String lhs;
	public String rhs;
	
	BinOpInstruction(String op, String dest, String lhs, String rhs) {
		this.op = op;
		this.dest = dest;
		this.rhs = lhs;
		this.rhs = rhs;
	}

}
