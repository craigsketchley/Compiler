package IntermediateLanguage;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Program
{

	public List<Function> functions;
	
	public Program()
	{
		functions = new ArrayList<Function>();
	}
	
	@Override
	public String toString()
	{
		//Iterate over the functions, building the string
		Iterator<Function> it = functions.iterator();
		StringBuilder output = new StringBuilder(String.format("( %s", it.next()));
		while(it.hasNext()) {
			output.append(String.format("\n  %s", it.next()));
		}
		
		//Close the method and output
		output.append(" )");
		return output.toString();
	}
}
