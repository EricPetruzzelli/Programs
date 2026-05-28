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

    
}
