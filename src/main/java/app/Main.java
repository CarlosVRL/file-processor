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

        ItemFinder itemFinder =  new ItemFinder();

        String filename = args[0];
        ArrayList< ArrayList<String>> rowToValues = readSourceFile(filename);
        //int[] conflictMarkers = checkForConflicts(rowToValues);
        int[] duplicateMarkers = itemFinder.checkForDuplicates(rowToValues);
        writeDuplicatesToCsv(rowToValues, duplicateMarkers);
    }

    private static int[] checkForConflicts(ArrayList< ArrayList<String>> rowToValues) {
        int[] res = new int[rowToValues.size()];

        int i = 0;
        int conflictCounter = 1;
        for (ArrayList<String> row : rowToValues) {
            conflictCounter = checkLineItemsForConflicts(row, res, conflictCounter, i, rowToValues);
            i++;
        }

        return res;
    }

    private static int checkLineItemsForConflicts(ArrayList<String> row, int[] res, int conflictCounter, int rowIndex, ArrayList< ArrayList<String>> rowToValues) {
        boolean conflictDetected = false;

        // skip if the line has already been claimed as a duplicate
        if (res[rowIndex] != 0) {
            return conflictCounter;
        }

        // check the other line items to see if there's a match
        int i = 0;
        for (ArrayList<String> rowIter : rowToValues) {
            if (i == rowIndex) {
                // skip: the row cannot be a duplicate of itself
            } else if (res[i] != 0) {
                // a duplication has already been claimed
            } else {
                // check each array for conflicts

                for (String src : row) {
                    if (rowIter.contains(src)) {
                        // common element detected
                        conflictDetected = true;
                        res[i] = conflictCounter;
                    }
                }
            }

            i++;
        }

        if (conflictDetected) {
            res[rowIndex] = conflictCounter;
            conflictCounter++;
        }

        return conflictCounter;
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
                for (int i = 1 /* position 0 is Column A */; i < row.size(); ++i) {
                    if (isFirst) {
                        columnTwo += row.get(i);
                        isFirst = false;
                    } else {
                        columnTwo += " | " + row.get(i);
                    }
                }

                String columnThree = (conflictMarkers[position] != 0) ?
                        String.valueOf(conflictMarkers[position]) : "";

                printer.printRecord(columnOne, columnTwo, columnThree);
                position++;
            }

            printer.flush();
            printer.close();
            writer.close();

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
                String columnOne = record.get(Headers.PN).toLowerCase();
                values.add(columnOne);

                // Column Two may be empty, single, or multi-valued (|)
                String columnTwo = record.get(Headers.ALT_PN);
                if (!columnTwo.equals("")) {
                    String[] tokens = columnTwo.split("\\|");
                    for (String token : tokens) {
                        values.add(token.trim().toLowerCase());
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
