package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvUtil {
    public static ArrayList<String[]> loadData(String filename, String delimiter) throws IOException {
        ArrayList<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(delimiter);
                data.add(fields);
            }
        } catch (IOException e) {
            throw e;
        }
        return data;
    }

    public static void saveData(ArrayList<String[]> data, String filename, String delimiter) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String[] fields : data) {
                String line = String.join(delimiter, fields);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
