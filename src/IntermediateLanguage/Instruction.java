package IntermediateLanguage;

import java.util.List;

/**
 * Interface for Instruction type
 */

public interface Instruction 
{
	/**
	 * @return a list of registers evaluated by the instruction
	 */
	public List<Register> getReferencedRegisters();
	
	/**
	 * @return the register modified by the instruction (or null)
	 */
	public Register getAssignedRegister();
}
