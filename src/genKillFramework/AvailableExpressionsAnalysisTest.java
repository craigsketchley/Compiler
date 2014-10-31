package genKillFramework;

import intermediateLanguage.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cfg.*;

/**
 * Tests the Available Expressions analysis.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class AvailableExpressionsAnalysisTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Program input = Parser.parse("input/redundantLoadsAssignmentSpec");
		
		for (Function function : input.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
				
			AvailableExpressionsAnalysis lv = new AvailableExpressionsAnalysis(cfg);
			Map<Node, Set<String>> output = lv.analyse();

			System.out.println(cfg);

			System.out.println("***PRINTING MAP***");
			for(Node k : cfg.getAllNodes())
			{
				System.out.println(String.format("Node: %s - Map: %s", k, output.get(k)));
			}
		}
	}

}
