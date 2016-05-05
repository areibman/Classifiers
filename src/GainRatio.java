import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akrylic on 3/4/2016.
 */
public class GainRatio
{
   /** Attribute attribute;
    Attribute classifier;
    double gain;
    double split;
    HashMap<String, ArrayList<Item>> set;
    ArrayList<List<String>> transactionSubsets;
    public GainRatio(Attribute classifier, Attribute attribute, ArrayList<List<String>> transactionSubsets) //gainFind is going to be 0 (e/p)
    {
        this.classifier = classifier;
        this.transactionSubsets = transactionSubsets;
        this.attribute = attribute;
        String name = attribute.name;

        double attributeEntropy = entropy(transactionSubsets);

        for (Item i: attribute.getPossibleValues())
        {

        }

       /** for (String each: set.keySet()) //this is the basic formula for gain: H(S) - Sum(Ratios)*Entropies
        {
            ArrayList<Item> subset = set.get(each);
            double subsetSize = subset.size();
            double subsetEntropy = (subsetSize/lineSize)*entropy(gainFind, subset);
            gain -= subsetEntropy;
        }

        //for the final step we need to divide gain by splitEntropy
        split =0;
        for (String s: set.keySet()) //this is the basic formula for gain: H(S) - Sum(Ratios)*Entropies
        {
            ArrayList<Item> subset2 = set.get(s);
            double subsetSize2 = subset2.size();
            double subsetEntropy2 = (subsetSize2/lineSize)*(Math.log(subsetSize2/lineSize)/Math.log(2));
            split -= subsetEntropy2;
        }

    }
    public double getGainRatio()
    {
        return gain/split;
    }

    private double entropy(ArrayList<List<String>> lists) //entropy for an attribute or item
    {
        HashMap<String, Double > classifierCounter = new HashMap<>(); //Count the occurences of each item in the classifier
        for(Item i : classifier.getPossibleValues()){
                classifierCounter.put(i.name, 0.0); //there should only be two for mushroom
        }
        for(List<String> l : lists)
        {
            if(!classifierCounter.containsKey(l.get(0)))
                throw new NullPointerException("More classifiers than expected... tried to add "+l.get(0));

            classifierCounter.put(l.get(0), classifierCounter.get(l.get(0))+1.0);
        }
        if(classifierCounter.size()>2)
            throw new NullPointerException("Too many classifiers..." + classifierCounter.size());
        double entropy = 0;
        double total = 0;
        for(Map.Entry<String, Double> me :classifierCounter.entrySet())
        {
            total += me.getValue();
        }
        for(Map.Entry<String, Double> me :classifierCounter.entrySet())
        {
            entropy-=(me.getValue()/total)*(Math.log(me.getValue()/total)/Math.log(2));
        }
        return entropy;
    }
  /**  private double itemEntropy(ArrayList<List<String>> lists, Item item) //entropy for an attribute or item
    {
        HashMap<String, Double > classifierCounter = new HashMap<>(); //Count the occurences of each item in the classifier
        for(Item i : classifier.getPossibleValues()){
            classifierCounter.put(i.name, 0.0); //there should only be two for mushroom
        }
        for(List<String> l : lists)
        {
            if(!classifierCounter.containsKey(l.get(0)))
                throw new NullPointerException("More classifiers than expected... tried to add "+l.get(0));
            //if (l.contains(item.))
            classifierCounter.put(l.get(0), classifierCounter.get(l.get(0))+1.0);
        }
        if(classifierCounter.size()>2)
            throw new NullPointerException("Too many classifiers..." + classifierCounter.size());
        double entropy = 0;
        double total = 0;
        for(Map.Entry<String, Double> me :classifierCounter.entrySet())
        {
            total += me.getValue();
        }
        for(Map.Entry<String, Double> me :classifierCounter.entrySet())
        {
            entropy-=(me.getValue()/total)*(Math.log(me.getValue()/total)/Math.log(2));
        }
        return entropy;
    } **/
 /**   public double gain() //Retired code
    {
        ArrayList<Double> sumList = new ArrayList<>(); //When calculating the gainratio, you need the sums of the children entropies*the proportions
        //find the entropies of the children
        for(Map.Entry<Attribute, Node> entry: node.childList.entrySet())
        {
            entry.getValue().attribute.ratio();
            double childSize= entry.getValue().attribute.array[0] + entry.getValue().attribute.array[1];
            double childEntropy = -((entry.getValue().attribute.array[0]/childSize))*(Math.log(entry.getValue().attribute.array[0]/childSize)/Math.log(2)) -((entry.getValue().attribute.array[1]/childSize)*(Math.log(entry.getValue().attribute.array[1]/childSize)/Math.log(2)));
            sumList.add((childSize/size)*childEntropy);
        }
        double sum=0;
        for (int i = 0; i<sumList.size(); i++)
        {
            sum+=sumList.get(i);//technically should be -= but we treat it as a variable and subtract it from entropy so it's ok
        }
        double gain = entropy()-sum;
        return gain;

    }**/
   /** public double splitInfo() //Retired code
    {
        ArrayList<Double> sumList = new ArrayList<>();
        //find the entropies of the children
        for(Map.Entry<Attribute, Node> entry: node.childList.entrySet())
        {
            entry.getValue().attribute.ratio(); //this just makes sure attribute computes the ratio... sort of like a mini constructor
            double childSize= entry.getValue().attribute.array[0] + entry.getValue().attribute.array[1];
            double logOfChild = (Math.log(childSize/size))/(Math.log(2));
            sumList.add((childSize/size)*logOfChild);
        }
        double sum=0;
        for (int i = 0; i<sumList.size(); i++)
        {
            sum-=sumList.get(i); //splitInfo is a negative
        }
        double split = entropy()-sum;
        return split;
    }**/

}
