import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import javafx.scene.chart.*;
import java.util.Collections;

public class GuiClock extends Application{

   public static GridPane pane = new GridPane();
   public BorderPane mainPane; //main pane to add everything to

   public static Button btEnter = new Button("Add");
   public static Button clockIn = new Button("Clock in");
   public static Button clockOut = new Button("Clock out");
   public static Button stats = new Button("Statistics");
   public static Button returnStat = new Button("Return");
   public static Button listStat = new Button("Full List");

   public static HBox titleBox = new HBox();

   public static int x = 2;
   public static int y = 2;
   public static double totalTime;

   public static double valueHighest;
   public static String keyHighest;
   public static Label stringLa; 



   //map is the current state of every task, clocked-in or clocked-out
   public static LinkedHashMap<String, Double> map = new LinkedHashMap<>();

   public static LinkedHashMap<String, Double> tempMap = new LinkedHashMap<>();
   public static HashMap<String, Double> totalTimeMap = new HashMap<>();
   public static LinkedHashMap<String, Double> totalListMap = new LinkedHashMap<>();
   public static LinkedHashMap<String, Double> deleteMap = new LinkedHashMap<>();
   //upon clocking out, set the state of the task back to clock-in, and total up
   //the time spent and put it into totalTimeMap, print to new file
   //maybe beneath, or above, or to the side of the task, display the clock-in
   //time, and have a button in the upper right that let's you see the totals
   //from totalTimeMap, maybe in a cool display


   public static void main(String[] args) throws IOException{
	  launch(args);
   }



