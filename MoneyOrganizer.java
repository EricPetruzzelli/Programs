//overarching class for budget and location

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

    
}
