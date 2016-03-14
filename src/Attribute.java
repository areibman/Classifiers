import java.util.*;

/**
 * Created by Akrylic on 3/5/2016.
 */
public class Attribute
{
    ArrayList<String> possibleValues= new ArrayList<>(); //An attribute can have a bunch of different values ie varying shapes.
                                                         // This lists the variants
    HashMap<String, Integer> classifierCount = new HashMap<>();
    HashMap<String, Integer> typeCount = new HashMap<>();
    double[] array = new double[2]; // slot 0 is e, slot 1 is p
    String name; //Since this can be basically used with any dataset,
                 //I'm not going to bother with naming each attribute. Instead, the index number will take the place of the name
    public Attribute(String name, ArrayList<String> possibleItems)
    {
        this.name= name;
        this.possibleValues =possibleItems;
        array[0] = 0;
        array[1] = 0;
    }
    public void add(String item) //Pointless now, addTypeCount does this and more
    {
        if (!possibleValues.contains(item))
        {
            possibleValues.add(item);
        }
    }
    public void addTypeCount(String classifier)
    {
        if(typeCount.get(classifier)!=null)
        {
            typeCount.put(classifier, 1);
        }
        else
        {
            typeCount.put(classifier, typeCount.get(classifier)+1);
        }
        if (typeCount.size()>2)
        {
            System.out.println("Whoops! Something went wrong, we're dealing with more than 2 classifiers! (Yes/No)");
            throw new NullPointerException();
        }
    }
    public void addClassifierCount(String classifier) {
        if (classifierCount.get(classifier)!=null)
        {
            classifierCount.put(classifier, 1);
        }
        else
        {
            classifierCount.put(classifier, classifierCount.get(classifier)+1);
        }
    }
    public void ratio() //Every attribute needs a ratio of e/p.
    // Main should iterate through every item in training and calculate the ratio for each attribute
    {
        int i = 0;
        for(Map.Entry<String, Integer> entry : classifierCount.entrySet())
        {
            System.out.println("Adding "+entry.getKey()+" to slot "+i+" in the array");
            array[i] = (double)entry.getValue();
            i++;
        }
    }
    public ArrayList<String> getPossibleValues()
    {
        return possibleValues;
    }

}
