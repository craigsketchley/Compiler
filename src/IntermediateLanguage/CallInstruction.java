package IntermediateLanguage;

import java.util.List;

public class CallInstruction implements Instruction {
	
	public int register;
	public String functionId;
	public List<Integer> args;
	
	public CallInstruction(int register, String functionId, List<Integer> args) {
		this.register = register;
		this.functionId = functionId;
		this.args = args;
	}

	public String toString() {
		String argString = args.toString().replace(", ", " ");
		argString = argString.substring(1, argString.length() - 1);
		return String.format("(call r%d %s %s)", register, functionId, argString);
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
