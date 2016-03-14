import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Akrylic on 3/7/2016.
 */
public class PickAttribute
{
    Attribute attributeToPick;
    double gain;
    HashMap<String, ArrayList<Item>> set;

    public PickAttribute(Attribute a, ArrayList<Attribute> attributes, ArrayList<Item> items) //Given the classifier a, find the largest gain ratio from all attributes
    {
        gain = -999; //just making sure the gain ratio is in the right range...
        set = null;  //reset
        attributeToPick= null;
        for(Attribute each : attributes)
        {
            HashMap<String, ArrayList<Item>> tempSet;
            GainRatio gr = new GainRatio(a, each, items);
            double tempGain = gr.getGainRatio();
            tempSet = gr.set;
            if(gain<tempGain)
            {
                gain = tempGain;
                attributeToPick=each;
                set = tempSet;
            }
        }
    }
    public Attribute getChosenAttribute()
    {
        System.out.println("Gain ratio of attribute "+gain);
        System.out.println("attribute "+attributeToPick.name);
        return attributeToPick;
    }
}
