import java.util.*;
import java.io.*;
import java.lang.Math;


/**
IST 222
Computational Problem Solving III
Data Structures Timer HW
Clayton Brimm
April 27th 2018
**/

public class DSTimer{
   //variables
   private ArrayList<String> words;
   private String[] search;
   private final String INPUTFILE = "words_alpha.txt";
   private int SEARCH_ITEMS = 1000;
   private HashMap<String, Long> times = new HashMap<>();
   private Stack<String> stack = new Stack<String>();;
   
   //main
   public static void main(String[] args){
      new DSTimer();
   }//main
   
   //constructor
   public DSTimer(){
      System.out.println("*******************WELCOME TO DATA STRUCTURE TIMER*******************\n\n");
      words = loadWords(new File(INPUTFILE));
      search = loadSearch();
      System.out.println("----------------TESTING DATASTRUCTURE SPEEDS-----------------\n");
      search1D(words, search);
      searchRow2D(words, search);
      searchCol2D(words, search);
      searchCollection(new ArrayList<String>(), words, search, "ArrayList");
      searchCollection(new Vector<String>(), words, search, "Vector");
      searchCollection(new HashSet<String>(), words, search, "HashSet");
      searchCollection(new TreeSet<String>(), words, search, "TreeSet");
      searchCollection(new LinkedHashSet<String>(), words, search, "LinkedHashSet");
      searchCollection(new LinkedList<String>(), words, search, "LinkedList");
      System.out.println("\n----------------TESTING COMPLETE-----------------\n");
      findWinner();
      //searchCollection(new ConcurrentSkipListSet<String>(), words, search);
   }//DSTimer constructor
   
   //Method for loading the unabridged english dictionary into words ArrayList
   public ArrayList loadWords(File theFile){
      try (BufferedReader br = new BufferedReader( new FileReader(theFile) ) ){
         String line;
         words = new ArrayList<>();
         while( (line=br.readLine()) != null ){
            words.add(line);
         }      
      }catch(IOException ioe){
         ioe.printStackTrace();
      }
      System.out.println("Loaded words ArrayList from "+INPUTFILE+": words size = "+words.size()+"\n");
      return words;
   }//loadWords()
   
   //This method takes a certain number of completely random strings from words arraylist and adds them to an array
   public String[] loadSearch(){
      search = new String[SEARCH_ITEMS];
      Random rand = new Random();
      int x = 0;
      while(x < SEARCH_ITEMS){
         String randomElement = words.get(rand.nextInt(words.size()));
         search[x] = randomElement;
         x++;
      }
      System.out.println("Loaded "+SEARCH_ITEMS+" random strings from words ArrayList into the search array \n");
      return search;
   }//loadSearch()
   
   //Searches 1D array using a sequential search
   private void search1D(ArrayList<String> _words, String[] _search){
      System.out.print("Searching 1D array using sequential search...");
      long startTime = System.nanoTime();
      int matchCount = 0;
      for(int i = 0, j = 0;i < _search.length && j < _words.size();){
         if(_search[i].equals(_words.get(j))){
            matchCount++;
            i++;
            j=0;
         }else{j++;}
      }
      long endTime   = System.nanoTime(); 
      long totalTime = endTime - startTime;
      System.out.println("DONE "+totalTime+" nanoseconds to complete.");
      times.put("1D array", totalTime);
   
   }//Search1D()
   
   //Searches a 2D array by row order
   private void searchRow2D(ArrayList<String> _words, String[] _search){
      Double x = Math.ceil(Math.sqrt(_words.size()));
      Integer size = x.intValue();
      //create 2D box array that is the size of the square root of the size of words arraylist
      String[][] twoD = new String[size][size];
      int k = 0;
      //add words arraylist to 2d array
      for(int i = 0; i < size; i++){
         for(int j = 0;j < size; j++){
            if(k < _words.size()){
               twoD[j][i] = _words.get(k);
               k++;
            }
         }
      }
      //search the 2d array for the search items row by row
      System.out.print("Searching 2D array using row search...");
      long startTime = System.nanoTime();
      int matchCount = 0;
      for(int i = 0; i < size && matchCount < _search.length; i++){
         for(int j = 0;j < size && matchCount < _search.length; j++){
            if(_search[matchCount].equals(twoD[j][i])){
               matchCount++;
               i=0;
               j=0;
            }
         }
      }
      long endTime   = System.nanoTime(); 
      long totalTime = endTime - startTime;
      System.out.println("DONE "+totalTime+" nanoseconds to complete.");
      times.put("2D array row", totalTime);
   }//searchRow2D()
   
   //Searches 2D array by column instead of row. Very similar method except i and j are switched at line 150
   private void searchCol2D(ArrayList<String> _words, String[] _search){
      Double x = Math.ceil(Math.sqrt(_words.size()));
      Integer size = x.intValue();
      String[][] twoD = new String[size][size];
      int k = 0;
      for(int i = 0; i < size; i++){
         for(int j = 0;j < size; j++){
            if(k < _words.size()){
               twoD[j][i] = _words.get(k);
               k++;
            }
         }
      }
      System.out.print("Searching 2D array using column search...");
      long startTime = System.nanoTime();
      int matchCount = 0;
      for(int i = 0; i < size && matchCount < _search.length; i++){
         for(int j = 0;j < size && matchCount < _search.length; j++){
            if(_search[matchCount].equals(twoD[i][j])){
               matchCount++;
               i=0;
               j=0;
            }
         }
      }
      long endTime   = System.nanoTime(); 
      long totalTime = endTime - startTime;
      System.out.println("DONE "+totalTime+" nanoseconds to complete.");
      times.put("2D array column", totalTime);
   }//searchCol2D()
   
   //Generic method that accepts any AbstractCollection type
   public <T extends AbstractCollection> void searchCollection(T collection, ArrayList<String> _words, String[] _search, String type){
      boolean contains;
      //adds words arraylist to the collection
      for(String item: _words)
         collection.add(item);
      
      System.out.print("Searching " + type + "...");
      long startTime = System.nanoTime();
      //use the .contains method to find each search item
      for(String item2: _search){
         if(collection.contains(item2)){
            contains = true;
         }else{
            contains = false;
            System.out.println("ERROR!!!!!!!!");
         }
      }
      long endTime   = System.nanoTime(); 
      long totalTime = endTime - startTime;
      System.out.println("DONE "+totalTime+" nanoseconds to complete.");
      times.put(type, totalTime);
   }//searchCollection()
   
   //Fun little method for getting a list with the fasted search being first
   public void findWinner(){
      //list of words that have been added to the stack
      ArrayList<String> addedList = new ArrayList<>(); 
      while(addedList.size() < times.size()){
         long slowest = 0;
         String slowestName = "error";
         //take the hashmap that stores the name of the DS as the key and its time as the value 
         for (String key : times.keySet()) {
            long timeValue = times.get(key);
            //find the slowest time that hasnt been added to the stack yet
            if(timeValue > slowest && !addedList.contains(key)){
               slowest = timeValue;
               slowestName = key;
            }
         }
         addedList.add(slowestName);
         stack.push(slowestName);
      }
      int pos = 0;
      //pop each value on the stack which will start with the fastest search and print them out
      while(!stack.empty()){
         pos++;
         String strng = stack.pop();
         long time = times.get(strng);
         double seconds = (double)time / 1E9;
         System.out.format("%-20s", pos+": "+strng);
         System.out.print(" "+seconds+" seconds\n");
      }
   }//findWinner()
   
}//DSTimer class