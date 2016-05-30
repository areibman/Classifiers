import org.omg.Messaging.SYNC_WITH_TRANSPORT;
//Naive bayes
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NaiveBayes {
    ArrayList<Attribute> attributes = new ArrayList<>();
    Database d;
    String classLabel;
    String classLabelB;
    public NaiveBayes(String file, String test, String outputname)
    {
        //ArrayList<List<String>>
        d = new Database(file);
        //Key: e    Value: count
        HashMap<String,Double> ProbA = new HashMap<>();
        //Key: p    Value: count
        HashMap<String,Double> ProbB = new HashMap<>();
        //Index: attribute  Key: category   Value: categoryCount / classCount
        ArrayList<HashMap<String,Double>> conditionalProbs = new ArrayList<>();
        ArrayList<HashMap<String,Double>> conditionalProbsB = new ArrayList<>();

        //get two class labels ??? NANI
        classLabel= d.allTransactions.get(0).get(0);
        for(List<String> record : d.allTransactions)
        {
            if(!record.get(0).equals(classLabel))
            {
                classLabelB=record.get(0);
            }
        }

        //alphabetize lexicographically
        if (classLabel.compareTo(classLabelB)>0)
        {
            System.out.println("switched");
            String temp = classLabel;
            classLabel=classLabelB;
            classLabelB=temp;
        }
        System.out.println("classLabels are "+classLabel+classLabelB);


        double total = 0;
        //count the classLabels in the training set
        HashMap<String , Double> counts = new HashMap<String,Double>();
        for(List<String> s : d.allTransactions)
        {
            if(counts.containsKey(s.get(0)))
            {
                counts.put(s.get(0), counts.get(s.get(0)) + 1.0);
            }
            if(!counts.containsKey(s.get(0)))
            {
                counts.put(s.get(0), 1.0);
            }
        }
        System.out.println(counts);

        //count total number of entries
        for(Map.Entry<String, Double> me : counts.entrySet())
            total += me.getValue();

        //assign probs P(a) P(b)
        for(Map.Entry<String, Double> me : counts.entrySet())
        {
            if (me.getKey().equals(classLabel))
                ProbA.put(classLabel, me.getValue());
            if (me.getKey().equals(classLabelB))
                ProbB.put(classLabelB,me.getValue());
        }


        System.out.println("ProbA :" + ProbA.entrySet());
        //e count, p count
        double a = ProbA.get(classLabel);
        double b = ProbB.get(classLabelB);
        System.out.println("ProbB: "+ProbB.entrySet());

        //Initialize conditional prob table
        for(String c: d.allTransactions.get(0))
        {
            conditionalProbs.add(new HashMap<String, Double>());
            conditionalProbsB.add(new HashMap<String, Double>());
        }

        int index = 0;
        //for each record
        for(List<String> list : d.allTransactions)
        {
            //for each attribute value
            for(String s: list)
            {
                //if classLabel = e
                if(list.get(0).equals(classLabel))
                {
                    //raw category count
                    if (conditionalProbs.get(index).containsKey(s))
                        conditionalProbs.get(index).put(s, conditionalProbs.get(index).get(s) + 1.0);
                    else
                        conditionalProbs.get(index).put(s, 1.0);
                }
                index++;
            }
            index=0;
        }

        //Counts
        for(HashMap<String, Double> attribute: conditionalProbs)
        {
            System.out.println(attribute);
        }

        //for each attribute
        for(HashMap<String, Double> h : conditionalProbs)
        {       //for each category, set value to category count / class count
                for (Map.Entry<String, Double> me : h.entrySet()) {
                    me.setValue(me.getValue() / a);
                }
                System.out.println("Conditional probs " + h);
        }

        index = 0;
        for(List<String> list : d.allTransactions)
        {
            for(String s: list)
            {
                if(list.get(0).equals(classLabelB))
                {
                    if (conditionalProbsB.get(index).containsKey(s))
                        conditionalProbsB.get(index).put(s, conditionalProbsB.get(index).get(s) + 1.0);
                    else
                        conditionalProbsB.get(index).put(s, 1.0);
                }
                index++;
            }
            index=0;
        }

        //counts
        System.out.println(" Counts B@@@@@@@@@@@@@@@@@@@@@2");
        for(HashMap<String, Double> attribute: conditionalProbsB)
        {
            System.out.println(attribute);
        }

        for(HashMap<String, Double> h : conditionalProbsB)
        {
            for (Map.Entry<String, Double> me : h.entrySet()) {
                me.setValue(me.getValue() / b);
            }
            System.out.println("conditionalprobsB: " + h);
        }
        System.out.println("Total: "+total);





        //Prob calculation

        Database testFile = new Database(test);

        //Index: Record Value: probability of e
        ArrayList<Double> result = new ArrayList<>();
        //A
        //for each record
        for(List<String> list : testFile.allTransactions)
        {
            double Bayes = 1;
            index = 0;
            //for each attribute
            for (HashMap<String, Double> hm : conditionalProbs)
            {
                    //for each category
                    for (Map.Entry<String, Double> me : hm.entrySet()) {

                        if (me.getKey().equals(list.get(index))) {
                            Bayes *= me.getValue();
                        }
                    }
                index++;
            }
            Bayes*=(a/total);
            result.add(Bayes);
        }
        System.out.println("B probabilities");
        //B
        ArrayList<Double> resultsB = new ArrayList<>();
        for(List<String> list : testFile.allTransactions)
        {
            double Bayes=1;
            index =0;
            for (HashMap<String, Double> hm : conditionalProbsB)
            {
                    for (Map.Entry<String, Double> me : hm.entrySet()) {
                        if (me.getKey().equals(list.get(index))) {
                            Bayes *= me.getValue();
                        }
                    }
                index++;

            }
            Bayes*=(b/total);
            resultsB.add(Bayes);
        }

        //print answer
        ArrayList<String> answer = new ArrayList();
        index=0;
        for(Double dub: result)
        {
            //System.out.println(dub+ " "+resultsB.get(index));
            if(dub>resultsB.get(index))
            {
                answer.add(classLabel);
            }
            else
            {
                answer.add(classLabelB);
            }
            index++;
        }

        System.out.println(answer.size()+" transaction set size "+testFile.allTransactions.size());
        double right = 0;
        index = 0;
        for(List<String> list: testFile.allTransactions)
        {
            System.out.println(testFile.allTransactions.get(index).get(0)+ " "+answer.get(index));
            if(testFile.allTransactions.get(index).get(0).equals(answer.get(index)))
            {
                right++;
            }
            index++;
        }
        double output = right/(double)result.size();
        System.out.println(output);

        try
        {
            PrintWriter FileOut = new PrintWriter(outputname);
            index=0;
            FileOut.println("Accuracy: " + output);
            index = 0;
            for(List<String> list: testFile.allTransactions)
            {
                FileOut.println("Actual value "+testFile.allTransactions.get(index).get(0)+ " Chosen Value: "+answer.get(index));
                index++;
            }
            //FileOut.println(f.pruneMap);
            //FileOut.println(f.pruneMaps);
            FileOut.close();
        }
        catch(Exception e)
        {
            System.out.println("file not found");
        }


    }


}
