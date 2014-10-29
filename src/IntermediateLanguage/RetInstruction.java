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
