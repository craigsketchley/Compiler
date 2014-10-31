package cfg;

import intermediateLanguage.Instruction;

import java.util.HashSet;
import java.util.Set;

/**
 * The Node for the Control Flow Graph. Holds a single instruction per node of
 * the CFG. Nodes can also by sentinal nodes (start/end) for the CFG.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class Node
{
	private int blockId; 
	private Instruction instruction;
	private Set<Node> successors; 
	private Set<Node> predecessors; 
	
	/**
	 * Default constructor
	 * returns a sentinel node with invalid block id (-1) and null statement
	 */
	public Node()
	{
		this(-1, null);
	}
	
	/**
	 * Creates a Node which holds a reference to the original code block from the source code
	 * and one instruction
	 * 
	 * @param blockId corresponding block from Intermediate Code
	 * @param instruction intermediate code instruction
	 */
	public Node(int blockId, Instruction instruction)
	{
		this.blockId = blockId;
		this.instruction = instruction;
		successors = new HashSet<Node>();
		predecessors = new HashSet<Node>();
	}
	
	/**
	 * Check if the Node is a sentinel, i.e. Entry and Exit nodes in the CFG
	 * @return true
	 */
	public boolean isSentinel()
	{
		return instruction == null;
	}
	
	public Instruction getInstruction()
	{
		return instruction;
	}
	
	public void setInstruction(Instruction instr)
	{
		instruction = instr;
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
}
