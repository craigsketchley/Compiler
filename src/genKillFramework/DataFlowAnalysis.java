package genKillFramework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CFG.*;
import IntermediateLanguage.Register;

public abstract class DataFlowAnalysis<T>
{
	public boolean isForward;
	
	public abstract Set<T> gen(Node n);
	
	public abstract Set<T> kill(Node n);

	/* Handles the OUT[B] = meet(IN[B]) 
	 * or IN[B] = meet(OUT[B]) internally for each node
	 */
	public abstract void meet(Node n);
	
	/* Handles the transfer IN[B] = transfer(OUT[B]) 
	 * or OUT[B] = transfer(IN[B]) internally for each node
	 */
	public abstract void transfer(Node n);
	
}
