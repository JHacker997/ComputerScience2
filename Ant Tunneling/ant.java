/*  John Hacker
 *  Ant Tunneling
 *  COP 3503 Spring18
 *  Instructor: Arup Guha
 */
//Import statement
import java.util.*;

//Save Guha from the evil ants holding him hostage
public class ant 
{
    //Member variables
    private static int h;
    private static int t;
    private static int c;
    private static int tunnel;
    private static int totalCost;
    private static int lowestCost;
    private static int connectedHills;
    protected static Scanner scan;
    private static DisjointSet campus;
    private static edge[] edges;
    
    //Find the lowest cost to connect all ant hills in multiple campuses
    public static void main(String[] args) 
    {
        //Read how many campuses are needed to be solved for
        scan = new Scanner(System.in);
        int n = scan.nextInt();
        
        //Loop through every campus
        for (c = 1; c <= n; c++)
        {
            //Build all the tunnels in the most efficient way
            buildTunnels();
            
            //Check that tunnels connect all the hills and print their result
            printAnswer(connectedHills == h);
        }
    }
    
    
    //Build all the tunnels, from cheapest to most expensive
    private static void buildTunnels()
    {
        //Initialize necessary member variables
        totalCost = 0;
        connectedHills = 1;
        
        //Create a disjoint set for all of the hills on the current campus
        h = scan.nextInt();
        campus = new DisjointSet(h);
        
        //Read how many tunnels can be built
        t = scan.nextInt();
        
        //Create an array for all the possible tunnels
        edges = new edge[t];
        
        //Loop through all of the possible tunnels
        for (tunnel = 0; tunnel < t; tunnel++)
        {
            //Save all information for the current possible tunnel
            edges[tunnel] = new edge();
        }
        
        //Sort the array of tunnels by their costs
        Arrays.sort(edges);
        
        //Loop through all the possible tunnels again
        for (tunnel = 0; tunnel < t; tunnel++)
        {
            //Try to connect the hills with the current tunnel and check if it was successful
            if (campus.union(edges[tunnel].x - 1, edges[tunnel].y - 1))
            {
                //Add the cost to build that tunnel to the total cost
                totalCost += edges[tunnel].d;
                
                //Incrememnt the amount of total hills connected
                connectedHills++;
            }
            
            //Check if the tunnels have connected all of the hills
            if (connectedHills == h)
            {
                //Stop trying to build tunnels
                return;
            }
        }
    }
    
    
    //Print information about the hills connectivity
    private static void printAnswer(boolean result)
    {
        //Check if all the hils could be connected by the tunnels
        if (result)
        {
            //Print the cost off connecting all the hills
            System.out.println("Campus #" + c + ": " + totalCost);
        }
        else
        {
            //Have a snarky remark for the ants, who can not connect all their hills
            System.out.println("Campus #" + c + ": I'm a programmer, not a miracle worker!");
        }
    }
}



//Keep all information about each possible tunnel to build
class edge implements Comparable<edge>
{
    //Member variables
    protected int x;
    protected int y;
    protected int d;
    
    //Constructor
    protected edge()
    {
        //Read all the information that describes the tunnel
        x = ant.scan.nextInt();
        y = ant.scan.nextInt();
        d = ant.scan.nextInt();
    }
    
    //Implement compareTo from Comparable interface
    public int compareTo(edge other) 
    {
        //Sort the edges based on their d (cost)
		return d - other.d;
	}
}


// Arup Guha
// 1/22/2013
// Written in COP 3503 to illustrate a basic disjoint set class, with path compression.
// I modified this in-lecture code to include path compression and am reusing it for the Ants assignment, as was said to be fine during lecture

class DisjointSet 
{
    //Member variable
	private int[] parents;
    
	// Create the initial state of a disjoint set of n elements, 0 to n-1.
	protected DisjointSet(int n) 
	{
		// All nodes start as leaf nodes.
		parents = new int[n];
		for (int i=0; i<n; i++)
		{
		    //The indes of the array indicates its value and the value at the index is its root
			parents[i] = i;
		}
	}
	
	
	// Returns the root node of the tree storing id.
	protected int find(int id) 
	{
	    //Check the current pair is the root of the tree
        if (parents[id] == id)
        {
            //Return the id of the current pair as if it is its own parent
            return id;
        }
        
        //Set the parent of the current pair to the parent of its parent
        int res = find(parents[id]);
        parents[id] = res;
        
        //Return the root of the tree
		return res;
	}
	
	
    //Combine the trees that contain two values
	protected boolean union(int id1, int id2) 
	{
		// Find the parents of both nodes.
		int root1 = find(id1);
		int root2 = find(id2);
		
		// No union needed.
		if (root1 == root2)
		{
		    //Return that the two values were already in the same tree
		    return false;
		}
		
		//Combine the trees with these two roots
		parents[root1] = root2;
		
		// We successfully did a union.
		return true;
	}
}