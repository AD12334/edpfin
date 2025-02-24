package com.example.edpfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.Socket;
import java.net.UnknownHostException;
import java.time.DayOfWeek;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class Main extends Application {
    Scene scene;
    static Client client;


    @Override

    public void start(Stage stage) {
        try {

            stage.setWidth(500);
            stage.setHeight(500);
            stage.setTitle("EDP_PROJECT");
            //var label = new Label("Hello, JavaFX");
            GridPane pane = new GridPane();
            pane.getChildren().add(new Label("Hello World!"));
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/finishedproject/img_6.png"))));

            //**************************************Button 1**********************************************
            Button button1 = new Button("Schedule lecture");
            button1.setMinWidth(300);
            button1.prefHeightProperty().bind(pane.widthProperty().divide(5));//Binding the width and height of each button to our Frame
            button1.prefWidthProperty().bind(pane.widthProperty());
            button1.setFont(Font.font("Comic Sans", FontWeight.BOLD, 30));
            button1.setStyle("-fx-background-color: #00FF36; ");
            button1.setOnAction(e -> schedulelecture(stage, client));
            pane.add(button1, 0, 0);
            //**************************************Button 2**********************************************
            Text text = new Text("Cancel lecture");
            Button button2 = new Button(text.getText());
            button2.setMinWidth(300);
            button2.setOnAction(e -> removeLecture(stage, client));
            button2.prefHeightProperty().bind(pane.heightProperty().divide(5));
            button2.prefWidthProperty().bind(pane.widthProperty());
            button2.setFont(Font.font("Comic Sans", FontWeight.BOLD, 30));
            pane.add(button2, 0, 1);
            //**************************************Button 3**********************************************
            Image image3 = new Image(Objects.requireNonNull(getClass().getResource("/com/example/finishedproject/img_2.png")).toExternalForm());
            ImageView view3 = new ImageView(image3);
            view3.setFitWidth(300);
            view3.setFitHeight(300);
            view3.setPreserveRatio(true);
            Button button3 = new Button("", view3);
            button3.setMinWidth(300);
            button3.setOnAction(e -> {
                viewSchedule(stage, client);
            });
            button3.prefHeightProperty().bind(pane.heightProperty().divide(5));
            button3.prefWidthProperty().bind(pane.widthProperty());
            button3.setStyle("-fx-background-color: #044489; ");
            pane.add(button3, 0, 2);
            //**************************************Button 4**********************************************
            Image image4 = new Image(Objects.requireNonNull(getClass().getResource("/com/example/finishedproject/img_1.png")).toExternalForm());
            ImageView view4 = new ImageView(image4);
            view4.setFitWidth(300);
            view4.setFitHeight(100);
            view4.setPreserveRatio(true);
            Button button4 = new Button("", view4);
            button4.setMinWidth(300);
            button4.setOnAction(e -> options(stage, client));
            button4.prefHeightProperty().bind(pane.heightProperty().divide(5));
            button4.prefWidthProperty().bind(pane.widthProperty());
            button4.setStyle("-fx-background-color: #ffffff; ");
            pane.add(button4, 0, 3);
            //**************************************Button 5**********************************************
            Image image5 = new Image(Objects.requireNonNull(getClass().getResource("/com/example/finishedproject/img.png")).toExternalForm());
            ImageView view5 = new ImageView(image5);
            view5.setFitWidth(100);
            view5.setPreserveRatio(true);
            Button button5 = new Button("", view5);
            button5.setStyle("-fx-background-color: #ff0000; ");//BIG RED EXIT BUTTON
            button5.setMinWidth(300);
            //System.out.println("Ending communication");
            button5.prefWidthProperty().bind(pane.widthProperty());
            button5.prefHeightProperty().bind(pane.heightProperty().divide(5));
            button5.setOnAction(e -> {
                try {
                    stop();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            pane.add(button5, 0, 4);
            //**************************************Button 5**********************************************
            scene = new Scene(pane, 500, 500);

            stage.setMinWidth(500);
            stage.setMinHeight(500);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Unknown Host");
            e.printStackTrace();
        }
    }


    //stage.setScene(scene);
    //stage.show();
    private void schedulelecture(Stage stage, Client client) {
        stage.setHeight(240);
        stage.setWidth(450);

        GridPane LecturePane = new GridPane();
        LecturePane.setAlignment(Pos.CENTER);
        //*******************DAY********************
        ComboBox day = new ComboBox<>();
        day.setPromptText("What day is the Lecture");

        day.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");


        day.setStyle("-fx-background-color: #787e7d; -fx-border-width: 3px; -fx-border-color: black; -fx-border-style: solid;");
//            day.setAlignment(Pos.CENTER);
        day.setMinWidth(300);
        day.prefHeightProperty().bind(LecturePane.widthProperty());
        day.prefHeightProperty().bind(LecturePane.heightProperty().divide(5));


//
        LecturePane.add(day, 20, 0);
        //*******************TIME********************
        ComboBox<String> time = new ComboBox<>();
        time.setPromptText("What time is the Lecture");
        time.getItems().addAll("9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00");
//            time.setOnKeyPressed(e->time.clear());
        time.setDisable(true);
//            time.setFont(Font.font("Comic Sans",FontWeight.BOLD,10));
        time.setStyle("-fx-background-color: #787e7d; -fx-border-width: 3px; -fx-border-color: black; -fx-border-style: solid;");
        time.setMinWidth(300);
        time.prefHeightProperty().bind(LecturePane.heightProperty());
        time.prefHeightProperty().bind(LecturePane.heightProperty().divide(5));
        day.setOnAction(e -> time.setDisable(false));

//
        LecturePane.add(time, 20, 1);
        //*******************ROOM********************
        TextField room = new TextField("What room is the lecture?");
        room.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));

        room.setDisable(true);
        room.setStyle("-fx-background-color: #787e7d; -fx-border-width: 3px; -fx-border-color: black; -fx-border-style: solid;");
        room.setOnKeyPressed(e -> room.clear());
        room.setMinWidth(300);
        room.prefHeightProperty().bind(LecturePane.heightProperty());
        room.prefHeightProperty().bind(LecturePane.heightProperty().divide(5));
        time.setOnAction(e -> room.setDisable(false));

        room.setOnKeyPressed(e -> {
            if (room.getText().equals("What room is the lecture?")) {
                room.clear();
            }
        });
        room.setOnMouseExited(e -> {
            if (room.getText().equals("")) {
                room.setText("What room is the lecture?");
            }
        });
        LecturePane.add(room, 20, 2);
        //*******************MODULE********************
        TextField module = new TextField("What module is the lecture for?");
        module.setDisable(true);
        module.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        module.setStyle("-fx-background-color: #787e7d; -fx-border-width: 3px; -fx-border-color: black; -fx-border-style: solid;");


        module.setOnKeyPressed(e -> {
            if (module.getText().equals("What module is the lecture for?")) {
                module.clear();
            }

        });
        module.setOnMouseExited(e -> {
            if (module.getText().equals("")) {
                module.setText("What module is the lecture for?");
            }
        });
        module.setMinWidth(300);
        module.prefHeightProperty().bind(LecturePane.heightProperty());
        module.prefHeightProperty().bind(LecturePane.heightProperty().divide(5));

        LecturePane.add(module, 20, 3);
        //*******************MENU BUTTON********************
        Button exit = new Button("MENU");
        exit.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        exit.setMinWidth(300);

        LecturePane.add(exit, 20, 6);

        exit.setOnAction(e -> start(stage));
        //*******************SUBMIT BUTTON********************
        Button submit = new Button("SUBMIT");
        submit.setDisable(true);
        submit.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        submit.setMinWidth(300);
        LecturePane.add(submit, 20, 5);
        //*******************CONTROLLERS********************

        //Confirmation textfield

        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        textField.setAlignment(Pos.CENTER);
        textField.setMinWidth(300);
        LecturePane.add(textField, 20, 7);
        room.setOnAction(e -> {
            String choice = (String) room.getText();

            if (choice.length() > 6) {//CSG001
                textField.setText("Please enter a valid room (length <= 5)");
                room.clear();
                room.setText("What room is the lecture?");

            } else
                textField.clear();
            module.setDisable(false);
        });
        module.setOnAction(e -> {
            String choice = (String) module.getText();
            if (choice.length() > 6) {//MS4023
                textField.setText("Please enter a valid module (length <= 5)");
                module.clear();
                module.setText("What module is the lecture for ");
            } else {
                textField.clear();
                submit.setDisable(false);
            }
        });


        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String dow = (String) day.getValue();
                String dayOfWeek = String.valueOf(DayOfWeek.valueOf(dow.toUpperCase()).getValue() - 1);

                textField.setText(client.addLecture(dayOfWeek, time.getValue(), room.getText().toUpperCase(), module.getText()));
            }
        });


        //*******************SCENE SETUP********************


        StackPane stack = new StackPane();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/finishedproject/img_4.png")).toExternalForm());


        ImageView imageView = new ImageView(image);


        imageView.fitWidthProperty().bind(LecturePane.widthProperty());
        imageView.fitHeightProperty().bind(LecturePane.heightProperty());
        imageView.setPreserveRatio(false);

        stack.getChildren().add(imageView);

        stack.getChildren().add(LecturePane);


        Scene ScheduleScene = new Scene(stack, 450, 240);
        stage.setMinWidth(450);
        stage.setMinHeight(240);
        stage.getIcons().add(image);


        stage.setScene(ScheduleScene);
        stage.show();
    }

    private void removeLecture(Stage stage, Client client) {
        GridPane grid = new GridPane();
        stage.setWidth(450);
        stage.setHeight(300);


        ///Thinking we could have a dropdown box for the day
        ///Another dropdown box for the time
        ///A lecture information section?
        ///AND A CONFIRM BUTTON
        grid.setAlignment(Pos.CENTER);
        //**************************DATE****************************
        ComboBox date = new ComboBox();
        date.setPromptText("Select  lecture date");
        date.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        date.prefHeightProperty().bind(grid.heightProperty());
        date.prefHeightProperty().bind(grid.heightProperty().divide(5));
        date.setMinWidth(300);
        grid.add(date, 0, 0);
        //**************************TIME****************************
        ComboBox time = new ComboBox();
        time.setPromptText("Select  lecture time");
        time.getItems().addAll("9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00");

        time.prefHeightProperty().bind(grid.heightProperty());
        time.prefHeightProperty().bind(grid.heightProperty().divide(5));
        time.setMinWidth(300);
        time.setDisable(true);//Might make it so that time is only shown when the date has been selected first
        grid.add(time, 0, 1);
        //**************************LECTURE INFO****************************
        TextField info = new TextField();
        info.prefHeightProperty().bind(grid.heightProperty());
        info.prefHeightProperty().bind(grid.heightProperty().divide(5));
        info.setDisable(true);

        info.setMinWidth(300);
        grid.add(info, 0, 2);

        //**************************BACKGROUND****************************
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/finishedproject/img_4.png")).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(grid.widthProperty());
        imageView.fitHeightProperty().bind(grid.heightProperty());
        imageView.setPreserveRatio(false);
        //**************************SUBMIT BUTTON****************************
        Button submit = new Button("SUBMIT");
        submit.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        submit.setDisable(true);
        submit.prefHeightProperty().bind(grid.heightProperty());
        submit.prefHeightProperty().bind(grid.heightProperty().divide(5));
        submit.setMinWidth(300);

        grid.add(submit, 0, 3);

        //**************************MENU BUTTON****************************
        Button menu = new Button("MENU");
        menu.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));

        menu.prefHeightProperty().bind(grid.heightProperty().divide(5));
        menu.setMinWidth(300);
        menu.setOnAction(e -> start(stage));

        grid.add(menu, 0, 4);
        //Confirmation textfield
        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        textField.setAlignment(Pos.CENTER);
        textField.setMinWidth(300);
        grid.add(textField, 0, 5);


        //**************************SCENE SETUP****************************
        StackPane stack = new StackPane();
        stack.getChildren().add(imageView);
        stack.getChildren().add(grid);
        //**************************CONTROLLERS****************************
        date.setOnAction(e -> {
            time.setDisable(false);
        });

        time.setOnAction(e -> {
            info.setDisable(false);
            info.setText("Date: " + date.getValue() + " " + "\nTime: " + time.getValue());
            submit.setDisable(false);
        });


        submit.setOnAction(e -> {
            String dow = (String) date.getValue();
            String dayOfWeek = String.valueOf(DayOfWeek.valueOf(dow.toUpperCase()).getValue() - 1);
            textField.setText(client.removelecture(dayOfWeek, (String) time.getValue()));
        });
        Scene remove_scene = new Scene(stack, 450, 300);
        stage.getIcons().add(image);

        stage.setScene(remove_scene);
        stage.show();
    }
    //**************************VIEW SCHEDULE****************************

    private void viewSchedule(Stage stage, Client client) {
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        String[] schedule = client.viewSchedule();

        String[][] schedule2D = new String[schedule.length][];
        for (int i = 0; i < schedule.length; i++) {
            //Our initial array was an arrray of strings each containing the lecture info for a given day
            //In order to interpret this info we are going to split each array using the delimiter /
            //We can put these split contents into another array which we store in another array for each day (array of days which is an array of lecture info)
            // Split each string in the input array and store it in the corresponding row of the 2D array
            //WE had an array of just days
            //Now we have a 2d array for days and times
            schedule2D[i] = schedule[i].split("/");
        }
        LinkedHashMap<Integer, String> monday = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> tuesday = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> wednesday = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> thursday = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> friday = new LinkedHashMap<>();
        HashMap<String, String> colors = new HashMap<>();
        //For each entry in schedule store its values and its index
        String[] times = {"9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"};
        LinkedHashMap<Integer, String>[] maparr = new LinkedHashMap[]{monday, tuesday, wednesday, thursday, friday};//An array of hashmaps for each day
        for (int i = 0; i < maparr.length; i++) {
            for (int j = 0; j < times.length; j++) {
                maparr[i].put(j, schedule2D[i][j]); //Occupying the specific day within our array with the relevant info
                //j is our index time (i.e 9:00-10:00 = index 0)
                //schedule2D is our lecture info for that time
                //i accesses the day
                //j accesses the time

            }
        }
        for (int i = 0; i < maparr.length; i++) {
            System.out.println(maparr[i]);
        }
        String[] colorarr = {
                "-fx-text-fill: green",
                "-fx-text-fill: blue",
                "-fx-text-fill: red",
                "-fx-text-fill: orange",
                "-fx-text-fill: purple"
        };

        for (int i = 0; i < maparr.length; i++) {
            for (int j = 0; j < times.length; j++) {
                if (schedule2D[i][j].equals(" ")) {
                    continue;
                } else {
                    String[] temp = schedule2D[i][j].split(","); //IF THE SLOT HAS NO LECTURE THEN NO COLOR NEEDED
                    String module = temp[1];
                    if (!colors.containsKey(module)) {//IF THE MODULE ISNT ALREADY IN OUR HASHMAP
                        colors.put(module, colorarr[colors.size()]);
                        //PUT THE MODULE CODE AS KEY
                        //PUT THE COLOR AS THE VALUE
                        //WE CAN USE THE SIZE OF THE COLOR HASHMAP AS AN INDEX INTO OUR COLORS ARRAY
                    }
                }
            }
        }
        //WE NOW HAVE A HASHMAP THAT MAPS A MODULE TO A COLOR
        //SCHEDULE IS AN ARRAY WHERE THE KEY IS THE DAY (e.g. schedule[0] - monday stuff)
        //THE CONTENTS IN EACH KEY IS THE LECTURES SCHEDULED FOR THAT DAY
        //EACH LECTURE IS ADDED TO THE KEY IN ACCORDANCE TO ITS ORDER IN THE DAY
        //e.g. a tuesday with only one lecture will be at index 2 and will contain null values apart from one area

        stage.setWidth(900);
        stage.setHeight(500);

        GridPane grid = new GridPane();
        grid.minWidthProperty().bind(stage.widthProperty());
        grid.minHeightProperty().bind(stage.heightProperty());
        grid.maxHeightProperty().bind(stage.heightProperty());

        //**************************EXIT BUTTON****************************
        Button exit = new Button("EXIT");
        exit.setOnAction(e -> start(stage));
        exit.setAlignment(Pos.CENTER);
        exit.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        exit.minWidthProperty().bind(grid.widthProperty());
        exit.prefWidthProperty().bind(grid.widthProperty());
        GridPane.setColumnSpan(exit, 11);
        exit.prefHeightProperty().bind(stage.heightProperty().divide(25));
        exit.minHeightProperty().bind(stage.heightProperty().divide(25));
        grid.add(exit, 0, 0); // Column row

        //**************************TEXTFIELDS****************************
        VBox[] arr = new VBox[5];
        for (int i = 0; i < 5; i++) {
            VBox vbox = new VBox();
            vbox.minWidthProperty().bind(stage.widthProperty().divide(6));
            vbox.maxWidthProperty().bind(stage.widthProperty().divide(6));
            vbox.minHeightProperty().bind(stage.heightProperty());
            vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");

            arr[i] = vbox;
        }
        VBox vbox = new VBox();
        vbox.minWidthProperty().bind(stage.widthProperty().divide(6));
        vbox.maxWidthProperty().bind(stage.widthProperty().divide(6));
        vbox.minHeightProperty().bind(stage.heightProperty());
        vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-style: solid;");

        String[] titles = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};

        for (int i = 0; i < titles.length; i++) {
            TextField t = new TextField();
            t.setEditable(false);
            t.setText(titles[i]);
            t.setAlignment(Pos.CENTER);
            t.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));
            t.setStyle("-fx-background-color: transparent; -fx-border-color: BLACK");
            arr[i].getChildren().add(t);
        }


        // Time
        TextField Time = new TextField();
        Time.setText("TIME");
        Time.setAlignment(Pos.CENTER);
        Time.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));
        Time.setStyle("-fx-background-color: transparent; -fx-border-color: BLACK");
        vbox.getChildren().add(Time);

        // Lecture Times

        for (int i = 0; i < times.length; i++) {
            TextField t = new TextField();
            t.setAlignment(Pos.CENTER);
            t.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));
            t.setEditable(false);
            t.maxHeightProperty().bind(vbox.heightProperty().divide(10));
            t.minHeightProperty().bind(vbox.heightProperty().divide(10));
            t.setStyle("-fx-border-color: BLACK");
            t.setText(times[i]);
            vbox.getChildren().add(t);

        }


        // TextBoxes for v2 onwards
        for (int i = 0; i < 5; i++) { //5 days
            for (int j = 0; j < 9; j++) {//9 possible lecture slots
                TextField t = new TextField();
                t.setAlignment(Pos.CENTER);
                t.setEditable(false);
                t.maxHeightProperty().bind(vbox.heightProperty().divide(10));
                t.minHeightProperty().bind(vbox.heightProperty().divide(10));
                t.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));


                if (maparr[i].get(j).equals(" ")) {
                    t.setStyle("-fx-border-color: BLACK");
                } else {
                    String[] temp = maparr[i].get(j).split(",");
                    String module = temp[1];
                    t.setStyle("-fx-border-color: BLACK; " + colors.get(module));
                }


                t.setText(maparr[i].get(j)); //setting the text for each day
                arr[i].getChildren().add(t);
            }
        }
        // Adding the finished product
        grid.add(vbox, 0, 1);
        for (int i = 1; i < 6; i++) {
            grid.add(arr[i - 1], i, 1);
        }

        //**************************STAGE SETUP****************************
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true); // Ensure the width fits the stage
        scrollPane.setFitToHeight(true); // Ensure the height fits the stage
        scrollPane.minWidthProperty().bind(stage.widthProperty());
        scrollPane.minHeightProperty().bind(stage.heightProperty());
        scrollPane.maxWidthProperty().bind(stage.widthProperty());
        scrollPane.maxHeightProperty().bind(stage.heightProperty());

        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.show();
    }


    private void options(Stage stage, Client client) {
        stage.setHeight(500);
        stage.setWidth(800);
        GridPane grid = new GridPane();
        grid.maxHeightProperty().bind(stage.heightProperty());
        grid.maxWidthProperty().bind(stage.heightProperty());
        grid.prefHeightProperty().bind(stage.heightProperty());
        grid.prefWidthProperty().bind(stage.heightProperty());
        Button menu = new Button("MENU");
        menu.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        menu.prefHeightProperty().bind(grid.heightProperty().divide(20));
        menu.prefWidthProperty().bind(grid.widthProperty());

        menu.setAlignment(Pos.CENTER);
        menu.setOnAction(e -> start(stage));

        Button requestroom = new Button("REQUEST ROOM");
        requestroom.setFont(Font.font("Comic Sans", FontWeight.BOLD, 10));
        requestroom.prefHeightProperty().bind(grid.heightProperty().divide(20));
        requestroom.prefWidthProperty().bind(grid.widthProperty());
        requestroom.setAlignment(Pos.CENTER);


        TextField text = new TextField();
        text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 40));
        text.prefHeightProperty().bind(grid.heightProperty());
        text.prefWidthProperty().bind(grid.widthProperty());
        text.setStyle("-fx-border-color: BLACK");
        text.setEditable(false);
        text.setAlignment(Pos.CENTER);

        requestroom.setOnAction(e -> text.setText(client.Option()));


        grid.add(text, 0, 2);


        grid.add(menu, 0, 0);
        grid.add(requestroom, 0, 1);
        Scene scene = new Scene(grid);
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        stage.setScene(scene);

        stage.show();


    }

    public void stop() throws InterruptedException, IOException {
        String message = client.Exit();


        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.WARNING);

        // set content text
        a.setContentText(message + ": Communication with the server has concluded");

        // show the dialog
        a.show();


        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(3), e -> System.exit(0)));//Creates a keyframe that executes 3 seconds after being called
        delay.setCycleCount(1);//repeats once
        delay.play();//Calling the delay


    }

    public static void main(String[] args) {
        try {
            client = new Client(new Socket(InetAddress.getLocalHost(), 1054));
        } catch (Exception e) {
            System.out.println("WHOOPS");

        }
        launch();
    }
}
