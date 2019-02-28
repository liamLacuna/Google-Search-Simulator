/*
Author:       Yang Li
Project Name: SERP Engine Simulator 2.0
Package:      Google Search 2.0
Class:        BinarySearchTree.java
 */

package GoogleSearch2;

class BinarySearchTree{

    //Create the Node of the root
    private Node root;

    /* Constructor */
    public BinarySearchTree(){
    }

    /* Tree search method */
    public Node tree_search(Node temp, int key) {

        /* return the node if the key match */
        if(temp == null || key == temp.getTotalScore())
            return temp;
        if(key < temp.getTotalScore())
            return tree_search(temp.getLeft(), key); /* keep going the left tree if the key less than */
        else
            return tree_search(temp.getRight(), key); /* keep going the right tree if the key greater than */
    }

    /* Tree minimum method */
    public Node tree_minimum(Node temp) {
        while (temp.getLeft() != null)
            temp = temp.getLeft(); /* get the very end of the left tree since it's the smallest */
        return temp;
    }

    /* Tree maximum method */
    public Node tree_maximum(Node temp) {
        while (temp.getRight() != null)
            temp = temp.getRight(); /* get he very end of the right tree since it's the largest */
        return temp;
    }

    /*  Tree successor method
        Getting the smallest Node of the right tree
     */
    public Node tree_successor(Node temp) {
        if(temp.getRight() != null)
            return tree_minimum(temp.getRight());
        Node y = temp.getParent();
        while(y != null && temp == y.getRight()){
            temp = y;
            y = y.getParent();
        }
        return y;
    }

    /* Tree insert method */
    public void tree_insert(Node z){
        Node y = null;
        Node x = this.root;

        while(x != null){
            y = x;
            if(z.getTotalScore() < x.getTotalScore())
                x = x.getLeft();
            else x = x.getRight();
        }
        z.setParent(y);
        if(y == null)
            setRoot(z);
        else if(z.getTotalScore() < y.getTotalScore())
            y.setLeft(z);
        else y.setRight(z);
    }

    /* Tree transplant method */
    public void tree_transplant(Node u, Node v){
        if (u.getParent() == null)
            this.root = v;
        else if (u == u.getParent().getLeft())
            u.getParent().setLeft(v);
        else
            u.getParent().setRight(v);
        if (v != null) v.setParent(u.getParent());
    }

    /* Tree delete method */
    public void tree_delete(Node z){
        if(z.getLeft() == null){
            tree_transplant(z, z.getRight());
        }
        else if (z.getRight() == null){
            tree_transplant(z, z.getLeft());
        }
        else {
            Node y = tree_minimum(z.getRight());
            if(y.getParent() != z){
                tree_transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            tree_transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
        }
    }

    /*  setter of root */
    public void setRoot(Node root) {
        this.root = root;
    }

    /* getter of root */
    public Node getRoot() {
        return root;
    }
}

