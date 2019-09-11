/*
 * John Hacker
 * COP 3503 - Spring18
 * NP3 - Number of Optimal Pacman Paths
 */

//Import statement
import java.util.*;

public class pacman
{
    //Class variables
    private static int numRows;
    private static int numCols;
    private static Vertex[][] board;
    private static Queue<Vertex> queue;
    private static Vertex current;
    private static Vertex next;
    private static Vertex last;
    private static final long MODULO = 1000000007;
    
    /*
     * Startof program
     */
    public static void main(String args[])
    {
        //Read the input
        readInput();
        
        //Solve the problem
        solve();
        
        //Print the solution to the problem
        printSolution();
    }
    
    /*
     * Find the max value, how many paths can be used to find that value, and the alphabetically-first optimal path
     */
    private static void solve()
    {
        //Instantiate the queue
        queue = new LinkedList<Vertex>();
        
        //Add the starting vertex to the queue
        queue.add(board[0][0]);
        board[0][0].setUsed(true);
        
        //Loop until the queue is empty
        while (!queue.isEmpty())
        {
            //Take out the next vertex
            current = queue.poll();
            
            //Check if the vertex is on the bottom edge of the board and if the next vertex has been used
            if (current.getRow() < numRows - 1 && !board[current.getRow() + 1][current.getCol()].getUsed())
            {
                //Set the next vertex to be added to the queue
                next = board[current.getRow() + 1][current.getCol()];
                
                //Check if the next vertex is on the left edge of the board
                if (next.getCol() > 0)
                {
                    //Check if the optimal path came from the left
                    if (board[next.getRow()][next.getCol() - 1].getValue() > board[next.getRow() - 1][next.getCol()].getValue())
                    {
                        last = board[next.getRow()][next.getCol() - 1];
                        next.setValue(next.getValue() + last.getValue());
                        next.setNumPaths(last.getNumPaths());
                        next.setPath(last.getPath() + "R");
                    }
                    //Check if the optimal path came from above
                    else if (board[next.getRow() - 1][next.getCol()].getValue() > board[next.getRow()][next.getCol() - 1].getValue())
                    {
                        last = board[next.getRow() - 1][next.getCol()];
                        next.setValue(next.getValue() + last.getValue());
                        next.setNumPaths(last.getNumPaths());
                        next.setPath(last.getPath() + "D");
                    }
                    //Both the left and above paths were equal in value
                    else
                    {
                        last = board[next.getRow() - 1][next.getCol()];
                        next.setNumPaths(last.getNumPaths());
                        last = board[next.getRow()][next.getCol() - 1];
                        next.setValue(next.getValue() + last.getValue());
                        next.setNumPaths(next.getNumPaths() + last.getNumPaths());
                        next.setPath(last.getPath() + "R");
                    }
                }
                //Player only had the option to move right
                else
                {
                    last = board[next.getRow() - 1][next.getCol()];
                    next.setValue(next.getValue() + last.getValue());
                    next.setNumPaths(last.getNumPaths());
                    next.setPath(last.getPath() + "D");
                }
                
                //Add the next vertex to the queue and set it to be used
                next.setUsed(true);
                queue.offer(next);
            }
            
            //Check if the vertex is on the right edge of the board and if the next vertex has been used
            if (current.getCol() < numCols - 1 && !board[current.getRow()][current.getCol() + 1].getUsed())
            {
                //Set the next vertex to be added to the queue
                next = board[current.getRow()][current.getCol() + 1];
                
                //Check if the vertex is on the top edge of the board
                if (next.getRow() > 0)
                {
                    //Check if the optimal path came from the left
                    if (board[next.getRow()][next.getCol() - 1].getValue() > board[next.getRow() - 1][next.getCol()].getValue())
                    {
                        last = board[next.getRow()][next.getCol() - 1];
                        next.setValue(next.getValue() + last.getValue());
                        next.setNumPaths(last.getNumPaths());
                        next.setPath(last.getPath() + "R");
                    }
                    //Check if the optimal path came from above
                    else if (board[next.getRow() - 1][next.getCol()].getValue() > board[next.getRow()][next.getCol() - 1].getValue())
                    {
                        last = board[next.getRow() - 1][next.getCol()];
                        next.setValue(next.getValue() + last.getValue());
                        next.setNumPaths(last.getNumPaths());
                        next.setPath(last.getPath() + "D");
                    }
                    //Both the left and above paths were equal in value
                    else
                    {
                        last = board[next.getRow() - 1][next.getCol()];
                        next.setNumPaths(last.getNumPaths());
                        last = board[next.getRow()][next.getCol() - 1];
                        next.setValue(next.getValue() + last.getValue());
                        next.setNumPaths(next.getNumPaths() + last.getNumPaths());
                        next.setPath(last.getPath() + "R");
                    }
                }
                //The player only had the option to move right
                else
                {
                    last = board[next.getRow()][next.getCol() - 1];
                    next.setValue(next.getValue() + last.getValue());
                    next.setNumPaths(last.getNumPaths());
                    next.setPath(last.getPath() + "R");
                }
                
                //Add the next vertex to the queue and set it to be used
                next.setUsed(true);
                queue.offer(next);
            }
        }
    }
    
