/*
 *  John Hacker
 *  COP3503 - Spring18
 *  Camera Time
 */

//Imports
import java.util.*;

public class CameraTime
{
    //Member variables
    private static Scanner scan;
    private static int numObjects;
    private static double distance;
    private static double beginPart;
    private static double endPart;
    private static ArrayList<MyObject> myObjects;
    private static CriticalPoint xGCV;
    private static int picsTaken;
    private static PriorityQueue<CriticalPoint> critPoints;
    private static ArrayList<CriticalPoint> pastCritPoints;
    
    public static void main(String args[]) {
        //Read the input
        readInput();
        
        //Initialize other important information
        initOthers();
        
        //Finds minimum pictures to be taken
        solve();
        
        //Print the amount of pictures that need to be taken
        System.out.println(picsTaken);
    }
    
    /*
     *  Finds minimum pictures needed to capture all objects
     */
    private static void solve()
    {
        //Check if there are no objects to take a picture of
        if (numObjects == 0)
        {
            //Exit with no pictures taken
            return;
        }
        
        //Local variables
        boolean takePic;
        
        //Loop through all of the critical points on the x axis
        for (int i = 0; myObjects.size() > 0 && i < 2 * numObjects; i++)
        {
            //Move the GCV to the next critical point
            xGCV = critPoints.poll();
            
            //Reset test variable to default
            takePic = false;
            
            //Check if there are still critical points after the current one
            if (critPoints.size() > 0)
            {
                //Loop until the next critical point is not at the same x value
                while (xGCV.getXValue() == critPoints.peek().getXValue())
                {
                    //Check if the next poll is the end of a viewing area
                    if (!critPoints.peek().getIfBeginVisible())
                    {
                        //Indicate that a picture should be taken now
                        takePic = true;
                        
                        //Pull the critical point out of critical points to be checked
                        critPoints.poll();
                    }
                    else
                    {
                        //Move the critical point to past ones
                        pastCritPoints.add(critPoints.poll());
                    }
                }
            }
            //Check if the critical point is the beginning of an area of visibility
            if (xGCV.getIfBeginVisible())
            {
                //Add the critical point the array list of already passed points
                pastCritPoints.add(xGCV);
            }
            else if (!xGCV.getIfBeginVisible() || takePic)
            {
                //Loop until the passed objects are all no longer considered
                while (pastCritPoints.size() > 0)
                {
                    //Remove all the objects with their picture taken and their future critical points
                    critPoints.remove(myObjects.get(0).getEndVisible());
                    pastCritPoints.remove(0);
                    myObjects.remove(0);
                }
                
                //Increase pictures taken by one
                picsTaken++;
            }
        }
    }
    
    /*
     *  Read and store data from StdIn
     */
    private static void readInput()
    {
        //Instantiate Scanner
        scan = new Scanner(System.in);
        
        //Read all the input from StdIn
        numObjects = scan.nextInt();
        distance = scan.nextDouble();
        beginPart = scan.nextDouble();
        endPart = scan.nextDouble();
    }
    
    /*
     *  Initialize other member variables and calculate the critical values
     */
    private static void initOthers()
    {
        //Local variables
        double x;
        double y;
        boolean behind = false;
        
        //Instantiate collections
        myObjects = new ArrayList<MyObject>();
        critPoints = new PriorityQueue<CriticalPoint>();
        pastCritPoints = new ArrayList<CriticalPoint>();
        
        //Loop through all the objects
        MyObject temp;
        for (int i = 0; i < numObjects; i++)
        {
            //Read the coordinates of the object
            x = scan.nextDouble();
            y = scan.nextDouble();
            
            //Check if the object is above the Great Occluder
            if (y > distance)
            {
                //Insantiate a temp for the current object
                temp = new MyObject(x, y, beginPart, endPart, distance);
                
                //Add the temp to an array list
                myObjects.add(temp);
                
                //Add the critical points of where the object is first and last visible to a priority queue
                critPoints.add(temp.getBeginVisible());
                critPoints.add(temp.getEndVisible());
                
                //Store that there is an object behind the wall
                behind = true;
            }
        }
        
        //Check if there are any objects behind the wall
        if (behind || numObjects == 0)
        {
            //Start counting pics at 0
            picsTaken = 0;
        }
        else
        {
            //Pics taken will only be one and this should not be changed later
            picsTaken = 1;
        }
        
        //Sort the array of objects by when they are first visible
        Collections.sort(myObjects);
    }
}

