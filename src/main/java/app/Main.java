package app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            usage();
            return;
        }

        String filename = args[0];

        try {
            Reader in = new FileReader(filename);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            for (CSVRecord record : records) {
                String columnOne = record.get(0);
                String columnTwo = record.get(1);

                System.out.println(columnOne + " : " + columnTwo);
            }

            System.out.println("Args:");
            for (String arg : args) {
                System.out.println(arg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("Usage: java -jar *.jar FILENAME");
    }
}
