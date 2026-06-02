import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.LinkedList;

public class FinanceMain //moneyOrganizer
{
    //SEVERAL HASHMAP OF SORTED SETS OF TRANSACTIONS!!!

     static int YEARSTART=26;
     static int YEAREND=27;

     static MoneyOrganizer moneyOrganizer;
     static Graph theGraph;
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
            System.err.println("DEBUG ON \nSTARTING MAIN");

        ReadInfo(INFO_FILE_NAME);
        initializeOptions();
        String op = theGraph.read();
        int x;
        String option;

        if(op.contains("explore"))
        {
            option="explore";
        }
        else if(op.contains("get values"))
        {
            option = "get values";
        }
        else
            option="root";

        x=Integer.valueOf(op.substring(0, op.indexOf(option)));
        switch (option) {
            case "root":
                break;
            case "explore":
                System.out.print("EXPLORE CHOSEN");
                break;
            case "get values":
                break;
            default:
                throw new AssertionError();
        }
            
        //.contains then switch could work?



    }
    private static void initializeOptions()
    {
        if(debug)
            System.err.println("INITIALIZE STARTED");
        theGraph= new Graph("1. EXPLORE\n2.GET VALUES");

        String printThis="1. PRINT MAINTAGS\n2. PRINT ALL TAGS\n3. PRINT LOCATIONS\n4. PRINT ALL TRANSACTIONS";
        theGraph.addNode("explore", printThis, "root");
        printThis = "1. GET TOTAL\n2. GET TOTAL FROM TAG\n3. GET TOTAL FROM LOCATIONS";
        theGraph.addNode("get values", printThis, "root");
        if(debug)
            System.err.println("INITIALIZE COMPLETED");
    }
    private static class Graph
    {
        final String FIRSTLINE = "\n\nCHOOSE AN OPTION\n";
        final String LASTLINE = "\n0. END PROGRAM\n-1. GO BACK\n";
        final Node root;

        public Graph(String printOut)
        {
            root = new Node("root",printOut,null);
        }
        public void addNode(String nodeName, String printOutNode, String parentNode)
        {
            search(parentNode).addChild(nodeName, printOutNode);
        }
        private void printOut(Node node)
        {
            System.out.print(FIRSTLINE+node+LASTLINE); 
        }



        private Node search(String name)
        {
            return searchHelper(name, root);
        }
        private Node searchHelper(String nameGoal, Node curNode)
        {
            if(curNode.name.equals(nameGoal))
                return curNode;
            for(Node child: curNode.children)
            {
                searchHelper(nameGoal,child);
            }
            return null;
        }
        public String read()
        {
            return read(root);
        }
        public String read(Node node)
            {
                printOut(node);
                int x=0;
                Scanner scanner=new Scanner(System.in);
                try{x= scanner.nextInt();} //error here?
                catch(InputMismatchException e)
                {
                    System.out.println("PLEASE ENTER AN ACCEPTED VALUE");
                    return read(node);
                }
                //if choose branching option
                if(x==0)
                {
                    System.out.print("ENDING PROGRAM");
                    System.exit(0);
                    return "";
                }
                else if(x==-1)
                {
                    return read(node.parent);
                }
                else if(node.children.size()>=x)
                {
                    return read(node.children.get(x-1));
                }
                else
                {
                    return x+node.name;
                }
            }
        
        private class Node
        {
            String name;
            String printOut;
            LinkedList<Node> children;
            Node parent;
            public Node(String name, String printOut, Node parent)
            {
                this.name=name;
                this.printOut=printOut;
                this.parent=parent;
                children=new LinkedList<>();
            }
            public void addChild(String name, String printOut)
            {
                children.add(new Node(name, printOut,this));
            }
            public String toString()
            {
                return printOut;
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
