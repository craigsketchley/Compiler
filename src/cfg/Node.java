package cfg;

import intermediateLanguage.Instruction;

import java.util.HashSet;
import java.util.Set;

public class Node
{
	private int blockId; 
	private Instruction instruction;
	//private ControlFlowInformation in;
//	private Set<Register> in; 
//	private Set<Register> out; //TODO: Need to generalize this to an interface
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
//		out = new HashSet<Register>();
//		in = new HashSet<Register>();
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
	
	public void removePredecessor(Node p)
	{
		predecessors.remove(p);
	}
	
	public void clearPredecessors()
	{
		predecessors = new HashSet<Node>();
	}
	
	public int getBlockId()
	{
		return blockId;
	}
	
	public void addSuccessor(Node successor)
	{
		this.successors.add(successor);
	}
	
	public void removeSuccessor(Node p)
	{
		successors.remove(p);
	}
	
	public void clearSuccessors()
	{
		successors = new HashSet<Node>();
	}
	
	@Override
	public String toString()
	{
		return String.format("%d %s ", blockId, instruction);
	}

//	public Set<Register> getOut()
//	{
//		return out;
//	}
//
//	public Set<Register> getIn()
//	{
//		return in;
//	}
//
//	public void setIn(Set<Register> in)
//	{
//		this.in = in;
//	}
		
}