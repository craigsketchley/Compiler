package IntermediateLanguage;

import java.util.List;

public class BrInstruction implements Instruction {
	
	public Register register;
	public int blockTrue;
	public int blockFalse;
	
	public BrInstruction(Register register, int blockTrue, int blockFalse) {
		this.register = register;
		this.blockTrue = blockTrue;
		this.blockFalse = blockFalse;
	}

	public String toString() {
		return String.format("(br %s %d %d)", register, blockTrue, blockFalse);
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
