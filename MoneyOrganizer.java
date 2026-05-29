import java.util.HashSet;
import java.util.Set;
public class MoneyOrganizer 
{
    final private Set<Transaction> theSet;

    public MoneyOrganizer(Set<Transaction> transactionSet)
    {
        theSet=transactionSet;
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
    public Set<String> getTags()
    {
        Set<String> output = new HashSet<>();

        for(Transaction transaction: theSet)
        {
            for(String tag: transaction.tags)
            {
                output.add(tag);
            }
        }
        return output;
    }

}
