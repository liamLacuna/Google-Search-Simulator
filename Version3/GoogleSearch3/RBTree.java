/*
Author:       Yang Li
Project Name: SERP Engine Simulator 3.0
Package:      Google Search 3.0
Class:        RBTree.java
 */

package GoogleSearch3;

class RBTree{

    //Create the Node of the root
    private Node root;
    private Node nil;

    /* Constructor */
    public RBTree(){
        nil = new Node(-1, -1, -1, -1, -1, -1, "", "", -1, "");
        nil.setColor("Black");
        setRoot(getNil());
    }

    /* Tree search method */
    public Node RBTree_search(Node temp, int key) {

        /* return the node if the key match */
        if(temp == nil || key == temp.getTotalScore())
            return temp;
        if(key < temp.getTotalScore())
            return RBTree_search(temp.getLeft(), key); /* keep going the left tree if the key less than */
        else
            return RBTree_search(temp.getRight(), key); /* keep going the right tree if the key greater than */
    }

    /* RB Tree Left Rotate Method */
    public void left_rotate(Node x){
        Node y = x.getRight();
        x.setRight(y.getLeft());
        if(y.getLeft() != getNil())
            y.getLeft().setParent(x);
        y.setParent(x.getParent());
        if(x.getParent() == getNil())
            setRoot(y);
        else if(x == x.getParent().getLeft())
            x.getParent().setLeft(y);
        else x.getParent().setRight(y);
        y.setLeft(x);
        x.setParent(y);
    }

    /* RB Tree Right Rotate Method */
    public void right_rotate(Node x){
        Node y = x.getLeft();
        x.setLeft(y.getRight());
        if(y.getRight() != getNil())
            y.getRight().setParent(x);
        y.setParent(x.getParent());
        if(x.getParent() == getNil())
            setRoot(y);
        else if(x == x.getParent().getRight())
            x.getParent().setRight(y);
        else x.getParent().setLeft(y);
        y.setRight(x);
        x.setParent(y);
    }

    /* RB Tree Insert Method */
    public void RBTree_insert(Node z){
        Node y = getNil();
        Node x = getRoot();

        //first Node skip
        while (x != getNil()){
            y = x;
            if(z.getTotalScore() < x.getTotalScore())
                x = x.getLeft();
            else x = x.getRight();
        }

        //set parent of z to y
        z.setParent(y);
        if(y == getNil())
            setRoot(z);                 //set the root to z since the its parent is NIl
        else if(z.getTotalScore() < y.getTotalScore())
            y.setLeft(z);
        else y.setRight(z);
        z.setLeft(getNil());            //set left to NIl
        z.setRight(getNil());           //set right to NIL
        z.setColor("Red");              //insert initially as "Red"
        RBTree_insert_fixUp(z);         //fix the color
    }

    /* RB Tree Insert FixUp Method */
    public void RBTree_insert_fixUp(Node z){
        //change color while its parent's color is red
        while(z.getParent().getColor().equals("Red")) {
            //if its parent is the left child of its grandparent
            if (z.getParent() == z.getParent().getParent().getLeft()) {

                //set the uncle to y, its grandparent right child
                Node y = z.getParent().getParent().getRight();

                //if uncle is red
                if (y.getColor().equals("Red")) {
                    z.getParent().setColor("Black");                            //case 1
                    y.setColor("Black");                                        //case 1
                    z.getParent().getParent().setColor("Red");                  //case 1
                    z = z.getParent().getParent();                              //case 1
                } else { //if the uncle is black
                    //z is the right child
                    if (z == z.getParent().getRight()) {
                        z = z.getParent();                                      //case 2
                        left_rotate(z);                                         //case 2
                    }
                    z.getParent().setColor("Black");                            //case 3
                    z.getParent().getParent().setColor("Red");                  //case 3
                    right_rotate(z.getParent().getParent());                    //case 3
                }
            } //if its parent is the right child of its grandparent
            else if (z.getParent() == z.getParent().getParent().getRight()) {

                //set the uncle to y, its grandparent left child
                Node y = z.getParent().getParent().getLeft();

                //if the uncle is red
                if (y.getColor().equals("Red")) {
                    z.getParent().setColor("Black");                            //case 1
                    y.setColor("Black");                                        //case 1
                    z.getParent().getParent().setColor("Red");                  //case 1
                    z = z.getParent().getParent();                              //case 1
                } else { //if the uncle is black
                    //z is the left child
                    if (z == z.getParent().getLeft()) {
                        z = z.getParent();                                      //case 2
                        right_rotate(z);                                        //case 2
                    }
                    z.getParent().setColor("Black");                            //case 3
                    z.getParent().getParent().setColor("Red");                  //case 3
                    left_rotate(z.getParent().getParent());                     //case 3
                }
            }
        }
        root.setColor("Black");
    }

