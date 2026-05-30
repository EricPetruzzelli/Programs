import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
public class MoneyOrganizer 
{
    final private Set<Transaction> theSet;

    public MoneyOrganizer(Set<Transaction> transactionSet)
    {
        theSet=transactionSet;
    }
//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=GENERAL+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=

    public double totalAmount()
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
    

}
