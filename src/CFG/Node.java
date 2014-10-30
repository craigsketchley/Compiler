package CFG;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import IntermediateLanguage.Instruction;
import IntermediateLanguage.Register;

public class Node
{
	private int blockId; 
	private Instruction instruction;
	//private ControlFlowInformation in;
	private Set<Register> out; 
	private Set<Node> successors; 
	private Set<Node> predecessors; 
	
	/**
	 * default constructor
	 * returns a sentinel node with invalid block id (-1) and null statement
	 */
	public Node()
	{
		this(-1, null);
	}
	
	/**
	 * @param blockId
	 * @param st
	 */
	public Node(int blockId, Instruction instruction)
	{
		this.blockId = blockId;
		this.instruction = instruction;
		successors = new HashSet<Node>();
		predecessors = new HashSet<Node>();
		out = new HashSet<Register>();
	}
	
	public boolean isSentinel()
	{
		return instruction == null;
	}
	
	public Instruction getInstruction()
	{
		return instruction;
	}
	
	public Set<Node> getAllSuccessors()
	{
		return successors;
	}
	public Set<Node> getAllPredecessors() 
	{
		return predecessors;
	}
	
	public void addPredecessor(Node p)
	{
		this.predecessors.add(p);
	}
	
	public void clearPredecessors()
	{
		predecessors = new HashSet<Node>();
	}
	
	public void removePredecessor(Node p)
	{
		predecessors.remove(p);
	}
	
	public int getBlockId()
	{
		return blockId;
	}
	
	public void addSuccessor(Node successor)
	{
		this.successors.add(successor);
	}
	
	@Override
	public String toString()
	{
		return String.format("%d %s : set %s", blockId, instruction, out);
	}

	public Set<Register> getOut()
	{
		return out;
	}
		
}