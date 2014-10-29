package IntermediateLanguage;

public class AssignmentStatement implements Statement {
	
	public String id;
	public Expression expr;
	
	AssignmentStatement(String id, Expression expr) {
		this.id = id;
		this.expr = expr;
	}

}
