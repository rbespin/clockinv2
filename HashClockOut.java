/* The purpose of this program is to print final times of tasks
 * to new files, and initialize files for HashClockIn.java to use
 */

//need to create a program that initializes a file for tasks we want to monitor


import java.io.*;
import java.util.*;

public class HashClockOut{

   /*
    * This method will take as input the name of a file that contains clock in
    * times. This file will have been initialized by HashClockIn.java
    * It is of the form 
    task time
    task time
    task time
    * where task is the current task being tracked, and time is the clock in time
    * for that task

    * @param clockOutInput - the file name of clock-in's to initialize HashMap

    * @param map - a blank HashMap<String, Integer> that will contain names of
    * tasks and clock in times

    * @return - This method will return a HashMap<String, Integer> that contains
    * the clock in times of tasks
    */
   public static HashMap<String, Time> initializeClockOutMap(String clockOutOutput, 
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
                  continue;
               }
               else{
                  map.put(key, null);
                  if(scanner.hasNext()){
                     scanner.next();
                  }
               }
            }
         }
      }
      return map;
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

   public static void removeTask(String task, HashMap<String, Time> map){
      if(task == null){
         return;
      }
      for(String key:map.keySet()){
         if(key.equals(task)){
            map.remove(task);
            return;
         }
      }
   }

   public static void clockOut(String task, HashMap<String, Time> map) throws IOException{
      if(task == null){
         return;
      }
      for(String key:map.keySet()){
         if(key.equals(task)){
            Time outTime = new Time();
            Time tempTime = map.get(key);
            map.put(key, outTime);
            if(Integer.parseInt(outTime.getTime()) < 
                  Integer.parseInt(tempTime.getTime())){
               int adj = Integer.parseInt(outTime.getTime()) + 2400;
               int totalTime = adj - Integer.parseInt(tempTime.getTime());
               addToFinalFile(task, totalTime, map);
               map.put(key, null);
               return;
            }
            if(Integer.parseInt(outTime.getTime()) == 
                  Integer.parseInt(tempTime.getTime())){
               map.put(key, null);
               return;
            }
            if(Integer.parseInt(outTime.getTime()) > 
                  Integer.parseInt(tempTime.getTime())){
               int totalTime = Integer.parseInt(outTime.getTime()) - 
                  Integer.parseInt(tempTime.getTime());
               addToFinalFile(task, totalTime,  map);
               map.put(key, null);
               return;

            }
         }
      }

   }

   //totaldata.txt
   public static void addToFinalFile(String task, int totalTime, 
         HashMap<String, Time> map) throws IOException{
      String finalFile = "totaldata.txt";
      File myFile = new File(finalFile);
      Scanner scanner = new Scanner(myFile);
      HashMap<String, Integer> tempMap = new HashMap<>();
      while(scanner.hasNext()){
         String key = scanner.next();
         if(scanner.hasNextInt()){
            int value = scanner.nextInt();
            tempMap.put(key, value);
         }
      }
      scanner.close();
      for(String str:tempMap.keySet()){
         int count = 0;
         if(str.equals(task)){
            count++;
            int totTime = tempMap.get(str) + totalTime;
            tempMap.put(task, totTime);
            PrintWriter pw2 = new PrintWriter(new FileOutputStream(myFile, false));
            for(String str2:tempMap.keySet()){
               pw2.print(str2);
               pw2.print(" ");
               pw2.println(tempMap.get(str2));
            }
            pw2.close();
            map.put(str, null);
            break;
         }
         else if(!tempMap.containsKey(task) && map.containsKey(task) && (count == 0)){
            tempMap.put(task, totalTime);
            PrintWriter pw2 = new PrintWriter(new FileOutputStream(myFile, false));
            for(String str2:tempMap.keySet()){
               pw2.print(str2);
               pw2.print(" ");
               pw2.println(tempMap.get(str2));
            }
            pw2.close();

            return;
         }
      }

   }





   public static void main(String[] args) throws IOException{

      HashMap<String, Time> testMap = new HashMap<>();
      testMap = initializeClockOutMap("testOutput.txt", testMap);

      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key));
      }
      initializeClockOutFile("HashClockOutput.txt",testMap);
      removeTask("bubbles", testMap);
      removeTask("math", testMap);
      System.out.println();
      System.out.println("the removed task map is now: ");
      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key));
      }
      clockOut("english", testMap);
      clockOut("tutor", testMap);
      initializeClockOutFile("HashClockOutput.txt", testMap);
      for(String key:testMap.keySet()){
         System.out.print(key);
         System.out.print(" ");
         System.out.println(testMap.get(key));
      }
   }
}
