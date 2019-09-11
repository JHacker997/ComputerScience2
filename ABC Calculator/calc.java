/*
 *  John Hacker
 *  COP 3403 - Spring18
 *  a-b-c Calculator
 */

//Import libraries
import java.util.*;

/*
 *  This program finds the least steps to get to a target, given three operations
 */
public class calc
{
    //Class variables
    private static Scanner scan;
    private static int numCases;
    private static int addA;
    private static int multB;
    private static int divC;
    private static int target;
    private static final int DIGITS = 1000000;
    
    /*
     *  Main method
     */
    public static void main(String args[]) {
        //Instantiate scanner for StdIn
        scan = new Scanner(System.in);
        
        //Read how many input cases must be solved
        numCases = scan.nextInt();
        
        //Loop through all the input cases
        for (int i = 0; i < numCases; i++)
        {
            //Read all the constant operands of the current case
            addA = scan.nextInt();
            multB = scan.nextInt();
            divC = scan.nextInt();
            
            //Read the current target to calculate towards
            target = scan.nextInt();
            //Solve the min operations to perform to get the target
            System.out.println(calculateTarget());
        }
    }
    
    /*
     *  Uses BFS to find minimum operations that result in the target
     */
    private static int calculateTarget()
    {
        //Check if the target is zero
        if (target == 0)
        {
            //It takes zero operations to go from 0 to 0
            return 0;
        }
        
        //Local variable
        Node cur;
        int curAdd;
        int curMult;
        int curDiv;
        
        //Create a queue to navigate through
        Queue<Node> nodes = new LinkedList<Node>();
        boolean used[] = new boolean[DIGITS];
        Arrays.fill(used, false);
        
        //Add the intial value of the calculator to the queue
        nodes.add(new Node(0, 0));
        used[0] = true;
        
        //Loop until the queue is empty
        while (!nodes.isEmpty())
        {
            //Take the head of the queue
            cur = nodes.poll();
            
            //Perform each operation
            curAdd = (cur.getValue() + addA) % DIGITS;
            curMult = (cur.getValue() * multB) % DIGITS;
            curDiv = (cur.getValue() / divC) % DIGITS;
            
            //Check if any of the new values are the target value
            if (target == curAdd || target == curMult || target == curDiv)
            {
                //Return the how many operations it took to find this solution
                return cur.getDistance() + 1;
            }
            
            //Check if any of the new values have alread been added
            if (!used[curAdd])
            {
                //Add the new node to the queue
                nodes.add(new Node(curAdd, cur.getDistance() + 1));
                used[curAdd] = true;
            }
            
            if (!used[curMult])
            {
                //Add the new node to the queue
                nodes.add(new Node(curMult, cur.getDistance() + 1));
                used[curMult] = true;
            }
            
            if (!used[curDiv])
            {
                //Add the knew node to the queue
                nodes.add(new Node(curDiv, cur.getDistance() + 1));
                used[curDiv] = true;
            }
        }
        
        //Return the fail case indicator
        return -1;
    }
}

/*
 *  Node in BFS tree
 */
class Node
{
    //Class variables
    private Integer value;
    private int distance;
    
    /*
     *  Constructor
     */
    protected Node(int initV, int initD)
    {
        //Initialize the class variables
        value = initV;
        distance = initD;
    }
    
    protected Integer getValue()
    {
        return value;
    }
    
    protected int getDistance()
    {
        return distance;
    }
}