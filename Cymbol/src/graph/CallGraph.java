package graph;

import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class CallGraph {
	private List<String> funcNames = new ArrayList<String>();
	private Map<String, List<String>> funcCalls = new HashMap<String, List<String>>();
	
	public List<String> getDeclaredFunctions() {
		return funcNames;
	}
	
	public Map<String, List<String>> getFunctionCalls() {
		return funcCalls;
	}
	
	public boolean declareToken(Token funcDecl) {
		String name = funcDecl.getText();
		if(!funcNames.contains(name)) {
			funcNames.add(name);
			return true;
		} else {
			return false;
		}
	}
	
	public void assertFunctionCall(Token caller, Token callee) {
		String callerName = caller.getText();
		String calleeName = callee.getText();
		
		List<String> callees;
		if(funcCalls.containsKey(callerName)) {
			callees = new ArrayList<String>();
		} else {
			callees = funcCalls.get(callerName);
		}
		
		if(!callees.contains(calleeName)) {
			callees.add(calleeName);
		} else {
			System.out.println("Dropped repeated call from " + callerName + " to " + calleeName);
		}
		
		funcCalls.put(callerName, callees);
	}
}
