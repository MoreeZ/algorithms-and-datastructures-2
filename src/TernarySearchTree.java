/**
 * * Java Program to Implement Ternary Search Tree
 **/

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

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
    private ArrayList<String> al;
    private ArrayList<Integer> ids;

    /**
     * Constructor
     **/
    public TernarySearchTree(HashMap<Integer, Vertex> stops) {
        root = null;
        for (Map.Entry<Integer, Vertex> set : stops.entrySet()) {
            this.insert(cleanStopName(set.getValue().stop_name));
//            ids.add(set.getKey());
        }

    }

    private String cleanStopName (String name) {
        String newName = name;
        // Prefixes
        newName.replaceAll("^WB", "");
        newName.replaceAll("^NB", "");
        newName.replaceAll("^SB", "");
        newName.replaceAll("^EB", "");
        // Postfixes
        newName.replaceAll("WB$", "");
        newName.replaceAll("NB$", "");
        newName.replaceAll("SB$", "");
        newName.replaceAll("EB$", "");
        newName.trim();
        return newName;
    }

    /**
     * function to check if empty
     **/
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * function to clear
     **/
    public void makeEmpty() {
        root = null;
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

    /**
     * function to print tree
     **/
    public String toString() {
        al = new ArrayList<String>();
        traverse(root, "");
        return "\nTernary Search Tree : " + al;
    }

    /**
     * function to traverse tree
     **/
    private void traverse(TSTNode r, String str) {
        if (r != null) {
            traverse(r.left, str);
            str = str + r.data;
            if (r.isEnd) al.add(str);
            traverse(r.middle, str);
            str = str.substring(0, str.length() - 1);
            traverse(r.right, str);
        }
    }


}


///**
// * class TernarySearchTree
// **/
//public class TernarySearch {
//
//    public static void main(String[] args) {
////        TernarySearchTree tst = new TernarySearchTree();
////        tst.insert("pine");
////        tst.insert("pineapple");
////        tst.insert("pimple");
////        tst.insert("parent");
////        tst.insert("patriot");
////        tst.insert("parrot");
////        tst.insert("pinch");
////        tst.insert("apple");
////        tst.insert("pen");
////
////        String result = tst.search("parrot");
////        System.out.println(result);
//
//    }
//}