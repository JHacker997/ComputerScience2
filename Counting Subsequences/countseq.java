/*
 * John Hacker
 * COP 3503 - Spring18
 * Old Program #4
 */

//Import statement
import java.util.*;

public class countseq
{
    //Class variables
    private static int numCases;
    private static String seq;
    private static String subseq;
    private static Scanner scan;
    private static long total;
    private static long[][] table;
    
    /*
     * Reads input and calls solving method
     */
  	public static void main(String [] args)
  	{
        // Get the input.
        scan = new Scanner(System.in);
        
        //Read number of input cases
        numCases = scan.nextInt();
        
        //Loop through all of the input cases
        for (int i = 0; i < numCases; i++)
        {
            //Read the sequence and given subsequence
            seq = scan.next();
            subseq = scan.next();
            
            //Solve and print solution
            System.out.println(lcsdyn());
        }
  	}
  	
  	/*
  	 * Dynamically find the amount of times y appears as a subsequence of x
  	 */
  	public static long lcsdyn()
  	{
        //Local variables
        total = 0;
        table = new long[seq.length()+1][subseq.length()+1];
        
        // Fill in each LCS value in order from bottom to top
        for (int i = seq.length() - 1; i >= 0; i--)
        {
            // Fill in each LCS value in order from right to left
            for (int j = subseq.length() - 1; j >= 0; j--)
            {
                // If last characters of prefixes match, add one to former value.
                if (seq.charAt(i) == subseq.charAt(j))
                {
                    //Check if in the far right column
                    if (j == subseq.length() - 1)
                    {
                        //Mark as first instance of char being found
                        table[i][j] = 1;
                    }
                    else
                    {
                        //Loop through all the below the current row
                        for (int k = i + 1; k < seq.length(); k++)
                        {
                            //Add all instances of next chars that appear after the current char
                            table[i][j] += table[k][j+1];
                        }
                    }
                    
                    //Check if in the first column
                    if (j == 0)
                    {
                        //Add all completed subseqs from each instance of the first char of the subseq in seq
                        total += table[i][j];
                    }
                }
            }
        }
        
        // This is our answer.
        return total;
  	}
}