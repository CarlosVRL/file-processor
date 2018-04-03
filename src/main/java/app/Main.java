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
        writeDuplicatesToCsv(rowToValues);
    }

    private static void writeDuplicatesToCsv(ArrayList<ArrayList<String>> rowToValues) {
        System.out.println("Processing " + rowToValues.size() + " rows");
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.tmp.csv"));
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Column A", "Column B", "Column C"));

            for (ArrayList<String> row : rowToValues) {

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

                printer.printRecord(row.get(0), columnTwo, "C");

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
                    values.add(columnTwo);
                    values.add("test");
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
