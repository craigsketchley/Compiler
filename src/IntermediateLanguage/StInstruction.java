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
	public List<Register> getReferencedRegisters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Register> getAssignedRegisters()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
