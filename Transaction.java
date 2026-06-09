import java.util.ArrayList;
public class Transaction implements Comparable<Transaction>
{
    public int[] date= new int[3];
    public int[] OcDate= new int[3];
    public double cost;
    public String typeOrigin;
    public ArrayList<String> tags;
    public String location;
    public String description;
    public String budget;
    public Transaction(int[] OcDate,int[] date, double cost, String typeOrigin, ArrayList<String> tags, String location, String description)
    {
        this.OcDate=OcDate;
        this.date=date;
        this.cost=cost;
        this.tags=tags;
        this.typeOrigin=typeOrigin;
        this.location=location;
        this.description=description;
    }
    public Transaction(int[] OcDate,int[] date, double cost, String typeOrigin, ArrayList<String> tags, String location, String description, String budget)
    {
        this.OcDate=OcDate;
        this.date=date;
        this.cost=cost;
        this.tags=tags;
        this.typeOrigin=typeOrigin;
        this.location=location;
        this.description=description;
        this.budget=budget;
    }
    @Override
    public String toString()
    {
        String[] arg = tags.toArray(new String[0]);
        return printDate(OcDate) +"\n"+printDate(date) +"\n"+cost+"\n"+typeOrigin+"\n"+Printables.printArrayList(tags)+"\n"+location+"\n"+description+"\n"+budget;
    }


    private String printDate(int[] arg)
    {
        return arg[1] + " "+arg[2]+" "+ arg[0];
    }
    @Override
    public int compareTo(Transaction other) //year month day format
    {

        int index;
        if(OcDate[0]>other.OcDate[0])
        {
            index=0;
        }
        else if(OcDate[1]>other.OcDate[1])
        {
            index=1;
        }
        else
        {
            index=2;
        }
        return OcDate[index]-other.OcDate[index];
    }

    
}


