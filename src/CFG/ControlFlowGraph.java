package CFG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import IntermediateLanguage.*;

public class ControlFlowGraph 
{
	public Node start; 
	public Node end;
	public Node root;
	public Function originalFunction;
	public ArrayList<Integer> originalBlockIdSequence;
	
		
	public ControlFlowGraph(Function f)
	{
		start = new Node();
		end = new Node();
		originalFunction = f;
		originalBlockIdSequence = new ArrayList<>();
		
		ArrayList<Node> tempNodeList = new ArrayList<Node>();
		HashMap<Integer, Node> tempBlockMap = new HashMap<Integer, Node>(); 
		
		
		List<Block> blocks = f.blocks;

		for(int i = 0; i < blocks.size(); ++i)
		{	
			List<Instruction> instructions = blocks.get(i).instructions;
			
			// Add the block id to the sequence, to store the block ordering
			originalBlockIdSequence.add(blocks.get(i).id);
			
			for(int j=0; j < instructions.size(); ++j)
			{
				Node n = new Node(blocks.get(i).id, instructions.get(j));	
				if(j == 0)
				{
					tempBlockMap.put(blocks.get(i).id, n);
					if(i==0)
						//This is the root node
						start.addSuccessor(n);
				}
				tempNodeList.add(n);		
			}
		}
		
		//Create the links
		
		for(int i = 0; i < tempNodeList.size(); ++i)
		{
			Node n = tempNodeList.get(i);
			Instruction st = n.getInstruction();
			if(st instanceof BrInstruction)
			{
				int trueId = ((BrInstruction) st).blockTrue;
				int falseId = ((BrInstruction) st).blockFalse;
				if(falseId != trueId)
				{
					n.addSuccessor(tempBlockMap.get(trueId));
				}
				n.addSuccessor(tempBlockMap.get(falseId));
				
			}
			else if(st instanceof RetInstruction)
			{
				end.addPredecessor(n);
			}
			else if(i < tempNodeList.size() - 1)
			{
				n.addSuccessor(tempNodeList.get(i + 1));
			}
			//Error ? or end of function without return?
		}
	}
	
	public void removeUnreachableCode()
	{
		Queue<Node> queue = new LinkedList<Node>();
		
		List<Node> visited = new ArrayList<Node>();
		
		queue.add(start);
		while(!queue.isEmpty())
		{
			Node n = queue.poll();
			visited.add(n);
			
			for(Node s : n.getAllSuccessors())
			{
				if(!visited.contains(s))
				{	
					s.clearPredecessors();
					queue.add(s);
				}
				s.addPredecessor(n);
			}			
		}
	}
	
	public Function convertToFunction()
	{
		Function f = new Function(originalFunction.id, originalFunction.args);
		HashMap<Integer, Block> blockMap = new HashMap<Integer, Block>();
		
		Stack<Node> stack = new Stack<Node>();
		List<Node> visited = new ArrayList<Node>();
		
		stack.push(start);
		while(!stack.isEmpty())
		{
			Node n = stack.pop();
			visited.add(n);
			
			int bId = n.getBlockId();
			if(!blockMap.containsKey(bId))
				blockMap.put(bId, new Block(bId));				
			blockMap.get(bId).instructions.add(n.getInstruction());
			
			for(Node succ : n.getAllSuccessors())
			{
				if(!visited.contains(succ))
					stack.push(succ);
			}
		}
		
		/*Insert into the Function in the original order*/
		for(int id : originalBlockIdSequence)
		{
			if(blockMap.containsKey(id))
				f.blocks.add(blockMap.get(id));
		}
		
		return f;
	}
	
	
	
	
	public String toString()
	{
		String output = "digraph " + originalFunction.id + " {\n";
		
		LinkedList<Node> queue = new LinkedList<>();
		ArrayList<Node> visited = new ArrayList<>();
		
		queue.add(start);
		
		HashMap<Node, Character> nodeChars = new HashMap<>();
		
		char current = 'A';
		
		// Setup all nodes with unique characters...
		while (!queue.isEmpty())
		{
			Node n = queue.poll();
			visited.add(n);
			
			// Watch out for the start/end nodes with no instructions...
			output += "\t" + current + " [label=\"" + n + "\"];\n";				
			
			nodeChars.put(n, current++);
			
			for (Node s : n.getAllSuccessors())
			{
				if (!visited.contains(s)) {
					queue.add(s);
				}
			}
		}
		
		output += '\n';
				
		// Iterate through visited nodes, print all the links...
		for (Node currentNode : visited) {
			Set<Node> successors = currentNode.getAllSuccessors();
			
//			System.out.println(currentNode);
//			System.out.println(successors);
			for (Node neighbour : successors) {
				output += "\t" +
						nodeChars.get(currentNode) + " -> " +
						nodeChars.get(neighbour) + ";\n";
			}
		}
		output += "}\n";
		
		return output;
	}
	

} 
