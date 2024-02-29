package assignment.birds;

import javafx.scene.chart.PieChart;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;
    //I changed the definition below to make root null, otherwise there were issues
    OrderedDictionary() {
        root = null;
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/birds/DictionaryException.java
     */
    @Override
    public BirdRecord find(DataKey k) throws DictionaryException {
        Node current = root;
        int comparison;
        // If the root is empty, there is nothing to search
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        Node node = searchDict(current, k);
        // If the node can't be found, it doesn't exist
        if(node == null) {
            throw new DictionaryException("There is no record matches the given key");
        } else return node.getData();
    }

    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param r
     * @throws birds.DictionaryException
     */
    @Override
    public void insert(BirdRecord r) throws DictionaryException {
        // Write this method
        //System.out.println("INSIDE INSERT");
        root = insertRecursive(root,null, r);

    }

    private Node insertRecursive(Node root, Node parent, BirdRecord r) {
        //System.out.println("INSIDE INSERTRECURSIVE");
        if(root == null){
            root = new Node(r);
            //System.out.println("PRINT THIS MESSAGE IF THIS GOES THRU");
            //System.out.println(root.getData().getDataKey().getBirdName());
            return root;
        }
        //System.out.println("ABOUT TO COMPARRE");
        if(r.getDataKey().compareTo(root.getData().getDataKey()) == -1){
            root.setLeftChild(insertRecursive(root.getLeftChild(), root, r));
        }
        else if(r.getDataKey().compareTo(root.getData().getDataKey()) == 1){
            root.setRightChild(insertRecursive(root.getRightChild(), root, r));
        }

        return root;
    }

    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws birds.DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
        // Write this method
        // Track the current node
        Node locationNode = searchDict(root, k);

        // Try to delete the node
        Node node = deleteNode(root, k);

        // If the node deleted is the same as the root, assign a new root
        if(locationNode.getData().getDataKey().compareTo(root.getData().getDataKey()) == 0) {
            root = node;
        }

    }

    private Node deleteNode(Node root, DataKey key) {
        if (root == null) return root;

        // Compare the key with the root's key using the compareTo method
        int cmp = key.compareTo(root.getData().getDataKey());

        // If the key to be deleted is smaller than the root's key, then it lies in the left subtree
        if (cmp < 0)
            root.setLeftChild(deleteNode(root.getLeftChild(), key));

        // If the key to be deleted is greater than the root's key, then it lies in the right subtree
        else if (cmp > 0)
            root.setRightChild(deleteNode(root.getRightChild(), key));

        // If the key is the same as the root's key, then this is the node to be deleted
        else {
            // Node with only one child or no child
            if (root.getLeftChild() == null) {
                Node temp = root.getRightChild();
                if (temp != null)
                    temp.setParent(root.getParent()); // Update the parent of the child
                return temp;
            } else if (root.getRightChild() == null) {
                Node temp = root.getLeftChild();
                if (temp != null)
                    temp.setParent(root.getParent()); // Update the parent of the child
                return temp;
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            Node successor = minValueNode(root.getRightChild());

            // Copy the inorder successor's content to this node
            root.setData(successor.getData());

            // Delete the inorder successor
            root.setRightChild(deleteNode(root.getRightChild(), successor.getData().getDataKey()));
        }
        return root;
    }

    /**
     * Returns the successor of k (the record from the ordered dictionary with
     * smallest key larger than k); it returns null if the given key has no
     * successor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws birds.DictionaryException
     */
    @Override
    public BirdRecord successor(DataKey k) throws DictionaryException{
        // Write this method
        Node myRoot = root;

        // If the root is null, the DB is empty
        if(myRoot == null) {
            return null;
        }
//        System.out.println(myRoot.getData().getDataKey().getBirdName());

        if (root.isEmpty()) {
            throw new DictionaryException("There is no record matches the given key");
        }

        // Search for the node with the given key
        Node node = searchDict(myRoot, k);

        // If the node can't be found
        if(node == null) {
            System.out.println("Bird could not be found");
            return null;
        }

        // If the node is the largest node, it has no successor
        if(node.getData() == largest()) {
            return null;
        }

        // If the node has a right child, return the min node on that side
        if(node.hasRightChild()) {
            return minValueNode(node.getRightChild()).getData();
        }


        Node successor = null;
        while(myRoot != null) {
            // Search the left side
            if(node.getData().getDataKey().compareTo(myRoot.getData().getDataKey()) < 0) {
                successor = myRoot;
                myRoot = myRoot.getLeftChild();
            } else if(node.getData().getDataKey().compareTo(myRoot.getData().getDataKey()) > 0) { // Search the right side
                myRoot = myRoot.getRightChild();
            } else break; // If it's a match break the loop
        }

        if(successor != null) { // If a successor has been found, return it
            return successor.getData();
        } else return null; // Else return null
    }

    // Helper function to find min node in a given subtreee
    private Node minValueNode(Node node) {
        Node current = node;
        // Loop down to find the leftmost leaf
        while (current != null && current.getLeftChild() != null)
            current = current.getLeftChild();
        return current;
    }

    // Helper function to find max node in a given subtree
    private Node maxValueNode(Node node) {
        Node current = node;
        // Loop down to find the leftmost leaf
        while (current != null && current.getRightChild() != null)
            current = current.getRightChild();
        return current;
    }


    // Helper function that takes a key and returns the node with that key
    private Node searchDict(Node node, DataKey k) {
        // If the node is null or the key matches, return the node
        if(node == null || node.getData().getDataKey().compareTo(k) == 0) return node;

        // If the key is less than the current nodes key, search left
        if(k.compareTo(node.getData().getDataKey()) < 0)
            return searchDict(node.getLeftChild(), k);

        // Else search right
        return searchDict(node.getRightChild(), k);
    }

    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws birds.DictionaryException
     */
    @Override
    public BirdRecord predecessor(DataKey k) throws DictionaryException{
        // Write this method
        Node myRoot = root;

        if (root.isEmpty()) {
            throw new DictionaryException("There is no record matches the given key");
        }

        // Search for the node with the given key
        Node node = searchDict(myRoot, k);

        // If the node is the smallest, there can be no predecessor
        if(node.getData() == smallest()) {
            return null;
        }

        // If the node has a left child, return the max node on that side
        if(node.hasLeftChild()) {
            return maxValueNode(node.getLeftChild()).getData();
        }

        Node parent = node.getParent();

        // Search the parents to rise up the tree
        while(parent != null && node == parent.getLeftChild()) {
            node = parent;
            parent = parent.getParent();
        }
        // If the parent is null, no predecessor could be found
        if(parent == null) {
            return null;
        } else return parent.getData(); // Else return the parent

    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */

    //I REMOVED THE "throws DictionaryException" FROM BOTH smallest AND largest CAUSE IT WASNT WORKING
    //ALSO DO THEY EVEN NEED TO THROW AN EXCEPTION??
    @Override
    public BirdRecord smallest(){
        // Write this method
        return findLargestSmallest(root, 1);

        //This is old code
        //return smallRecur(root, null);
    }

    /* This is old code
    private BirdRecord smallRecur(Node root, BirdRecord smallestRec){
        if(root == null){
            return null;
        }
        smallestRec = root.getData();
        System.out.println(smallestRec.getDataKey().getBirdName());
        smallRecur(root.getLeftChild(), smallestRec);
        return smallestRec;
    }

     */

    /**
	 * Returns the record with largest key in the ordered dictionary. Returns
	 * null if the dictionary is empty.
     */
    @Override
    public BirdRecord largest(){
        // Write this method
        return findLargestSmallest(root, -1);
    }

    /**
     * Helper function for largest() and smallest(). Recursively finds
     * the largest/smallest value. The size parameter should either be
     * (-1) for largest(), or (1) for smallest.
     */
    private BirdRecord findLargestSmallest(Node tempNode, int size){
        if(root == null){
            return null;
        }

        BirdRecord leftMin, rightMin;
        BirdRecord min = tempNode.getData();

        //Recurse through left subtree
        if(tempNode.hasLeftChild()){
            leftMin = findLargestSmallest(tempNode.getLeftChild(), size);
            if(min.getDataKey().compareTo(leftMin.getDataKey()) == size){
                min = leftMin;
            }
        }

        //Recurse through right subtree
        if(tempNode.hasRightChild()){
            rightMin = findLargestSmallest(tempNode.getRightChild(), size);
            if(min.getDataKey().compareTo(rightMin.getDataKey()) == size){
                min = rightMin;
            }
        }
        return min;
    }

/*
    private BirdRecord traversal(BirdRecord smallest){
        if(root == null){
            return null;
        }
        smallest = traversalRecursive(root, smallest);
        System.out.println(smallest.getDataKey().getBirdName() + "popopopopop");
        return smallest;
    }

    private BirdRecord traversalRecursive(Node root, BirdRecord smallest){
        if(root != null) {
            traversalRecursive(root.getLeftChild(), smallest);
            if(root.getData().getDataKey().compareTo(smallest.getDataKey()) == 1){
                System.out.println("YES THIS IS SMALLER");
                smallest = root.getData();
                System.out.println(smallest.getDataKey().getBirdName());
            }
            traversalRecursive(root.getRightChild(), smallest);
        }
        System.out.println("The last value of smallest is" + smallest.getDataKey().getBirdName());
        return smallest;
    }

 */

    public void inorder(){
        inorderRecur(root);
    }

    private void inorderRecur(Node root){
        if(root != null){
            inorderRecur(root.getLeftChild());
            System.out.print(root.getData().getDataKey().getBirdName() +" ");
            inorderRecur(root.getRightChild());
        }
    }

    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        if(root == null) return true;
        return root.isEmpty();
    }
}
