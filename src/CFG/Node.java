package CFG;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import IntermediateLanguage.Instruction;
import IntermediateLanguage.Register;

public class Node
{
	private int blockId; 
	private Instruction st;
	//private ControlFlowInformation in;
	private Set<Register> out; 
	private Set<Node> successors; 
	private Set<Node> predecessors; 
	
	public Node()
	{
		this.blockId = -1;
		this.st = null;
		successors = new HashSet<Node>();
		predecessors = new HashSet<Node>();
		out = new HashSet<Register>();
	}
	
	public Node(int blockId, Instruction st)
	{
		this.blockId = blockId;
		this.st = st;
		successors = new HashSet<Node>();
		predecessors = new HashSet<Node>();
		out = new HashSet<Register>();
	}
	
	public Instruction getInstruction()
	{
		return st;
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
		p.successors.add(this);
	}
	
	public void clearPredecessors()
	{
		predecessors = new HashSet<Node>();
	}
	
	public int getBlockId()
	{
		return blockId;
	}
	
	public void addSuccessor(Node s)
	{
		this.successors.add(s);
		s.predecessors.add(this);
	}
	
	@Override
	public String toString()
	{
		return blockId + " " + st + " : " + " set " + out;
	}

	public Set<Register> getOut()
	{
		// TODO Auto-generated method stub
		return out;
	}
	
	
}
