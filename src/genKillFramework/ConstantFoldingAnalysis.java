package genKillFramework;

import java.util.Map;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Register;
import Lattice.Lattice;

public class ConstantFoldingAnalysis extends DataFlowAnalysis<Map<Register, Lattice<Integer>>>
{

	public ConstantFoldingAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Register, Lattice<Integer>>gen(Node n)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Register, Lattice<Integer>> kill(Node n)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Register, Lattice<Integer>> meet(Node n)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateMeet(Map<Register, Lattice<Integer>> map, Node n)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Register, Lattice<Integer>> transfer(Node n)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Node, Map<Register, Lattice<Integer>>> analyse()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
