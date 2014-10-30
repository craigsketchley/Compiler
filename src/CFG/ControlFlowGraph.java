package CFG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	public Node start; //sentinel node before the entry point of the function
	public Node end; //sentinel node reached from all return statements
	public Function originalFunction; //part of the original AST 
	public List<Integer> originalBlockIdSequence;
	public List<Node> allNodes; //holds all live nodes, updated if optimised.
	

	/**
	 * Construct a CFG based on a given function
	 * @param function
	 */
	public ControlFlowGraph(Function function)
	{
		this.start = new Node();
		this.end = new Node();
		this.originalFunction = function;
		this.originalBlockIdSequence = new ArrayList<>();
		this.allNodes = new ArrayList<>();
		this.allNodes.add(this.start);
		
		HashMap<Integer, Node> tempBlockMap = new HashMap<Integer, Node>(); 
		
		List<Block> blocks = originalFunction.blocks;

		//Process the instructions into CFG nodes.
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
					if(i == 0)
					{
						//This is the root node
						start.addSuccessor(n);
						n.addPredecessor(start);
					}
				}
				this.allNodes.add(n);		
			}
		}
		
		//Create the links between CFG nodes.
		for(int i = 0; i < this.allNodes.size(); ++i)
		{
			Node n = this.allNodes.get(i);
			Instruction st = n.getInstruction();
			
			//if the statement is a break instruction
			if(st instanceof BrInstruction)
			{
				int trueId = ((BrInstruction) st).blockTrue;
				int falseId = ((BrInstruction) st).blockFalse;
				if(falseId != trueId)
				{
					n.addSuccessor(tempBlockMap.get(trueId));
					tempBlockMap.get(trueId).addPredecessor(n);
				}
				n.addSuccessor(tempBlockMap.get(falseId));
				tempBlockMap.get(falseId).addPredecessor(n);
			}
			
			//otherwise, if the statement is a return instruction, it is followed by end
			else if(st instanceof RetInstruction)
			{
				n.addSuccessor(end);
				end.addPredecessor(n);
			}
			
			//otherwise, it is followed by the next one, if we are not at the end
			else if(i < this.allNodes.size() - 1)
			{
				n.addSuccessor(this.allNodes.get(i + 1));
				this.allNodes.get(i + 1).addPredecessor(n);
			}
			
			//otherwise, error? or end of function without return?
		}
		this.allNodes.add(this.end);
	}
	
	/**
	 * This optimisation attempts to remove any unreachable code,
	 * using a DFS
	 */
	public void removeUnreachableCode()
	{
		//perform a dfs to find all reachable nodes
		//update all nodes to include only reachable.
		allNodes = dfs(true);
		
		//for each reachable node
		for(Node n : allNodes) {
			//iterate over all predecessors, cutting links from unreachable nodes
			Iterator<Node> it = n.getAllPredecessors().iterator();
			while(it.hasNext()) {
				if(!allNodes.contains(it.next())) {
					it.remove();
				}
			}
			/* we only remember the sentinel start/end nodes,
			 * the unreachable nodes will be garbage collected
			 */
		}
	}
	
	/**
	 * Given a node, remove the node from this CFG.
	 * @throws Exception 
	 * 
	 * TODO: Use custom exceptions.
	 */
	public void removeNode(Node inNode) throws Exception
	{
		if (!allNodes.contains(inNode))
		{
			throw new Exception(String.format("CFG does not contain the provided node: %s", inNode));
		}
		
		if (inNode.isSentinel())
		{
			throw new Exception("Cannot remove the start or end node");
		}
		
		// TODO: Currently cannot remove branch statements explicitly, handled by unreachable code?
		if (inNode.getInstruction() instanceof BrInstruction)
		{
			throw new Exception(String.format("Cannot remove branch instruction: %s", inNode));
		}
		
		// TODO: Currently cannot remove return statements explicitly, handled by unreachable code?
		if (inNode.getInstruction() instanceof RetInstruction)
		{
			throw new Exception(String.format("Cannot remove return instruction: %s", inNode));
		}
		
		// Find the node in the CFG, and remove it by updating links...
		Set<Node> predecessors = inNode.getAllPredecessors();
		Set<Node> successors = inNode.getAllSuccessors();
		
		if (successors.size() > 1)
		{
			// Something went horribly wrong. Should only have 1 successor.
			throw new Exception(
				String.format("This instruction should have more than 1 successor! %s", inNode));
		}
		
		Node successor = successors.iterator().next(); // Should only contain 1 element.
		
		// Detach inNode from successor.
		successor.removePredecessor(inNode);
		inNode.removeSuccessor(successor);
		
		// For all the inNodes predecessors...
		Iterator<Node> it = predecessors.iterator();
		while(it.hasNext())
		{
			Node predecessor = it.next();
			// Detach inNode from predecessor.
			predecessor.removeSuccessor(inNode);
			
			// Hook up inNodes successor and predecessor.
			predecessor.addSuccessor(successor);
			successor.addPredecessor(predecessor);
		}
		
		/*Removing references to predecessors of the removed node*/
		inNode.clearPredecessors();
		// Remove it from the allNodes reference.
		allNodes.remove(inNode);
	}
	
	/**
	 * @return the reachable nodes of the function as an AST.
	 */
	public Function convertToFunction()
	{
		Function function = new Function(originalFunction.id, originalFunction.args);
		HashMap<Integer, Block> blockMap = new HashMap<Integer, Block>();
		
		/* For each reachable node (using a dfs), add its statement to a list
		 * for the appropriate block. We're guaranteed to populate each block
		 * in the correct order.
		 */
		for(Node n : allNodes) {
			int id = n.getBlockId();
			if(!blockMap.containsKey(id)) {
				blockMap.put(id, new Block(id));
			}
			blockMap.get(id).instructions.add(n.getInstruction());
		}
		
		/* Append non-empty blocks to the function in the original order */
		for(int id : originalBlockIdSequence) {
			if(blockMap.containsKey(id)) {
				function.blocks.add(blockMap.get(id));
			}
		}
		
		return function;
	}
		
	/**
	 * String representation of the CFG, suitable for use with graphviz.
	 */
	public String toString()
	{
		HashMap<Node, Character> nodeChars = new HashMap<Node, Character>();
		//TODO: limited to 26 values? Use multiple chars depending on how many required? 'AAA'
		char current = 'A';

		// Start a digraph
		StringBuilder output = new StringBuilder(
				String.format("digraph %s {\n", originalFunction.id));
		// Setup all nodes with unique characters...
		for(Node n : allNodes) {
			output.append(String.format("\t%c [label=\"%s\"];\n", current, n));
			nodeChars.put(n, current);
			current++;
		}
		output.append('\n');

		// Iterate through visited nodes, print all the links...
		for(Node n : allNodes) {
			for(Node m : n.getAllSuccessors()) {
				output.append(String.format("\t%c -> %c;\n",
						nodeChars.get(n), nodeChars.get(m)));
			}
		}
		// End the digraph scope
		output.append("}\n");
		
		return output.toString();
	}
	
	/** TODO
	 * (misplaced) notes for liveness
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
