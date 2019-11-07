package com.jsonparserassign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class App {
    

    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // reading path,prefix of Input file, prefix of Output file and file size limit
        String path = br.readLine();
        String prefixInput = br.readLine();
        String prefixOutput = br.readLine();
        int fileSize = Integer.parseInt(br.readLine());

        // Intializing GsonBuiler object
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        // Check if path given to us is valid or found in our system or not
        if (!checkPathExist(path)) {
            throw new Error("Path not Found");
        }

        File file = new File(path);
        // Getting all the files which are inside the directory
        File[] listFile = file.listFiles();
        ArrayList<String> fileName = new ArrayList<String>();
        for (File f : listFile) {
            if (f.isFile() && f.getName().startsWith(prefixInput)) {
                fileName.add(f.getName());
            }
        }

        // This is for functional requirement
        // suffix wise taking file
        Collections.sort(fileName);

        // Starting output file Count
        int fileCount = 0;

        // Opening Output file
        File outPut = openNewFile(path, prefixOutput, fileCount);

        for (int i = 0; i < fileName.size(); i++) {
            // Fetching data from the given file and put in a string variable
            // and also change the "//" to "\\" if working on windows
            String fileData = fetchFileData(path + "//" + fileName.get(i));

            // Parse the string into json using Gson library
            Strike strike = getDataFromObject(fileData);

            // For intial case when there is nothing in output file
            if (i == 0) {
                if (checkFileSize(outPut,getBytesFromList(strike.strikers) , fileSize)) {
                    // If limit is less than the inital length then throw error
                    throw new Error("File Size limit is veryLess");
                } else {
                    // Write to file
                    writeIntoFile(outPut, gson.toJson(strike));
                }
            } else {
                // fetching all the data from output file
                String fileD = fetchFileData(outPut.getAbsolutePath());
                Strike tempStrike = getDataFromObject(fileD);

                // Checking whether after appending will it cross the limit
                if (!checkFileSize(outPut, getBytesFromList(strike.strikers), fileSize)) {
                    tempStrike.strikers.addAll(strike.strikers);
                    writeIntoFile(outPut, gson.toJson(tempStrike));
                } else {
                    // If yes then create a new File and append into new File
                    ++fileCount;
                    outPut = openNewFile(path, prefixOutput, fileCount);
                    writeIntoFile(outPut, gson.toJson(strike));
                }
            }
        }
        br.close();
    }

    public static boolean checkPathExist(String path) {
        if (!Paths.get(path).toFile().isDirectory()) {
            return false;

        }
        return true;
    }

    public static String fetchFileData(String path) {
        File file = new File(path);
        String st = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                st += str;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return st;
    }

    public static Strike getDataFromObject(String fileData) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        // System.out.println(fileData);
        Strike student = gson.fromJson(fileData, Strike.class);
        // String jsonString = gson.toJson(student.strikers);
        return student;
    }

    public static File openNewFile(String path, String prefix, int lastCount) {
        return new File(path + "//" + prefix + "" + (++lastCount) + ".json");
    }

    public static boolean checkFileSize(File file, long appendLength, int limit) {
        //System.out.println(file.length() + "   "+appendLength);
        if (file.length() + appendLength > limit) {
            return true;
        } else {
            return false;
        }
    }

    public static void writeIntoFile(File file, String data) {
        System.out.println("Writing into File");
        FileWriter fw;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
      public static long getBytesFromList(List list) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(list);
		out.close();
		return baos.toByteArray().length;
	}
}

class Strike {
    public List<Strikers> strikers;

    public Strike() {
    }

    public Strike(List<Strikers> strikers) {
        this.strikers = strikers;
    }

}

class Strikers implements Serializable{
    public String name;
    public String club;

    public Strikers(String name, String club) {
        super();
        this.name = name;
        this.club = club;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }
}
