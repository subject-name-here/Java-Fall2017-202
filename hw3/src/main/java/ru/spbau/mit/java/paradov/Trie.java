package ru.spbau.mit.java.paradov;

import java.io.*;
import java.util.HashMap;

/**
 * Class realizes trie and implements Serializable interface,
 * which allows to put strings from InputStream into trie
 * and get all strings from it to OutputStream.
 */
public class Trie implements Serializable {
    /**
     * An element of trie. It contains information if word ends in here,
     * how many words starts with prefix, links to children nodes and
     * getters and setters for easier work with HashMap of children.
     */
    private class Node implements Serializable {
        /** Flag which says is there a word ending in this node. */
        boolean isTerminal = false;

        /** HashMap, which contains links to children nodes. */
        HashMap<Character, Node> children = new HashMap<>();

        /** Number of words starting with this prefix. */
        int startsWithPrefix = 0;

        Node getChild(Character c){
            return children.get(c);
        }

        boolean hasChild(Character c){
            return children.containsKey(c);
        }

        void createChild(Character c){
            children.put(c, new Node());
        }

        Node cutChild(Character c){
            return children.remove(c);
        }
    }

    /** Root of trie. In string language, root is equals to empty string. */
    private Node root = new Node();

    /**
     * Checks if stirng is in trie.
     * @param element string to find in trie
     * @return true, if element is in trie, false otherwise
     */
    public boolean contains(String element) {
        Node current = root;

        for (Character c : element.toCharArray()){
            if (!current.hasChild(c)) {
                return false;
            }
            current = current.getChild(c);
        }

        return current.isTerminal;
    }

    /**
     * Checks if string is in trie, and if it's not, adds it.
     * @param element string to add in trie
     * @return true, if string was not in trie, false otherwise
     */
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        root.startsWithPrefix++;
        Node current = root;

        for (Character c : element.toCharArray()){
            if (!current.hasChild(c)) {
                current.createChild(c);
            }
            current = current.getChild(c);
            current.startsWithPrefix++;
        }

        current.isTerminal = true;
        return true;
    }

    /**
     * Removes element from trie. If element wasn't in, returns false.
     * @param element string to be removed from trie.
     * @return false, if element eas not in trie
     */
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        root.startsWithPrefix--;
        Node current = root;

        for (Character c : element.toCharArray()){
            if (current.getChild(c).startsWithPrefix == 1) {
                current = current.cutChild(c);
            } else {
                current = current.getChild(c);
            }
            current.startsWithPrefix--;
        }

        current.isTerminal = false;
        return true;
    }

    /**
     * Returns how many words is in trie.
     * @return number of words in trie
     */
    public int size() {
        return root.startsWithPrefix;
    }

    /**
     * Returns how many words start with prefix.
     * @param prefix string to find how many words start with it
     * @return how many words start with prefix
     */
    public int howManyStartsWithPrefix(String prefix) {
        Node current = root;

        for (Character c : prefix.toCharArray()){
            if (!current.hasChild(c)) {
                return 0;
            }
            current = current.getChild(c);
        }

        return current.startsWithPrefix;
    }

    /**
     * Puts trie into OutputStream with help of ObjectOutputStream.
     * @param out stream where we write trie to
     * @throws IOException if something's wrong with creating ObjectOutputStream,
     * or writing in it, or flushing and closing it
     */
    public void serialize(OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(this);

        oos.flush();
        oos.close();
    }

    /**
     * Turns data from InputStream into new trie, replaces the old one.
     * @param in stream from which we get data.
     * @throws IOException if something's wrong with creating ObjectInputStream,
     * reading from it or closing it.
     */
    public void deserialize(InputStream in) throws IOException {
        try(ObjectInputStream ois = new ObjectInputStream(in)) {
            Trie newTrie = (Trie) ois.readObject();
            root = newTrie.root;
        } catch (ClassNotFoundException e) {
            System.out.println("Class wasn't found. Deserialization failed.");
        }
    }

}
