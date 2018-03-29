/* The purpose of this file is to initialize HashMaps in order to write
 * to files at given names. This will be the portion where we 'clock in'
 */

//need to create a program that initializes a file for tasks we want to monitor


import java.io.*;
import java.util.*;

public class HashClockIn{

   /* This method will take as input a string containing a list of tasks to 
    * keep track of, and return a HashMap of the strings of tasks, as well
    * as their starting time(clock in time);

    * @param fileInput - file name of list of tasks to keep track of

    * @param map - HashMap<String, Time> to keep track of time for individual objects

    * @return - this method returns a HashMap of all of the tasks listed in the 
    fileInput file, as well as their accompanying starting clockin times
    */

   /* This method assumes the file exists, and assumes it is a file in a specific
    * format
    */

   /* file should be of the format

      task
      task
      task clock-in-time
      task
      task clock-in-time

    * the clock-in-time part is for tasks that have already been clocked in. It is 
    the time in military time.
    */
   public static HashMap<String, Time> initializeClockInMap(String clockOutOutput, 
         HashMap<String, Time> map) throws IOException{
      if(clockOutOutput != null){
         File myFile = new File(clockOutOutput);
         if(myFile.exists() && !myFile.isDirectory()){
            Scanner scanner = new Scanner(myFile);
            while(scanner.hasNext()){
               String key = scanner.next();
               if(scanner.hasNextInt()){
                  int value = scanner.nextInt();
                  Time timeValue = new Time(value);
                  map.put(key, timeValue);
               }
               else{
                  if(scanner.hasNext()){
                     scanner.next();
                  }
                  map.put(key, null);
               }
            }
         }
      }
      return map;
   }

   /* This method adds a task to an initialized HashMap<String, Integer> containing
   * tasks that are either in a null state or clocked in
   
   * @param task - the name of the task to be added

   * @param map - the HashMap<String, Integer> that is already initialized 
   */
   public static void addTask(String task, HashMap<String, Time> map) throws IOException{
      map.put(task, null);
   }

   public static void clockIn(String task, HashMap<String, Time> map) throws IOException{
      for(String key:map.keySet()){
         if(key.equals(task) && !(map.get(key) == null)){
            System.out.println("Already clocked in at: " + task);
            return;
         } 
         if(key.equals(task) &&( map.get(key) == null)){
            map.put(task, new Time());
            return;
         }
      }
      addTask(task, map);
      clockIn(task, map);
   }


   /* this method will write the file that the clockout program will use to initialize
    * @param clockOutInput - name of file where we will print the hashmap to
    * @param map - HashMap<String, Time> object that will be printed to new file
    */
   public static void initializeClockOutFile(String clockOutInput, 
         HashMap<String,Time> map) throws IOException{

      File newFile = new File(clockOutInput);
      PrintWriter pw = new PrintWriter(new FileOutputStream(clockOutInput, false));

      for(String key : map.keySet()){
         pw.print(key);
         pw.print(" ");
         if(map.get(key) == null){
            pw.println("null");
            continue;
         }
         pw.println(map.get(key).getTime());
      }
      pw.close();
   }


   public static void main(String[] args) throws IOException{

      HashMap<String, Time> testMap = new HashMap<>();
      testMap = initializeClockInMap("HashClockOutput.txt", testMap);

      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key));
      }

      initializeClockOutFile("testOutput.txt",testMap);

      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key));
      } 
      addTask("tutor", testMap);
    //  initializeClockOutFile("testOutput.txt", testMap);

      System.out.println();
      System.out.println("The added task HashMap is now...");

      clockIn("math", testMap);
      clockIn("english", testMap);
      clockIn("physics", testMap);
      clockIn("physic", testMap);
      clockIn("tutor", testMap);
      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key)); 
      } 
      initializeClockOutFile("testOutput.txt", testMap);

   }
}
