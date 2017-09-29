package Main;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Stephen Shirley on 9/27/2017.
 */
public class Main {
    //The list of words to encode and decode
    private static final String[] stringsToEncode = {"enqueue", "cabbabbac", "madam"};

    public static void main(String[] args) {
        for (String text : stringsToEncode) {

            ArrayList<Node> nodeList = buildNodeList(text);

            ArrayList<Node> sortedNodeList = SortNodesByFrequency(nodeList);

            Node nodeTree = createNodeTree(sortedNodeList);

            System.out.print("Word: " + text);

            printStringBits(text);

            BitSet encodedWord = createEncodedWord(text, nodeTree);

            printBitset(encodedWord);

            String decodedWord = decodeWord(nodeTree, encodedWord);

            System.out.print("\nDecoded Word: " + decodedWord + "\n\n");
        }
    }

    //Builds a list of nodes based on the frequency of the characters in the input word.
    public static ArrayList<Node> buildNodeList(String text) {
        ArrayList<Character> knownCharacters = new ArrayList<>();
        ArrayList<Node> nodeList = new ArrayList<>();

        for (char character : text.toCharArray()) {
            if (!knownCharacters.contains(character)) {
                knownCharacters.add(character);
                nodeList.add(new Node(1, character));
            } else {
                for (Node existingNode : nodeList) {
                    if (existingNode.huffChar == character) {
                        existingNode.frequency++;
                        break;
                    }
                }
            }
        }

        return nodeList;
    }

    //Takes in a BitSet and prints each bit to output
    public static void printBitset(BitSet input) {
        System.out.print("\nEncoded word: ");
        for (int i = 0; i < input.length() - 1; i++) {
            System.out.print(input.get(i) == true ? 1: 0);
        }
    }

    //Takes in a string and prints the bits of the string
    public static void printStringBits(String word) {
        System.out.print("\nWord as Bits: " + new BigInteger(word.getBytes()).toString(2));
    }

    //Traverses a huffman tree for each encoded character and returns the actual character.
    public static String decodeWord(Node nodeTree, BitSet encodedWord) {
        Node currentNode = nodeTree;
        StringBuilder decodedWord = new StringBuilder();
        for (int i = 0; i < encodedWord.length(); i++) {
            if (currentNode.leftChild == null && currentNode.rightChild == null) {
                decodedWord.append(currentNode.huffChar);
                currentNode = nodeTree;
            }
            if (encodedWord.get(i) == true) {
                if (currentNode.rightChild != null)
                    currentNode = currentNode.rightChild;
            }
            else {
                if (currentNode.leftChild != null)
                    currentNode = currentNode.leftChild;
            }
        }

        return decodedWord.toString();
    }

    //Takes in a Huffman Tree and the word it was made from, and encodes the word character by character.
    //Returns a bitset containing the entire encoded word.
    public static BitSet createEncodedWord(String word, Node nodeTree) {
        BitSet encodedWord = new BitSet();
        int currentBit = -1;
        for (char character: word.toCharArray()) {
            Stack<Node> stack = new Stack<Node>();
            Node nodePointer = nodeTree;
            while (!stack.isEmpty() || nodePointer != null) {
                if (nodePointer != null) {
                    if (nodePointer.huffChar == character) {
                        break;
                    }
                    currentBit++;
                    encodedWord.set(currentBit, false);
                    stack.push(nodePointer);
                    nodePointer = nodePointer.leftChild;
                }
                else {
                    Node prevNode = stack.pop();
                    nodePointer = prevNode.rightChild;
                    if (nodePointer != null && nodePointer.huffChar == character) {
                        encodedWord.set(currentBit);
                        break;
                    }
                    currentBit--;
                }
            }
        }
        encodedWord.set(currentBit + 1);

        return encodedWord;
    }


    //Takes in a sorted list of nodes and creates a huffman tree.
    public static Node createNodeTree(ArrayList<Node> sortedNodeList) {
        while (sortedNodeList.size() > 1) {
            //Remove the two lowest nodes
            Node lowestNode = sortedNodeList.remove(0);
            Node secondLowestNode = sortedNodeList.remove(0);

            int newFrequency = lowestNode.frequency + secondLowestNode.frequency;
            char symbol = '*';
            //Create a new node with the combined frequencies of the two lowest nodes.
            Node newNode = new Node(newFrequency, symbol, secondLowestNode, lowestNode);
            sortedNodeList.add(newNode);
            sortedNodeList = SortNodesByFrequency(sortedNodeList);
        }
        return sortedNodeList.get(0);
    }

    //Takes in a list of nodes and sorts them by frequency.
    public static ArrayList<Node> SortNodesByFrequency(ArrayList<Node> nodeList) {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.frequency - o2.frequency;
            }
        });

        return nodeList;
    }
}
