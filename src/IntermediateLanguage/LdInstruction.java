package IntermediateLanguage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LdInstruction implements Instruction {
	
	public Register register;
	public String variable;
	
	public LdInstruction(Register register, String variable) {
		this.register = register;
		this.variable = variable;
	}

	@Override
	public String toString() {
		return String.format("(ld %s %s)", register, variable);
	}

	@Override
	public List<Register> getReferencedRegisters() {
		return Collections.emptyList();
	}

	@Override
	public Register getAssignedRegister() {
		return register;
	}

}
