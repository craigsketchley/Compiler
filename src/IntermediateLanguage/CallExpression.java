package IntermediateLanguage;

import java.util.List;

public class CallExpression implements Expression {
	
	public String id;
	public List<String> args;
	
	CallExpression(String id, List<String> args) {
		this.id = id;
		this.args = args;
	}

}
