import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("Please input the output file name:");
        //BuildTree tree = new BuildTree("mushroom.training");
        //tree.build(tree.classifier, tree.getAttributes(),tree.items);
        //Database d = new Database("mushroom.training");
        //d.getAttributes();
        //d.getClassifier();
        //BuildTree b = new BuildTree("mushroom.training");
        Scanner sc = new Scanner(System.in);
        System.out.println("Please type your training set file name ie mushroom.training");
        String train = sc.next();
        System.out.println("Please type your test file name ie mushroom.test");
        String test = sc.next();
        System.out.println("Please type your output file name ie output.txt");
        String s = sc.next();
        NaiveBayes nb = new NaiveBayes(train, test, s);

    }

}
