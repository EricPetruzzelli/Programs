import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class FinanceMain 
{
    //SEVERAL HASHMAP OF SORTED SETS OF TRANSACTIONS!!!
     static Map<String, SortedSet<Transaction>> TypeMap=new HashMap<>();
     static Map<String, SortedSet<Transaction>> LocationMap=new HashMap<>();
     static Map<String, SortedSet<Transaction>> OriginMap=new HashMap<>();


    final public static String INFO_FILE_NAME="FinanceInfo.txt";
    //debuggers
    static boolean debug=true;
    static boolean debugDetail=true;
    public static void main(String[] args) 
    {
        if(debug)
            System.err.println("DEBUG ON \n STARTING MAIN");

        ReadInfo(INFO_FILE_NAME);


        while(true)
        {
            System.out.println("\n\nCHOOSE AN OPTION");
            System.out.println("1. PRINT TYPES");
            System.out.println("2. GET AMOUNT OF MONEY IN A LOCATION");
            System.out.println("0. END PROGRAM");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextInt()) {
                case 1:
                    printHashMap(TypeMap);
                    break;
                case 2:
                    getAmountInLocation(LocationMap,scanner.next());
                    break;
                case 0:
                    System.out.println("ENDING PROGRAM");
                    System.exit(0);
                    break;
                default:
                    System.out.println("ENTER VALID INPUT");
            }
        }

    }

    static double getAmountInLocation(Map<String, SortedSet<Transaction>> map, String key)
    {
        double total=0;
        Iterator<Transaction> list = map.get(key).iterator();
        while(list.hasNext())
        {
            total=total + list.next().cost;
        }
        return total;
    }
    static void printHashMap(Map<String, SortedSet<Transaction>> map)
    {
        Iterator<Transaction> curList;
        String curKey;
        if(debug)
            System.err.println("PRINTING A HASHMAP");
        Iterator<String> keyIterator= new TreeSet<String>(map.keySet()).iterator(); //Tree Set sorts the set
        while(keyIterator.hasNext())
        {
            curKey=keyIterator.next();
            System.out.print("\n"+curKey+":\n");
            curList = map.get(curKey).iterator();//ERROR HERE
            while(curList.hasNext())
            {
                System.out.println(curList.next()+"\n");
            }
        }
    }

    static void ReadInfo(String fileName)
    {
        int[] curOcDate=new int[3];
        int[] curDate=new int[3];
        Double curCost;
        String curOrigin;
        String curType;
        String curActualLocation;
        String curDescription; 

        Transaction curTransaction;
        if(debug)
            System.out.println("STARTING READINFO");
/*
Date: (mmddyy)
Cost:
Type Origin: (CVS, Midnight Oil, etc )
Type: (Transfer, work, Fashion, rent, fun, etc)
Actual Location or Transfer To: (' for automatically set??)
Description: (nail polish, T-Shirt, concert tickets, etc)
*/

        Scanner scanner=null;
        File file = new File(fileName);
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
        }
        int amountToSkip = scanner.nextInt();
        
        for (int i=0; i<amountToSkip; i++)
            scanner.nextLine();
        
        
        /*reads through the file in this order
        line read for date(s)

        */
        while(scanner.hasNextInt())
        {
            //scans line for date(s)

            String nextLine=scanner.nextLine();
            while(nextLine.equals(""))
            {
                nextLine=scanner.nextLine();
            }
            Scanner nextLineScanner = new Scanner(nextLine);
            curOcDate[0]= nextLineScanner.nextInt();//error here?
            curOcDate[1]=nextLineScanner.nextInt();
            curOcDate[2]= nextLineScanner.nextInt();
            if(nextLineScanner.hasNextInt())
            {
                curDate[0]= nextLineScanner.nextInt(); 
                curDate[1]=nextLineScanner.nextInt();
                curDate[2]= nextLineScanner.nextInt();
                
            }
            else 
            {
                curDate=curOcDate.clone();
            }
            
                

            curCost=scanner.nextDouble(); 
            scanner.nextLine();
            curOrigin=scanner.nextLine();
            curType=scanner.nextLine();
            curActualLocation=scanner.nextLine();
            curDescription= scanner.nextLine();
           curTransaction=new Transaction(curOcDate,curDate, curCost, curOrigin, curType, curActualLocation, curDescription);
           if(debugDetail)
           {
            System.err.println(curTransaction+"\n");
           }
            mapPut(TypeMap, curType, curTransaction);
            mapPut(LocationMap, curActualLocation, curTransaction);
            mapPut(OriginMap, curOrigin, curTransaction);
        }



        if(debug)
            System.out.println("COMPLETED READINFO");
        scanner.close();
    }

    public static void mapPut(Map<String, SortedSet<Transaction>> map, String key, Transaction value)
    {

        if(map.get(key)==null)
        {
            map.put(key,new TreeSet<Transaction>());
        }
        
        map.get(key).add(value);
    }

}
