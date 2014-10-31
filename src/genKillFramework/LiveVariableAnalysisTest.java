package genKillFramework;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.*;

public class LiveVariableAnalysisTest
{

	@Test
	public void test()
	{
		Program input = Parser.parse("input/deadCodeAssignmentSpec");
		
//		Map<Node, Set<Register>> expected = null;
//		try
//		{
//			expected = buildMapFromFile("expected/liveTest1");
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		
		
		for (Function function : input.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
				
//			System.out.println(expected);
			
			LiveVariableAnalysis lv = new LiveVariableAnalysis(cfg);
			Map<Node, Set<Register>> output = lv.analyse();

			System.out.println(cfg);

			System.out.println("***PRINTING MAP***");
			for(Node k : cfg.getAllNodes())
			{
				System.out.println(output.get(k));
			}
//			String actual = cfg.toString();
			//assertTrue(actual.replaceAll("\\s+","").equalsIgnoreCase(expected.replaceAll("\\s+","")));
//			assertEquals(expected, expected);
		}
		
	}

	@Test
	public void testDoubleReturn()
	{
		Program input = Parser.parse("input/deadCodeDoubleReturn");
	
		for (Function function : input.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
				
			LiveVariableAnalysis lv = new LiveVariableAnalysis(cfg);
			Map<Node, Set<Register>> output = lv.analyse();

			System.out.println(cfg);

			System.out.println("***PRINTING MAP***");
			for(Node k : cfg.getAllNodes())
			{
				System.out.println(String.format("Node: %s - Map: %s", k, output.get(k)));
			}
		}
		
	}
	
	
	
	
	
	
	static String readFile(String path) 
			  throws IOException 
	{
		Charset encoding = Charset.defaultCharset();
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
//	public static Map<Node, Set<Register>> buildMapFromFile(String path) throws Exception {
//        Map<Node, Set<Register>> map = new HashMap<Node, Set<Register>>();
//        BufferedReader in = new BufferedReader(new FileReader(path));
//        
//        String line = "";
//        while ((line = in.readLine()) != null) 
//        {
//        	//E.g. 0 ( ld r1 x ) : r1 r5 r10
//            String parts[] = line.trim().split(":");
//            int blockId = Integer.parseInt(parts[0].trim().split(" ",2)[0]);
//            Instruction instruction = Parser.parseInstruction(parts[0].trim().split(" ",2)[1]);
//            //Create the Node
//            Node n = new Node(blockId, instruction);
//            //Build the set of register
//            Set<Register> regSet = new HashSet<Register>();
//            for(int i=1; i < parts.length; ++i)
//            {
//            	regSet.add(new Register(Integer.parseInt(parts[i].substring(1, parts[i].length()))));
//            }
//            map.put(n, regSet);
//        }
//        in.close();
//        return map;
//    }
}
