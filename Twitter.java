import java.util.ArrayList;

public class Twitter {
	
	ArrayList<Tweet> totalTweetList;
	ArrayList<String> stopWords;
	MyHashTable<String, Tweet> tableByAuthor;
	MyHashTable<String, ArrayList<Tweet>> tableByDate;
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		tableByAuthor = new MyHashTable<>(10);
		tableByDate = new MyHashTable<>(10);
		totalTweetList = new ArrayList<Tweet>();
		for(Tweet tweet:tweets){
			addTweet(tweet);
		}
		this.stopWords = stopWords;
	}
	
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		totalTweetList.add(t);
		String name = t.getAuthor();
		this.tableByAuthor.put(name,t); //now this t becomes his latest tweet, we only save it

		String date = t.getDateAndTime().substring(0,10);
		ArrayList<Tweet> currentTweetList = new ArrayList<>();
		if (this.tableByDate.get(date) != null) {
			currentTweetList = this.tableByDate.get(date);
		}
		currentTweetList.add(t);
		this.tableByDate.put(date,currentTweetList);

	}
	

    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
    	return tableByAuthor.get(author);
    }


    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
        return tableByDate.get(date);
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
	private boolean containsIgnoreCase(ArrayList<String> list, String s){
		for(String word : list){
			if(word.equalsIgnoreCase(s)) return true;
		}
		return false;
	}



    public ArrayList<String> trendingTopics() {
       //1. loop through the whole AL to parse
		MyHashTable<String, Integer> tableOfFrequency = new MyHashTable<>(7);
		for(Tweet tweet : totalTweetList){
			String text = tweet.getMessage();
			ArrayList<String> listOfWords = Twitter.getWords(text);
			ArrayList<String> wordsHaveBeenChecked = new ArrayList<>();
			for(String word: listOfWords){
				word = word.toLowerCase();
				// 1. check if that's stop word
				boolean isStopWord = false;
				if(this.stopWords!=null){
					for(String stopWord:this.stopWords){
						if(stopWord.equalsIgnoreCase(word)) {
							isStopWord = true;
							break;
						}
					}
				}
				boolean hasBeenChecked = containsIgnoreCase(wordsHaveBeenChecked,word);
				if(!isStopWord && !hasBeenChecked){
					if(tableOfFrequency.get(word)==null) { // initialize it
						tableOfFrequency.put(word, 1);
					}
					else {
						int prevTimes = tableOfFrequency.get(word);
						tableOfFrequency.put(word, prevTimes+1);
					}
					wordsHaveBeenChecked.add(word);
				}

			}
		}
		return MyHashTable.fastSort(tableOfFrequency);


    }
	public MyHashTable trendingTopicsTest() {
		//1. loop through the whole AL to parse
		MyHashTable<String, Integer> tableOfFrequency = new MyHashTable<>(10);
		for(Tweet tweet : totalTweetList){
			String text = tweet.getMessage();
			ArrayList<String> listOfWords = Twitter.getWords(text);
			ArrayList<String> wordsHaveBeenChecked = new ArrayList<>();
			for(String word: listOfWords){
				word =word.toLowerCase();
				// 1. check if that's stop word
				boolean isStopWord = false;
				if(this.stopWords!=null){
					for(String stopWord:this.stopWords){
						if(stopWord.equalsIgnoreCase(word)) {
							isStopWord = true;
							break;
						}
					}
				}
				boolean hasBeenChecked = containsIgnoreCase(wordsHaveBeenChecked,word);
				if(!isStopWord && !hasBeenChecked){
					if(tableOfFrequency.get(word)==null) {
						tableOfFrequency.put(word, 1);
					}
					else {
						int prevTimes = tableOfFrequency.get(word);
						tableOfFrequency.put(word, prevTimes+1);
					}
					wordsHaveBeenChecked.add(word);
				}
			}
		}
		//MyHashTable.fastSort(tableOfFrequency);

		return tableOfFrequency;

	}

    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    public static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    

}