    /* RB Tree Transplant Method */
    public void RBTree_transplant(Node u, Node v){
        if(u.getParent() == getNil())
            setRoot(v);
        else if(u == u.getParent().getLeft())
            u.getParent().setLeft(v);
        else
            u.getParent().setRight(v);
        v.setParent(u.getParent());
    }

    /* RB Tree Delete Method */
    public void RBTree_delete(Node z){
        Node y = z;
        Node x;

        //get the original color of the Node Z
        String y_original = y.getColor();

        if(z.getLeft() == getNil()){                //z have the right child
            x = z.getRight();
            RBTree_transplant(z, z.getRight());
        }
        else if(z.getRight() == getNil()){          //z have the left child
            x = z.getLeft();
            RBTree_transplant(z, z.getLeft());
        }
        else {                                      //z have the both child
            y = RBTree_minimum(z.getRight());
            y_original = y.getColor();
            x = y.getRight();
            if(y.getParent() == z)
                x.setParent(y);
            else {
                RBTree_transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            RBTree_transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }
        if(y_original.equals("Black"))
            RBTree_delete_fixUp(x);
    }

    /* RB Tree Delete FixUp Method */
    public void RBTree_delete_fixUp(Node x){
        while(x != getRoot() && x.getColor().equals("Black")){
            if(x == x.getParent().getLeft()) {
                Node w = x.getParent().getRight();
                if(w.getColor().equals("Red")){
                    w.setColor("Black");                        //Case 1
                    x.getParent().setColor("Red");              //Case 1
                    left_rotate(x.getParent());                 //Case 1
                    w = x.getParent().getRight();               //Case 1
                }
                if(w.getLeft().getColor().equals("Black") && w.getRight().getColor().equals("Black")){
                    w.setColor("Red");                          //Case 2
                    x = x.getParent();                          //Case 2
                }
                else {
                    if(w.getRight().getColor().equals("Black")){
                        w.getLeft().setColor("Black");          //Case 3
                        w.setColor("Red");                      //Case 3
                        right_rotate(w);                        //Case 3
                        w = x.getParent().getRight();           //Case 3
                    }
                    w.setColor(x.getParent().getColor());       //Case 4
                    x.getParent().setColor("Black");            //Case 4
                    w.getRight().setColor("Black");             //Case 4
                    left_rotate(x.getParent());                 //Case 4
                    setRoot(x);                                 //Case 4
                }
            }
            else {
                Node w = x.getParent().getLeft();
                if(w.getColor().equals("Red")){
                    w.setColor("Black");                        //Case 1
                    x.getParent().setColor("Red");              //Case 1
                    right_rotate(x.getParent());                //Case 1
                    w = x.getParent().getLeft();                //Case 1
                }
                if(w.getRight().getColor().equals("Black") && w.getLeft().getColor().equals("Black")){
                    w.setColor("Red");                          //Case 2
                    x = x.getParent();                          //Case 2
                }
                else {
                    if(w.getLeft().getColor().equals("Black")){
                        w.getRight().setColor("Black");         //Case 3
                        w.setColor("Red");                      //Case 3
                        left_rotate(w);                         //Case 3
                        w = x.getParent().getLeft();            //Case 4
                    }
                    w.setColor(x.getParent().getColor());       //Case 4
                    x.getParent().setColor("Black");            //Case 4
                    w.getLeft().setColor("Black");              //Case 4
                    right_rotate(x.getParent());                //Case 4
                    setRoot(x);                                 //Case 4
                }
            }
        }
        x.setColor("Black");
    }

    /* RB Tree minimum method */
    public Node RBTree_minimum(Node temp) {
        while (temp.getLeft() != nil)
            temp = temp.getLeft(); /* get the very end of the left tree since it's the smallest */
        return temp;
    }

    /*  setter of root */
    public void setRoot(Node root) {
        this.root = root;
    }

    /* getter of root */
    public Node getRoot() {
        return root;
    }

    /* getter of nil */
    public Node getNil() {
        return nil;
    }
}

