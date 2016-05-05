import sun.security.jca.GetInstance;

import java.lang.reflect.Array;
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
    Attribute classifier;  //Classifier must be known beforehand. Assume it's column 0, include that in readme
    ArrayList<Attribute> attributes = new ArrayList<>();
    Database d;
    public BuildTree(String file)
    {
        d = new Database(file);
        classifier = d.getClassifier();
        attributes = d.getAttributes();
        build(attributes, d.allTransactions);
    }
    public ArrayList<Attribute> getAttributes()
    {
        attributes.remove(attributes.size() - 1);
        return attributes;
    }
    public Attribute build(ArrayList<Attribute> attributes, ArrayList<List<String>> allTransactions) //Adapted from algorithm Han, p. 333
    {

        for(Attribute a : attributes) //Determine the classifier counts for each attribute as well as their items
        {
            for(List<String> s : allTransactions)
            {
                a.putCount(s.get(0));
                for(Item i : a.possibleItems)
                {
                    if(s.get(Integer.parseInt(a.name)).equals(i.name))
                    {
                        i.putCount(s.get(0));
                    }
                }
            }
            //Assign entropies to the attributes and items
            a.entropy= entropy(a);
            //System.out.println(a.name+" Entropy "+a.entropy);
            for(Item i : a.possibleItems)
            {
                i.entropy=entropy(i);
                //System.out.println(a.name+ " Item: "+i.name+" Entropy: "+i.entropy);
            }
        }

        //Information gain set for each attribute and Split info and Gain Ratio
        for(Attribute a: attributes)
        {
            double gain = a.entropy;
            double size = 0;
            for(Map.Entry<String, Integer> me : a.classifierCount.entrySet())
            {
                size += me.getValue();
            }
            for (Item i: a.possibleItems)
            {
                double itemSize = 0;
                for(Map.Entry<String, Integer> me : i.classifierCount.entrySet())
                {
                    itemSize += me.getValue();
                    System.out.println("Item: "+i.name+ " classifier: "+me.getKey()+" Value "+me.getValue());
                }
                double ratio = itemSize/size;
                gain+= -ratio*i.entropy;
            }
            a.gain = gain;
            System.out.println(a.name+" Gain: "+a.gain);


            //Split info
            double splitInfo=0;
            for (Item i: a.possibleItems)
            {
                double itemSize = 0;
                for(Map.Entry<String, Integer> me : i.classifierCount.entrySet())
                {
                    itemSize += me.getValue();
                }
                double ratio = itemSize/size;
                System.out.println(i.name+ " itemSize "+itemSize+ " Ratio: "+ratio);
                if(ratio!=0.0) {
                    splitInfo += -ratio * (Math.log(ratio) / Math.log(2));
                }
            }
            //Gain Ratio
            System.out.println(gain+" splitInfo "+splitInfo+ " ");
            a.gainRatio=gain/splitInfo;
            if(splitInfo==0)
            {
                a.gainRatio=0;
            }
        }

        //Pick an Attribute
        double highestGR=0;
        String nameOfHighest;
        int index = 0;
        int indexOfHighest =-1;
        for(Attribute a : attributes)
        {
            System.out.println("Attribute: "+a.name+" gain ratio: "+a.gainRatio);
            if(a.gainRatio>highestGR)
            {
                highestGR = a.gainRatio;
                nameOfHighest = a.name;
                indexOfHighest = index;
            }
            index++;
        }
        ArrayList<Attribute> newAttributes = new ArrayList<>();
        for(Attribute a: attributes) //clear newAttributes of oldAttribute's data
        {
            ArrayList<Item> newPossibleItems = new ArrayList<>();
            for(Item i: a.possibleItems)
            {
                newPossibleItems.add(new Item(i.name));
            }
            newAttributes.add(new Attribute(a.name, newPossibleItems));
        }
        for(Attribute a: newAttributes)
        {
            for(Item i: a.possibleItems)
                i.classifierCount.clear();
        }
        if(newAttributes.size()==1) //end recursive loop
        {
            if(allTransactions.size()!=0)
            {
                System.out.println("Bottom of Tree, Classified as: " + allTransactions.get(0).get(0));
                ArrayList<Item> returnTemp = new ArrayList<>();
                returnTemp.add(new Item(allTransactions.get(0).get(0)));
                return new Attribute("0", returnTemp);
            }
        }
        else if (allTransactions.size()!=0)
        {
            System.out.println("Attribute: " + attributes.get(indexOfHighest).name + " Gain Ratio: " + highestGR);

            //Recursion step
            ArrayList<Integer> count = new ArrayList<>();

            for(Map.Entry<String,Integer> me: attributes.get(indexOfHighest).classifierCount.entrySet())
            {
                count.add(me.getValue());
            }
            System.out.println(attributes.get(indexOfHighest).name+" "+count.get(0)+ " @@@@@@ "+count.get(1));
            if(! (count.get(0)==0 || count.get(1) == 0))
            {
                newAttributes.remove(indexOfHighest);
                for (Item i : attributes.get(indexOfHighest).possibleItems)
                {
                    ArrayList<List<String>> newTransactions = new ArrayList<>(allTransactions.size());
                    for(List<String> s : allTransactions)
                    {
                        List<String> f = new ArrayList<>();
                        for (String a : s)
                        {
                            String x = a;
                            f.add(x);
                        }
                        newTransactions.add(f);
                    }

                    for (int x = 0; x < newTransactions.size(); x++) //remove processed lines from database
                    {
                        if (newTransactions.get(x).get(indexOfHighest).equals(i.name))
                        {
                            System.out.println("Removed "+newTransactions.get(x).get(indexOfHighest));
                            newTransactions.remove(x);
                            x = 0;
                        }
                    }
                    System.out.println("Building new tree");
                    i.children.add(build(newAttributes, newTransactions));
                }
            }
            else
            {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@PURE SET FOUND@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println("Classified as: " + allTransactions.get(0).get(0));
                ArrayList<Item> returnTemp = new ArrayList<>();
                returnTemp.add(new Item(allTransactions.get(0).get(0)));
                return new Attribute("0", returnTemp);
            }
        }
        return attributes.get(indexOfHighest);

       /** if(indexOfHighest!=-1)
        {
            newAttributes.remove(indexOfHighest);
            for(Item i: attributes.get(indexOfHighest).possibleItems)
            {

            }
            allTransactions.remove()
            build(newAttributes, allTransactions);
        }**/

        /**
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
        return rootNode;**/

    }


    private double entropy(Attribute gainFind)
    {         //This is the same method from gain ratio, I'm just using it again as a quick way to find leaves of the tree
            //Since leaves will have entropies of 0 (they will be pure!)

        double entropy = 0;
        double size = 0;
        for(Map.Entry<String, Integer> me : gainFind.classifierCount.entrySet())
        {
            size += me.getValue();
        }
        for(Map.Entry<String, Integer> me : gainFind.classifierCount.entrySet())
        {
            double ratio = me.getValue()/size;
            entropy += - (ratio)*(Math.log(ratio) / Math.log(2));
        }
        return entropy;
        /**System.out.println(gainFind.possibleValues);
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
         **/
    }
    private double entropy(Item item)
    {
        //This is the same method from gain ratio, I'm just using it again as a quick way to find leaves of the tree
        //Since leaves will have entropies of 0 (they will be pure!)

        double entropy = 0;
        double size = 0;
        for (Map.Entry<String, Integer> me : item.classifierCount.entrySet()) {
            size += me.getValue();
        }
        //System.out.println("Item name: "+item.name+ " Size: "+size);
        for (Map.Entry<String, Integer> me : item.classifierCount.entrySet()) {
            double ratio = me.getValue() / size;
            entropy += - (ratio) * (Math.log(ratio) / Math.log(2));
        }
        return entropy;
    }

   /** private String majority(Attribute classifier, ArrayList<Item> items)
    {
        //ArrayList<String> classifierPossibleValues = classifier.getPossibleValues();
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

    }**/
}
