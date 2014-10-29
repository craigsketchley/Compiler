package IntermediateLanguage;

import java.util.List;

/* This is likely to change later into an interface (implemented by different operations */

public class BinOpInstruction implements Instruction {
	
	public String op;
	public int dest;
	public int lhs;
	public int rhs;
	
	public BinOpInstruction(String op, int dest, int lhs, int rhs) {
		this.op = op;
		this.dest = dest;
		this.rhs = lhs;
		this.rhs = rhs;
	}

	public String toString() {
		return String.format("(%s r%d r%d r%d)", op, dest, lhs, rhs);
	}

	@Override
	public List<Integer> getReferencedRegisters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAssignedRegisters()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