    /*
     * Prints the max attainable value, the amount of paths for that value, and the alphabetically-first optimal path
     */
    private static void printSolution()
    {
        Vertex end = board[numRows - 1][numCols - 1];
        System.out.println(end.getValue() + " " + (end.getNumPaths() % MODULO));
        System.out.println(end.getPath());
    }
    
    /*
     * Read all of the input and intialize the game board
     */
    private static void readInput()
    {
        //Instantiate scanner
        Scanner scan = new Scanner(System.in);
        
        //Read the size of the board
        numRows = scan.nextInt();
        numCols = scan.nextInt();
        
        //Instantiate the game board based on the read dimensions
        board = new Vertex[numRows][numCols];
        
        //Loop through all of the rows in the board
        for (int r = 0; r < numRows; r++)
        {
            //Loop through all of the columns in the board
            for (int c = 0; c < numCols; c++)
            {
                //Check if the current spot is the most top left or most bottom right
                if ((r != 0 || c != 0) && (r != numRows - 1 || c != numCols - 1))
                {
                    //Read the value at the current spot on the board
                    board[r][c] = new Vertex(scan.nextInt(), r, c);
                }
                else
                {
                    //Read the P or E to get rid of it and keep the value on the board at default 0
                    board[r][c] = new Vertex(0, r, c);
                    scan.next();
                }
            }
        }
    }
    
    /*
     * Print the game board
     */
    private static void printBoard()
    {
        for (int r = 0; r < numRows; r++)
        {
            for (int c = 0; c < numCols; c++)
            {
                if (c == numCols - 1)
                {
                    System.out.println(board[r][c].getValue());
                }
                else
                {
                    System.out.print(board[r][c].getValue() + " ");
                }
            }
        }
        System.out.println();
    }
}

/*
 * A spot on the board that stores its value and the path to get to it from P
 */
class Vertex
{
    //Class variables
    private long value;
    private long numPaths;
    private int row;
    private int col;
    private String path;
    private boolean used;
    
    //Constructor
    public Vertex(int newValue, int newRow, int newCol)
    {
        value = newValue;
        row = newRow;
        col = newCol;
        numPaths = 1;
        path = "";
        used = false;
    }
    
    protected long getValue()
    {
        return value;
    }
    
    protected long getNumPaths()
    {
        return numPaths;
    }
    
    protected int getRow()
    {
        return row;
    }
    
    protected int getCol()
    {
        return col;
    }
    
    protected String getPath()
    {
        return path;
    }
    
    protected boolean getUsed()
    {
        return used;
    }
    
    protected void setValue(long newValue)
    {
        value = newValue;
    }
    
    protected void setNumPaths(long newNumPaths)
    {
        numPaths = newNumPaths;
    }
    
    protected void setPath(String newPath)
    {
        path = newPath;
    }
    
    protected void setUsed(boolean newUsed)
    {
        used = newUsed;
    }
}