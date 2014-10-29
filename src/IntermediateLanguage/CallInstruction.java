package IntermediateLanguage;

import java.util.List;

public class CallInstruction implements Instruction {
	
	public String register;
	public String functionId;
	public List<String> args;
	
	CallInstruction(String register, String functionId, List<String> args) {
		this.register = register;
		this.functionId = functionId;
		this.args = args;
	}

}
