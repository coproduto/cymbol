package graph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GraphWriter {
	private Map<String, String> functionsToNodes = new HashMap<String, String>();
	
	private String formatNode(int index, String type, String name, int argCount) {
		String args = new String(new char[argCount]).replace("\0", "int, ");
		if(argCount > 0) {
			args = args.substring(0, args.length() - 2);
		} else {
			args = args + " ";
		}
		return ("n" + index + 
				" [label=\"" + type + " " + name + "(" + args + ")\"]"
				+ "\n");
	}
	
	private String writeNode(CallGraph g, String functionName, int nodeIndex) {
		String type = g.getTypeOfFunction(functionName);
		Integer argCount = g.argumentCountOfFunction(functionName);
		
		functionsToNodes.put(functionName, "n" + nodeIndex);
		
		if(type == null) {
			System.out.println("No type info for function " + functionName);
			type = "void";
		} 
		
		if(argCount == null) {
			System.out.println("No argument count for function " + functionName);
			argCount = 0;
		}
		
		return formatNode(nodeIndex, type, functionName, argCount);
	}
	
	private String writeCall(String caller, String callee) {
		String callerNode = functionsToNodes.get(caller);
		String calleeNode = functionsToNodes.get(callee);
		
		if(callerNode == null) {
			System.out.println("No node for caller function " + caller);
		}
		
		if(calleeNode == null) {
			System.out.println("No node for called function " + callee);
		}
		
		if(callerNode != null && calleeNode != null) {
			return callerNode + " -> " + calleeNode;
		} else {
			return "?? -> ??";
		}
	}
	
	
	
	public String writeDotFileContents(CallGraph g) {
		String file = "";
		
		file = file + "digraph CallGraph {\n";
		Iterator<String> functionNameIterator = g.getDeclaredFunctions().iterator();
		Iterator<Map.Entry<String, List<String>>> functionCallIterator = g.getFunctionCalls().entrySet().iterator();
		int nodeIndex = 1;
		while(functionNameIterator.hasNext()) {
			String functionName = functionNameIterator.next();
			file = file + this.writeNode(g, functionName, nodeIndex);
			nodeIndex += 1;
		}
		if(nodeIndex > 1) {
			file = file + "\n";
		}
		int writtenCalls = 0;
		while(functionCallIterator.hasNext()) {
			Map.Entry<String, List<String>> call = functionCallIterator.next();
			Iterator<String> calledFunctionsIterator = call.getValue().iterator();
			while(calledFunctionsIterator.hasNext()) {
				String calledFunction = calledFunctionsIterator.next();
				file = file + this.writeCall(call.getKey(), calledFunction);
			}
			writtenCalls += 1;
		}
		if(writtenCalls > 0) {
			file = file + "\n";
		}
		
		file = file + "}\n";
		
		return file;
	}
}
