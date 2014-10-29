package IntermediateLanguage;

import java.util.List;

public class RetInstruction implements Instruction {
	
	public Register register;
	
	public RetInstruction(Register register) {
		this.register = register;
	}

	public String toString() {
		return String.format("(ret %s)", register);
	}

	@Override
	public List<Integer> getReferencedRegisters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAssignedRegisters() {
		// TODO Auto-generated method stub
		return null;
	}

}
