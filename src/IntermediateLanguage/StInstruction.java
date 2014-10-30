package IntermediateLanguage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StInstruction implements Instruction {
	
	public String id;
	public Register register;
	
	public StInstruction(String id, Register register) {
		this.id = id;
		this.register = register;
	}

	@Override
	public String toString() {
		return String.format("(st %s %s)", id, register);
	}

	@Override
	public List<Register> getReferencedRegisters() {
		return Arrays.asList(register);
	}

	@Override
	public Register getAssignedRegister() {
		return null;
	}

}
