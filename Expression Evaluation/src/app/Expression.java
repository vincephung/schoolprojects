package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	expr = expr.trim(); // removes leading and trailing spaces from the string input
    	expr = expr.replaceAll("\\s+", ""); // removes whitespaces between characters NOT SURE IF IT REMOVES TABS
    	expr = expr.replaceAll("\\t+", "");
    	// Remove tab might need replaceall \\t
    	
    	// MIGHT NEED TO CHANGE NAME FROM STRING TO ST AND 0 GO AFTER 9
    	// might not need the numbers 0123456789 "might have multiple digits"  delims + "0123456789"
    	String noBracketDelims = " \t*+-/()]"+ "0123456789";
    	StringTokenizer string = new StringTokenizer(expr, noBracketDelims); // takes the input and delims are given and  numbers 1-9
    	
    	//CHECK IF ARRAY IS EMPTY SO array[] <-- nothing inside
    	while(string.hasMoreTokens()) {
    		String stringToken = string.nextToken(); // might need to chefck if it starts with ( ex. (varx + 2)
    		if(stringToken.indexOf('[')>=0) { // if there is a [ then the index is greater than or = to 0
	    			int bracketLocation = stringToken.indexOf('[');  // find location of the bracket
	    			String stringArray = stringToken.substring(0,bracketLocation); // create a new string name of the array right before '['
	    			Array newArray = new Array(stringArray);
	    			if(!arrays.contains(newArray)) {
	    				arrays.add(newArray);	
	    			} 
	    			if(!checkDelims(stringToken.substring(bracketLocation+1))){  // this is so that if A[a] it doesnt return A[a, it'll put a into variable
	    				if(stringToken.substring(bracketLocation+1).indexOf('[')>= 0) {
	    					Array newArray2 = new Array(stringToken.substring(bracketLocation+1));
	    				if(!arrays.contains(newArray2)) {
	    					arrays.add(newArray2);
	    				}
	    			}else {
	      			  Variable variables = new Variable(stringToken.substring(bracketLocation+1));
	      			  if(!vars.contains(variables)) {
	      				  vars.add(variables); 
	      			  }
	    			}
	
	    			}
	    			String afterBracket = stringToken.substring(bracketLocation+1);
	    			String newStringArray = null;
	    			while(afterBracket.contains("[")) {
	    				int tempBracketLocation = afterBracket.indexOf('[');  // find location of the bracket
		    			 newStringArray = afterBracket.substring(0,tempBracketLocation); // create a new string name of the array right before '['
		    			Array newArray3 = new Array(newStringArray);
		    			if(!arrays.contains(newArray3)) {
		    				arrays.add(newArray3);	
		    			} 
		    			afterBracket = newStringArray;
	    			}
    			}
    		else { 
  			  if(stringToken.indexOf('.') == 0) { // for some reason it would put decimals as variables , so this fixes that
				  break;
			  }
    			  Variable variables = new Variable(stringToken);
    			  if(!vars.contains(variables)) {
    				  vars.add(variables); 
    			  }
    		}
    }

    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	float result = 0;
    	expr = expr.trim(); // removes leading and trailing spaces from the string input
    	expr = expr.replaceAll("\\s+", ""); // removes whitespaces between characters 
    	expr = expr.replaceAll("\\t+", "");
    	String noBracketDelims = " \t*+-/()]"+"0123456789";

    	
    	//check for brackets or arrays
    	boolean nestExpr = false;
    	
    	//Checks if expression has brackets and takes care of them
    	if(checkBrackets(expr)) {
    		while(checkBrackets(expr)) {
    		int startBracketLocation = 0;
    		int endBracketLocation = 0;
    		String insideArray = null;
    		
    		//just added this section
    		boolean nestedBracket = false;
    		int temp = 0;
    		int temp2 = 0;
    		int temp3 = expr.indexOf(']');
    		int beforeLastEndBracket = expr.indexOf(']');
    		int lastEndBracketLocation = expr.indexOf(']');
    		int lastBracketLocation = 0;
    		int counter = 0;
    		int endBracketCounter = 0;
    		int innerBracketCounter = 0;
    		
    		for(int i = 0; i < expr.length(); i++) {
    			if(expr.charAt(i) == '[') {
    				innerBracketCounter++;
    				startBracketLocation = expr.indexOf('['); //only finds first instance
    				if(counter == 0) {
    				temp2 = startBracketLocation;
    				}
    				else if(counter >= 1) {
    					temp2 = temp; // TEMP2 = TEMP temp2 = 0

    				}
    				temp = lastBracketLocation;
    				lastBracketLocation = expr.indexOf('[',lastBracketLocation+1);
    				if(counter >= 1) {
    					if(lastBracketLocation < 0) {
    						lastBracketLocation = temp;
    					}
    				}
    				counter++;
    			}
    			else if(expr.charAt(i) == ']') {
    				endBracketCounter++;
    				endBracketLocation = expr.indexOf(']');
					if(endBracketCounter>0) {
						beforeLastEndBracket = temp3;
	    				temp3 = lastEndBracketLocation;  // MIGHT NEED TO CHANGE TO TEMP2
					}
					// THIS accounts for A[B[2]] + B[2] So, it'll check A[B[2]] first then B[2]
    				if(endBracketCounter == innerBracketCounter) {
    					break;
    				}
					
    				lastEndBracketLocation = expr.indexOf(']',lastEndBracketLocation+1);
    				
    				if(lastEndBracketLocation < 1) {
    					lastEndBracketLocation = temp3; // might need to change to temp2
    				}

    			}			
    		}
        	counter = 0; // PUT COUNTER OVER HERE

    		
    		// checks if the bracket is nested. so A[B[2]]
			if((lastBracketLocation < lastEndBracketLocation) && (lastBracketLocation > startBracketLocation)) { // might need to be lastbracketlocation not start
				nestedBracket = true;
				nestExpr = true;
			}
			 if((lastBracketLocation > beforeLastEndBracket)){  // checks A[2] + B[2]
				nestedBracket = false;
				nestExpr = false;
			}

    			if(nestedBracket == true) {
    				nestExpr = true;
    				insideArray = expr.substring(startBracketLocation+1,lastEndBracketLocation);
    			}
    			else {
        			insideArray = expr.substring(startBracketLocation+1,endBracketLocation); // MIGHT NOT NEED +1 !!!1
    			}

    			
    			result = evaluate(insideArray,vars,arrays); // this is the value inside of the array so basically the index
    			StringTokenizer arrayString = new StringTokenizer(expr, noBracketDelims); 
    			String stringToken = null;
    			String arrayName = null;
    			int bracketLocation = 0;
				int result2 = Math.round((int)result); // MIGHT NOT NEED ROUND
    			while(arrayString.hasMoreTokens()) {
    				stringToken = arrayString.nextToken();
    				if(stringToken.indexOf('[')>=0) { // if there is a [ then the index is greater than or = to 0
	    	    		bracketLocation = stringToken.indexOf('[');  // find location of the bracket
	    	    		arrayName = stringToken.substring(0,bracketLocation);  // name of the array
    			}

    			// loop goes through array list, finds if the current array matches one in the array list and gets the value of the index, so A[4] = 5;
    			for(int j = 0; j < arrays.size() ;j++) {
    				if(arrays.get(j).name.equals(arrayName)) {
    				result = arrays.get(j).values[result2]; 
    					break;
    				}
    			}
    			if(stringToken.indexOf('[')>0) {
    				break; 
    				}
    			}
    			
    			// convert the answer to a string
    			String stringResult = Float.toString(result);
				if(nestedBracket == false) {
					temp2 = 1;
				}

    			//changed substring endbracket to LAST END BRACKET LOCATION
    			if(nestExpr == true) {
    			expr = expr.substring(0,startBracketLocation-arrayName.length()) + stringResult + expr.substring(lastEndBracketLocation+1); // MIGHT HAVE TO CHANGE RESULT TO SOMETHING 
    			}else {
        			expr = expr.substring(0,startBracketLocation-arrayName.length()) + stringResult + expr.substring(endBracketLocation+1); // MIGHT HAVE TO CHANGE RESULT TO SOMETHING 

    			}
    			if (expr.contains("]")&&(!expr.contains("["))) {
    				expr = removeBracket(expr);
    			}

    	//	}
    	}
    	}
    	
    	//check for parenthesis. Algorithm or plan right now is to check for ( then substring and recurse new string
    	//RIGHT NOW THIS ONLY ACCOUNTS FOR ONE PAIR. the last one, so 2+(3*2) + (9*8) it would find the (9*8)
		for(int k = 0; k < expr.length(); k++) {
    	if(checkParent(expr)) {
    		int startParentLocation = 0;
    		int endParentLocation = 0;
    		String innerString = null;
    		int innerParentCounter = 0;
    		int testInnerParent = 0;
    		for(int i = 0; i < expr.length(); i++) {
    			if(expr.charAt(i) == '(') {
    				startParentLocation = expr.indexOf('('); //only finds first instance
    				if(innerParentCounter == 0) {
    				testInnerParent = expr.indexOf('(',startParentLocation);
    				}
    				else {
    					testInnerParent = expr.indexOf('(',testInnerParent+1);
    				}
    				innerParentCounter++;

    			}
    			// find first occurence of ) and then the inside loop would be the last ( and the first )
    			else if(expr.charAt(i) == ')') {
    				endParentLocation = expr.indexOf(')');
    				break;
    			}
    		}

    			// inner is between last ( occurence and first ) , so ((4+4)+2) 4+4 would be inside
    			innerString = expr.substring(testInnerParent+1,endParentLocation);
    			result = evaluate(innerString,vars,arrays); // might change result to something else
    			String stringResult = Float.toString(result);
    			expr = expr.substring(0,testInnerParent) + stringResult + expr.substring(endParentLocation+1);
    			if (expr.contains(")")&&(!expr.contains("("))) {
    				expr = removeParent(expr);
    			}

    	}
		}
    	// if expression contains only a single number or character variable "base case"
    	StringTokenizer string = new StringTokenizer(expr, delims); // dont think this is even necessary
    		if(!checkDelims(expr)||checkNegative(expr)) { // might need to change this to only contains numbers / chars
    			if(Character.isLetter(expr.charAt(0))) { 
    				for(int i = 0; i < vars.size(); i++) {
    					if(vars.get(i).name.equals(expr)){
    						return vars.get(i).value;      // if the expression only contains a letter such as a WRONG NOT AT CHAR AT 0
    					}
    				}
    			}
    			if(!Character.isLetter(expr.charAt(0))) {
    				return Float.parseFloat(expr); // if the expression only contains a single number, such as 3 CHECK THIS
    			}
    		}
    		
    		// for stuff with no brackets or () so like 3+4 or a + b
	    	if (checkOperators(expr)) {
	    		boolean firstNumNegative = false;
	        	Stack<Character> numOperations = new Stack<Character>();
	        	expr = twoNegative(expr);
	        	expr = plusMinus(expr);
	        	for(int i = 0; i < expr.length();i++) {
	        		
	        		//	just added this, checks if negative number
	        		if(i ==0 && expr.charAt(0) == '-') {
	        			firstNumNegative = true;
	        			continue;
	        		}
	        		
	        		if(expr.charAt(i) == '+'||expr.charAt(i) == '-'||expr.charAt(i) == '/'||expr.charAt(i) == '*') {
	        			if(expr.charAt(i+1)== '-') { //account for 21+-62
	        			numOperations.push(expr.charAt(i));
	        			i = i+2;
	        			 //skip to next iteration of i
	        			}else {
	        				numOperations.push(expr.charAt(i));
	        			}
	        		}
	        	}
	        	StringTokenizer basicString = new StringTokenizer(expr, " \t*+/()[]"); // DELIMS EXCEPT "-" subtracton 
	        	Stack<Float> numberStack = new Stack<Float>();
	        	while(basicString.hasMoreTokens()) {
		    		String subExp = basicString.nextToken();
		    		// for methods like 9-10, you need to put 9 then 10 not whole exp 9-10;
		    		if(hasSubtract(subExp)&&(!checkNegative(subExp))) {
		    			StringTokenizer stringWithNegative = new StringTokenizer(subExp,delims);
		    			while(stringWithNegative.hasMoreTokens()) {
			    			String newSubExp = stringWithNegative.nextToken();
			    			if(firstNumNegative) {
			    				newSubExp = "-"+newSubExp;
			    				firstNumNegative = false;
			    			}
			    			numberStack.push(evaluate(newSubExp,vars,arrays));
		    			}
		    		}
		    		else {
			    		numberStack.push(evaluate(subExp,vars,arrays));

		    		}
		    	}
	
	        	// want to store your original stack in reverse order because OG is 1-> 2-> 3 , so 3 is on top and comes out first
	        	// when you want to do an expression, the first number, 1 should come out first instead
	        	Stack<Float> reverseNumberStack = new Stack<Float>(); 
	        	
	        	// can CHANGE THIS BACK TO WHILE IS EMPTY LOOP
	        	//for (int i = 0; i < numberStack.size()+1; i++) {
	        	while(!numberStack.isEmpty()) {
	        		reverseNumberStack.push(numberStack.pop());
	        	}
	        	
	        	Stack<Character> reverseNumOperations = new Stack<Character>();
	        	
	        	//  CAN CHANGE THIS BACK TO WHILE LOOP
	        	//for (int i = 0; i < numOperations.size()+1; i++) {
	        	while(!numOperations.isEmpty()){
	        		reverseNumOperations.push(numOperations.pop());
	        	}
	        	while(!reverseNumberStack.isEmpty()) { // might change this to reverse num stack not operations
		        	float num1 = reverseNumberStack.pop();

		        	float num2 = reverseNumberStack.pop();
		        	Character operator1 = reverseNumOperations.pop();
		        	Character operator2 = ' ';
		        	if(!reverseNumOperations.isEmpty()){
	        		   operator2 = reverseNumOperations.peek(); // mAYBE POP
		        	}
		        	boolean multOrDivide = false;
		        	multOrDivide = orderOperations(operator1,operator2);
		        	if(multOrDivide == true) { // meaning there is a * or /
		        		operator2 = reverseNumOperations.pop();
		        		float num3 = reverseNumberStack.pop();
		        		result = calculations(num2,num3,operator2);
		        		reverseNumOperations.push(operator1); // push first into stack so 2+4*5 would push the + back inside
		        		//reverseNumberStack.push(num1); MIGHT NEED TO CHANGE SoMeTHIN WITH THIS
		        		if(reverseNumOperations.isEmpty()) {
		        			break;
		        		}else {
		        			reverseNumberStack.push(result);
			        		reverseNumberStack.push(num1);
		        		}
		        		
		        	}
		        	else {
			        	result = calculations(num1,num2,operator1);
			        	if(reverseNumOperations.isEmpty()) {
			        		break;	// MIGHT NOT NEED BREAK
		        	}	else {
		        			reverseNumberStack.push(result); // adds result back to top of stack
		        	}

	        	}
	    	
	    	}
	    	}
    	
    	return result;
    }
	    	
	    	// method to check the calculations
        private static float calculations(float x, float y, char operator) {
    	float answer = 0;
    	if(operator == '+') {
    		answer = x + y;
    	}else if(operator == '-') {
    		answer = x - y;
    	}else if(operator == '*') {
    		answer = x * y;
    	}else if(operator == '/') {
    		answer = x / y;
    	}
    	return answer;
    }
        
        //method for order of operations, for example * and / take presedence
        private static boolean orderOperations(char x, char y) {
        	if ((x == '+' || x == '-') && (y == '*' || y == '/'))  {
        		return true;
        	}
        	return false;
        }
        
        //method checks to see if the expression contains a delim DOES NOT WORK FOR TAB
        // THIS IS WHAT IS MESSING EVERYTHING UP
        private static boolean checkDelims(String expression) {
        	for(int i = 0; i < expression.length(); i++) {
        		if(expression.charAt(i) == ('+') || expression.charAt(i) == ('-') || expression.charAt(i) == ('/')|| 
        				expression.charAt(i) == ('*')|| expression.charAt(i) == ('[')||expression.charAt(i) == (']')
        				||expression.charAt(i) == ('(')||expression.charAt(i) == (')')||expression.charAt(i) == (' ')) {
        			return true;
        		}        			
        	}
        	return false;
        	}
        
        //check if the expression contains two subtraction signs such as 12--91 -- 21 becomes 12 + 91 + 21
        private static String twoNegative(String expression) {
        	if(expression.indexOf("--") >0 ) {
        	int location = 0;
        	String newExpression = null;
        	location = expression.indexOf("--");
        	while(location > 0) {
        		newExpression = expression.substring(0,location) + "+" + expression.substring(location + 2); // replaces -- with +
        		location = newExpression.indexOf("--",location+1);
        	}
        	return newExpression;
        	}else {
        		return expression;
        	}
        }
        //check if expression is +- so 25+-20 = 5
        private static String plusMinus(String expression) {
        	if(expression.indexOf("+-") >0 ) {
            	int location = 0;
            	String newExpression = null;
            	location = expression.indexOf("+-");
            	while(location > 0) {
            		newExpression = expression.substring(0,location) + "-" + expression.substring(location + 2); // replaces -- with +
            		location = newExpression.indexOf("--",location+1);
            	}
            	return newExpression;
            	}else {
            		return expression;
            	}
        }
        
        //check if the expression subtracts any numbers, so 9-10 is true.
        private static boolean hasSubtract(String expression) {
        	for(int i = 0; i < expression.length();i++) {
        		if(expression.charAt(i)==('-')) {
        			return true;
        		}
        	}
        	return false;
        }
        
        //method to check if expression has parenthesis
        private static boolean checkParent(String expression) {
        	for(int i = 0; i < expression.length(); i++) {
        		if(expression.charAt(i) == ('(')||expression.charAt(i) == (')')) {
        			return true;
        		}        			
        	}
        	return false;
        	}
        
        //method to check if expression has brackets
        private static boolean checkBrackets(String expression) {
        	for(int i = 0; i < expression.length(); i++) {
        		if(expression.charAt(i) == ('[')||expression.charAt(i) == (']')) {
        			return true;
        		}        			
        	}
        	return false;
        	}
        
        //method to check if expression has operators
        private static boolean checkOperators(String expression) {
        	for(int i = 0; i < expression.length(); i++) {
        		if(expression.charAt(i) == ('+') || expression.charAt(i) == ('-') || expression.charAt(i) == ('/')|| 
        				expression.charAt(i) == ('*')) {
        			return true;
        		}        			
        	}
        	return false;
        	}
        
        //method to remove extra bracket , not really needed 
        private static String removeBracket(String expression) {
        	return expression.substring(0,expression.indexOf(']')) + expression.substring(expression.indexOf(']')+1);
        }
        
        //method to remove extra parenthesis at end like 4+2+) , not really needed
        private static String removeParent(String expression) {
        	return expression.substring(0,expression.indexOf(')')) + expression.substring(expression.indexOf(')')+1);
        }
        
        //method to check if the expression is something like -92 , if so combine - + 92
        private static boolean checkNegative(String expression) {
 	
        	if(expression.charAt(0)=='-') {
        		if(checkDelims(expression.substring(1))) {  // if the expression after the initial '-' sign as any delims, means that it is not just a digit, 
        			return false;
        		}else {
        			return true;
        		}
        	}
        	return false;
        }
        
        //method to remove the negative sign in something if the expression is 92
        private static String removeNegative(String expression) {
        	if(expression.charAt(0)== '-'){
        		return expression.substring(1);
        	}
        	return expression; // if not negative
        }
        }

