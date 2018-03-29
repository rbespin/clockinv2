/* The purpose of this file is to initialize HashMaps given 
 *  a text file to be later used
 */

import java.io.*;
import java.util.*;

public class HashInitialize{

   /*this method assumes the file exists
    *this method also assumes the file is in a specific format

    * @return - this method returns a HashMap initialized with the file contents
    * of a String(the task at hand) and an integer(total amount of time that task has had)

    * @param fileInput - this parameter is a string, the name of the file to be 
    * initializing a HashMap. The file should be of the form (string, int, println);
    * This input string should be of the form inputFile.txt


    * @param map - This parameter is a hashmap, for which we will input file info
    */

   /* This method will initialize a HashMap from the file that contains the total
    * amount of time spent on tasks.
    */
   public static HashMap<String, Integer> initializeTotalMap(String fileInput, 
         HashMap<String, Integer> map) throws IOException{
      if(fileInput != null){
         File myFile = new File(fileInput);
         if(myFile.exists() && !myFile.isDirectory()){
            Scanner scanner = new Scanner(myFile);
            String key;
            int value;
            while(scanner.hasNext()){
               key = scanner.next();
               if(scanner.hasNext()){
                  value = scanner.nextInt();
                  map.put(key,value);
               }
            }
         }
      }
      return map;
   }



   /* This method will print the HashMap containing information for cumulative
    * data to a file. It will overwrite existing file, and write a .txt file

    * @param map - this parameter will be the HashMap to write information from

    * @param outputFile - this parameter will be the file to which we overwrite information 
    *  from the HashMap to.

    */
   public static void initializeTotalFile(String outputFile, HashMap<String, Integer> map)
      throws IOException{
         String fileString = outputFile.toLowerCase();

         //modifying input string to have .txt extension
         fileString = fileString + ".txt";

         File myFile = new File(outputFile);

         //PrintWriter object, ensuring that we overwrite a file if it exists.
         PrintWriter pw = new PrintWriter(new FileOutputStream(fileString, false));

         for(String key : map.keySet()){
            pw.print(key);
            pw.print(" ");
            int value = map.get(key);
            pw.println(value);
         }
         pw.close();
      }


   public static void main(String[] args) throws IOException{
      HashMap<String, Integer> map = new HashMap<>();

      map.put("anthropology", 189990);
      map.put("math", 120);
      map.put("cs", 121);
      map.put("spanish", 150);

      for(String key:map.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(map.get(key));
      }

      initializeTotalFile("totaldata", map);

      HashMap<String, Integer> testMap = new HashMap<String, Integer>();

      testMap = initializeTotalMap("totaldata.txt", testMap);

      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key));
      }
   }


}
