package Main;

/*
 Represents a node on a huffman tree.
 Can hold an actual character value or be a blank node with just frequency.
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
