/**
 * * Java Program to Implement Ternary Search Tree
 **/

import java.util.HashMap;
import java.util.Map;

/**
 * class TSTNode
 **/
class TSTNode {
    char data;
    boolean isEnd;
    TSTNode left, middle, right;

    /**
     * Constructor
     **/
    public TSTNode(char data) {
        this.data = data;
        this.isEnd = false;
        this.left = null;
        this.middle = null;
        this.right = null;
    }
}

/**
 * class TernarySearchTree
 **/
public class TernarySearchTree {
    private TSTNode root;
    private HashMap<String, Integer> stopSearchData;
//    private ArrayList<Integer> ids;

    /**
     * Constructor
     **/
    public TernarySearchTree(HashMap<Integer, Vertex> stops) {
        root = null;
        stopSearchData = new HashMap<>();
        for (Map.Entry<Integer, Vertex> set : stops.entrySet()) {
            String cleanName = cleanStopName(set.getValue().stop_name);
            this.insert(cleanName);
            stopSearchData.put(cleanStopName(set.getValue().stop_name), set.getValue().stop_id);
        }
    }

    public Vertex getStopData (HashMap<Integer, Vertex> stops, String stopName) {
        try {
            int idOfStop = stopSearchData.get(stopName);
            return stops.get(idOfStop);
        } catch (Exception e) {
            return null;
        }
    }

    public String cleanStopName (String name) {
        String newName = new String(name);
        // Prefixes
        newName = newName.replaceAll("^WB", "");
        newName = newName.replaceAll("^NB", "");
        newName = newName.replaceAll("^SB", "");
        newName = newName.replaceAll("^EB", "");
        // Postfixes
        newName = newName.replaceAll("WB$", "");
        newName = newName.replaceAll("NB$", "");
        newName = newName.replaceAll("SB$", "");
        newName = newName.replaceAll("EB$", "");
        newName = newName.trim();
        return newName;
    }

    /**
     * function to insert for a word
     **/
    public void insert(String word) {
        root = insert(root, word.toCharArray(), 0);
    }

    /**
     * function to insert for a word
     **/
    public TSTNode insert(TSTNode r, char[] word, int ptr) {
        if (r == null) r = new TSTNode(word[ptr]);
        if (word[ptr] < r.data) r.left = insert(r.left, word, ptr);
        else if (word[ptr] > r.data) r.right = insert(r.right, word, ptr);
        else {
            if (ptr + 1 < word.length) r.middle = insert(r.middle, word, ptr + 1);
            else r.isEnd = true;
        }
        return r;
    }

    /**
     * function to delete a word
     **/
    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }

    /**
     * function to delete a word
     **/
    private void delete(TSTNode r, char[] word, int ptr) {
        if (r == null) return;
        if (word[ptr] < r.data) delete(r.left, word, ptr);
        else if (word[ptr] > r.data) delete(r.right, word, ptr);
        else {            /** to delete a word just make isEnd false **/
            if (r.isEnd && ptr == word.length - 1) r.isEnd = false;
            else if (ptr + 1 < word.length) delete(r.middle, word, ptr + 1);
        }
    }

    /**
     * function to search for a word
     **/
    public String search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    /**
     * function to search for a word
     **/
    private String search(TSTNode r, char[] word, int ptr) {
        if (r == null)
            return "";
        if (word[ptr] < r.data)
            return search(r.left, word, ptr);
        else if (word[ptr] > r.data)
            return search(r.right, word, ptr);
        else {
            if (r.isEnd && ptr == word.length - 1) {
                return new String(word);
            } else if (ptr == word.length - 1) {
                char[] newWord = new char[word.length + 1];
                for (int i = 0; i < word.length; i++) {
                    newWord[i] = word[i];
                }
                newWord[word.length] = r.middle.data;
                return search(r.middle, newWord, ptr + 1);
            } else
                return search(r.middle, word, ptr + 1);
        }
    }
}