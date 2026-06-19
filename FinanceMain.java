import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.reflect.*;
import java.util.SortedSet;
public class FinanceMain //moneyOrganizer
{
    //SEVERAL HASHMAP OF SORTED SETS OF TRANSACTIONS!!!




     static MoneyOrganizer moneyOrganizer;
     static Graph theGraph;
    final public static String INFO_FILE_NAME="FinanceInfo.txt";
    final public static String SPECIALCASES="SpecialCases.txt";
    //debuggers
    static boolean debug=true;
    static boolean debugDetail=false;
    public static void main(String[] args) 
    {
        if(debug)
            System.err.println("DEBUG ON \nSTARTING MAIN");

        
       try{ moneyOrganizer=new MoneyOrganizer(ReadInfo(INFO_FILE_NAME),readSpecial(SPECIALCASES));}
       catch(FileNotFoundException e){System.err.println("FILE NOT FOUND"); System.exit(0);}
        initializeOptions();
        String op = theGraph.read();
        int x;
        String option;
        char[] charArray = op.toCharArray();
        for(int index=0; index<op.length();index++)
        {
            if(!Character.isDigit(charArray[index]))
            {
                option=op.substring(index);
            }
        }

        if(op.contains("explore"))
        {
            option="explore";
        }
        else if(op.contains("get transactions"))
        {
            option = "get transactions";
        }
        else if(op.contains("budgeting"))
        {
            option = "budgeting";
        }
        else
            option="root";

        x=Integer.valueOf(op.substring(0, op.indexOf(option)));
        switch (option) {
            case "root":
                break;
            case "explore":
                switch (x) {
                    case 1:
                        Set<String> mainTags = moneyOrganizer.getMainTags();
                        double percent;
                        for(String tag: mainTags)
                        {
                            percent = (moneyOrganizer.getTotalFromTag(tag));
                            System.out.println(tag+" "+moneyOrganizer.getTotalFromTag(tag)+"");
                        }
                        askSortByCost(mainTags, 0);
                            
                        break;
                    case 2:
                        Set<String> tags = moneyOrganizer.getTags();
                        for(String tag: tags)
                            System.out.println(tag+" "+moneyOrganizer.getTotalFromTag(tag));
                        askSortByCost(tags, 0);
                        break;
                    case 3:
                        Set<String> locations = moneyOrganizer.getLocations();
                        for(String location: locations)
                            System.out.println(location+" "+moneyOrganizer.getTotalInLocation(location));
                        askSortByCost(locations, 1);
                        break;
                    case 4:
                        Set<String> origins = moneyOrganizer.getOrigins();
                        for(String origin: origins)
                            System.out.println(origin+" "+moneyOrganizer.getTotalFromOrigin(origin));
                        askSortByCost(origins, 2);
                        break;
                    case 5: //print ALL transactions
                        System.out.println(moneyOrganizer.stringOfTransactions());
                        break;
                    default:
                        throw new AssertionError();
                }

                break;
            case "get transactions":

                break;
            case "budgeting":
                switch (x) {
                    case 1: //print all budgets and values
                        for(String budget: moneyOrganizer.getBudgets())
                        {
                            System.out.println(budget+" value is "+ moneyOrganizer.getBudgetTotal(budget));
                        }
                        break;
                }
        }
            
        //.contains then switch could work?
    }
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
    private static void initializeOptions()
    {
        if(debug)
            System.err.println("INITIALIZE STARTED");
        theGraph= new Graph("1. EXPLORE\n2. GET TRANSACTIONS\n3. BUDGETING");

        String printThis="1. PRINT MAINTAGS\n2. PRINT ALL TAGS\n3. PRINT LOCATIONS\n4. PRINT ORIGINS";
        theGraph.addNode("explore", printThis, "root");
        theGraph.addNode("get transactions", printThis, "root");
        printThis = "1. PRINT BUDGET TYPES\n2. FUN BUDGET\n3. GET PROFESSIONAL EXPENSE BUDGET";
        theGraph.addNode("budgeting", printThis, "root");
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
            String methodName;// Which method to call at runtime


            public Node(String name, String printOut, Node parent)
            {
                this.name=name;
                this.printOut=printOut;
                this.parent=parent;
                children=new LinkedList<>();
            }
            public Node(String name, String printOut, Node parent, String methodName)
            {
                this.name=name;
                this.printOut=printOut;
                this.parent=parent;
                children=new LinkedList<>();
                this.methodName=methodName;
            }
            public void addChild(String name, String printOut)
            {
                children.add(new Node(name, printOut,this));
            }
            public String toString()
            {
                return printOut;
            }
            /**
             * does a method. usually prints something out
             */
            public void activate() throws Exception
            {
                FinanceMain obj = new FinanceMain();
                Method m = FinanceMain.class.getMethod(methodName);
                m.invoke(null);
            }
        }
    }
    static SortedSet<Transaction> ReadInfo(String fileName)
    {
        SortedSet<Transaction> transactionSet = new TreeSet<Transaction>();
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
           if(debugDetail)
           {
            System.err.println(curTransaction+"\n");
           }
            transactionSet.add(curTransaction);
        }
        if(debug)
            System.out.println("COMPLETED READINFO");
        scanner.close();
        return transactionSet;
    }

    static Set<String> readSpecial(String fileName) throws FileNotFoundException
    {
        Set<String> output=new HashSet<String>();
        File file = new File(fileName);
        Scanner scanner;
        try {scanner= new Scanner(file);}
        catch (Exception e) { throw new FileNotFoundException("Special file not found");}

        while(true)
        {
            if(scanner.nextLine().equals("SPEND BUDGET"))
                break;
        }
        while(scanner.hasNextLine())
            output.add(scanner.nextLine());
        scanner.close();
        return output;        
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
                    output.add(new SimpleTransaction(moneyOrganizer.getTotalFromTag(string), string));
                    break;
                case 1:
                    output.add(new SimpleTransaction(moneyOrganizer.getTotalInLocation(string), string));
                    break;
                case 2:
                    output.add(new SimpleTransaction(moneyOrganizer.getTotalFromOrigin(string), string));
                    break;
                default:
                    throw new AssertionError();
            }
        }
        return output;
    }
}
