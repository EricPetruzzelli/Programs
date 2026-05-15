import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;

public class FinanceMain 
{
    //SEVERAL HASHMAP OF SORTED SETS OF TRANSACTIONS!!!
     Map<String, SortedSet<Transaction>> typeMap=new HashMap<>();









    final public static String INFO_FILE_NAME="FinanceInfo.txt";
    //debuggers
    static boolean debug=true;
    public static void main(String[] args) 
    {
        if(debug)
            System.out.println("DEBUG ON \n STARTING MAIN");

        ReadInfo(INFO_FILE_NAME);
    }

    static void ReadInfo(String fileName)
    {
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
        System.out.println(scanner.next());
        
        scanner.close();

        //saving dates



        if(debug)
            System.out.println("COMPLETED READINFO");
    }

}
