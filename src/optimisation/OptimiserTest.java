package optimisation;

import static org.junit.Assert.assertEquals;
import intermediateLanguage.Function;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;

import org.junit.Test;

import cfg.ControlFlowGraph;

public class OptimiserTest {

	@Test
	public void testUnreachableAndDeadCode() {
		Program input = Parser.parse("input/optimiserUnreachableAndDeadCode");
		Program expected = Parser.parse("expected/optimiserUnreachableAndDeadCode");
		
		Program output = new Program();
		
		for (int i = 0; i < input.functions.size(); i++) {
			Function functionIn = input.functions.get(i);
			
			ControlFlowGraph cfg = new ControlFlowGraph(functionIn);
			cfg.removeUnreachableCode();
			output.functions.add(cfg.convertToFunction());
		}
		
		output = Optimiser.optimise(output, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

}
