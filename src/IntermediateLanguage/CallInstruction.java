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
		return String.format("(call r%d %s %s)", register, functionId, args);
	}

}
