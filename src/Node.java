import sun.reflect.generics.tree.Tree;

import java.util.HashMap;

/**
 * Created by Akrylic on 3/4/2016.
 */
public class Node
{
    public Attribute attribute;
    public Item item;
    public boolean root; //True if parent, false if child
    public boolean decision; //Is this the final node in the tree?
    public double GainRatio; //Only parent nodes have these. When traversing, used to choose which parent node to go to next
    public HashMap<Item, Node> childList; //Attribute nodes have child lists
    public HashMap<Attribute,Node> parentList; //Item nodes have parent lists
    public String type; //Children are a bit different than roots because they aren't attributes of their own
                        //We still need to name them though (ie for Attribute size: s, m, l, xl)
    public Node (Attribute A) //create a parent
    {
        childList = new HashMap<>();
        root = true;
        attribute = A;
    }

    public Node (Item i) //create a child
    {
        double entropy;
        parentList = new HashMap<>();
        root = false;
        item = i;
    }

    public void childPut(Item i, Node n)
    {
        childList.put(i, n);
    }
    public void parentPut(Attribute a, Node n)
    {
        parentList.put(a, n);
    }


    public String toString()
    {
        if(root)
            return ("Root: "+attribute.name+" Children: "+childList);
        else
            return ("Leaf: "+type);
    }




}
