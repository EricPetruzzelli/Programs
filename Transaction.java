public class Transaction implements Comparable<Transaction>
{
    public int[] date= new int[3];
    public int[] OcDate= new int[3];
    public double cost;
    public String typeOrigin;
    public String type;
    public String location;
    public String description;
    public Transaction(int[] OcDate,int[] date, double cost, String typeOrigin, String type, String location, String description)
    {
        this.OcDate=OcDate;
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
        return printDate(OcDate) +"\n"+printDate(date) +"\n"+cost+"\n"+typeOrigin+"\n"+type+"\n"+location+"\n"+description;
    }
    private String printDate(int[] arg)
    {
        String str="";
        for(int i=0; i<arg.length;i++)
            str=str+arg[i]+" ";
        return str;
    }

    @Override
    public int compareTo(Transaction other)
    {
        int index=0;
        if(OcDate[2]==other.OcDate[2])
        {
            if(OcDate[1]==other.OcDate[1])
            {
                if(OcDate[0]==other.OcDate[0])
                {
                    return -1;
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
        return OcDate[index]-other.OcDate[index];
    }
}
