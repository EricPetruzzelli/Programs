//overarching class for budget and location

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

abstract class MoneyOrganizer 
{
    Map<String, SortedSet<Transaction>> MainTagMap=new HashMap<>();
    Map<String, SortedSet<Transaction>> TagMap=new HashMap<>();
    Map<String, SortedSet<Transaction>> LocationMap=new HashMap<>();
    Map<String, SortedSet<Transaction>> OriginMap=new HashMap<>();
   


    public void setMaps(Map<String, SortedSet<Transaction>> MainTagMap, Map<String, SortedSet<Transaction>> TagMap, Map<String, SortedSet<Transaction>> LocationMap, Map<String, SortedSet<Transaction>> OriginMap)
    {
        this.MainTagMap=MainTagMap;
        this.TagMap=TagMap;
        this.LocationMap=LocationMap;
        this.OriginMap=OriginMap;
    }

    
}
