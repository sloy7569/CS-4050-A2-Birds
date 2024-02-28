package assignment.birds;

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
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
            }
        }

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
        /*if(root.hasLeftChild()){
            System.out.println(root.getLeftChild().getData().getDataKey().getBirdName());
        }*/
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
        return null; // change this statement
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
        return null; // change this statement
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
        return root.isEmpty();
    }
}
