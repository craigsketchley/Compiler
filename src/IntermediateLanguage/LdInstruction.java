package IntermediateLanguage;

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

}