   @Override
	  public void start(Stage primaryStage) throws IOException
	  {



		 //SETTING SCENE
		 mainPane = new BorderPane();
		 Scene scene = new Scene(mainPane);
		 primaryStage.setTitle("Clock in");
		 primaryStage.setScene(scene);

		 //HBOX TOP TITLE
		 HBox titleBox = new HBox();
		 Text titleText = new Text();
		 titleText.setText("");
		 titleBox.getChildren().add(titleText);
		 titleBox.setAlignment(Pos.TOP_RIGHT);
		 titleBox.setPadding(new Insets(15,15,15,15));
		 titleBox.getChildren().add(stats);

		 //CENTER GRIDPANE  
		 pane.setPrefSize(500,650);
		 pane.setPadding(new Insets(15,15,15,15));

		 //GRIDPANE ELEMENTS
		 Label addLa = new Label("Click to add new Task");
		 TextField  addEl = new TextField();

		 //ADDING GRIDPANE ELEMENTS
		 pane.add(addLa,0,0);
		 pane.add(addEl,0,1);
		 pane.add(btEnter, 1,1);
		 pane.setHgap(15);
		 pane.setVgap(15);
		 initialize();

		 //EVENTS

		 //event to handle full list 
		 listStat.setOnAction(e ->{
			   try{
			   GridPane gridPane = new GridPane();
			   gridPane.setPrefSize(500,650);
			   gridPane.setPadding(new Insets(30,30,30,30));
			   gridPane.setHgap(20.0);
			   gridPane.setVgap(15.0);
			   mainPane.setCenter(null);
			   titleBox.setSpacing(15);
			   titleBox.getChildren().clear();
			   titleBox.getChildren().add(returnStat);
			   File listFile = new File("listtotals.txt");
			   Scanner scanner = new Scanner(listFile);
			   int row = 1;
			   while(scanner.hasNext()){
			   totalListMap.put(scanner.next(),scanner.nextDouble());
			   }

			   scanner.close();

			   File newFile = new File("totals.txt");
			   scanner = new Scanner(listFile);
			   while(scanner.hasNext()){
				  deleteMap.put(scanner.next(), scanner.nextDouble());
			   }
			   scanner.close();

			   scanner = new Scanner(listFile);
			   while(scanner.hasNext()){
				  Label stringLa = new Label(scanner.next());
				  Label doubleLa = new Label(Math.floor(scanner.nextDouble()) + " minutes");
				  gridPane.add(stringLa, 1,row);
				  gridPane.add(doubleLa, 3, row);
				  Button deleteListTask = new Button("Delete");
				  int innerRow = row;

				  deleteListTask.setOnAction(f->{
						gridPane.getChildren().remove(deleteListTask);
						Button confirm = new Button("DELETE");
						Button deny = new Button("Cancel");
						gridPane.add(confirm, 6,innerRow);
						gridPane.add(deny, 5, innerRow);

						deny.setOnAction(g ->{
							  gridPane.getChildren().remove(confirm);
							  gridPane.getChildren().remove(deny);
							  gridPane.add(deleteListTask,5,innerRow);
							  });
						confirm.setOnAction(h ->{
							  try{
							  totalListMap.remove(stringLa.getText());

							  deleteMap.remove(stringLa.getText());
							  System.out.println("removing from totalListMap and deleteMap...");
							  PrintWriter pw = new PrintWriter(new FileOutputStream(listFile, false));
							  for(String key:totalListMap.keySet()){
							  pw.print(key + " ");
							  pw.println(totalListMap.get(key));
							  }
							  pw.close();

							  File newFile2 = new File("totals.txt");
							  pw = new PrintWriter(new FileOutputStream(newFile2, false));
							  for(String key:deleteMap.keySet()){
							  pw.print(key + " ");
							  pw.println(deleteMap.get(key));
							  }
							  pw.close();
							  gridPane.getChildren().remove(confirm);
							  gridPane.getChildren().remove(deny);
							  gridPane.getChildren().remove(stringLa);
							  gridPane.getChildren().remove(doubleLa);
							  }catch(IOException ex){};
						});

				  });

				  gridPane.add(deleteListTask, 5, row);
				  row++;
			   }
			   scanner.close();
			   initialize();
			   ScrollPane scrollPane = new ScrollPane(gridPane);
			   scrollPane.setFitToHeight(true);
			   mainPane.setCenter(scrollPane);
			   }catch(IOException ex){};
		 });  

		 //event to handle return to homepage button
		 returnStat.setOnAction(e ->{
			   try{
			   mainPane.setCenter(null);
			   titleBox.getChildren().clear();

			   titleBox.getChildren().add(stats);

			   initialize();
			   ScrollPane scrollPane = new ScrollPane(pane);
			   scrollPane.setFitToHeight(true);
			   mainPane.setCenter(scrollPane);
			   mainPane.setTop(titleBox);
			   }catch(IOException ex){}
			   });

		 //event to handle statistics page
		 stats.setOnAction(e ->{
			   try{
			   System.out.println("stats page");
			   Pane statsPane = new Pane();
			   statsPane.setPrefSize(500,650);
			   CategoryAxis xAxis = new CategoryAxis();
			   NumberAxis yAxis = new NumberAxis();
			   BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
			   bc.setTitle("Most Hours Spent on Activity");
			   bc.setMinSize(500,650);
			   xAxis.setLabel("Task");
			   yAxis.setLabel("Minutes");


			   XYChart.Series<String, Number>  series1 = new XYChart.Series<String, Number>();
			   TreeMap<Double, String> bcMap = new TreeMap<>();
			   TreeMap<String, Double> compMap = new TreeMap<String, Double>();
			   File newFile4 = new File("totals.txt");
			   Scanner scanner4 = new Scanner(newFile4);
			   while(scanner4.hasNext()){
			   compMap.put(scanner4.next(), scanner4.nextDouble());
			   }
			   scanner4.close();
			   for(String key: compMap.keySet()){
				  String key5 = key;
				  double value5 = compMap.get(key);
				  bcMap.put(value5, key5);
			   }


			   TreeMap<Double, String> sortedMap = new TreeMap<Double, 
				  String>(Collections.reverseOrder());

			   sortedMap.putAll(bcMap);

			   int iterate = 0;
			   for(Double j : sortedMap.keySet()){
				  if(iterate >= 10){
					 break;
				  }
				  System.out.print(j + " ");
				  System.out.println(sortedMap.get(j));
				  Number number = j;
				  series1.getData().add(new XYChart.Data<String, 
						Number>(sortedMap.get(j), number));
				  iterate++;
			   }

			   File listFile = new File("listtotals.txt");
			   PrintWriter lf = new PrintWriter(new FileOutputStream(listFile, false));
			   for(Double lfKey:sortedMap.keySet()){
				  lf.print(sortedMap.get(lfKey) + " ");
				  lf.println(lfKey);
			   }
			   lf.close();
			   statsPane.getChildren().add(bc);

			   bc.getData().add(series1);

			   mainPane.setCenter(null);
			   mainPane.setCenter(statsPane);
			   titleBox.getChildren().remove(stats);
			   titleBox.getChildren().add(listStat);
			   titleBox.getChildren().add(returnStat);
			   titleBox.setSpacing(15);
			   }catch(IOException ex){}

		 });

		 //event to handle the creation of a task

		 btEnter.setOnAction(e ->{
			   try{

			   Label lab = new Label(addEl.getText());
			   lab.setMinWidth(150);
			   lab.setMinHeight(25);


			   Pattern patternCheck = Pattern.compile("\\s");
			   Matcher matcherCheck = patternCheck.matcher(lab.getText());

			   if(!map.containsKey(lab.getText())){
			   if(!matcherCheck.find()  && 
					 /*     lab.getText().chars().allMatch(Character::isLetter) && */
					 (lab.getText() != "")){
			   map.put(lab.getText().toLowerCase(), 0.0);
			   initialize();
			   }
			   }
			   }
			   catch(IOException ex){}

		 });


		 //ADDING ELEMENTS TO MAINPANE

		 ScrollPane scrollPane = new ScrollPane(pane);
		 scrollPane.setFitToHeight(true);
		 mainPane.setCenter(scrollPane);
		 mainPane.setTop(titleBox);
		 primaryStage.show();

	  }



