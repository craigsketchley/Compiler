package genKillFramework;

import java.util.Set;

import CFG.*;

public interface DataFlowAnalysis<T>
{
	public Set<T> gen(Node n);
	
	public Set<T> kill(Node n);

	/* Handles the OUT[B] = meet(IN[B]) 
	 * or IN[B] = meet(OUT[B]) internally for each node
	 */
	public void meet(Node n);

	/* Handles the transfer IN[B] = transfer(OUT[B]) 
	 * or OUT[B] = transfer(IN[B]) internally for each node
	 */
	public void transfer(Node n);
	
	public void analyse(ControlFlowGraph cfg);
}