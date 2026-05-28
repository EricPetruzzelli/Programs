import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.LinkedList;

public class FinanceMain //moneyOrganizer
{
    //SEVERAL HASHMAP OF SORTED SETS OF TRANSACTIONS!!!

     static int YEARSTART=26;
     static int YEAREND=27;

     static MoneyOrganizer moneyOrganizer;
     static TransactionCalander OccuranceCal=new TransactionCalander(true);
     static TransactionCalander LocationCal=new TransactionCalander(false);

     static double totalAmount;
     


    final public static String INFO_FILE_NAME="FinanceInfo.txt";
    //debuggers
    static boolean debug=true;
    static boolean debugDetail=false;
    public static void main(String[] args) 
    {
        if(debug)
            System.err.println("DEBUG ON \n STARTING MAIN");

        ReadInfo(INFO_FILE_NAME);


        while(true)
        {
            System.out.println("\n\nCHOOSE AN OPTION");
            System.out.println("1. PRINT ALL TRANSACTIONS");
            System.out.println("0. END PROGRAM");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextInt()) {
                case 1:
                    System.out.print(moneyOrganizer.stringOfTransactions());
                    break;
                case 2:
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
        Set<Transaction> transactionSet = new HashSet<Transaction>();
        int[] curOcDate=new int[3];
        int[] curDate=new int[3];
        Double curCost;
        String curOrigin;
        ArrayList<String> tags;
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
            //year month day format, so different than input format
            curOcDate[1]= nextLineScanner.nextInt();
            curOcDate[2]=nextLineScanner.nextInt();
            curOcDate[0]= nextLineScanner.nextInt();
            if(nextLineScanner.hasNextInt())
            {
                curDate[1]= nextLineScanner.nextInt(); 
                curDate[2]=nextLineScanner.nextInt();
                curDate[0]= nextLineScanner.nextInt();
                
            }
            else 
            {
                curDate=curOcDate.clone();
            }
            
                

            curCost=scanner.nextDouble(); 
            scanner.nextLine();
            curOrigin=scanner.nextLine();


            tags=new ArrayList<>();
            nextLine=scanner.nextLine();
            nextLineScanner = new Scanner(nextLine);
            while(nextLineScanner.hasNext())
            {
                tags.add(nextLineScanner.next());
            }

            



            curActualLocation=scanner.nextLine();
            curDescription= scanner.nextLine();
           curTransaction=new Transaction(curOcDate,curDate, curCost, curOrigin, tags, curActualLocation, curDescription);
           if(debugDetail)
           {
            System.err.println(curTransaction+"\n");
           }
            transactionSet.add(curTransaction);
            OccuranceCal.addTransaction(curTransaction);
            LocationCal.addTransaction(curTransaction);
        }

        moneyOrganizer=new MoneyOrganizer(transactionSet);
        if(debug)
            System.out.println("COMPLETED READINFO");
        scanner.close();
    }

    private static class TransactionCalander
    {
        //each date will be an arraylist of arraylists of arraylists of sets (YY/MM/DD)
        static LinkedList<LinkedList<LinkedList<TreeSet<Transaction>>>> calander;
        static boolean useOcDate;        
        //uses several arraylists in succession. If space or initial runtime become an issue, could turn this into a tree instead
        public TransactionCalander(boolean useOcDate)
        {
            calander=new LinkedList<>();

            for(int yearIndex=0; yearIndex< YEAREND-YEARSTART+1;yearIndex++)
            {
                calander.add(new LinkedList<>());
                for(int monthIndex=0; monthIndex<12;monthIndex++)
                {
                    calander.get(yearIndex).add(new LinkedList<>());
                    for(int dayIndex=0;dayIndex<31;dayIndex++)
                        calander.get(yearIndex).get(monthIndex).add(new TreeSet<>());
                }
            }




            this.useOcDate=useOcDate;
        }
        //date is month day year
        public static void addTransaction(Transaction transaction)
        {//year month day format
            int[] date;
            if(useOcDate)
                date=transaction.OcDate.clone();
            else
                date=transaction.date.clone();

            date[1]=date[1]-1;
            date[2]=date[2]-1; //reduce by one so fits into 12 system (so january is 00)
            int yearIndex = date[0]-YEARSTART;
            calander.get(yearIndex).get(date[1]).get(date[2]).add(transaction);




        }
    }
}
