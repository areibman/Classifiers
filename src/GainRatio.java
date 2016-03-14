import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akrylic on 3/4/2016.
 */
public class GainRatio
{
    Attribute attribute;
    double gain;
    double split;
    HashMap<String, ArrayList<Item>> set;
    public GainRatio(Attribute gainFind, Attribute attribute, ArrayList<Item> items) //gainFind is going to be 0 (e/p)
    {
        this.attribute = attribute;
        ArrayList<String> possibleAttributeValues =attribute.getPossibleValues();
        String name = attribute.name;
        set = new HashMap<>();
        for (String each: possibleAttributeValues)
        {
            set.put(each, new ArrayList<Item>());
        }
        for (Item i : items)
        {
            HashMap<String , String> itemsMap = i.getItemMap();
            String itemValue = itemsMap.get(name);
            if(!set.containsKey(itemValue))
            {
                throw new NullPointerException("Looks like something messed up with reading the data");
            }
            set.get(itemValue).add(i);
        }
        double lineSize = items.size();
        //System.out.println("Gain find attribute: "+attribute.name);
        //for(Item i : items)
        //{
        //    System.out.println(i.getItemMap());
        //}
        gain = entropy(gainFind, items);

        for (String each: set.keySet()) //this is the basic formula for gain: H(S) - Sum(Ratios)*Entropies
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

    private double entropy(Attribute gainFind, ArrayList<Item> items) //entropy used to calculate the gain ratio
    {
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
        //System.out.println(gainFindCount);

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
