package app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            usage();
            return;
        }

        String filename = args[0];
        ArrayList< ArrayList<String>> rowToValues = readSourceFile(filename);
        int[] conflictMarkers = new int[rowToValues.size() + 1]; // add 1 for headers
        writeDuplicatesToCsv(rowToValues, conflictMarkers);
    }

    private static void writeDuplicatesToCsv(ArrayList<ArrayList<String>> rowToValues, int[] conflictMarkers) {
        System.out.println("Processing " + rowToValues.size() + " rows");
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.tmp.csv"));
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Column A", "Column B", "Column C"));

            int position = 0;
            for (ArrayList<String> row : rowToValues) {

                String columnOne = row.get(0);

                String columnTwo = "";
                boolean isFirst = true;
                for (int i = 1; i < row.size(); ++i) {
                    if (isFirst) {
                        columnTwo += row.get(i);
                        isFirst = false;
                    } else {
                        columnTwo += " | " + row.get(i);
                    }
                }

                int columnThree = conflictMarkers[position];

                printer.printRecord(columnOne, columnTwo, columnThree);
                position++;
            }

            printer.flush();


        } catch (Exception e) {
            e.printStackTrace();
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
                    String[] tokens = columnTwo.split("\\|");
                    for (String token : tokens) {
                        values.add(token.trim());
                    }
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

    public enum Headers {
        PN, ALT_PN
    }
}
