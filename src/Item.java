import java.util.HashMap;

/**
 * Created by Akrylic on 3/7/2016.
 */
public class Item //Just like how parent nodes have Attributes, child nodes have items
{
    public static int count = 0; //The number of items in all maps
    public int index;
    public HashMap<String,String> itemMap;
    public Item()
    {
        index = count;
        itemMap = new HashMap<String, String>();
        count++;
    }

    public void addType(String item, String value) //Add a type
    {
        itemMap.put(item, value);
    }
    public HashMap<String ,String > getItemMap()
    {
        return itemMap;
    }
}
