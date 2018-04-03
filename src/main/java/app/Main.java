package app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class Main {

    public enum Headers {
        PN, ALT_PN
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            usage();
            return;
        }

        String filename = args[0];

        ArrayList< ArrayList<String>> rowToValues = readSourceFile(filename);

        for (ArrayList<String> row : rowToValues) {
            System.out.println("Row Start");
            for (String value : row) {
                System.out.println(value);
            }
            System.out.println("Row End");
        }
    }

    private static ArrayList<ArrayList<String>> readSourceFile(String filename) {
        ArrayList<ArrayList<String>> rowToValues = new ArrayList<>();
        try {
            Reader in = new FileReader(filename);
            Iterable<CSVRecord> records =
                    CSVFormat.RFC4180
                            .withFirstRecordAsHeader()
                            .withHeader(Headers.class)
                            .parse(in);

            for (CSVRecord record : records) {
                // Initialize the row container
                ArrayList<String> values = new ArrayList<>();

                // Column One assumes (1) value
                String columnOne = record.get(Headers.PN);
                values.add(columnOne);

                // Column Two may be empty, single, or multi-valued (|)
                String columnTwo = record.get(Headers.ALT_PN);
                if (!columnTwo.equals("")) {
                    values.add(columnTwo);
                }

                // Add to the main array
                rowToValues.add(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowToValues;
    }

    private static void usage() {
        System.out.println("Usage: java -jar *.jar FILENAME");
    }
}
