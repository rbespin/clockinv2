import java.util.*;
import java.io.*;
import java.text.*;
import java.time.*;

public class Time{
   public String date;
   public String time;
   public String name;
   public int month;
   public int day;

   public Time(){
      this.date = LocalDateTime.now().getMonthValue()
         + "/" + LocalDateTime.now().getDayOfMonth();
      this.time = LocalDateTime.now().getHour()*100 + LocalDateTime.now().getMinute() + 
         ((double)LocalDateTime.now().getSecond() / 60 ) + "";
      this.month = LocalDateTime.now().getMonthValue();
      this.day = LocalDateTime.now().getDayOfMonth();
   }

   public Time(int time){
      this.date = LocalDateTime.now().getMonthValue()
         + "/" + LocalDateTime.now().getDayOfMonth();
      this.time = String.valueOf(time);
   }


   public String getTime(){
      return this.time;
   }

   public int getMonth(){
      return this.month;
   }

   public int detDay(){
      return this.day;
   }

   @Override
      public String toString(){
         return ("Date: " + this.date + " " + "Time: " + 
               this.time);
      }
   public static void main(String[] args){
      Time test = new Time();
      System.out.println(test.toString());
   }
}

