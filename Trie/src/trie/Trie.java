package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		int counter = 0; // this keeps track of the number of inputs in the trie, so first input is 0, 2nd is 1
		short matchCounter = 0; // keeps track of letters that match up between 2 prefixes
		TrieNode root = new TrieNode(null,null,null);
		TrieNode ptr = root;
		TrieNode prevPtr = null;
		TrieNode ptrChildren = null;
		TrieNode currentFirstGenChild = null;
		Indexes triplet;
		Indexes prevTriplet;
		short endIndex = 0;
		short childEndIndex = 0;
		short firstGenStartIndex = 0;
		short childStartIndex = 0;
		boolean createPrefix = false;
		
		while(counter < allWords.length) {  // runs loop until the end of array length
		// might need to change all to lowercase
		//checks if the input contains a number or anything other than a character and returns null
		for(int i = 0; i < allWords.length; i++) {
			// MIGHT NOT NEEED TO CHECK IF DIGIT
			//for(int j = 0; j < allWords[i].length(); j++) {
				//System.out.println(allWords[i].charAt(j));
				//if(!Character.isDigit(allWords[i].charAt(j))) {
					//System.out.println(allWords[i].charAt(j));
					

					//return null; // if string contains a non letter return null
				//}
				allWords[i].toLowerCase(); // MIGHT NOT NEED THIS LINE
			//}
		}
		
			String currentWord = allWords[counter];
			int currentWordLength = currentWord.length();
			endIndex = (short) (currentWordLength - 1);
			short newPrefixWordIndex = 0;
			short smallestMatch = 0;
			short lastMatch = 0;
			short largestMatch = 0;

			if(counter > 0) { // if more than 2 words are inserted, then you can put prefix / check prefix
				for(short i = 0; i <= counter-1;i++) { // ONLY checks the number of words inputted before the current word
					
					if((i > 0) && (smallestMatch == 0) && (matchCounter > 0)) {
						smallestMatch = matchCounter;
						largestMatch = matchCounter;
					}
					lastMatch = matchCounter;
					if(lastMatch < smallestMatch) {
						smallestMatch = lastMatch;
					}
					else if(lastMatch > largestMatch) {
						largestMatch = lastMatch;
					}
					if(i > 0) {
						matchCounter = 0;
					}
					
					for(int j = 0; j < currentWordLength;j++) {
						if(currentWord.charAt(j) == allWords[i].charAt(j)) { // if letter from the current word matches letter at same index of another word
							// THIS IS for when you have to check muscle, pottery, possible, then possum , multiple prefixs COMMENTED out for now
							matchCounter++;
							newPrefixWordIndex = i; // check the instructions after adding possum you check for the pottery child (pottery came first)
							// if current word doesn't match any of the other words, then NO prefix is created
							createPrefix = true;
						}
						else {
							if(matchCounter > largestMatch) {
								largestMatch = matchCounter;
							}
							if(smallestMatch == 0) {
								smallestMatch = matchCounter;
							}
							break; // break out of loop if the letter does not match
						}
					}
				}
			}
			//creating new trieNode after inputting word.
			TrieNode treeChild = new TrieNode(null,null,null);
			TrieNode prefix = new TrieNode(null,null,null);
			int theCount = 0;
			//childEndIndex = (short) (matchCounter -1);
			childEndIndex = (short) (largestMatch -1);
			boolean afterFirstGen = false;
			boolean nextGen = false;
			boolean containPrefix = false;
			boolean firstGen = false;
			boolean firstPrefix = false;
			boolean creatingPrefix = false;
			boolean createSmallerPrefix = false;
			TrieNode prevParent = null;
			boolean makePrefix = false;
			boolean prefixInPrefix = false;
			boolean skip = false;
			//if more than TWO letters of the beginning words match each other create prefix node / edit
			// creating the prefix node, initializing prefix
			
			//if there are no matches, do not create prefix MIGHT NOT EVEN NEED THIS LINE
			if(smallestMatch == 0 && largestMatch == 0 && matchCounter == 0) {
				createPrefix = false;
			}

			if(createPrefix) {
				ptr = root.firstChild;
				// LOOPS through first gen to see which LEAF node needs to be turned into a prefix
				while(ptr!=null) {
					firstGen = checkFirstGen(root, ptr);	
					if(ptr.firstChild!= null) {
					createSmallerPrefix = insidePrefix(ptr.substr.wordIndex,largestMatch,ptr.substr.endIndex,currentWord,allWords);
					}
					// only applise to prefixes
					if(firstGen) {
						if(largestMatch < allWords[ptr.substr.wordIndex].substring(0,ptr.substr.endIndex+1).length() ) {
						skip = false;
						}
						else {
							skip = true;
						}
						// IF IT IS FIRST GEN IT CANNOT CONTAIN A PREFIX
						containPrefix = false;
						firstGenStartIndex = 0;
						// check for SMALLEST prefix
						if(ptr.firstChild != null) {
							firstPrefix = insidePrefix(ptr.substr.wordIndex,largestMatch,ptr.substr.endIndex,currentWord,allWords);
						}
						
					}
					else {
						firstGenStartIndex = smallestMatch;
						if(ptr.firstChild == null) {
							makePrefix = containsPrefix(ptr.substr.wordIndex,largestMatch,currentWord,allWords);
						}
						// IF the parent node word = the current prefix then SKIP this
						// JUST CHANGED THIS TO LARGESTMATCH -1
						// JUST MADE WHILE LOOP AND SWITCHED 
						//String parentPrefixWord = allWords[ptr.substr.wordIndex].substring(0,largestMatch); from ptr to newptr
						//TrieNode newPtr = ptr;
					//	while(newPtr!=null) {
						// just added IF PTR.FIRST CHILD
						if(ptr.firstChild == null) {
							String parentPrefixWord = allWords[ptr.substr.wordIndex].substring(0,largestMatch);
							String curWordPrefix = currentWord.substring(0,largestMatch);
							 if(parentPrefixWord.equals(curWordPrefix)){
								prefixInPrefix = true;
							}else {
								prefixInPrefix = false;
							}

						}
						
						else {
							String parentPrefixWord = null;
							TrieNode newPtr = ptr.firstChild;

							while(newPtr!=null) {
								if(largestMatch > allWords[newPtr.substr.wordIndex].length()) {
									parentPrefixWord = allWords[newPtr.substr.wordIndex].substring(0);
								}
								else {
								parentPrefixWord = allWords[newPtr.substr.wordIndex].substring(0,largestMatch);
								}
								String curWordPrefix = currentWord.substring(0,largestMatch);

								if(parentPrefixWord.equals(curWordPrefix)){
									prefixInPrefix = true;
									break;
							}
									newPtr = newPtr.sibling;
								
								
						}
						}
						
						
					//		 newPtr = newPtr.sibling;
					//	}
						if((largestMatch < allWords[prevParent.substr.wordIndex].substring(0,prevParent.substr.endIndex+1).length()) && (!firstGen)) {
								skip = true;
							}
						else if(containPrefix && ptr.firstChild!=null && (largestMatch > allWords[ptr.substr.wordIndex].substring(0,ptr.substr.endIndex+1).length())) { // just added this
							skip = true;
						}
						else {
							skip = false;
						}

				}
					if(ptr.firstChild !=null) {
						containPrefix = containsPrefix(ptr.substr.wordIndex,largestMatch,currentWord,allWords);
					}

					
					// JUST ADDED THIS LINE YOU CAN DELIETE LATER!!!!!! JUST ADDED WOAWODHAWIHDAOISDHSIOADHOIASHDISOAHDOI
					// WOOHOO JJUST ADDED'
					int currentMatches = 0;
					boolean moreMatches = false;
					currentMatches = checkMatches(ptr.substr.wordIndex,currentWord,allWords);
					if(!firstGen) {
						if(prefixInPrefix) {
							moreMatches = false;
						}
						else if(currentMatches <= largestMatch) {
							moreMatches = true;
						}
						else {
							moreMatches = false;
						}
					}
					
					//CHECK if you need to create another prefix , the potato example
					// if NOT first gen
					if(ptr.firstChild != null && containPrefix) {
						String parentsPrefixWord = allWords[ptr.substr.wordIndex].substring(0,ptr.substr.endIndex+1);
						String curWordPrefixs = currentWord.substring(0,parentsPrefixWord.length());
						if(largestMatch > parentsPrefixWord.length()) {
							creatingPrefix = true;
						}
						else if(parentsPrefixWord.equals(curWordPrefixs)){
							creatingPrefix = false;
						}
						else {
							creatingPrefix = true;
						}
						
					}

					//smallest match might need to be biggest match
					if(((ptr.substr.wordIndex == newPrefixWordIndex)&&(ptr.firstChild == null))) {
						if(ptr.firstChild == null) {
							//firstGenStartIndex = smallestMatch;
							//just added this part
							if(firstGen) {
							triplet = new Indexes(newPrefixWordIndex, firstGenStartIndex, childEndIndex); // MIGHT NOT BE NEWPREFIX WORD INDEX FOR 1ST NDEX
							}
							else {
								triplet = new Indexes(newPrefixWordIndex, (short) (prevParent.substr.endIndex+1), (short) (largestMatch-1));
							}
							prefix.substr = triplet;
							ptr.substr = prefix.substr;
							afterFirstGen = true;

							break;
						}
						// if it is ALREADY a prefix and you need to turn one of its children into a prefix			
					}	
					// makes PTR go to its child 
					//when you are creating a prefix this run
					// USE TO HAVE contain prefix
					// JUST ADDED MORE MATCHES
					else if((creatingPrefix)&&(ptr.firstChild!=null) && (containPrefix) && skip &&(!moreMatches)) {
		
					
						firstGenStartIndex = smallestMatch;
						childEndIndex = (short)(matchCounter - 1);						
						prevPtr = ptr;
						prevParent = ptr;
						ptr = ptr.firstChild;
						theCount = 1;

						continue;
						}
					
					// pre fix out of pre fix , potato example
					else if((makePrefix)&&(theCount == 1) && (ptr.firstChild == null)&&(prefixInPrefix) && (!skip)) {
						//changing prevPtr in first slot to just ptr
						prevTriplet = ptr.substr;
						triplet = new Indexes(ptr.substr.wordIndex,(short) (prevParent.substr.endIndex+1),(short) (largestMatch-1)); // MIGHT NOT BE firstGenStartIndex 
						prefix.substr = triplet;
						ptr.substr = prefix.substr;
						afterFirstGen = true;
						nextGen = true;

						break;
					}
					// muse example, adding 3 children to a node not just 2
					// technically not creating a prefix
					//WHEN YOU ARE NOT CREATING A PREFIX THIS RUNS
					else if((containPrefix) && (ptr.firstChild != null) && (!creatingPrefix)) {
						triplet = new Indexes(counter,largestMatch,(short) (currentWordLength-1));
						treeChild.substr = triplet;
						createPrefix = false;

						break;
					}
					// check for smallest prefix
					else if(firstPrefix && firstGen){
						//first gen start might need to just be 0
						triplet = new Indexes(ptr.substr.wordIndex,firstGenStartIndex,(short) (largestMatch-1));
						prefix.substr = triplet;

						break;
						
					}
					// creating a SMALLER prefix, so if prefix was poss with words possum and possible, if you then added "posiden" it would create "POS" prefix
					else if(createSmallerPrefix && ptr.firstChild != null) {
						triplet = new Indexes(ptr.substr.wordIndex,(short) (prevParent.substr.endIndex+1),(short) (largestMatch-1));
						prefix.substr = triplet;

						break;
					}
					prevPtr = ptr;
					ptr = ptr.sibling;
				}
					matchCounter = 0; // RESETS MATCH COUNTER
				}
			
	
			// IF you do not create a prefix you create a child of the root
			//creating first gen children
			if(createPrefix == false) {

				// initialize values for trienode firstchild
				// might just be for first children of the root !!!
				if(!containPrefix) {
					triplet = new Indexes(counter,firstGenStartIndex,endIndex);
					treeChild.substr = triplet;
						
					// THIS creates the first child of the root
					if(root.firstChild == null) {
						root.firstChild = treeChild; // connects the root with the first child you made only
						currentFirstGenChild = root.firstChild;
					}
					else {
						currentFirstGenChild.sibling = treeChild; 
						currentFirstGenChild = treeChild;
					}
					
					TrieNode ptr2 = treeChild;
					firstGen = checkFirstGen(root,ptr2);
					if(firstGen) {
						firstGenStartIndex = 0;
						treeChild.substr.startIndex = firstGenStartIndex;
					}
				}
				//creating more than 2 siblings
				else {
					ptrChildren = ptr.firstChild;
					TrieNode prevChildPtr = null;
					while(ptrChildren!=null) {
						prevChildPtr = ptrChildren;
						ptrChildren = ptrChildren.sibling;
					}
					prevChildPtr.sibling = treeChild;
					
				}
			}
			//creating a PREFIX
			else {

				//create child for the prefix word
				TrieNode originalChild = new TrieNode(null,null,null);
				if(nextGen) {
					//triplet = prevTriplet;
					triplet = new Indexes(prefix.substr.wordIndex,largestMatch, (short) (allWords[ptr.substr.wordIndex].length()-1));// MIGHT NEED TO FIX THIS !!!!
					originalChild.substr = triplet;

				}
				
				else if(firstPrefix) {

					//replaces "be" with b . Puts b before be
					TrieNode prevFirstChild = null;
					TrieNode checkingFirstChild = root.firstChild;
					while(checkingFirstChild != ptr) {
						prevFirstChild = checkingFirstChild;
						checkingFirstChild = checkingFirstChild.sibling;
					}
					prefix.firstChild = ptr;
					if(root.firstChild == ptr) {
						root.firstChild = prefix;
					}else {
						//THIS MIGHT NEED TO BE CHECKINGFIRSTCHILD
						prevFirstChild.sibling = prefix;
					}
					ptr.substr.startIndex = (short) (prefix.substr.endIndex + 1);
					// might not need this
					ptr.sibling = originalChild;
					//prevFirstChild.sibling = prefix;
					triplet = new Indexes(counter, largestMatch,(short) (currentWord.length()-1));
					originalChild.substr = triplet;
					currentFirstGenChild = prefix;
					
				}
				// make the NEW smaller prefix node the parent of the current prefix node
				else if(createSmallerPrefix && ptr.firstChild != null) {
					ptr.substr.startIndex = (short) (prefix.substr.endIndex+1);
					TrieNode prevFirstChild = null;
					TrieNode checkingFirstChild = prevParent.firstChild;
					while(checkingFirstChild != ptr) {
						prevFirstChild = checkingFirstChild;
						checkingFirstChild = checkingFirstChild.sibling;
					}
					prefix.firstChild = ptr;
					if(prevParent.firstChild == ptr) {
						prevParent.firstChild = prefix;
					}
					prevFirstChild.sibling = prefix;
					ptr.sibling = originalChild;
					triplet = new Indexes(counter, largestMatch,(short) (currentWord.length()-1));
					originalChild.substr = triplet;
							
				}
				else {

					triplet = new Indexes(newPrefixWordIndex, largestMatch,(short) (allWords[newPrefixWordIndex].length()-1));// character at index (prefix)
					originalChild.substr = triplet;
				}
				
				if(!firstPrefix && !createSmallerPrefix) {

					triplet = new Indexes(counter,largestMatch,endIndex);
					treeChild.substr = triplet;
					ptr.firstChild = originalChild;
					originalChild.sibling = treeChild;
				}		
			}	
			counter++;
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION

		return root;
	}
	
	//check to see if word contains the prefix
	private static boolean containsPrefix(int wordLocation,int endLocation, String currentWord, String[] stringArray) {
		String[] strArray = stringArray;
		int matches = 0;
		int length = Math.min(stringArray[wordLocation].length(), currentWord.length());
		// CHANGED LENGTH FOR ENDLOCATION
		for(int i = 0; i < endLocation;i++) {
				if (strArray[wordLocation].charAt(i) == currentWord.charAt(i)) {
					matches++;
				}
			}	
		if(matches == 0) {
			return false;
		}
		String prefix = strArray[wordLocation].substring(0,matches);
		currentWord = currentWord.substring(0,prefix.length());
		if (currentWord.equals(prefix)) {
			return true;
		}
		return false;
		
	}
	
	// check to see if word is first gen
	private static boolean checkFirstGen(TrieNode root, TrieNode currentPtr) {
		TrieNode ptr = root.firstChild;
		while(ptr!=null) {
			if(currentPtr.substr == ptr.substr) {
				return true;
			}
			ptr = ptr.sibling;
		}
		return false;
	}
	
	// check if there is a smaller prefix
	private static boolean insidePrefix(int wordLocation,int largestMatch, int endLocation, String currentWord,String[]stringArray) {
		String[] strArray = stringArray;
		String prefix = strArray[wordLocation].substring(0,endLocation +1);
		String currentPrefix = currentWord.substring(0,largestMatch);
		
		if(prefix.charAt(0) != currentPrefix.charAt(0)) {
			return false;
		}
		// MUST BE FIRST GEN
		if(currentPrefix.length() < prefix.length()) {
			return true;
		}
		return false;
	}
	
	//check number of matches of word and ptr
	private static int checkMatches(int wordLocation, String currentWord, String[] stringArray) {
		String[] strArray = stringArray;
		int matches = 0;
		int length = Math.min(stringArray[wordLocation].length(), currentWord.length());
		for(int i = 0; i < length;i++) {
				if (strArray[wordLocation].charAt(i) == currentWord.charAt(i)) {
					matches++;
				}
			}
		return matches;
	}
	
	
	

	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		//create arrayList
		ArrayList<TrieNode> leafNodeList = new ArrayList<TrieNode>();
		
		// if nothing is in the trie return null
		if (root == null) {
			return null;
		}
		
		// intialize ptr as root
		TrieNode currentPtr = root;
		// the current word corresponding to the index of the word list array
		String currentWord = null;
		String currentPrefix = null;
		boolean containsPrefix = false;
		boolean moveDown = false;
		
		while(currentPtr!= null) {
			// for the first check, it starts as the actual ROOT, so you have to make the first ptr its child
			// after the very first check, ptr will be an actual NODE not root.
			if(currentPtr.substr == null) {
				currentPtr = currentPtr.firstChild;        // as explained above, this moves ptr to its CHILD if ptr is pointing to root;
			}
			currentWord = allWords[currentPtr.substr.wordIndex];
		    currentPrefix = currentWord.substring(0,currentPtr.substr.endIndex+1);  // prefix of the current word, so if word is Door, prefix is do
		    containsPrefix = containPrefix(prefix, currentWord);
		    moveDown = goDown(currentPrefix,prefix);
		    // if the pointer has the input prefix go down OR if the input prefix has a prefix itself.
		    if(containsPrefix || moveDown) {
		    	// IF the pointer has no children, this means it is a leaf node, therefore add it to the list
		    	if(currentPtr.firstChild == null) {
		    		leafNodeList.add(currentPtr); // add to list
		    		currentPtr = currentPtr.sibling; // moves to next node
		    	}
		    	// if ptr is NOT a leaf node, you have to go down the list
		    	else {
		    		//addall means you keep list and add
		    		leafNodeList.addAll(completionList(currentPtr.firstChild,allWords,prefix)); //recursion , have to do this to check sibling prefix nodes
		    		currentPtr = currentPtr.sibling; //moves to next node
		    	}
		    }
		    else {
		    	// IF the word does not have the prefix, more onto the next pointer node
		    	currentPtr = currentPtr.sibling;
		    }
		}
		// IF the prefix is not contained in the trie return null
		if(leafNodeList.isEmpty()) {
			return null;
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return leafNodeList;
	}
	
	// check to see if current word contains the given prefix
	private static boolean containPrefix(String prefix, String currentWord) {
		String currentWordPrefix = currentWord;
		if(prefix.length() > currentWord.length()) {
			currentWordPrefix = currentWord.substring(0);
		}
		else {
			currentWordPrefix = currentWord.substring(0,prefix.length());
		}
		if(prefix.equals(currentWordPrefix)) {
			return true;
		}
		return false;
		
		}
	
	// method to go down the list of the trie IF input prefix contains another prefix, so if word was pig, and input was po, you would still go down because of prefix p
	private static boolean goDown(String currentPrefix,String prefix) {
		String currentWordPrefix = currentPrefix;
		if (currentPrefix.length() > prefix.length()) {
			currentWordPrefix = prefix.substring(0);
		}
		else {
			currentWordPrefix = prefix.substring(0,currentPrefix.length());
		}
		if(currentWordPrefix.equals(currentPrefix)) {
			return true;
		}
		return false;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
