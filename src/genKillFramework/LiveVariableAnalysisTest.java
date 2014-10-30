package genKillFramework;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import CFG.ControlFlowGraph;
import IntermediateLanguage.Function;
import IntermediateLanguage.Parser;
import IntermediateLanguage.Program;

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
				
			System.out.println(expected);
			
			LiveVariableAnalysis lv = new LiveVariableAnalysis();
			GenKill gk = new GenKill(cfg, lv);
			gk.analyseBackward();
			
			System.out.println(cfg);

			//String actual = cfg.toString();
			//assertEquals(actual.toString(), expected.toString());
		}
		
	}

	static String readFile(String path) 
			  throws IOException 
	{
		Charset encoding = Charset.defaultCharset();
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
