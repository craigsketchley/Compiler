package genKillFramework;

import static org.junit.Assert.*;

import org.junit.Test;

import CFG.ControlFlowGraph;
import IntermediateLanguage.Function;
import IntermediateLanguage.Parser;
import IntermediateLanguage.Program;

public class GenKillTest
{

	@Test
	public void test()
	{
		Program program = Parser.parse("input/example");
		for (Function f : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(f);
			
			System.out.println(cfg);
			
			LiveVariableAnalysis lv = new LiveVariableAnalysis();
			GenKill gk = new GenKill(cfg, lv);
			gk.analyseBackward();
			
			System.out.println(cfg);
		}
	}

}
