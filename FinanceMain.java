import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;

public class FinanceMain 
{
    //SEVERAL HASHMAP OF SORTED SETS OF TRANSACTIONS!!!
     static Map<String, SortedSet<Transaction>> TypeMap=new HashMap<>();
     static Map<String, SortedSet<Transaction>> LocationMap=new HashMap<>();
     static Map<String, SortedSet<Transaction>> OriginMap=new HashMap<>();


    final public static String INFO_FILE_NAME="FinanceInfo.txt";
    //debuggers
    static boolean debug=true;
    static boolean debugDetail=false;
    public static void main(String[] args) 
    {
        if(debug)
            System.out.println("DEBUG ON \n STARTING MAIN");

        ReadInfo(INFO_FILE_NAME);
    

    }

    static void ReadInfo(String fileName)
    {
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
        
        

        while(scanner.hasNextInt())
        {
           curDate[0]= scanner.nextInt();
           curDate[1]=scanner.nextInt();
           curDate[2]= scanner.nextInt();

            curCost=scanner.nextDouble(); //ERROR HERE
            scanner.nextLine();
            curOrigin=scanner.nextLine();
            curType=scanner.nextLine();
            curActualLocation=scanner.nextLine();
            curDescription= scanner.nextLine();

           curTransaction=new Transaction(curDate, curCost, curOrigin, curType, curActualLocation, curDescription);
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
