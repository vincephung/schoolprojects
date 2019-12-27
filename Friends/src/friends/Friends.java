package friends;

import java.util.ArrayList;
import java.util.Arrays;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// stores the shortest chain in an arraylist
		ArrayList<String> smallestChain = new ArrayList<String>();
	
		//stores a queue of the people who have been visited
		Queue<Person> peopleQueue = new Queue<Person>();
	
		//create Array of people you have visited
		Person[] parentArray = new Person[g.members.length];
			
		//get the vertexNum or index of P1 MIGHT NOT NEED
		int initialVertexNum = g.map.get(p1);

		//fills the array with null value
		Arrays.fill(parentArray, null);
		
		//enqueue P1 into the queue
		peopleQueue.enqueue(g.members[initialVertexNum]);
		
		// this is using a BFS
		while(!peopleQueue.isEmpty()) {
			Person currentPerson = peopleQueue.dequeue();
			
			//find the neighbor of the currentPerson
			// loops through the neighbors and enqueue them if not visited
			for(Friend nbr = currentPerson.first; nbr!=null; nbr = nbr.next) {
					
				// if you have not visited the neighbor, visit it
					if(parentArray[nbr.fnum] == null) {
						
					//Stores the parent of the neighbor into the array
					parentArray[nbr.fnum] = currentPerson; 
					
					// enqueue this neighbor into the queue
					peopleQueue.enqueue(g.members[nbr.fnum]);
					
					//if the name of the neighbor is the same as P2, the end ADD all of the people you visited to the arrayList
					if(g.members[nbr.fnum].name.equals(p2)) {
						currentPerson = g.members[nbr.fnum];
						
						//while(!parentArray[g.map.get(currentPerson.name)].equals(p1)){
						// goes to the current person, adds to chain and finds its parent and adds to chain
						//STOPS when reaches the starting pereson
						while(!p1.equals(currentPerson.name)){ // OR WHEN PARENT == NULL;
							smallestChain.add(currentPerson.name);
							currentPerson = parentArray[g.map.get(currentPerson.name)]; // gets the parent of the current person
						}
						smallestChain.add(p1); // adds initial p1
						//since chain was added in reverse order, you must reverse it back so, 5 4 3 2 1 becomes 1 2 3 4 5
						smallestChain = reverse(smallestChain);
						//System.out.println(smallestChain);
						//return smallestChain;
						}
					}
				}
		}
		// if nothing is in the path, return null OR if same name for start and end, so if you insert sam then end at sam, return null since its path is 0
		if(smallestChain.isEmpty() || (smallestChain.size() == 1)) {
			return null;
		}
		
		return smallestChain;
		
		//
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		// if there is no shortest path then return null
	}
	// private method to reverse arraylist
	private static ArrayList<String> reverse(ArrayList<String> a) {
		for(int i = 0; i < a.size(); i++) {
			a.add(i, a.remove(a.size()-1));
		}
		return a;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/

		// create arraylist to store cliques inside of
		ArrayList<ArrayList<String>> theCliques = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		
		
		//Create subclique arraylist
		ArrayList<String> subClique = new ArrayList<String>();

		
		//queue for BFS of people
		Queue<Person> peopleQueue = new Queue<Person>();
		
		// array to store people
		Person[] peopleArray = new Person[g.members.length];
		
		//boolean array to see which ones have been visited
		boolean[] hasVisited = new boolean[g.members.length];

		//finds first rutgers value starting point
		boolean schoolFound = false;
		int startIndex = 0;
		
		//loops through the graph, gets the first person who goes to input school (ex. rutgers, penn state) and runs BFS with starting value of that person
		// i might have to be different, correspond to the graph instead of array
		for(int i = 0; i < peopleArray.length; i++) {
			//System.out.println(g.members[i].name + "Who is this person");
			// if this person is not a student, move on
			if(!g.members[i].student) {
				continue;
			}
			if(g.members[i].school.equals(school)) {
				Person start = g.members[i];
				startIndex = g.map.get(start.name);
				schoolFound = true;
				if(!hasVisited[i]) {	
					subClique.clear(); // CLEAR IS CAUSING THE PROBLEMS, ITS REMOVING INSIDE OF THE ARRAYLIST WHICH AFFECTS THE TOTAL ARRAYLIST					
					subClique =	bfs(startIndex,hasVisited,peopleQueue, g, school,subClique);						
					//theCliques.add(subClique);
					// initially add the subclique to the big clique
					if(theCliques.isEmpty()) {
						theCliques.add(subClique);
						//copy the clique into a temp variable
						temp = copy(theCliques);
					}
					else {
						//add the clique to the new temp
						temp.addAll(theCliques);
						//copy the new temp into temp, so it doesn't reset when you clear subclique
						temp = copy(temp);
					}
				//	System.out.println("Temp is " + temp);
					//System.out.println("AFter total array" + theCliques);
				}
			}
		}
		// might not need this tbh
		if(schoolFound == false) {
			return null;
		}
		
		//return answer
	//	System.out.println("Temp is " + temp);
		return temp;
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		// IMPORTANT!!!!
		//return null;
		
	}
	
	//method to run BFS
	private static ArrayList<String> bfs(int startIndex, boolean []hasVisited, Queue<Person> peopleQueue, Graph g, String school, ArrayList<String> subClique) {
		// show that you visited this word/index/person
		hasVisited[startIndex] = true;
		peopleQueue.enqueue(g.members[startIndex]);
		
		// add inital person
		subClique.add(g.members[startIndex].name);
		
		while(!peopleQueue.isEmpty()) {
			Person currentPerson = peopleQueue.dequeue();
			
			for(Friend nbr = currentPerson.first; nbr!=null; nbr = nbr.next) {
				// if you have not visited the neighbor, visit it
				if(!hasVisited[nbr.fnum]) {
					hasVisited[nbr.fnum] = true;
					
					//if this neighbor's school does not match the target, example input is rutgers but this neighbor is penn state, move onto next neighbor
					//System.out.println(g.members[nbr.fnum].school);
					//System.out.println(g.members[nbr.fnum].name);
					if(!g.members[nbr.fnum].student) {
						continue;
					}
					if(!g.members[nbr.fnum].school.equals(school)||!g.members[nbr.fnum].student) {
					//	System.out.println(g.members[nbr.fnum].name);
					//	System.out.println(g.members[nbr.fnum].school + "WOHOHOHOH");
						continue;
					}
					
				//Stores the parent of the neighbor into the array
				// parentArray[nbr.fnum] = currentPerson; 
				
				// enqueue this neighbor into the queue
				peopleQueue.enqueue(g.members[nbr.fnum]);
				
				// add this neighbor to the subclique
				subClique.add(g.members[nbr.fnum].name); 
				//System.out.println("current subclique is " + subClique);
				}
			}
		}
		//System.out.println("returned subclique is " + subClique);

		return subClique;
	}
	
	//method to copy one arraylist contents into another
	private static ArrayList<ArrayList<String>> copy(ArrayList<ArrayList<String>> array){
		ArrayList<ArrayList<String>> clone = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < array.size(); i++) {
			clone.add((ArrayList<String>) array.get(i).clone());
		}
		return clone;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		//create connectors arraylist
		ArrayList<String> connectorNames = new ArrayList<String>();
		// arraylist to hold previous people, this makes sense later.
		ArrayList<String> previousName = new ArrayList<String>();

		
		//create a stack holding people might not need stack
		Stack<Person> peopleStack = new Stack<Person>();
		
		//array to hold which people have been visited already
		boolean[] hasVisited = new boolean[g.members.length];
		
		//stores the dfsNum
		int[]dfsNum = new int [g.members.length];
		
		//stores the back(v) num
		int[]backNum = new int [g.members.length];
		
		//dfs and back numbers
		int dfsNumCounter = 1;
		int backNumCounter = 1;
		
		for(int v = 0; v < hasVisited.length; v++) {
			// if this person has been visited, SKIP to next person/vertex
			if(hasVisited[v]) {
				continue;
			}
			else if (!hasVisited[v]) {
				//Runs the dfs method. New starting point everytime it is run. only runs when this person has not been visited yet
				String initialPerson = g.members[v].name;
				dfs(connectorNames, hasVisited, g, g.members[v],dfsNum, backNum, dfsNumCounter, backNumCounter,initialPerson,previousName);
			}
		}
		
		//might not need this statement.
		if(connectorNames.isEmpty()) {
			return null;
		}
		else {
			//System.out.println(connectorNames);
			return connectorNames;
		}
			
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
	}
	// private method to run DFS
	private static ArrayList<String> dfs(ArrayList<String> connectorNames, boolean[] hasVisited, Graph g, Person startPerson, int[]dfsNum, int[]backNum, int dfsNumCounter, int backNumCounter, String initialPerson,ArrayList<String> previousName) {
		int firstPersonIndex = g.map.get(startPerson.name);
		
		// first person has been visited
		hasVisited[firstPersonIndex] = true;
		
		// Sets the DFS and BAcknum to the counter for every new unvisited vertex
		dfsNum[firstPersonIndex] = dfsNumCounter;
		backNum[firstPersonIndex] = backNumCounter;
				// loops through the neighbors of the current person
			for(Friend nbr = startPerson.first ; nbr!= null; nbr=nbr.next) {
				
				//if neighbor hasnt been visited, visit them
				if(!hasVisited[nbr.fnum]) {
					//increment both DFS and backNum counters for each NEW unvisited vertex
					dfsNumCounter++; 
					backNumCounter++; 
					// Change person from Friend class to Person
					Person neighbor = g.members[nbr.fnum];
					
					//DFS using recursion, it'll go through the graph one by one vertex
					connectorNames = dfs(connectorNames,hasVisited,g,neighbor,dfsNum,backNum,dfsNumCounter,backNumCounter,initialPerson,previousName);
					
					//Adds person to connector arraylist
					// this is the check if dfsnum(v) <= back(w) to check if connector, when dfs backs up from neighbor 
					if(dfsNum[g.map.get(startPerson.name)] <= backNum[nbr.fnum]) {
						//check if this person is a connector, they must not already be in the list
						//If the person is not already in arraylist
						if(!connectorNames.contains(startPerson.name)) {
							//If the current person is not the starting point or is next to end point
							if((!startPerson.name.equals(initialPerson)) || (previousName.contains(startPerson.name)))  {
								connectorNames.add(startPerson.name);
							}
						}
					}
					// if dfsNum(v) > back(w), change the back num of current person
					else {
						// back number of the current person
						int indexOfStartPerson = backNum[g.map.get(startPerson.name)];
						//back number of current person's neighbor
						int indexOfNeighbor = backNum[nbr.fnum];
						// change the back number of the current person to the minimum of the back num of neighbor and current person
						backNum[g.map.get(startPerson.name)] = Math.min(indexOfStartPerson, indexOfNeighbor);
					}
					previousName.add(startPerson.name);
				//	System.out.println(previousName);
				//	System.out.println(startPerson.name + " was added ");
				}
				
				// If neighbor has been visited, change its back number
				// If a neighbor, w, is already visited then back(v) is set to min(back(v),dfsnum(w))
				else if (hasVisited[nbr.fnum]){
					//back number of the current person
					int backNumStartPerson = backNum[g.map.get(startPerson.name)];
					//DFS number of the neighbor
					int dfsNeighbor = dfsNum[nbr.fnum];
					//change the backnumber of current person to the minimum of back of current and DFS of neighbor
					backNum[g.map.get(startPerson.name)] = Math.min(backNumStartPerson, dfsNeighbor);					
				}
			}
			return connectorNames;
			
			}
		}
	//}

