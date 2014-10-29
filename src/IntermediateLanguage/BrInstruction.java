package IntermediateLanguage;

import java.util.Arrays;
import java.util.Collections;
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

	@Override
	public String toString() {
		return String.format("(br %s %d %d)", register, blockTrue, blockFalse);
	}

	@Override
	public List<Register> getReferencedRegisters() {
		return Arrays.asList(register);
	}

	@Override
	public List<Register> getAssignedRegisters() {
		return Collections.emptyList();
	}
}
