public class Transaction implements Comparable<Transaction>
{
    public int[] date= new int[3];
    public double cost;
    public String typeOrigin;
    public String type;
    public String location;
    public String description;
    public Transaction(int[] date, double cost, String typeOrigin, String type, String location, String description)
    {
        this.date=date;
        this.cost=cost;
        this.type=type;
        this.typeOrigin=typeOrigin;
        this.location=location;
        this.description=description;
    }
    @Override
    public String toString()
    {
        return printDate() +"\n"+cost+"\n"+typeOrigin+"\n"+type+"\n"+location+"\n"+description;
    }
    private String printDate()
    {
        String str="";
        for(int i=0; i<date.length;i++)
            str=str+date[i]+" ";
        return str;
    }

    @Override
    public int compareTo(Transaction other)
    {
        int index=0;
        if(date[2]==other.date[2])
        {
            if(date[1]==other.date[1])
            {
                if(date[0]==other.date[0])
                {
                    return 0;
                }
                else
                    index=0;
            }
            else
                index=1;
        }
        else
        {
            index=2;
        }
        return date[index]-other.date[index];
    }
}
