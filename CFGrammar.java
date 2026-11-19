import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class CFGrammar {
	Hashtable<Character, Set<String>> listOfProductions = new Hashtable<>();
	
	public CFGrammar() {}
	
	public void addProductions(VarProduction vp) {
		
		Set<String> productions = listOfProductions.get(vp.getLHS());
		
		// This lhs doesn't have productions yet
		if (productions == null) {
			productions = new HashSet<>();
			listOfProductions.put(vp.getLHS(), productions);
		}
		
		for (String rhs: productions) {
			productions.add(rhs);
		}
	}
	
	public void removeLambdaProds() {
		// First, look for lambda productions
		ArrayList<Character> nullableVars = new ArrayList<>();
		
		for (Character lhs: listOfProductions.keySet()) {
			Set<String> prods = listOfProductions.get(lhs);
			
			for (String rhs: prods) {
				if (rhs.length() == 0) {
					nullableVars.add(lhs);
					break;
				}
			}
		}
		
		// Second, look for productions that only contain nullable variables
		boolean keepLooking = true;	// If list of nullable vars is updated, keep looking
		while (keepLooking) {
			keepLooking = false;
			
			// Check all Productions
			for (Character lhs: listOfProductions.keySet()) {
				if (nullableVars.contains(lhs)) continue; // Skip confirmed nullable
				Set<String> prods = listOfProductions.get(lhs);
				
				// If the production only consists of nullable vars,
				// it is nullable as well.
				for (String rhs: prods) {
					// Count nullable vars in production
					int numOfNullable = 0;
					for (char c: rhs.toCharArray()) {
						if (nullableVars.contains(c))
							++numOfNullable;
					}
					// If Prod only consists of nullable vars
					// Add it to nullable vars
					if (numOfNullable == rhs.length()) {
						nullableVars.add(lhs);
						keepLooking = true;
						break;
					}
				}
			}	
		}
		
		// TODO: Modify productions including nullable vars
		
	}
	
	public void removeUnitProds() {
		// First, look for unit prods
		ArrayList<Character> unitVars = new ArrayList<>();
		
		for (Character lhs: listOfProductions.keySet()) {
			Set<String> prods = listOfProductions.get(lhs);
			
			for (String rhs: prods) {
				// If the rhs only contains one variable, it is a unit production
				if (rhs.length() == 1 && Character.isUpperCase(rhs.charAt(0))) {
					unitVars.add(lhs);
					break;
				}
			}
		}
		
		// TODO: Substitute unit productions
		Hashtable<Character, Set<String>> oldProductions 
		= (Hashtable<Character, Set<String>>)listOfProductions.clone();
		
	}
	
	public void removeUselessProds() {
		// First, look for terminal prods
		ArrayList<Character> usefulVars = new ArrayList<>();
				
		for (Character lhs: listOfProductions.keySet()) {
			Set<String> prods = listOfProductions.get(lhs);
					
			for (String rhs: prods) {
				boolean isUseful = true;
				
				for (char c: rhs.toCharArray()) {
					if (Character.isUpperCase(c))
						isUseful = false;
				}
				if (isUseful) {
					usefulVars.add(lhs);
					break;
				}
			}
		}
		
		// Second, look for useful variable productions
		boolean keepLooking = true;	// If list of useful vars is updated, keep looking
		while (keepLooking) {
			keepLooking = false;
			
			// Check all Productions
			for (Character lhs: listOfProductions.keySet()) {
				if (usefulVars.contains(lhs)) continue; // Skip confirmed useful
				Set<String> prods = listOfProductions.get(lhs);
				
				// If the production only consists of useful vars or terminals,
				// it is useful as well.
				for (String rhs: prods) {
					// Count useful vars in production
					int numOfUseful = 0;
					int numOfVars = 0;
					for (char c: rhs.toCharArray()) {
						if (Character.isUpperCase(c))
							++numOfVars;
						
						if (usefulVars.contains(c))
							++numOfUseful;
					}
					// Prod only consists of terminals or useful vars
					if (numOfUseful == numOfVars && rhs.length() > 0) {
						usefulVars.add(lhs);
						keepLooking = true;
						break;
					}
				}
			}
		}
		
		// Remove useless vars
		ArrayList<Character> uselessVars = new ArrayList<>();

		for (Character lhs: listOfProductions.keySet()) {
			if (!usefulVars.contains(lhs)) {
				uselessVars.add(lhs);
				// Remove var from productions
				for (Character lhs2: listOfProductions.keySet()) {
					Set<String> newProds = new HashSet<>();
					for (String rhs: listOfProductions.get(lhs2)) {
						String newProd = rhs.replaceAll(lhs.toString(), "");
						newProds.add(newProd);
					}
					listOfProductions.replace(lhs2, newProds);

				}
			}
		}

		for (Character c: uselessVars) {
			listOfProductions.remove(c);
		}
		
		// TODO: Remove unreachable vars
		

	}
	
}
