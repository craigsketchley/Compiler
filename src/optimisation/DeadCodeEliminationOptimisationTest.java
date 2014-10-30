package optimisation;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import IntermediateLanguage.Parser;
import IntermediateLanguage.Program;

public class DeadCodeEliminationOptimisationTest
{

	@Test
	public void test()
	{
		Program input = Parser.parse("input/assignmentSpecExample");
		Program expected = Parser.parse("expected/dTest1");
		
		System.out.println(expected);
		Program output = null;
		try
		{
			output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(output);
		assertEquals(expected.toString(), output.toString());
	}
}
