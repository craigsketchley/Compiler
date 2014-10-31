package cfg;

import intermediateLanguage.Block;
import intermediateLanguage.BrInstruction;
import intermediateLanguage.Function;
import intermediateLanguage.Instruction;
import intermediateLanguage.RetInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Holds the Control Flow Graph for a given function from the AST. It can run
 * unreachable code optimisation. 
 * 
 * The CFG has sentinel nodes linked to the entry, and to all exit points of
 * the function. Additionally, links in the graph are bidirectional. This
 * allows us to easily traverse the CFG in either direction.
 * 
 * Each node in the graph represents a single command, allowing for highly
 * granular optimisations.
 * 
 * Each node stores its original block ID, and we remember the sequence in
 * which they were read. This allows rebuilding of the AST from the CFG.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class ControlFlowGraph 
{
	private Node start; //sentinel node before the entry point of the function
	private Node end; //sentinel node reached from all return statements
	private Function originalFunction; //part of the original AST 
	private List<Integer> originalBlockIdSequence;
	private List<Node> allNodes; //holds all live nodes, updated if optimised.
	
	/**
	 * Construct a CFG based on a given function.
	 * 
	 * @param function The function to analyse
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
				//Get the block ID to branch to on true, and make sure it exists
				int trueId = ((BrInstruction) st).blockTrue;
				if(!tempBlockMap.containsKey(trueId))
				{
					throw new RuntimeException(
							"Branch instruction to a block ID that does not exist: " + trueId);
				}

				//Get the block ID to branch to on false, and make sure it exists
				int falseId = ((BrInstruction) st).blockFalse;
				if(!tempBlockMap.containsKey(falseId))
				{
					throw new RuntimeException(
							"Branch instruction to a block ID that does not exist: " + falseId);
				}
				
				//Link this node to the node starting the true block
				n.addSuccessor(tempBlockMap.get(trueId));
				tempBlockMap.get(trueId).addPredecessor(n);

				//If the true/false blocks are different, link to the false block too
				if(falseId != trueId)
				{
					n.addSuccessor(tempBlockMap.get(falseId));
					tempBlockMap.get(falseId).addPredecessor(n);
				}
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
	 * using a DFS.
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
	 * 
	 * Cannot be called on a sentinel Node or a Node containing a
	 * BrInstruction, RetInstruction.
	 * 
	 * @throws RuntimeException 
	 */
	public void removeNode(Node inNode) throws RuntimeException
	{
		if (!allNodes.contains(inNode))
		{
			throw new RuntimeException(
					String.format("CFG does not contain the provided node: %s", inNode));
		}
		
		if (inNode.isSentinel())
		{
			throw new RuntimeException("Cannot remove the start or end node");
		}
		
		// TODO: Currently cannot remove branch statements explicitly, handled by unreachable code?
		if (inNode.getInstruction() instanceof BrInstruction)
		{
			throw new RuntimeException(String.format("Cannot remove branch instruction: %s", inNode));
		}
		
		// TODO: Currently cannot remove return statements explicitly, handled by unreachable code?
		if (inNode.getInstruction() instanceof RetInstruction)
		{
			throw new RuntimeException(String.format("Cannot remove return instruction: %s", inNode));
		}
		
		// Find the node in the CFG, and remove it by updating links...
		Set<Node> predecessors = inNode.getAllPredecessors();
		Set<Node> successors = inNode.getAllSuccessors();
		
		if (successors.size() != 1)
		{
			// Something went horribly wrong. Should only have 1 successor.
			throw new RuntimeException(
				String.format("This instruction should have only 1 successor! %s", inNode));
		}
		Node successor = successors.iterator().next(); // Should only contain 1 element.
		
		// Detach inNode from successor.
		successor.removePredecessor(inNode);
		
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
	 * Converts and outputs the current state of the Control Flow Graph as a function.
	 * 
	 * @return the reachable nodes of the control flow graph as a function AST.
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
	 * A Breadth First Search (BFS) of the Control Flow Graph.
	 * 
	 * @param forward if true, traverse from start->end, else from end->start
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
	 * A Depth First Search (DFS) of the Control Flow Graph.
	 * 
	 * @param forward if true, traverse from start->end, else from end->start
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
	
	/**
	 * Returns the list of all the nodes within the CFG.
	 * 
	 * @return list of all the nodes in the CFG
	 */
	public List<Node> getAllNodes()
	{
		return new ArrayList<Node>(allNodes);
	}
	
	/**
	 * Override of the toString method. Outputs a representation of the CFG,
	 * suitable for use with graphviz.
	 */
	@Override
	public String toString()
	{
		HashMap<Node, String> nodeLabels = new HashMap<Node, String>();

		// Start a digraph
		StringBuilder output = new StringBuilder(
				String.format("digraph %s {\n", originalFunction.id));

		// Create all nodes with unique labels
		int index = 0;
		for(Node n : allNodes) {
			String label = "n" + index;
			output.append(String.format("\t%c [label=\"%s\"];\n", label, n));
			nodeLabels.put(n, label);
			++index;
		}
		output.append('\n');

		// Iterate through visited nodes, print all the links...
		for(Node n : allNodes) {
			for(Node m : n.getAllSuccessors()) {
				output.append(String.format("\t%c -> %c;\n",
						nodeLabels.get(n), nodeLabels.get(m)));
			}
		}
		// End the digraph scope
		output.append("}\n");
		
		return output.toString();
	}

} 
