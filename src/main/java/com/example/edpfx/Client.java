package com.example.edpfx;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * ServerApp:The server application will receive 4  types  of  requests ((1).Add a Lecture,(2).Remove a Lecture,(3).Display Schedule, (4). Others)
 * from client and will process those requests. Server  app  will
 * have a memory‐based data
 * collection(e.g.,ArrayList,HashMap,etc.) that stores one course(e.g.,LM051-2026)schedule by adding/removing
 * lectures for modules based on the requests from client
 */
public class Client {
    private Socket socket;
    static HashMap<String, Integer> intervals = new HashMap<>();

    public Client(Socket socket) {
        this.socket = socket;
        intervals.put("9:00-10:00", 0);
        intervals.put("10:00-11:00", 1);
        intervals.put("11:00-12:00", 2);
        intervals.put("12:00-13:00", 3);
        intervals.put("13:00-14:00", 4);
        intervals.put("14:00-15:00", 5);
        intervals.put("15:00-16:00", 6);
        intervals.put("16:00-17:00", 7);
        intervals.put("17:00-18:00", 8);
    }

    public String messageServer(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(message);
            return in.readLine();
        } catch (IOException e) {
            return "Error sending message to server";
        }
    }

    public String addLecture(String day, String time, String room, String code) {
        String message = "A" + day + "/" + intervals.get(time) + "/" + room + "/" + code + "\n";
        System.out.println(message);

        String response = messageServer(message);
        System.out.println(response);

        return response;
    }

    public String removelecture(String day, String time) {
        String message = "R" + day + "/" + intervals.get(time) + "\n";
        System.out.println(message);
        return messageServer(message);
    }

    public String[] viewSchedule() {
        String message = "V";
        try {
            System.out.println("Staring viewSchedule method");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("print Writer created");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("buffered reader created");

            out.println(message);
            System.out.println("message sent");
            String[] lectures = new String[5];
            for (int i = 0; i < 5; i++) {
                lectures[i] = in.readLine();
                System.out.println("Recieving messages" + lectures[i]);
            }
            System.out.println("Strings recieved");
            return lectures;
        } catch (IOException e) {
            return null;
        }
        //return null;
    }

    public String Option() {
        String message = "O\n";
        return messageServer(message);
    }

    public String Exit() throws IOException {
        String message = "S";
        System.out.println(message);
        String reply = messageServer(message);
        close();
        System.out.println("Client socket has been closed");
        return reply;
    }

    public void close() throws IOException {
        socket.close();
    }
}

