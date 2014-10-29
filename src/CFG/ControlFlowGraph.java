package CFG;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class ControlFlowGraph 
{
	private Node root;
		
	public ControlFlowGraph(Function f)
	{
		ArrayList<Node> tempNodeList = new ArrayList<Node>();
		HashMap<Integer, Node> tempBlockMap = new HashMap<Integer, Node>(); 
		
		
		ArrayList<Block> blocks = f.getBlocks();
		//ASSUME BLOCKS ARE NOT EMPTY
		for(int i = 0; i < blocks.size(); ++i)
		{	
			ArrayList<Statement> statements = blocks.get(i).getStatements();
			
			for(int j=0; j < statements.size(); ++j)
			{
				Node n = new Node(blocks.get(i).getBlockId(), statements.get(j));	
				if(j == 0)
				{
					tempBlockMap.put(blocks.get(i).getBlockId(), n);
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
			Statement st = n.getStatement();
			if(st instanceof BranchStatement)
			{
				int firstId = ((BranchStatement) st).getFirstBranch();
				int secondId = ((BranchStatement) st).getSecondBranch();
				if(firstId != secondId)
				{
					n.addSuccessor(tempBlockMap.get(secondId));
				}
				n.addSuccessor(tempBlockMap.get(firstId));
				
			}
			else if(st instanceof ReturnStatement)
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
		private Statement st;
		private ArrayList<Node> successors; 
		private ArrayList<Node> predecessors; 
		
		public Node(int blockId, Statement st)
		{
			this.blockId = blockId;
			this.st = st;
			successors = new ArrayList<Node>();
			predecessors = new ArrayList<Node>();
		}
		
		public Statement getStatement()
		{
			return st;
		}
		
		public ArrayList<Node> getAllSuccessors();
		{
			return successors;
		}
		public ArrayList<Node> getAllPredecessors(); 
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
