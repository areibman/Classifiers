import java.util.ArrayList;
import java.util.HashMap;
public class Item //Just like how parent nodes have Attributes, child nodes have items
    //Items are just Names with child nodes as attributes
{
    public String name; //name is going to be column number
    public double entropy;
    public String classifier;
    public HashMap<String, Integer> classifierCount = new HashMap<>(); //need to classify the yes and no's
    public Item(String name)
    {
        this.name = name;
    }
    public void putCount(String classifier)
    {
        if(classifierCount.get(classifier) == null || classifierCount.get(classifier) == 0)
        {
            classifierCount.put(classifier,1);
        }
        else
        {
            classifierCount.put(classifier,classifierCount.get(classifier)+1);
        }
    }
    public ArrayList<Attribute> children = new ArrayList<>();
}
