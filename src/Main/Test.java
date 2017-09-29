package Main;

import java.util.*;

/**
 * Created by Kingpin on 9/27/2017.
 */
public class Test {
    //The list of words to encode and decode
    private static final String[] stringsToEncode = {"enqueue", "cabbabbac", "madam"};

    public static void main(String[] args) {
        for (String text : stringsToEncode) {
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

            ArrayList<Node> sortedNodeList = SortNodesByFrequency(nodeList);

            Node nodeTree = createNodeTree(sortedNodeList);


            System.out.print("Word: " + text + "\n");
            BitSet encodedWord = createEncodedWord(text, nodeTree);
            System.out.print("BitSet: ");
            for (int i = 0; i < encodedWord.length() - 1; i++) {
                System.out.print(encodedWord.get(i) == true ? 1: 0);
            }
            System.out.print("\nDecoded Word: ");

            Node currentNode = nodeTree;
            for (int i = 0; i < encodedWord.length(); i++) {
                if (currentNode.leftChild == null && currentNode.rightChild == null) {
                    System.out.print(currentNode.huffChar);
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



            System.out.print("\n\n");



        }
    }

    public static BitSet createEncodedWord(String word, Node nodeTree) {
        String charCode = "";
        BitSet encodedWord = new BitSet();
        int currentBit = -1;
        for (char character: word.toCharArray()) {
            Stack<Node> stack = new Stack<Node>();
            Node nodePointer = nodeTree;
            while (!stack.isEmpty() || nodePointer != null) {
                if (nodePointer != null) {
                    charCode += "0";
                    if (nodePointer.huffChar == character) {
                        charCode = charCode.substring(0, charCode.length() - 1);
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
                    charCode = charCode.substring(0, charCode.length() - 1);
                    if (nodePointer != null && nodePointer.huffChar == character) {
                        charCode += "1";
                        encodedWord.set(currentBit);
                        break;
                    }
                    currentBit--;
                }
            }
        }
        encodedWord.set(currentBit + 1);

        System.out.print("Encoded chars: " + charCode + "\n");
        return encodedWord;
    }


    public static Node createNodeTree(ArrayList<Node> sortedNodeList) {
        while (sortedNodeList.size() > 1) {
            Node lowestNode = sortedNodeList.remove(0);
            Node secondLowestNode = sortedNodeList.remove(0);

            int newFrequency = lowestNode.frequency + secondLowestNode.frequency;
            char symbol = '*';
            Node newNode = new Node(newFrequency, symbol, secondLowestNode, lowestNode);
            sortedNodeList.add(newNode);
            sortedNodeList = SortNodesByFrequency(sortedNodeList);
        }
        return sortedNodeList.get(0);
    }

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
