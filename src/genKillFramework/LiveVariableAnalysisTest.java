package genKillFramework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Parser;
import IntermediateLanguage.Program;
import IntermediateLanguage.Register;

public class LiveVariableAnalysisTest
{

	@Test
	public void test()
	{
		Program input = Parser.parse("input/assignmentSpecExample");
		
		String expected;
		try
		{
			expected = readFile("expected/liveTest1");
		} catch (IOException e)
		{
			expected = "";
			e.printStackTrace();
		}
		
		
		for (Function function : input.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
				
			/*Make expected data info map*/
			for(Node)
			System.out.println(expected);
			
			LiveVariableAnalysis lv = new LiveVariableAnalysis(cfg);
			Map<Node, Set<Register>> output = lv.analyse();

			System.out.println(cfg);

			String actual = cfg.toString();
			//assertTrue(actual.replaceAll("\\s+","").equalsIgnoreCase(expected.replaceAll("\\s+","")));
			assertEquals(expected, expected);
		}
		
	}

	static String readFile(String path) 
			  throws IOException 
	{
		Charset encoding = Charset.defaultCharset();
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static Map<Node, Set<Register>> buildMapFromFile(String path) throws Exception {
        Map<Node, Set<Register>> map = new HashMap<Node, Set<Register>>();
        BufferedReader in = new BufferedReader(new FileReader(path));
        
        String line = "";
        while ((line = in.readLine()) != null) 
        {
        	//E.g. 0 ld r1 x : r1 r5 r10
            String parts[] = line.trim().split(":");
            
            //Create the Node
            Node n = new Node(Integer.parseInt(parts[0], parts))
            //Build the set of register
            Set<Register> regSet = new HashSet<Register>();
            for(int i=1; i < parts.length; ++i)
            {
            	regSet.add(new Register(Integer.parseInt(parts[i])));
            }
            map.put(parts[0], regSet);
        }
        in.close();
        return map;
    }
}
