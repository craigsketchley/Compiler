package IntermediateLanguage;

import java.util.List;

public class LcInstruction implements Instruction {
	
	public int register;
	public int value;
	
	public LcInstruction(int register, int value) {
		this.register = register;
		this.value = value;
	}
	
	public String toString() {
		return String.format("(lc r%d %d)", register, value);
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
