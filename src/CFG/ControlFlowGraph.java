package CFG;

import genKillFramework.Optimisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import IntermediateLanguage.Block;
import IntermediateLanguage.BrInstruction;
import IntermediateLanguage.Function;
import IntermediateLanguage.Instruction;
import IntermediateLanguage.RetInstruction;

public class ControlFlowGraph 
{
	public Node start; 
	public Node end;
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
	
	/**
	 * This optimisation attempts to remove any unreachable code,
	 * using a DFS
	 */
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
	
	public void flushControlFlowInfo()
	{
		
	}
	
	public String toString()
	{
		StringBuffer output = new StringBuffer("digraph " + originalFunction.id + " {\n");
		
		List<Node> nodes = bfs(true);
		HashMap<Node, Character> nodeChars = new HashMap<>();
		char current = 'A';
		
		// Setup all nodes with unique characters...
		for (Node n : nodes) {
			output.append("\t" + current + " [label=\"" + n + "\"];\n");
			nodeChars.put(n, current++);
		}
		
		output.append('\n');
				
		// Iterate through visited nodes, print all the links...
		for (Node currentNode : nodes) {
			Set<Node> successors = currentNode.getAllSuccessors();
			for (Node neighbour : successors) {
				output.append("\t" +
						nodeChars.get(currentNode) + " -> " +
						nodeChars.get(neighbour) + ";\n");
			}
		}
		output.append("}\n");
		
		return output.toString();
	}
	
	/**
	 * for liveness
	 * 
	 * 2 point lattice (live or not live)
	 * - represented as the set "out", where existence in set == live
	 * 
	 * at node n, each iteration:
	 * 	union for each successor node m:
	 * 			{registers_referenced_by_m} union { out_m / registers_set_by_m }
	 *  union out_n (allowing for fixed-point iteration)
	 *  
	 *  Note that the order in which we evaluate the nodes in each iteration is irrelevant
	 *  as registers_referenced_by_m will build up eventually.
	 *  going from end to start will be faster, but we can't guarantee a perfect order, since
	 *  the graph may have cycles.
	 *  
	 *  A BFS would be a reasonable heuristic for an ordering
	 */
	
	/**
	 * @param forward
	 * @return returns a breadth first search ordering from start->end or end->start
	 */
	public List<Node> bfs(boolean forward) {

		Queue<Node> queue = new LinkedList<Node>(); //pending nodes
		Set<Node> visited = new HashSet<Node>(); //visited nodes, set for fast lookup
		List<Node> ordered = new ArrayList<Node>(); //the ordering of the nodes

		Node node = forward ? start : end; //current node
		Set<Node> children = null; //children of current node
		queue.add(node);

		while(!queue.isEmpty()) {
			node = queue.poll();
			visited.add(node);
			ordered.add(node);
			//choose successors or predecessors depending on direction of search
			children = forward ? node.getAllSuccessors() : node.getAllPredecessors();
			for(Node child : children) {
				if(!visited.contains(child)) {	
					queue.add(child);
				}
			}
		}
		return ordered;
	}

	/**
	 * @param forward
	 * @return returns a depth first search ordering from start->end or end->start
	 */
	public List<Node> dfs(boolean forward) {

		Stack<Node> stack = new Stack<Node>(); //pending nodes
		Set<Node> visited = new HashSet<Node>(); //visited nodes, set for fast lookup
		List<Node> ordered = new ArrayList<Node>(); //the ordering of the nodes

		Node node = forward ? start : end; //current node
		Set<Node> children = null; //children of current node
		stack.add(node);

		while(!stack.isEmpty()) {
			node = stack.pop();
			visited.add(node);
			ordered.add(node);
			//choose successors or predecessors depending on direction of search
			children = forward ? node.getAllSuccessors() : node.getAllPredecessors();
			for(Node child : children) {
				if(!visited.contains(child)) {	
					stack.add(child);
				}
			}
		}
		return ordered;
	}


} 
