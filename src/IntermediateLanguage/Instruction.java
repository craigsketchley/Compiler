package IntermediateLanguage;

import java.util.List;

public interface Instruction 
{
	public List<Register> getReferencedRegisters();
	
	public Register getAssignedRegister();
}
