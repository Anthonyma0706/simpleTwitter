import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
        this.numEntries=0;
        this.numBuckets=initialCapacity;
        this.buckets = new ArrayList<LinkedList<HashPair<K, V>>>();
        for(int i=0;i<initialCapacity;i++) this.buckets.add(null);
    }


    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
        HashPair<K,V> pair = new HashPair(key, value);
        int hashValue = hashFunction(key); // this is the index
        LinkedList<HashPair<K,V>> listInSlot =  this.buckets.get(hashValue);

        if(listInSlot==null){ // means nothing in the bucket yet, linkedList is null
            LinkedList<HashPair<K,V>> linkedList = new LinkedList<>();
            listInSlot = linkedList;
        }
        else {
            for (HashPair<K, V> hashPair : listInSlot) {
                if (hashPair.getKey().equals(key)) { //change the value when keys are same
                    V origValue = hashPair.getValue();
                    hashPair.setValue(value); //override the value
                    this.buckets.set(hashValue, listInSlot);

                    this.numEntries++;
                    double a = numEntries; // cast them to double and use double division
                    double b = numBuckets;
                    if(a/b > MAX_LOAD_FACTOR){
                        rehash();
                    }
                    return origValue;
                }
            }
        }
        listInSlot.add(pair);
        this.buckets.set(hashValue,listInSlot);

        this.numEntries++;
        double a = numEntries;
        double b = numBuckets;
        if(a/b > MAX_LOAD_FACTOR){
            rehash();
        }
    	return null;

    }
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    
    public V get(K key) {
        int hashValue = hashFunction(key); // this is the index
        LinkedList<HashPair<K,V>> listInSlot =  this.buckets.get(hashValue);
        if(listInSlot!=null){
            for (HashPair<K, V> hashPair : listInSlot){
                if(hashPair.getKey().equals(key)){
                    return hashPair.getValue();
                }
            }
        }
        
    	return null;
    }
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1) 
     */
    public V remove(K key) {
        int hashValue = hashFunction(key); // this is the index
        LinkedList<HashPair<K,V>> listInSlot =  this.buckets.get(hashValue);
        if(listInSlot!=null){
            for (HashPair<K, V> hashPair : listInSlot){
                if(hashPair.getKey().equals(key)){
                    V value = hashPair.getValue();
                    listInSlot.remove(hashPair);
                    this.buckets.set(hashValue, listInSlot);
                    return value;
                }
            }
        }
        
    	return null;
    	
        //ADD YOUR CODE ABOVE HERE
    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */

    public void rehash(){
        int oldNum = numBuckets;
        ArrayList<LinkedList<HashPair<K,V>>> oldBucket = new ArrayList();
        ArrayList<LinkedList<HashPair<K,V>>> newBucket = new ArrayList();
        oldBucket = this.buckets;

        numBuckets = numBuckets * 2;
        for(int i=0;i<numBuckets;i++) newBucket.add(null);
        this.buckets = newBucket;
        this.numEntries=0; // reinitialize since later we use put to put each pair and that increases numEntries

        for(int j = 0; j<oldNum;j++){
            LinkedList<HashPair<K,V>> linkedList = oldBucket.get(j);
            if(linkedList!=null){
                for (HashPair<K,V> hashPair:linkedList){
                    this.put(hashPair.getKey(),hashPair.getValue()); // put this pair into the correct position in the new bucket!
                }
            }
        }
    }
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    
    public ArrayList<K> keys() {
        ArrayList<K> list = new ArrayList<>();
        for(LinkedList<HashPair<K,V>> linkedList: this.buckets){
            if (linkedList!=null){
                for(HashPair<K,V> hashPair : linkedList){
                    list.add(hashPair.getKey());
                }
            }
        }
        return list;
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
        ArrayList<V> list = new ArrayList<>();
        for(LinkedList<HashPair<K,V>> linkedList: this.buckets){
            if (linkedList!=null){
                for(HashPair<K,V> hashPair : linkedList){
                    list.add(hashPair.getValue());
                }
            }
        }
        return list;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        ArrayList<HashPair<K,V>> list = new ArrayList<>();
        for(HashPair<K,V> hashPair:results){
            list.add(hashPair); // 1. initialize the list with all the hashPairs
        }
        quickSort(list,0,list.size()-1);
        // 2. now we put all the value in the same order to the arrayList with keys
        ArrayList<K> listOfKeys = new ArrayList<>();
        for(HashPair<K,V> hashPair:list){
            listOfKeys.add(hashPair.getKey());
        }
    	return listOfKeys;

    }
    private static <K, V extends Comparable<V>> void quickSort(ArrayList<HashPair<K,V>> list, int leftIndex, int rightIndex){
        if(leftIndex>=rightIndex) return;
        else {
            int i = placeAndDivide(list,leftIndex,rightIndex); // pivot index

            quickSort(list,leftIndex,i-1);
            quickSort(list,i+1,rightIndex); // not include the pivot
        }

    }

    private static <K, V extends Comparable<V>> int placeAndDivide(ArrayList<HashPair<K,V>> list, int leftIndex, int rightIndex){
        V pivotValue = list.get(rightIndex).getValue();
        int wall = leftIndex-1;

        for(int i = leftIndex; i<rightIndex;i++){
            if(list.get(i).getValue().compareTo(pivotValue)>0){ /** remember we sort by descending order**/
                wall++;
                HashPair wallPair = list.get(wall);
                HashPair cur = list.get(i);
                list.set(wall, cur);
                list.set(i,wallPair);
            }
        }
        HashPair wallNext = list.get(wall+1);
        HashPair pivotPair = list.get(rightIndex);
        list.set(wall+1, pivotPair);
        list.set(rightIndex,wallNext);
        return wall+1;
    }

    
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {

    	LinkedList<HashPair<K,V>> listOfHashPair;

    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
            listOfHashPair = new LinkedList<>();
            ArrayList<LinkedList<HashPair<K,V>>> buckets = getBuckets();
            for(LinkedList<HashPair<K,V>> linkedList: buckets){
                if(linkedList!=null){
                    for(HashPair<K,V> hashPair:linkedList){
                        listOfHashPair.addFirst(hashPair);
                    }
                }
            }
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
        	return !listOfHashPair.isEmpty();
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            return listOfHashPair.removeFirst();

        }
        
    }
}
