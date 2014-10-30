package genKillFramework;

import java.util.Set;

import CFG.*;
import IntermediateLanguage.Register;

public interface DataFlowAnalysis<T>
{
	public abstract Set<T> gen(Node n);
	
	public abstract Set<T> kill(Node n);

	public abstract Set<T> meet(Set<T> s1, Set<T> s2);
	
	public abstract Set<Register> transfer(Node n);
}
