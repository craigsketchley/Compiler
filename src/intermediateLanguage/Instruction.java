package intermediateLanguage;

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
	
	/**
	 * Replace all registers with regnum matching from,
	 * with new registers with the regnum of to
	 * @param from
	 * @param to
	 */
	public void rewriteReferencedRegisters(Register from, Register to);

	/**
	 * Replace the assigned register with a new register with the regnum of to
	 * @param register
	 */
	public void rewriteAssignedRegister(Register register);
	
}
