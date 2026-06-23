import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
public class FinanceMain //moneyOrganizer
{
     static MoneyOrganizer moneyOrganizer;
     static Graph theGraph;
    final public static String INFO_FILE_NAME="FinanceInfo.txt";
    final public static String SPECIALCASES="SpecialCases.txt";
    final private static int[] NOENDDATE={Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
    final private static int[] NOSTARTDATE={-1,-1,-1};
    //debuggers
    static boolean debug=true;
    static boolean debugPrintEachTransaction=true;
    static boolean debugPrintTransactionList=false;
    public static void main(String[] args) throws Exception
    {
        if(debug)
            System.err.println("DEBUG ON \nSTARTING MAIN");
        moneyOrganizer=new MoneyOrganizer(ReadInfo(INFO_FILE_NAME));
        initializeOptions(); //does what the method says
        theGraph.read(); //everything should run in the graph, so stop here
    }
    private static int[][] askForDates()
    {
        int[][] output=new int[2][3];

        Scanner scanner = new Scanner(System.in);
        System.out.println("Start date (mm dd yy)? (Press 'enter' for none)");
        
        Scanner nxtLine = new Scanner(scanner.nextLine());
        if(!nxtLine.hasNext())
            output[0] = NOSTARTDATE;
        else
        {
            output[0][1]=nxtLine.nextInt();
            output[0][2]=nxtLine.nextInt();
            output[0][0]=nxtLine.nextInt();
        }
        System.out.println("End date (mm dd yy)? (Press 'enter' for none)");
        nxtLine = new Scanner(scanner.nextLine());
        
        if(!nxtLine.hasNext())
            output[1] = NOENDDATE;
        else
        {
            output[1][1]=nxtLine.nextInt();
            output[1][2]=nxtLine.nextInt();
            output[1][0]=nxtLine.nextInt();
        }        
        return output;
    }
   /** @param whichToDo Locations, Tags, etc
     * @purpose to print out name and price of tags, locations, etc in the Money Organizer as one concise method
     * @asksFor Start date and End date
     * @throws Exception */
    @SuppressWarnings("unchecked")
    public static void printSummary(String whichToDo) throws Exception
    {
        int[][] dates = askForDates();
        int[] startDate=dates[0];
        int[] endDate=dates[1];
        Method totalInMethod = moneyOrganizer.getClass().getMethod("getTotalIn"+whichToDo,String.class,int[].class,int[].class);
        Method getMethod = moneyOrganizer.getClass().getMethod("get"+whichToDo,int[].class,int[].class);
       // Set<String> stringSet = (Set<String>) getMethod.invoke(moneyOrganizer,startDate,endDate); //error here

        Set<String> stringSet = moneyOrganizer.getLocations(startDate, endDate);

        for(String str:stringSet) //size = 0? (NOT RIGHT)
        {
            System.out.println(str+" has $"+totalInMethod.invoke(moneyOrganizer, str,startDate,endDate));
        }
    }
    public static void printLocations()throws Exception
    {
        printSummary("Locations");
    }
    public static void printTags()throws Exception
    {
        printSummary("Tags");
    }
    
    /**
     * @purpose: Sets up the graph, which will activate the methods and go down the dialogue tree
     */
    private static void initializeOptions() throws Exception
    {
        if(debug)
            System.err.println("INITIALIZE STARTED");
        theGraph= new Graph("1. EXPLORE\n2. BUDGETING");

        String printThis="1. LOCATIONS\n2. PRINT TAGS";
        theGraph.addNode("explore", printThis, "root");
        theGraph.addInvokingNode("exploreLocations","printLocations","explore");
        theGraph.addInvokingNode("exploreTags","printTags","explore");

        
        if(debug)
            System.err.println("INITIALIZE COMPLETED");
    }
    /**
     * @purpose: To serve as a backbone to the system and dialogue
     * @possibleImprovements: Not have dialogue use memory when going back (keeps in stack when it doesn't have to)
     */
    private static class Graph
    {
        final String FIRSTLINE = "\n\nCHOOSE AN OPTION\n";
        final String LASTLINE = "\n0. END PROGRAM\n-1. GO BACK\n";
        final Node root;
        public Graph(String printOut)
        {
            root = new Node("root",printOut,null);
        }
        public void addNode(String nodeName, String printOutNode, String parentNode) throws Exception
        {
            search(parentNode).addChild(nodeName, printOutNode);
        }
        public void addInvokingNode(String nodeName, String methodName, String parentNode) throws Exception
        {
            search(parentNode).addInvokingChild(nodeName, methodName);
        }
        public void addInvokingNode(String nodeName, String methodName,String methodParam, String parentNode) throws Exception
        {
            search(parentNode).addInvokingChild(nodeName, methodName,methodParam);
        }
        private void printOut(Node node)
        {
            System.out.print(FIRSTLINE+node+LASTLINE); 
        }
        private Node search(String name) throws Exception
        {
            return searchHelper(name, root);
        }
        private Node searchHelper(String nameGoal, Node curNode) throws Exception
        {
            if(curNode.name.equals(nameGoal))
                return curNode;
            for(Node child: curNode.children)
            {
                return searchHelper(nameGoal,child);
            }
            throw new NullPointerException("Node "+nameGoal+" not found");
        }
        public void read() throws Exception
        {
            read(root);
        }
        public void read(Node node) throws Exception
            {
                printOut(node);
                Scanner scanner=new Scanner(System.in);
                if(node.methodName!=null)
                    node.invoke();
                int x = scanner.nextInt();
                switch (x) {
                    case 0:
                        System.out.println("EXITING PROGRAM");
                        System.exit(0);
                        break;
                    case -1:
                        read(node.parent);
                        break;
                    default:
                        read(node.children.get(x-1));
                }

            }
        private class Node
        {
            String name;
            String printOut="";
            LinkedList<Node> children;
            Node parent;
            String methodName;// Which method to invoke at runtime
            String methodParam; //Optional param for method to be invoked

            /**
             * @param name Name of node
             * @param printOut String of children and options
             * @param parent Node of parent
             * @apiNote for non-leaf nodes
             */
            public Node(String name, String printOut, Node parent)
            {
                this.name=name;
                this.printOut=printOut;
                this.parent=parent;
                children=new LinkedList<>();
            }
            /**
             * @param name Name of node
             * @param parent Node of parent
             * @param methodName name of method to invoke
             * @apiNote for leaf nodes
             */
            public Node(String name, Node parent, String methodName)
            {
                this.name=name;
                this.parent=parent;
                this.methodName=methodName;
            }
            /**
             * @param name Name of node
             * @param parent Node of parent
             * @param methodName name of method to invoke
             * @param methodParam param for method to be invoked
             * @apiNote for leaf nodes
             */
            public Node(String name, Node parent, String methodName, String methodParam)
            {
                this.name=name;
                this.parent=parent;
                this.methodName=methodName;
                this.methodParam=methodParam;
            }
            public void addChild(String name, String printOut)
            {
                children.add(new Node(name, printOut,this));
            }
            public void addInvokingChild(String name, String methodName)
            {
                children.add(new Node(name, this, methodName));
            }
            public void addInvokingChild(String name, String methodName, String methodParam)
            {
                children.add(new Node(name, this, methodName,methodParam));
            }
            public String toString()
            {
                return printOut;
            }
            /**
             * @purpose invokes a method. usually prints something out
             */
            //Would most likely need to be fixed (find more elegant way than if/else statements)
            public void invoke() throws Exception
            {
                FinanceMain obj = new FinanceMain();
                Method m;
                try{
                        if(FinanceMain.class.getMethod(methodName).getParameterCount()>0)
                            m = FinanceMain.class.getMethod(methodName, String.class);
                        else
                            m = FinanceMain.class.getMethod(methodName);
                    }
                catch (NoSuchMethodException e){throw new NoSuchMethodException("Method "+methodName+" not found in FinanceMain.class");} //error here
                if(FinanceMain.class.getMethod(methodName).getParameterCount()>0)
                    m.invoke(obj,methodParam);
                else
                    m.invoke(obj);
            }
        }
    }
    static ArrayList<Transaction> ReadInfo(String fileName)
    {
        ArrayList<Transaction> transactionSet = new ArrayList<Transaction>();
        int[] curOcDate=new int[3];
        int[] curDate=new int[3];
        Double curCost;
        String curOrigin;
        ArrayList<String> tags;
        String curActualLocation;
        String curDescription; 
        String curBudget=null;

        Transaction curTransaction;
        if(debug)
            System.out.println("STARTING READINFO");
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
            curOcDate=new int[3];
            curDate=new int[3];
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

            nextLine=scanner.nextLine();
            nextLineScanner = new Scanner(nextLine);
            curActualLocation=nextLineScanner.next(); //error here
            if(nextLineScanner.hasNext())
                curBudget=nextLineScanner.next();

            curDescription= scanner.nextLine();
            curTransaction=new Transaction(curOcDate,curDate, curCost, curOrigin, tags, curActualLocation, curDescription, curBudget);
           if(debugPrintEachTransaction)
           {
            System.err.println(curTransaction+"\n");
           }
            transactionSet.add(curTransaction); //issue must be here??????? wtf whywhywhwhywhywhywhywhywhywhywhywhywhy
        }
        if(debugPrintTransactionList)
        {
            System.err.println("PRINTING TRANSACTIONS IN READ METHOD A1");
            Collections.sort(transactionSet);
            for(Transaction trans: transactionSet)
                System.out.println(trans);
        }
        if(debug)
            System.out.println("COMPLETED READINFO");
        scanner.close();

        System.err.println("TESTING OUT");
        System.err.println(transactionSet.get(1));
        return transactionSet;
    }
    /**
     * @param set set of tags,locations, or origins
     * @param x 0 for tag, 1 for location, 2 for origin
     * @return asks if you want to sort by cost
     */
    public static void askSortByCost(Set<String> set, int x)
    {
        Set<SimpleTransaction> orderedSet;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sort by cost? (Y/N)");
            if(scanner.next().toUpperCase().equals("Y"))
            {
                orderedSet = sortByCost(set, x);
                for(SimpleTransaction simpleTransaction: orderedSet)
                {
                    System.out.println(simpleTransaction);
                }
            }
    }
    /**
     * @param set set of tags,locations, or origins
     * @param x 0 for tag, 1 for location, 2 for origin
     * @return ordered set of SimpleTransactions
     */
    public static TreeSet<SimpleTransaction> sortByCost(Set<String> set, int x)
    {
        TreeSet<SimpleTransaction> output=new TreeSet<>();
        for(String string: set)
        {
            switch (x) {
                case 0:
   //                 output.add(new SimpleTransaction(moneyOrganizer.getTotalInTags(string), string));
                    break;
                case 1:
   //                 output.add(new SimpleTransaction(moneyOrganizer.getTotalInLocation(string), string));
                    break;
                case 2:
                    output.add(new SimpleTransaction(moneyOrganizer.getTotalInOrigins(string), string));
                    break;
                default:
                    throw new AssertionError();
            }
        }
        return output;
    }
    /**
     * @purpose: Simple transaction when sorting by cost and printing statements
     */
    private static class SimpleTransaction implements Comparable<SimpleTransaction>
    {
        Double cost;
        String label;
        public SimpleTransaction(Double cost, String label)
        {
            this.cost=cost;
            this.label=label;
        }
        public int compareTo(SimpleTransaction other)
        {
            return (int) (this.cost*100-other.cost*100);
        }
        public String toString()
        {
            return this.label+" "+this.cost;
        }
    }
}
