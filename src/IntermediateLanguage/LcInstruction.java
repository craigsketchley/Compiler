package IntermediateLanguage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LcInstruction implements Instruction {
	
	public Register register;
	public int value;
	
	public LcInstruction(Register register, int value) {
		this.register = register;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.format("(lc %s %d)", register, value);
	}

	@Override
	public List<Register> getReferencedRegisters() {
		return Collections.emptyList();
	}

	@Override
	public List<Register> getAssignedRegisters() {
		return Arrays.asList(register);
	}

}