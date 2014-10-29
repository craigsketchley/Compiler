package CFG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import IntermediateLanguage.Block;
import IntermediateLanguage.BranchInstruction;
import IntermediateLanguage.Function;
import IntermediateLanguage.Instruction;
import IntermediateLanguage.ReturnInstruction;

public class ControlFlowGraph 
{
	private Node root;
		
	public ControlFlowGraph(Function f)
	{
		ArrayList<Node> tempNodeList = new ArrayList<Node>();
		HashMap<Integer, Node> tempBlockMap = new HashMap<Integer, Node>(); 
		
		
		List<Block> blocks = f.blocks;
		//ASSUME BLOCKS ARE NOT EMPTY
		for(int i = 0; i < blocks.size(); ++i)
		{	
			List<Instruction> instructions = blocks.get(i).insts;
			
			for(int j=0; j < instructions.size(); ++j)
			{
				Node n = new Node(blocks.get(i).id, instructions.get(j));	
				if(j == 0)
				{
					tempBlockMap.put(blocks.get(i).id, n);
					if(i==0)
						root = n;
				}
				tempNodeList.add(n);		
			}
		}
		
		//Create the links
		
		for(int i = 0; i < tempNodeList.size(); ++i)
		{
			Node n = tempNodeList.get(i);
			Instruction st = n.getInstruction();
			if(st instanceof BranchInstruction)
			{
				int trueId = ((BranchInstruction) st).blockTrue;
				int falseId = ((BranchInstruction) st).blockFalse;
				if(falseId != trueId)
				{
					n.addSuccessor(tempBlockMap.get(trueId));
				}
				n.addSuccessor(tempBlockMap.get(falseId));
				
			}
			else if(st instanceof ReturnInstruction)
			{
				
			}
			else if(i < tempNodeList.size() - 1)
			{
				n.addSuccessor(tempNodeList.get(i + 1));
			}
			//Error ? or end of function without return?
		}
	}
	
	
	public class Node
	{
		private int blockId; 
		private Instruction st;
		private ArrayList<Node> successors; 
		private ArrayList<Node> predecessors; 
		
		public Node(int blockId, Instruction st)
		{
			this.blockId = blockId;
			this.st = st;
			successors = new ArrayList<Node>();
			predecessors = new ArrayList<Node>();
		}
		
		public Instruction getInstruction()
		{
			return st;
		}
		
		public ArrayList<Node> getAllSuccessors()
		{
			return successors;
		}
		public ArrayList<Node> getAllPredecessors() 
		{
			return predecessors;
		}
		
		public void addPredecessor(Node p)
		{
			this.predecessors.add(p);
			p.successors.add(this);
		}
		
		public void addSuccessor(Node s)
		{
			this.successors.add(s);
			s.predecessors.add(this);
		}
	}
	
	
	

} 
