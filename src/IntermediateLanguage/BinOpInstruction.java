package IntermediateLanguage;

import java.util.List;

/* This is likely to change later into an interface (implemented by different operations */

public class BinOpInstruction implements Instruction {
	
	public String op;
	public Register dest;
	public Register lhs;
	public Register rhs;
	
	public BinOpInstruction(String op, Register dest, Register lhs, Register rhs) {
		this.op = op;
		this.dest = dest;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public String toString() {
		return String.format("(%s %s %s %s)", op, dest, lhs, rhs);
	}

	@Override
	public List<Register> getReferencedRegisters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Register> getAssignedRegisters() {
		// TODO Auto-generated method stub
		return null;
	}
}
