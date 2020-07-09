package com.adhoc.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileHelper {
    static String PathToQueries = "queries.txt";
    public static HashMap<Integer, String> readQueriesReturnDict(String path) throws IOException {

        HashMap<Integer, String> queriesDict = new HashMap<Integer, String>();
        if(path == null || path.isEmpty()) {
            path = PathToQueries;
        }

        File queryFile = new File(path);
        if(queryFile.exists()) {
            List<String> lines = Files.readAllLines(Paths.get(queryFile.toURI()));
            List<String> sentence = new ArrayList<>();
            for (String line: lines) {
                String[] lineSplit = line.split(" ");
                int key = Integer.valueOf(lineSplit[0]);
                for(int i = 1; i < lineSplit.length; i++) {
                    sentence.add(lineSplit[i]);
                }
                queriesDict.put(key, String.join(" ", sentence));
                sentence.clear();
            }
            return queriesDict;
        }
        return null;
    }

    public static void outputToFile(List<String> data) throws IOException {
        FileWriter outputFile = new FileWriter("output.txt");
        for(String line : data) {
            outputFile.write(line);
        }
        outputFile.close();

    }
}
