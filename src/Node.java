import sun.reflect.generics.tree.Tree;

import java.util.HashMap;

/**
 * Created by Akrylic on 3/4/2016.
 */
public class Node
{
    public Attribute attribute;
    public boolean root; //True if root, false if leaf
    public HashMap<String, Node> childList;
    public String type; //Children are a bit different than roots because they aren't attributes of their own
                        //We still need to name them though (ie for Attribute size: s, m, l, xl)
    public Node (Attribute A) //create a root
    {
        childList = new HashMap<>();
        root = true;
        attribute = A;
    }

    public Node (String type) //create a child
    {
        childList = new HashMap<>();
        this.type = type;
    }

    public void childPut(String A, Node n)
    {
        childList.put(A, n);
    }

    public String toString()
    {
        if(root)
            return ("Root: "+attribute.name+" Children: "+childList);
        else
            return ("Leaf: "+type);
    }




}
