package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		/* IMPLEMENT THIS METHOD */		
		BigInteger bigInteger = new BigInteger(); // create new Big Int object and initialize
		integer = integer.trim(); // removes the leading spaces in the input so [][]123 becomes 123
		
		//checks for negative or positive sign
		if(integer.charAt(0)== '-') { // checks if integer is negative
			integer = integer.substring(1);  //removes the negative sign from the string
			bigInteger.negative = true;
		}
		else if(integer.charAt(0) == '+') { // checks if integer is positive
			integer = integer.substring(1); // removes the positive sign from string
			bigInteger.negative = false;
		}
		
		if(integer.length() < 1) { // if the string is only + or -
			throw new IllegalArgumentException();
		}
		
		for(int i = 0; i < integer.length();i++) { // checks if any character is not a digit if so throw exception
			if(Character.isDigit(integer.charAt(i))== false) {
				throw new IllegalArgumentException();
			}
		}
				
		while(integer.charAt(0) == '0' && integer.length()>1) { // remove the 0s from the beginning
					integer = integer.substring(1);
		}
		if(integer.charAt(0) == '0') { // if you input -0 it will erase the negative.
			bigInteger.negative = false;
		}
		
		//creating linked list
		bigInteger.front = new DigitNode(integer.charAt(integer.length()-1)-'0',null);
		DigitNode ptr = bigInteger.front;
		for(int i = integer.length()-2; i >= 0;i--) {
			int data = integer.charAt(i) - '0';
			ptr.next = new DigitNode(data,null);
			ptr = ptr.next;
		}
		
		bigInteger.numDigits = integer.length(); // number of digits in linked list

		// following line is a placeholder for compilation
		return bigInteger;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		//CHECKLIST ALL TEST CASES, Check # of sum differences, number of digits etc.
		
		//creating and initializing big int objects
		BigInteger fBigInt = first;
		BigInteger sBigInt = second;
		BigInteger sumBigInt = new BigInteger();
		BigInteger differenceBigInt = new BigInteger();

		int sum;
		int larger;
		int smaller;
		boolean fLarger;
		boolean fLarger2;
		boolean equalSize;
		int difference;
		boolean bothFalse;
		boolean subtract;
		
		// if first input number is 0, then return the second number
		if(fBigInt.numDigits == 0 && fBigInt.front.digit == 0) {
			return sBigInt;
		}
		else if(sBigInt.numDigits == 0 && sBigInt.front.digit == 0) {   // if second number is 0, return first number
			return fBigInt;
		}
		
		// check to see if either bigint is negative or positive
		if(fBigInt.negative && sBigInt.negative) {  // if both numbers are negative, the sum must be negative
			bothFalse = true;
			subtract = false;  // you technically just add the two numbers and add a negative sign in front
			sumBigInt.negative = true;
		}
		else if (fBigInt.negative) {	// if first int is negative then you subtract
			subtract = true;
		}
		else if (sBigInt.negative) { // if second int is negative then you subtract
			subtract = true;
		}
		else {						// if both are not negative, then they are positive and you add
			subtract = false;
		}
		
		// checks which is bigger of the two
		if(fBigInt.numDigits > sBigInt.numDigits) {
			smaller = sBigInt.numDigits;
			larger = fBigInt.numDigits;
			fLarger = true;
		}
		else if(sBigInt.numDigits > fBigInt.numDigits) {
			smaller = fBigInt.numDigits;
			larger = sBigInt.numDigits;
			fLarger = false;
		}
		else {
			smaller = sBigInt.numDigits;
			larger = fBigInt.numDigits;
			equalSize = true;
			fLarger = true;
		}
		
		// "Carry over" when adding , so like 999 + 999
		DigitNode firstPtr = fBigInt.front;
		DigitNode secondPtr = sBigInt.front;
		int carrySum = 0;
		sum = fBigInt.front.digit + sBigInt.front.digit + carrySum;
		boolean carry = false;

		//Adding and carrying over first number in both linked lists
		if(subtract == false) {
			if (sum >= 10) {
				if (firstPtr.next != null || secondPtr.next != null){ 
					if(fLarger){
						carrySum = 1;
						sum = Integer.parseInt(Integer.toString(sum).substring(1));	
						if(firstPtr.next.digit + carrySum >=10) {
							carry = true;
						}
						else {
							carry = false;
						}
					}
					else {
						carrySum = 1;
						sum = Integer.parseInt(Integer.toString(sum).substring(1));
						if(secondPtr.next.digit + carrySum >=10) {
							carry = true;
						}
						else {
							carry = false;
						}
					}	
				}
			}
		}
		//creating front node for addition
		
		if(sum >= 10) {
			if(firstPtr.next == null && secondPtr.next == null) { // if the inputs are single digit, make sure 10 is stored as 0-1
				sum = Integer.parseInt(Integer.toString(sum).substring(1));	
				sumBigInt.front = new DigitNode(sum,null);
				sumBigInt.numDigits++;
				DigitNode ptr = sumBigInt.front;
				ptr.next = new DigitNode(1,null);
				ptr = ptr.next;
				sumBigInt.numDigits++;
				return sumBigInt;
			}
		}
			sumBigInt.front = new DigitNode(sum,null);
			sumBigInt.numDigits++;
			DigitNode ptr = sumBigInt.front;
		

		

		//subtraction linked list
		if (subtract) {
			int borrow = 0;
			//Checks is first big int is the larger one
			fLarger2 = isLarger(fBigInt,sBigInt);
			if(fLarger2) {
				difference = firstPtr.digit - secondPtr.digit - borrow;
				if(fBigInt.negative) {
					differenceBigInt.negative = true;
				}
			}
			else {
				difference = secondPtr.digit - firstPtr.digit;
				if(sBigInt.negative) {
					differenceBigInt.negative = true;
				}
			}

			//"borrowing" over in subtraction, so in 25 - 19, since 5 - 9 is less than 0, 5 must "borrow" from 2
			if(difference < 0) {
				if(fLarger2) {
					difference = difference + 10;
					borrow = 1;
				}
				else if(!fLarger2) {
					difference = difference + 10;
					borrow = 1;
				}
			}
			//creating front of subtraction linked list
			differenceBigInt.front = new DigitNode(difference,null);
			ptr = differenceBigInt.front;
			
			while((firstPtr.next != null) && (secondPtr.next != null)) {
				firstPtr = firstPtr.next;
				secondPtr = secondPtr.next;
				if(fLarger2) {
					difference = firstPtr.digit - secondPtr.digit - borrow;
						if(fBigInt.negative) {
							differenceBigInt.negative = true;
						}
					}
					else {
						difference = secondPtr.digit - firstPtr.digit - borrow;
						if(sBigInt.negative) {
							differenceBigInt.negative = true;
						}
					}				
				if(difference < 0) {
					if(fLarger2) {
					difference = difference + 10;
					borrow = 1;
					}
					else if(!fLarger2) {
						difference = difference + 10;
						borrow = 1;
					}
				}
				else if(difference >= 0) {
					borrow = 0;
				}
				if(difference == 0 && (firstPtr.next == null && secondPtr.next == null)) { // removes leading 0 in answer for same # of digits , ex 231 - 194
					break;
				}

				ptr.next = new DigitNode(difference,null);
				ptr = ptr.next;
				differenceBigInt.numDigits++; // number of digits in linked list
			}
			

			// adding leading numbers for example 231 - 30 becomes 201 not just 01
			for(int i = 0; i <(larger-smaller); i++) { 
				if ((fBigInt.numDigits > sBigInt.numDigits)&&(firstPtr.next != null)) {
					if(borrow == 1) {
						ptr.next = new DigitNode(firstPtr.next.digit - borrow,null);
						ptr = ptr.next;
						firstPtr = firstPtr.next;
						borrow = 0;
					}
					else {
						ptr.next = new DigitNode(firstPtr.next.digit,null);
						ptr = ptr.next;
						firstPtr = firstPtr.next;
					}
				}
				else if((sBigInt.numDigits > fBigInt.numDigits) && (secondPtr.next != null)) {
					if(borrow == 1) {
						ptr.next = new DigitNode(secondPtr.next.digit - borrow,null);
						ptr = ptr.next;
						secondPtr = secondPtr.next;
						borrow = 0;
					}
					else {
						ptr.next = new DigitNode(secondPtr.next.digit,null);
						ptr = ptr.next;
						secondPtr = secondPtr.next;
					}
				}
				differenceBigInt.numDigits++;
			}
			
			//remove leading 0s so 100 - 90 = 10 not 010  
			// Finds when the number that is not 0, so 00651 would be 3 since 1->5->6 is the third number then all zeros
			int notZero = 0;
			int counter = 0;
			differenceBigInt.numDigits = 0; // creating a new list so the number of digits must be zero
			DigitNode ptr3 = differenceBigInt.front;
			while(ptr3!= null) {
				if(ptr3.digit != 0) {
					notZero = counter;
				}
				counter++;
				ptr3 = ptr3.next;
			}
			
			//Finds the last number that is not zero and then removes the leading zeros so in 00651 it removes numbers "after" 00
			// so it 00651 would become 651
			counter = 0;
			DigitNode ptr4 = differenceBigInt.front;
			while(ptr4!=null) {
				differenceBigInt.numDigits++;
				if(notZero == counter) {
					ptr4.next = null;
					break;
				}
				counter++;
				ptr4= ptr4.next;
			}
			
			//When the returning number is 0 fix the list.
			if(differenceBigInt.numDigits == 1 && differenceBigInt.front.digit == 0 || differenceBigInt.numDigits == 0) {
				differenceBigInt.numDigits = 0;
				differenceBigInt.front = null;
				differenceBigInt.negative = false;
			}
			return differenceBigInt;         
			
		}
	
		//creating sum linked list
		int carryNumber;
		while((firstPtr.next != null) && (secondPtr.next != null)) {
			firstPtr = firstPtr.next;
			secondPtr = secondPtr.next;
			sum = firstPtr.digit + secondPtr.digit + carrySum; 
			// "Carry over" when adding , so like 999 + 999 
			if (sum >= 10) {
				if (firstPtr.next != null || secondPtr.next != null){
					if(fLarger){
						carrySum = 1;
						sum = Integer.parseInt(Integer.toString(sum).substring(1));  // i think could also use %10
						if(firstPtr.next.digit + carrySum >= 10) { // MIGHT need to remove the next
							carry = true;
						}
						else {
							carry = false;
						}
					}
					else {
						carrySum = 1;
						sum = Integer.parseInt(Integer.toString(sum).substring(1));	
						if(secondPtr.next.digit + carrySum >=10) {
							carry = true;
						}
						else {
							carry = false;
						}
					}	
				}
				else if(firstPtr.next == null && secondPtr.next == null) {
					sum = Integer.parseInt(Integer.toString(sum).substring(1));	
					ptr.next = new DigitNode(sum,null);
					ptr = ptr.next;
					sumBigInt.numDigits++;
					ptr.next = new DigitNode(1,null); // when you add something like 99 + 98, 9 + 9 will always be 1 and another number
					ptr = ptr.next;
					sumBigInt.numDigits++;
					break;
				}
			}
			else if(sum < 10) {
				carrySum = 0;
			}
			
			ptr.next = new DigitNode(sum,null);
			ptr = ptr.next;
			sumBigInt.numDigits++; 
		}
		
		while(carry) {
			if(firstPtr.next != null && secondPtr.next == null) {
				firstPtr = firstPtr.next;
				if(firstPtr.digit + carrySum >= 10) { 
					carryNumber = Integer.parseInt(Integer.toString(firstPtr.digit + carrySum).substring(1));	
					ptr.next = new DigitNode(carryNumber,null);
					ptr = ptr.next;
					if(firstPtr.next == null) {
						ptr.next = new DigitNode(Integer.parseInt(Integer.toString(firstPtr.digit + carrySum).substring(0,1)),null);
						ptr = ptr.next;
						sumBigInt.numDigits++;
						break;
					}
					carrySum = 1;
					if(firstPtr.next.digit + carrySum >= 10 && firstPtr.next != null) {
						carry = true;
					}
					else {
						carry = false;
						}
					}
				}
			else if (secondPtr.next != null && firstPtr.next == null) {
				secondPtr = secondPtr.next;
				if(secondPtr.digit + carrySum >= 10) {
					carryNumber = Integer.parseInt(Integer.toString(secondPtr.digit + carrySum).substring(1));	
					ptr.next = new DigitNode(carryNumber,null);
					ptr = ptr.next;
					if(secondPtr.next == null) {
						ptr.next = new DigitNode(Integer.parseInt(Integer.toString(secondPtr.digit + carrySum).substring(0,1)),null);
						ptr = ptr.next;
						sumBigInt.numDigits++;
						break;
					}
					carrySum = 1;
				if(secondPtr.next.digit + carrySum >= 10 && secondPtr.next != null) {
					carry = true;
				}
				else {
					carry = false;
				}
				}
			}
			else {  
				carry = false;
			}
		}
		
		//if one integer is larger than the other, ex. 12345 + 12. 
			for(int i = 0; i <(larger-smaller); i++) { 
				if ((fBigInt.numDigits > sBigInt.numDigits)&&(firstPtr.next != null)) {
					if(carrySum == 1) {
						ptr.next = new DigitNode(firstPtr.next.digit + carrySum,null); 
						ptr = ptr.next;
						firstPtr = firstPtr.next;
						carrySum = 0;
					}
					else {
						ptr.next = new DigitNode(firstPtr.next.digit,null); 
						ptr = ptr.next;
						firstPtr = firstPtr.next;
					}
						
				}
				else if((sBigInt.numDigits > fBigInt.numDigits) && (secondPtr.next != null)) {
					if(carrySum == 1) {
						ptr.next = new DigitNode(secondPtr.next.digit + carrySum,null);
						ptr = ptr.next;
						secondPtr = secondPtr.next;	
						carrySum = 0;
					}
					else {
						ptr.next = new DigitNode(secondPtr.next.digit,null); 
						ptr = ptr.next;
						secondPtr = secondPtr.next;
					}
				}
				sumBigInt.numDigits++;
			}
			
		// following line is a placeholder for compilation
			System.out.print(sumBigInt.numDigits);
		return sumBigInt;
	}
	
	/** Method to test which BigInt is "larger" for example, 999 is larger than 123.
	 * If the first number is larger, returns true
	 * Checks the front digit and continues the loop if the digits are equal until the end.
	 */
	private static boolean isLarger(BigInteger inputOne, BigInteger inputTwo) {
		boolean bigger = true;
		if(inputOne.numDigits > inputTwo.numDigits) {
			return true;
		}
		else if(inputTwo.numDigits > inputOne.numDigits){
			return false;
		}
		else {
			DigitNode ptrOne = inputOne.front;
			DigitNode ptrTwo = inputTwo.front;
			
			while(ptrOne != null && ptrTwo != null) {
				if(ptrOne.digit > ptrTwo.digit) {
					bigger = true;
				}
				else if(ptrOne.digit < ptrTwo.digit) {
					bigger = false;
				}
				ptrOne = ptrOne.next;
				ptrTwo = ptrTwo.next;
			}
			return bigger;
			
		}
		
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		// creating new objects
		BigInteger firstBigInt = first;
		BigInteger secondBigInt = second;
		BigInteger productAnswer = new BigInteger();
		int product = 0;

		//initialize all objects

		productAnswer.front = new DigitNode(0,null);
		
		DigitNode firstPtr = firstBigInt.front;
		DigitNode secondPtr = secondBigInt.front;
		int carryNumber = 0;
		int zeros = 0;
		for(int i = 0; i < firstBigInt.numDigits; i++) {
			BigInteger subProduct = new BigInteger();
			subProduct.front = new DigitNode(0,null);
			DigitNode subPtr = subProduct.front;
			subProduct.numDigits++;
				// Fixes for example in 21 * 12, when you do 20 * 2, it'll make it 40 instead of 4
				// adds a 0 before the number, so 40 * 10 would be  400 not 4
				for(int k = 0; k < zeros; k++) {
					subPtr.digit = 0;
					subPtr.next = new DigitNode(0,null); // creates the new 0 spot
					subProduct.numDigits++;
					subPtr = subPtr.next;
				}
			for(int j = 0; j < secondBigInt.numDigits; j++) {
				product = firstPtr.digit * secondPtr.digit + carryNumber;
				subPtr.digit = product;
				if(product < 10) {
					carryNumber = 0;
				}
				else if(product >=10) {
					subPtr.digit = Integer.parseInt(Integer.toString(product).substring(1));
					carryNumber = product/10;
				}
				if(secondPtr.next!=null) {
					secondPtr = secondPtr.next;
						subPtr.next = new DigitNode(0,null);
						subPtr = subPtr.next;
						subProduct.numDigits++;
				}
				else {
					if(carryNumber > 0) {
						subPtr.next = new DigitNode(carryNumber,null);
						subProduct.numDigits++;
						break;
					}
				}

			}
			carryNumber = 0; 
			productAnswer = BigInteger.add(productAnswer, subProduct);
			secondPtr = secondBigInt.front;
			firstPtr = firstPtr.next;
			zeros++;
		}
		
		//fixes negative numbers, so -5 * -5 = 25, -5 * 5 = -25 5 * -5 = -25 , 5 * 5 = 25
		if(firstBigInt.negative && secondBigInt.negative) {
			productAnswer.negative = false;
		}
		else if(firstBigInt.negative && !secondBigInt.negative) {
			productAnswer.negative = true;
		}
		else if(secondBigInt.negative && !firstBigInt.negative) {
			productAnswer.negative = true;
		}
		
		//remove leading 0s so 100 - 90 = 10 not 010  
		// Finds when the number that is not 0, so 00651 would be 3 since 1->5->6 is the third number then all zeros
		int notZero = 0;
		int counter = 0;
		productAnswer.numDigits = 0; // creating a new list so the number of digits must be zero
		DigitNode ptr3 = productAnswer.front;
		while(ptr3!= null) {
			if(ptr3.digit != 0) {
				notZero = counter;
			}
			counter++;
			ptr3 = ptr3.next;
		}
		
		//Finds the last number that is not zero and then removes the leading zeros so in 00651 it removes numbers "after" 6
		// so it 00651 would become 651
		counter = 0;
		DigitNode ptr4 = productAnswer.front;
		while(ptr4!=null) {
			productAnswer.numDigits++;
			if(notZero == counter) {
				ptr4.next = null;
				break;
			}
			counter++;
			ptr4= ptr4.next;
		}
		
		//When the returning number is 0 fix the list.
		if(productAnswer.numDigits == 1 && productAnswer.front.digit == 0 || productAnswer.numDigits == 0) {
			productAnswer.numDigits = 0;
			productAnswer.front = null;
			productAnswer.negative = false;
		}
		// following line is a placeholder for compilation
		return productAnswer;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
