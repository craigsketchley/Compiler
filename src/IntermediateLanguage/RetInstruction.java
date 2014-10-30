package IntermediateLanguage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RetInstruction implements Instruction {
	
	public Register register;
	
	public RetInstruction(Register register) {
		this.register = register;
	}

	@Override
	public String toString() {
		return String.format("(ret %s)", register);
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
