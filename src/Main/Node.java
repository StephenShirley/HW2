package Main;

/**
 * Created by Kingpin on 9/27/2017.
 */
public class Node {
    public int frequency;
    public char huffChar;
    public Node rightChild;
    public Node leftChild;

    public Node(int frequency, char huffChar) {
        this.frequency = frequency;
        this.huffChar = huffChar;
    }
    public Node(int frequency, char huffChar, Node leftChild, Node rightChild) {
        this.frequency = frequency;
        this.huffChar = huffChar;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
