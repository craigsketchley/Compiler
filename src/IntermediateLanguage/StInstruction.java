package IntermediateLanguage;

import java.util.List;

public class StInstruction implements Instruction {
	
	public String id;
	public Register register;
	
	public StInstruction(String id, Register register) {
		this.id = id;
		this.register = register;
	}

	public String toString() {
		return String.format("(st %s %s)", id, register);
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
