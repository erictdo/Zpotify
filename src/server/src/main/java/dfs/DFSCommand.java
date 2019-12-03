package main.java.dfs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.*;
import com.google.gson.stream.*;




public class DFSCommand {
    DFS dfs;

    public DFSCommand() throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Specify port: ");
        int p = in .nextInt();
        System.out.println("Specify port to join: ");
        int portToJoin = in .nextInt();

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
            String[] result = line.split("\\s");

            if (result[0].equals("join") && result.length > 1) {
                if (result.length == 2) {
                    dfs.join("127.0.0.1", Integer.parseInt(result[1]));
                } else {
                    System.out.println("Must provide port to join");
                }
            } else if (result[0].equals("print")) {
                dfs.print();
            } else if (result[0].equals("ls")) {
                dfs.lists();
            } else if (result[0].equals("touch") && result.length == 2) {
                dfs.create(result[1]);
            } else if (result[0].equals("delete")) {
                if (result.length == 2) {
                    dfs.delete(result[1]);
                } else {
                    System.out.println("Must provide a file name");
                }
            } else if (result[0].equals("read")) {
                if (result.length == 3) {
                    try {
                        dfs.read(result[1], Integer.parseInt(result[2]));
                    } catch (NumberFormatException e) {
                        System.out.println("Error: 3rd argument must be a page number");
                    }
                } else {
                    System.out.println("Must provide file name and page number");
                }
            } else if (result[0].equals("append")) {
                if (result.length == 3) {
                    if (result[1].contains("music")) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        var chunks = getMusicJsonChunks(result[2], 50);

                        System.out.println("Adding pages to music.json...");
                        int i = 0;
                        for (var chunk: chunks) {
                            String jsonStr = null;
                            try {
                                jsonStr = gson.toJson(chunk);
                                dfs.append(result[1], jsonStr);

                                System.out.println(String.format("Creating page [%d/%d]", ++i, chunks.size()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("Done");
                    } else {
                        dfs.append(result[1], new RemoteInputFileStream(result[2])); // User must specify filename they want to append data to and filepath of the data to be appended
                    }
                } else {
                    System.out.println("Must provide filename to append to and filepath of data to be appended");
                }
            } else if (result[0].equals("duplicate")) {
                if (result.length == 3) {
                    if (result[1].contains("music")) {

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        var chunks = getMusicJsonChunks(result[2], 50);

                        System.out.println("Adding pages to music.json...");
                        int i = 0;
                        for (var chunk: chunks) {
                            String jsonStr = null;
                            try {
                                jsonStr = gson.toJson(chunk);
                                dfs.replicateFile(result[1], jsonStr);

                                System.out.println(String.format("Creating page [%d/%d]", ++i, chunks.size()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("Done");
                    } else if (result[1].contains("test")) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        BufferedReader br = new BufferedReader(new FileReader(new File(result[2])));
                        try {
                            StringBuilder sb = new StringBuilder();
                            String fileLine = br.readLine();

                            while (fileLine != null) {
                                sb.append(fileLine);
                                sb.append("\n");
                                fileLine = br.readLine();
                            }
                            System.out.println("Creating 3 replicas...");
                            dfs.replicateFile(result[1], sb.toString());
                        } finally {
                            br.close();
                        }
                    } else {
                        System.out.println("Try something else");
                    }
                }
            } else if (result[0].equals("move")) { //Rename
                if (result.length == 3) {
                    dfs.move(result[1], result[2]);
                } else {
                    System.out.println("Must provide a file name and its new name");
                }
            } else if (result[0].equals("search")) {
                if (result.length == 3) {
                    // 1st parameter = pageGuide
                    // 2nd parameter = search text
                    dfs.search(Long.parseLong(result[1]), result[2]);
                } else {
                    System.out.println("Must provide a song name");
                }
            } else if (result[0].equals("push")) {
                if (result.length == 3) {
                    try {
                        dfs.push(result[1], Long.parseLong(result[2]));
                    } catch (NumberFormatException e) {
                        System.out.println("Error: 3rd argument must be a page number");
                    }
                } else {
                    System.out.println("Must provide file name and page number");
                }
            }
            else if (result[0].equals("pull")) {
                if (result.length == 3) {
                    try {
                        dfs.pull(result[1], Long.parseLong((result[2])));
                    } catch (NumberFormatException e) {
                        System.out.println("Error: 3rd argument must be a page number");
                    }
                } else {
                    System.out.println("Must provide file name and page number");
                }
            } else if (result[0].equals("leave")) {
                dfs.leave();
            }

            dfsMenu();
            line = buffer.readLine();
        }
    }

    public static List < JsonArray > getMusicJsonChunks(String filename, int numChunks) throws IOException {
        if (numChunks <= 0) {
            throw new NumberFormatException("Number of chunks must at least be 1.");
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray songArray = gson.fromJson(new FileReader(filename), JsonArray.class);

        ArrayList < JsonArray > songArrayChunks = new ArrayList < > (); // holds all the "tenths" of the music json
        JsonArray temp = null; // a "tenth" of the array

        // Split music.json
        for (int i = 0; i < songArray.size(); i++) {
            if (i % (int)(songArray.size() / numChunks) == 0) {
                temp = new JsonArray();
                songArrayChunks.add(temp);
            }

            if (temp != null) {
                temp.add(songArray.get(i));
            }
        }

        return songArrayChunks;
    }

    private void dfsMenu() {
        System.out.println("DFS Command Menu");
        System.out.println(" ls");
        System.out.println(" touch");
        System.out.println(" delete");
        System.out.println(" search");
        System.out.println(" read");
        System.out.println(" append");
        System.out.println(" duplicate");
        System.out.println(" move");
        System.out.println(" quit (to quit DFS Command and start server)");
        System.out.print("Enter a command: ");

    }

    static public void main(String args[]) throws Exception {
        Gson gson = new Gson();
        //        RemoteInputFileStream in = new RemoteInputFileStream("music.json", false);
        //        in.connect();
        //        Reader targetReader = new InputStreamReader(in);
        //        JsonReader jreader = new  JsonReader(targetReader);
        //        Music[] music = gson.fromJson(jreader, Music[].class);
        //
        //        if (args.length < 1 ) {
        //            throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
        //        }
        //        if (args.length > 1 ) {
        //            DFSCommand dfsCommand = new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        //        }
        //        else
        //        {
        //            DFSCommand dfsCommand = new DFSCommand( Integer.parseInt(args[0]), 0);
        //        }
        try {
            new DFSCommand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}