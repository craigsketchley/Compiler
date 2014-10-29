package IntermediateLanguage;

import java.util.List;

public class LcInstruction implements Instruction {
	
	public Register register;
	public int value;
	
	public LcInstruction(Register register, int value) {
		this.register = register;
		this.value = value;
	}
	
	public String toString() {
		return String.format("(lc %s %d)", register, value);
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
