package com.example.edpfx;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Server {
    static String[] modules = new String[5];
    static Map<String, Lecture[]> schedule = new HashMap<>();//This is null why?

    static HashMap<Integer, String> days = new HashMap<>();
    static HashMap<Integer, String> reverse_intervals = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1054);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Server is running and waiting for clients...");

            days.put(0, "Monday");
            days.put(1, "Tuesday");
            days.put(2, "Wednesday");
            days.put(3, "Thursday");
            days.put(4, "Friday");
            reverse_intervals.put(0, "9:00-10:00");
            reverse_intervals.put(1, "10:00-11:00");
            reverse_intervals.put(2, "11:00-12:00");
            reverse_intervals.put(3, "12:00-13:00");
            reverse_intervals.put(4, "13:00-14:00");
            reverse_intervals.put(5, "14:00-15:00");
            reverse_intervals.put(6, "15:00-16:00");
            reverse_intervals.put(7, "16:00-17:00");
            reverse_intervals.put(8, "17:00-18:00");


            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    System.out.println("Client connected!");
                    createMap(schedule);

                    handleMessages(socket, in, scanner); // Having this here is causing an error
                    //A message is processed upon reception, however once processed if no new message is available an error is thrown
                    //Be careful if edititng here -Adam 2025 library floor 2
                    //TODO FIX
                } catch (IOException e) {
                    System.err.println("Error handling client connection: " + e.getMessage());
                    e.printStackTrace(); // Print the full stack trace to the console
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    static void handleMessages(Socket socket, BufferedReader in, Scanner scanner) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String message;
        try {
            while ((message = in.readLine()) != null) { // Read messages until client disconnects
                if (message.length() > 0) {

                    String request = message.substring(0, 1);

                    String content = message.substring(1);
                    String[] info = content.split("/");
                    String day;
                    String time;
                    String room;
                    String code;


                    switch (request) {
                        case ("A"):
                            // String message = day + "/" + start + "/" + room + "/" + code + "\n";

                            day = info[0].toLowerCase();
                            time = info[1];
                            room = info[2];
                            code = info[3].toUpperCase();
                            //TODO FIX THIS
                            //time is a string interval but we cant parse 9:00 -10:00 to an integer
                            //TODO REMOVE THIS
                            System.out.println("Request received to schedule a lecture on " + day + "\n" + "In room: " + room + "\n" + "For module: " + code + "\nTime: " + time);
                            //TODO WE ARE RETURNING THE RESULT OF ATTEMPT SCHEDULE TO OUR CLIENT BUT WE ARENT ACTUALLY DOING ANYTHING WITH IT
                            out.println(attemptSchedule(Integer.parseInt(time), day, room, code)); //time, day, room, module

                            break;
                        case ("R"):
                            day = info[0];
                            time = info[1];

                            System.out.println("Request received to remove lecture on " + day + " " + time);
                            out.println(removelecture(day, Integer.parseInt(time)));
                            break;
                        case ("V"):
                            System.out.println("Request received to view schedule");
                            viewSchedule(socket);
                            break;
                        case ("O"):
                            System.out.println("Request received to show options");
                            try {
                                throw new IncorrectActionException();
                            } catch (IncorrectActionException e) {
                                out.println(e.getMessage());
                            }
                            break;
                        case ("S"):
                            System.out.println("Request received to quit");
                            out.println("TERMINATE");
                            try {
                                socket.close();
                                System.out.println("Client disconnected");
                            } catch (IOException e) {
                                System.err.println("Error closing socket: " + e.getMessage());

                            }

                            break;
                        default:
                            System.out.println(message);
                    }
                }
            }

        } catch (Exception e) {

        } //TODO FIX
    }

    //TODO FIX THIS BECAUSE WHY IS TIME AN INT
    public static String attemptSchedule(int time, String day, String room, String module) {
        if (!checkSchedule(time, day, room)) {
            return "There is already a lecture scheduled for this time and date";
        } else if (!checkModules(module)) {
            return "Can only create five modules";
        } else {
            //lecture time will be indexed according to the time eg. 9:00 -> 0
            Lecture lecture = new Lecture(room, module);
            schedule.get(day)[time] = lecture;
            System.out.println("the lecture" + schedule.get(day)[time]);
            return "Lecture has been scheduled for " + days.get(Integer.parseInt(day)) + " " + reverse_intervals.get(time);
        }
    }

    public static String removelecture(String day, int time) {
        if (schedule.get(day)[time] != null) {
//            System.out.println("I work");
//            System.out.println(schedule.get(day)[time]);
            schedule.get(day)[time] = null;
            return "Removed lecture at " + days.get(Integer.parseInt(day)) + " " + reverse_intervals.get(time);
        }
        //TODO WHEN SCHEDULING A LECTURE THEN REMOVING ONE SOMETHING GOES WRONG or vice versa
        return "No lecture scheduled for this time"; //Throw incorrect action exception here maybe??
    }

    public static void viewSchedule(Socket socket) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //FOR LOOP TO FORMAT THE OUTPUT
        for (int i = 0; i < schedule.size(); i++) {
            String index = String.valueOf(i);
            String message = "";
            for (int j = 0; j < schedule.get(index).length; j++) {
                if (schedule.get(index)[j] != null) {
                    message += schedule.get(index)[j] + "/";
                } else {
                    message += " /";
                }
            }
            System.out.println(message);
            out.println(message);
        }
        /*catch (IOException e) {
            System.err.println("Error sending schedule: " + e.getMessage());
            e.printStackTrace();
        }*/
    }

    public static boolean checkSchedule(int time, String day, String room) {
        //check if the time of the lecture doesn't clash with another lecture
        if (schedule.get(day)[time] != null  || schedule.get(day)[time].getRoom().equals(room)) {
            return false;
        }
        return true;
    }

    public static boolean checkModules(String module) {
        for (int i = 0; i < 5; i++) { //why doesnt this work
            if (module.equals(modules[i])) {
                return true;
            } else if (modules[i] == null) {
                modules[i] = module;
                return true;
            }
        }
        return false;
    }

    public static void createMap(Map<String, Lecture[]> map) {
        map.put("0", new Lecture[9]); //We initialise an array for each day along with the 9 possible lecture intervals
        map.put("1", new Lecture[9]);
        map.put("2", new Lecture[9]);
        map.put("3", new Lecture[9]);
        map.put("4", new Lecture[9]);
        /*
        ok so the error seems to be the following,  hashmap is of type string, lecture
         the hashmap is correctly initialised along with all of our arrays however the arrays are
        only allocated size 9 and not occupied therefore they contain null values,
        therefore when i attempt to index the hashmap to access elements in the array a
        nullpointerexception is thrown because we are trying to access a null value

        We need to populate the values in our arrays with default values
         */
    }
}