   //this method initializes the board to map's state. To clock in or clock out,
   //modify the static variable map, then initialize. OR handle clock-in and clock-out
   //in the eventhandlers located in initialize.
   public static void initialize() throws IOException{
	  File myFile = new File("initialize.txt");
	  Scanner scanner = new Scanner(myFile);

	  while(scanner.hasNext()){
		 map.put(scanner.next(), scanner.nextDouble());
	  }
	  scanner.close();

	  for(String key:map.keySet()){
		 if(!tempMap.containsKey(key)){

			Button nameClockInB = new Button("Clock in");
			nameClockInB.setDisable(false);

			Button nameClockOutB = new Button("Clock out");
			nameClockOutB.setDisable(true);

			Button removeButton = new Button("Remove");

			if(map.get(key) != 0){
			   nameClockInB.setDisable(true);
			   nameClockOutB.setDisable(false);
			}

			EventHandler<ActionEvent> handlerClockIn = event -> {
			   try{
				  nameClockOutB.setDisable(false);
				  nameClockInB.setDisable(true);
				  Time time = new Time();
				  double timeInt = Double.parseDouble(time.getTime());
				  System.out.println(timeInt);
				  map.put(key, timeInt);
				  tempMap.put(key, timeInt);
				  PrintWriter pw = new PrintWriter(new FileOutputStream(myFile, false));
				  for(String inputKey:map.keySet()){
					 tempMap.put(inputKey, map.get(inputKey));
				  }
				  for(String keyz: tempMap.keySet()){
					 pw.print(keyz + " ");
					 pw.println(map.get(keyz));
				  }
				  pw.close();

				  initialize();
				  System.out.println("clock-in pressed");
			   } catch(IOException ex){}};

			EventHandler<ActionEvent> handlerClockOut = event ->{
			   try{
				  nameClockOutB.setDisable(true);
				  nameClockInB.setDisable(false);

				  File newFile = new File("totals.txt");
				  Scanner newScanner = new Scanner(newFile);
				  while(newScanner.hasNext()){
					 totalTimeMap.put(newScanner.next(), newScanner.nextDouble());
				  }
				  newScanner.close();

				  File myFile2 = new File("initialize.txt");
				  newScanner = new Scanner(myFile2);
				  while(newScanner.hasNext()){
					 map.put(newScanner.next(), newScanner.nextDouble());
				  }
				  newScanner.close();

				  Time clockOutTime = new Time();
				  double clockOut = Double.parseDouble(clockOutTime.getTime());
				  double clockIn = map.get(key);


				  if(clockOut<clockIn){
					 clockOut = clockOut + 2400;
				  }
				  if(totalTimeMap.containsKey(key)){
					 totalTime = totalTimeMap.get(key) + (clockOut - clockIn);
				  }
				  else{
					 totalTime = clockOut - clockIn;
				  }

				  totalTimeMap.put(key, totalTime);

				  PrintWriter pw2 = new PrintWriter(new FileOutputStream(newFile, false));
				  for(String totalKey:totalTimeMap.keySet()){
					 pw2.print(totalKey + " ");
					 pw2.println(totalTimeMap.get(totalKey));
				  }
				  pw2.close();

				  map.put(key, 0.0);
				  tempMap.put(key, 0.0);
				  PrintWriter pw = new PrintWriter(new FileOutputStream(myFile, false));
				  for(String inputKey:map.keySet()){
					 tempMap.put(inputKey, map.get(inputKey));
				  }
				  for(String keyz: tempMap.keySet()){
					 pw.print(keyz + " ");
					 pw.println(map.get(keyz));
				  }
				  pw.close();

				  initialize();
				  System.out.println("clock-out pressed");
			   } catch(IOException ex){}};

			EventHandler<ActionEvent> handlerRemove = event -> {
			   try{
				  map.remove(key);
				  PrintWriter pw = new PrintWriter(new FileOutputStream(myFile, false));
				  tempMap = new LinkedHashMap<String, Double>();
				  nameClockOutB.setDisable(true);
				  nameClockInB.setDisable(true);
				  removeButton.setDisable(true);
				  for(String inputKey:map.keySet()){
					 tempMap.put(inputKey, map.get(inputKey));
				  }
				  for(String keyz: tempMap.keySet()){
					 pw.print(keyz + " ");
					 pw.println(map.get(keyz));
				  }
				  pw.close();

				  System.out.println("Task Removed");
			   }
			   catch(IOException ex){} 
			}; 


			nameClockInB.setOnAction(handlerClockIn);
			nameClockOutB.setOnAction(handlerClockOut);
			removeButton.setOnAction(handlerRemove);

			Label lab = new Label(key);
			lab.setMinWidth(150);
			lab.setMinHeight(25);

			if(key != ""){
			   pane.add(nameClockInB, 1, y);
			   pane.add(nameClockOutB, 2, y);
			   pane.add(lab, 0,y);
			   pane.add(removeButton,3,y);

			   y++;
			}
		 }
	  }
	  PrintWriter pw = new PrintWriter(new FileOutputStream(myFile, false));
	  for(String key:map.keySet()){
		 tempMap.put(key, map.get(key));
	  } 
	  for(String key: tempMap.keySet()){
		 pw.print(key + " ");
		 pw.println(map.get(key));
	  }
	  pw.close();

   }



}
