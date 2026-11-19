import java.util.ArrayList;
import java.util.Set;

public class VarProduction {
	private char lhsVar;					/// The left-hand side of the production
	private Set<String> productions;	/// The list of productions the var has.
	
	public VarProduction(char lhsVar, Set<String> productions) {
		this.lhsVar = lhsVar;
		this.productions = productions;
	}
	
	public char getLHS() {
		return lhsVar;
	}
	
	public String getProduction(int index) {
		return ((String[])productions.toArray())[index];
	}
	
	public int getNumOfProds(){
		return productions.size();
	}
	
}
