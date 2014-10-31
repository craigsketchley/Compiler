package genKillFramework;

import intermediateLanguage.Function;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;
import intermediateLanguage.Register;

import java.util.Map;

import lattice.Lattice;

import org.junit.Test;

import cfg.ControlFlowGraph;
import cfg.Node;

public class ConstantFoldingAnalysisTest
{
	@Test
	public void test() {
		Program input = Parser.parse("input/simpleFunction");
		
		for (Function function : input.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
				
			ConstantFoldingAnalysis cfa = new ConstantFoldingAnalysis(cfg);
			Map<Node, Map<Register, Lattice<Integer>>> output = cfa.analyse();

			System.out.println(cfg); 

			System.out.println("***PRINTING MAP***");
			for(Node k : cfg.getAllNodes())
			{
				System.out.println(String.format("Node: %s - Map: %s", k, output.get(k)));
			}
		}
	}

}
