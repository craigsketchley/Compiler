package IntermediateLanguage;

import java.util.List;

public class RetInstruction implements Instruction {
	
	public int register;
	
	public RetInstruction(int register) {
		this.register = register;
	}

	public String toString() {
		return String.format("(ret r%d)", register);
	}

	@Override
	public List<Integer> getReferencedRegisters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAssignedRegisters()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