/*
 *  Calculates and holds info on every critical point on the x axis
 */
class CriticalPoint implements Comparable<CriticalPoint>
{
    //Member variables
    private double xValue;
    private boolean ifBeginVisible;
    private boolean ifSkip;
    
    protected CriticalPoint(boolean ifBegin, double x0, double y0, double x1, double y1)
    {
        //Assign whether this cirtical point is the start of visibility of an object
        ifBeginVisible = ifBegin;
        
        //Check if the object is directly above or below
        if (x0 == x1 || y0 == y1)
        {
            //Make the critical point in line with the x axis, object, and end of glass partition
            xValue = x0;
        }
        else
        {
            //Calculate the x intercept of the line given its manipulated point-slope equation
            xValue = (0 - y0) / ((y1 - y0) / (x1 - x0)) + x0;
        }
    }
    
    //Return the x value of the critical point
    protected double getXValue()
    {
        return xValue;
    }
    
    //Return whether this cirtical point is the start of visibility of an object
    protected boolean getIfBeginVisible()
    {
        return ifBeginVisible;
    }
    
    //Returns if the critcal value should be skipped (image it comes from has had its picture taken)
    protected boolean getIfSkip()
    {
        return ifSkip;
    }
    
    //Override the compareTo method of the comparable interface
    public int compareTo(CriticalPoint other)
    {
        if (this.xValue - other.xValue > 0)
        {
            return 1;
        }
        else if (this.xValue - other.xValue < 0)
        {
            return -1;
        }
        return 0;
    }
}

/*
 *  Class for each object to be taken picture of
 */
class MyObject implements Comparable<MyObject>
{
    //Member variables
    private double x;
    private double y;
    private CriticalPoint beginVisible;
    private CriticalPoint endVisible;
    private boolean ifPicTaken;
    private boolean ifVisible;
    
    //Construct the object with given coordinates
    protected MyObject(double newX, double newY, double beginPart, double endPart, double distance)
    {
        //Initialize member variables
        x = newX;
        y = newY;
        ifPicTaken = false;
        ifVisible = false;
        
        //Check if the objects are behind the Great Occluder
        if (y > distance)
        {
            //Create the critical points for the limits of where the objects can be seen
            beginVisible = new CriticalPoint(true, beginPart, distance, x, y);
            endVisible = new CriticalPoint(false, endPart, distance, x, y);
        }
    }
    
    //Set the visibility of the object
    protected void setIfVisible(boolean newVisible)
    {
        ifVisible = newVisible;
    }
    
    //Take the picture and make the object invisible
    protected void takePic()
    {
        ifVisible = false;
        ifPicTaken = true;
    }
    
    //Return the X coordinate of the object
    protected double getX()
    {
        return x;
    }
    
    //Return the Y coordinate of the object
    protected double getY()
    {
        return y;
    }
    
    //Return whether the object has already had its picture taken
    protected boolean getIfPicTaken()
    {
        return ifPicTaken;
    }
    
    //Return whether the object is visible to the GCV
    protected boolean getIfVisible()
    {
        return ifVisible;
    }
    
    //Return the first x value that the object can be seen
    protected CriticalPoint getBeginVisible()
    {
        return beginVisible;
    }
    
    //Return the last x value that the object can be seen
    protected CriticalPoint getEndVisible()
    {
        return endVisible;
    }
    
    //Overried compareTo method from Comparable interface
    public int compareTo(MyObject other)
    {
        if (this.beginVisible.getXValue() - other.beginVisible.getXValue() > 0)
        {
            return 1;
        }
        else if (this.beginVisible.getXValue() - other.beginVisible.getXValue() < 0)
        {
            return -1;
        }
        return 0;
    }
}