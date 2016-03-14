import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("Building tree:");
        BuildTree tree = new BuildTree("mushroom.training");
        tree.build(tree.classifier, tree.getAttributes(),tree.items);

    }

}
