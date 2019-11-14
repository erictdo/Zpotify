//package main.java.dfs;

import java.io.*;
import java.util.Scanner;

import com.google.gson.*;
import com.google.gson.stream.*;

public class DFSCommand {
    DFS dfs;

    public DFSCommand() throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Specify port: ");
        int p = in.nextInt();
        System.out.println("Specify port to join: ");
        int portToJoin = in.nextInt();

        dfs = new DFS(p);

        if (portToJoin > 0) {
            System.out.println("Joining " + portToJoin);
            dfs.join("127.0.0.1", portToJoin);
        }

        dfsMenu();

        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String line = buffer.readLine();

        // User interface:
        // join, ls, touch, delete, read, tail, head, append, move
        while (!line.equals("quit")) {
            dfsMenu();

            String[] result = line.split("\\s");

            if (result[0].equals("join") && result.length > 1) {
                if (result.length == 2) {
                    dfs.join("127.0.0.1", Integer.parseInt(result[1]));
                } else {
                    System.out.println("Must provide port to join");
                }
            }
            if (result[0].equals("print")) {
                dfs.print();
            }
            if (result[0].equals("ls")) {
                dfs.lists();
            }
            if (result[0].equals("delete")) {
                if (result.length == 2) {
                    if (result[1].equals("music")) {
                        dfs.delete("music.json");
                    } else if (result[1].equals("user")) {
                        dfs.delete("user.json");
                    }
                } else {
                    System.out.println("Must provide a file name");
                }
            }
            if (result[0].equals("read")) {
                if (result.length == 3) {
                    try {
                        dfs.read(result[1], Integer.parseInt(result[2]));
                    } catch (NumberFormatException e) {
                        System.out.println("Error: 3rd argument must be a page number");
                    }
                } else {
                    System.out.println("Must provide file name and page number");
                }
            }
            if (result[0].equals("append")) {
                if (result.length == 3) {
                    dfs.append(result[1], new RemoteInputFileStream(result[2]));
                } else {
                    System.out.println("Must provide filename to append to and filepath of data to be appended");
                }
            }
            if (result[0].equals("move")) // Rename
            {
                if (result.length == 3) {
                    dfs.move(result[1], result[2]);
                } else {
                    System.out.println("Must provide a file name and its new name");
                }
            }

            if (result[0].equals("leave")) {
                dfs.leave();
            }

            dfsMenu();
            line = buffer.readLine();
        }
    }

    private void dfsMenu() {
        System.out.println("DFS Command Menu");
        System.out.println(" ls");
        System.out.println(" delete");
        System.out.println(" read");
        System.out.println(" append");
        System.out.println(" move");
        System.out.println(" quit (to quit DFS Command and start server)");
        System.out.print("Enter a command: ");

    }

    static public void main(String args[]) throws Exception {
        Gson gson = new Gson();
        // RemoteInputFileStream in = new RemoteInputFileStream("music.json", false);
        // in.connect();
        // Reader targetReader = new InputStreamReader(in);
        // JsonReader jreader = new JsonReader(targetReader);
        // Music[] music = gson.fromJson(jreader, Music[].class);
        //
        // if (args.length < 1 ) {
        // throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
        // }
        // if (args.length > 1 ) {
        // DFSCommand dfsCommand = new DFSCommand(Integer.parseInt(args[0]),
        // Integer.parseInt(args[1]));
        // }
        // else
        // {
        // DFSCommand dfsCommand = new DFSCommand( Integer.parseInt(args[0]), 0);
        // }
        try {
            new DFSCommand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
