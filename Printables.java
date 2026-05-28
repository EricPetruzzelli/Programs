import java.util.ArrayList;
public class Printables 
{
    public static String printIntArray(int[] arg)
    {
        String str="";
        for(int i=0; i<arg.length;i++)
            str=str+arg[i]+" ";

        return str.substring(0, str.length()-1);
    }

    public static String printArrayList(ArrayList list)
    {
        String str="";
        for(Object data: list)
        {
            str=str+data.toString()+" ";
        }
        return str.substring(0, str.length()-1);
    }

    
    
}
