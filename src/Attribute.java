import java.util.*;

/**
 * Created by Akrylic on 3/5/2016.
 */
public class Attribute
{
    ArrayList<Item> possibleItems= new ArrayList<>(); //An attribute can have a bunch of different values ie varying shapes.
                                                        // This lists the variants
    public HashMap<String, Integer> classifierCount = new HashMap<>(); //need to classify the yes and no's
    String name; //Since this can be basically used with any dataset,
                 //I'm not going to bother with naming each attribute. Instead, the index number will take the place of the name
    public Attribute(String name, ArrayList<Item> possibleItems)
    {
        this.name= name;
        this.possibleItems =possibleItems;
    }
    public double entropy;
    public double gain;
    double gainRatio;
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
    public ArrayList<Item> getPossibleValues()
    {
        return possibleItems;
    }

}
