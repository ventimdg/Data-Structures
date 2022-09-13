import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Dominic Ventimiglia
 */
public class BSTStringSet implements SortedStringSet, StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    public Node nodefinder(String s) {
        if (_root == null) {
            return null;
        }
        Node previous = _root;
        Node current = _root;
        while (true) {
            if (s.compareTo(current.s) < 0) {
                current = current.left;
            } else if (s.compareTo(current.s) > 0) {
                current = current.right;
            } else {
                return current;
            }
            if (current == null) {
                return previous;
            } else {
                previous = current;
            }
        }
    }

    @Override
    public void put(String s) {
        Node answer = nodefinder(s);
        if (answer == null) {
            _root = new Node(s);
        } else if (s.compareTo(answer.s) < 0) {
            answer.left = new Node(s);
        } else if (s.compareTo(answer.s) > 0){
            answer.right = new Node(s);
        }
    }

    @Override
    public boolean contains(String s) {
        Node answer = nodefinder(s);
        return answer != null && s.equals(answer.s);
    }

    @Override
    public List<String> asList() {
        List<String> answer = new ArrayList<>();
        BSTIterator helper = new BSTIterator(_root);
        while (helper.hasNext()) {
            answer.add(helper.next());
        }
        return answer;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;


        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }


    @Override
    public Iterator<String> iterator(String low, String high) {
        BSTStringSet newtree = new BSTStringSet();
        BSThelper(this, newtree, low, high);
        return new BSTIterator(newtree._root);

    }

    public static void BSThelper(BSTStringSet orig, BSTStringSet newtree, String low, String high) {
        List<String> helper = orig.asList();
        for (String str : helper) {
            int lownum = low.compareTo(str);
            int highnum = high.compareTo(str);
            if (lownum <= 0 && highnum >= 0) {
                newtree.put(str);
            }
        }
    }


    /** Root node of the tree. */
    private Node _root;
}
