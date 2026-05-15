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
        if(date[)
        return -1;
    }
}
