package com.example.edpfx;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    public Client(Socket socket) {
        this.socket = socket;
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
        String message = "A" + day + "/" + time + "/" + room + "/" + code + "\n";
        System.out.println(message);
        return messageServer(message);
    }

    public String removelecture(String day, String time) {
        String message = "R" + day + "/" + time + "\n";
        System.out.println(message);
        return messageServer(message);
    }

    public HashMap<String, Lecture[]> viewSchedule() {
        String message = "V\n";
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.println(message);
            HashMap<String, Lecture[]> schedule = (HashMap<String, Lecture[]>) in.readObject();
            return schedule;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String Option() {
        String message = "O\n";
        return message;
    }
}

