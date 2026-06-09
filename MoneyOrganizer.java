import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
public class MoneyOrganizer 
{
    final int YEARSTART=26;
    final int YEAREND=27;
    TransactionCalander budgetCal;
    TransactionCalander locationCal;
    final private Set<Transaction> theSet;
    final private Set<String> budgetExeptions;

    public MoneyOrganizer(Set<Transaction> transactionSet, Set<String> budgetExeptions)
    {
        theSet=transactionSet;
        this.budgetExeptions=budgetExeptions;
        budgetCal=new TransactionCalander(true);
        locationCal=new TransactionCalander(false);

        for(Transaction transaction: theSet)
        {
            budgetCal.addTransaction(transaction);
            locationCal.addTransaction(transaction);
        }
    }
//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+BUDGET+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=

//can either designate budget in file or hardcode it. will probably end up hardcoding it to reduce typing time and redundency.
//OR I COULD DO BOTH... when it reads a specile file, create and add a new budget transaction. this will take up more memory though
//i think imma just end up hard coding it (may change later but doubt it)
//ProfessionalExpense Budget
//Necessity Budget
//Spending Budget
//Saving Budget
//
public Set<String> getBudgetExceptions()
{
    return budgetExeptions;
}
public Double getSpendBudgetTotal()
{
    Double output=0.0;

    for(Transaction transaction: theSet)
    {
        if(budgetExeptions.contains(transaction.tags.get(0)))
        {
            output+=transaction.cost;
        }
    }
    return output;
}

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=GENERAL+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=

    public double getTotal()
    {
        double output=0;
        for(Transaction transaction: theSet)
        {
            if(!transaction.tags.get(0).equals("Transfer"))
            {
                output+=transaction.cost;
            }
        }
        return output;
    }
    public String stringOfTransactions()
    {
        String output="";
        for(Transaction transaction: theSet)
        {
            output=output+transaction+"\n\n";
        }
        return output.substring(0,output.length()-2);
    }
    private String stringOfTransactions(Set<Transaction> transactions)
    {
        String output="";
        for(Transaction transaction: transactions)
        {
            output=output+transaction+"\n\n";
        }
        return output.substring(0,output.length()-2);
    }

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=TAGS+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    public Set<String> getMainTags()
    {
        Set<String> output = new TreeSet<>();
        for(Transaction transaction: theSet)
        {
            output.add(transaction.tags.get(0));
        }
        return output;

    }


    public Set<String> getTags()
    {
        Set<String> output = new TreeSet<>();

        for(Transaction transaction: theSet)
        {
            for(String tag: transaction.tags)
            {
                output.add(tag);
            }
        }
        return output;
    }
    public String stringOfTransactionsFromTag(String tag)
    {
        return stringOfTransactions(getTransactionsFromTag(tag));
    }
    public double getTotalFromTag(String tag)
    {
        double output=0;
        for(Transaction transaction: theSet)
        {
            if(transaction.tags.contains(tag))
                output+= transaction.cost;
        }
        return output;
    }
    
    private Set<Transaction> getTransactionsFromTag(String tag)
    {
        Set<Transaction> output =new HashSet<>();
        for(Transaction transaction: theSet)
        {
            if(transaction.tags.contains(tag))
                output.add(transaction);
        }
        return output;
    }
    
    

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=LOCATIONS+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    public Set<String> getLocations()
    {
        Set<String> output = new TreeSet<>();

        for(Transaction transaction: theSet)
        {
            output.add(transaction.location);
        }
        return output;
    }

    public double getTotalInLocation(String location)
    {
        double output=0;
        for(Transaction transaction: theSet)
        {
            if(transaction.location.equals(location))
                output+=transaction.cost;
        }
        return output;
    }
//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=ORIGINS+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    public Set<String> getOrigins()
    {
        Set<String> output = new TreeSet<>();
        for(Transaction transaction: theSet)
        {
            output.add(transaction.typeOrigin);
        }
        return output;
    }
    public double getTotalFromOrigin(String origin)
    {
        double output=0;
        for(Transaction transaction: theSet)
        {
            if(transaction.typeOrigin.equals(origin))
                output+=transaction.cost;
        }
        return output;
    }

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=CALANDER+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=


     private class TransactionCalander
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
        public void addTransaction(Transaction transaction)
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


