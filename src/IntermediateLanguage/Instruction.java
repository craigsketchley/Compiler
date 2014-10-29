package IntermediateLanguage;

import java.util.List;

public interface Instruction 
{
	public List<Integer> getReferencedRegisters();
	
	public List<Integer> getAssignedRegisters();

}
