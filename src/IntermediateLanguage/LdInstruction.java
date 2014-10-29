package IntermediateLanguage;

import java.util.List;

public class LdInstruction implements Instruction {
	
	public int register;
	public String variable;
	
	public LdInstruction(int register, String variable) {
		this.register = register;
		this.variable = variable;
	}

	public String toString() {
		return String.format("(ld r%d %s)", register, variable);
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
