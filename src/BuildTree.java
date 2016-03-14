import sun.security.jca.GetInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Akrylic on 3/6/2016.
 */
public class BuildTree //recursively build a tree
{
    Attribute classifier;
    ArrayList<Attribute> attributes = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();

    public BuildTree(String file)
    {
        Database d = new Database(file);
        classifier = d.getClassifier();
        attributes = d.getAttributes();
        items = d.getItems();
    }
    public ArrayList<Attribute> getAttributes()
    {
        attributes.remove(attributes.size()-1);
        return attributes;
    }
    public Node build(Attribute classifier, ArrayList<Attribute> attributes, ArrayList<Item> items) //Adapted from algorithm Han, p. 333
    {
        //System.out.println(classifier.getPossibleValues());
        double ent = entropy(classifier, items);
        //System.out.println("Entropy of branch node: "+ent+" and attribute: "+classifier.name);
        System.out.println("Number of attributes left: "+attributes.size());
        String leaf;
        if(ent == 0)
        {
            leaf = items.get(0).getItemMap().get(classifier.name);
            System.out.println("Leaf created: "+leaf);
            Node leafNode = new Node(leaf);
            return leafNode;
        }
        if (attributes.size()==0)
        {
            leaf= majority(classifier, items);
            Node leafNode = new Node(leaf);
            return leafNode;
        }
        //pick a root to start with, then remove it from the list of possible attributes
        PickAttribute pick = new PickAttribute(classifier, attributes, items);
        Attribute root = pick.getChosenAttribute();
        attributes.remove(root);
        System.out.println("Removed "+root.name+" from possible attributes");
        Node rootNode = new Node(root);

        HashMap<String, ArrayList<Item>> itemMap = pick.set;
        for (String s : itemMap.keySet()) //recursively build the tree
        {
            ArrayList<Item> itemSet = itemMap.get(s);
            if(itemSet.size()==0) //This is when there's a dead end, create a leaf
            {
                leaf = majority(classifier, items);
                Node leafNode = new Node(leaf);
                System.out.println("Created leaf: " + s);
                rootNode.childPut(s, leafNode);
            }
            else //Keep building instead
            {
                Node childNode = build(classifier, attributes, itemSet);
                rootNode.childPut(s, childNode);
            }
        }
        attributes.add(root);
        return rootNode;
    }

    private double entropy(Attribute gainFind, ArrayList<Item> items)
    {         //This is the same method from gain ratio, I'm just using it again as a quick way to find leaves of the tree
            //Since leaves will have entropies of 0 (they will be pure!)
        System.out.println(gainFind.possibleValues);
        ArrayList<String> possibleGainFindValues = gainFind.getPossibleValues();
        String gainFindName = gainFind.name;
        HashMap<String ,Integer> gainFindCount = new HashMap<>(); //To calculate entropy, you need the counts of the classifier
        for(String s : possibleGainFindValues)
        {
            gainFindCount.put(s, 0); //Just create a hashmap of each of the possible classifiers (in this case e/p)
        }
        for(Item i : items)
        {
            HashMap<String, String> itemsMap = i.getItemMap();
            String itemValue = itemsMap.get(gainFindName);
            if(!gainFindCount.containsKey(itemValue))
            {
                throw new NullPointerException("Something went wrong with reading the data...");
            }
            gainFindCount.put(itemValue, gainFindCount.get(itemValue) + 1);
        }
        int itemSize = items.size();
        double entropy = 0;
        for(String s: possibleGainFindValues)
        {
            double count = (double) gainFindCount.get(s);
            if(count!=0)
            {
                if (count == itemSize)
                    return 0; //Pure subset
                double ratio = count / itemSize;
                double logFunc = -ratio * ((Math.log(ratio) / Math.log(2))); //Info(D) = -Sum(Ratio)*Log_2(Ratio)
                entropy += logFunc;
            }
        }
        //System.out.println("Entropy is: "+entropy);
        return entropy;
    }

    private String majority(Attribute classifier, ArrayList<Item> items)
    {
        ArrayList<String> classifierPossibleValues = classifier.getPossibleValues();
        String classifierName = classifier.name;
        HashMap<String, Integer> classifierCounter  = new HashMap<>();
        for(String each: classifierPossibleValues)
        {
            classifierCounter.put(each, 0); //create a map with all of the possible values
        }
        for (Item i : items)
        {
            HashMap<String,String> itemMap= i.getItemMap();
            String classifierItem = itemMap.get(classifierName);
            if(!classifierCounter.containsKey(classifierItem))
            {
                throw new NullPointerException("Looks like something get wrong in finding the majority class... ");
            }
            classifierCounter.put(classifierItem, classifierCounter.get(classifierItem) + 1); //count how many classifier items there are and +1 if there's a duplicate
        }
        String majorityClass="";
        int max = 0;
        for(String each : classifierCounter.keySet())
        {
            int temp = classifierCounter.get(each);
            if(max<temp)
            {
                max = temp;
                majorityClass = each;
            }
        }
        return majorityClass;

    }
}
