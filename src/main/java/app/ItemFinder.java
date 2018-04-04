package app;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ItemFinder {

    private int conflictCounter;

    public ItemFinder() {
        this.conflictCounter = 1;
    }

    /**
     * Check the passed array and return an array of duplicate markers.
     *
     * @param rows
     * @return
     */
    public int[] checkForDuplicates(ArrayList<ArrayList<String>> rows) {
        int[] markers = new int[rows.size()];

        int index = 2;
        //for (ArrayList<String> row : rows) {
            markers = findDuplicatesByRow(rows.get(index), rows, markers, index);
            //index++;
        //}

        return markers;
    }

    /**
     * Find items matching the target row contents. Recursive search.
     *
     * @param row
     * @param rows
     * @param markers
     * @param rowIndex
     * @return
     */
    public int[] findDuplicatesByRow(ArrayList<String> row, ArrayList<ArrayList<String>> rows, int[] markers, int rowIndex) {

        // Return if the row is already marked
        if (markers[rowIndex] != 0) {
            System.err.println("auto-exit");
            return markers;
        }

        boolean duplicateDetected = false;

        // Initialize the rows containing items in common (first-level matching)
        List<Integer> unprocessedRowIndexes = new CopyOnWriteArrayList<>();
        unprocessedRowIndexes.add(rowIndex); // The row itself

        // Initialize the processed (matching list)
        List<Integer> processedRowIndexes = new ArrayList<>(); // empty

        // Get all the (other) rows with common elements, assuming they have not been "consumed"

        // Get all the (primary and alternate) items which match the search string,
        // and their corresponding indexes, and add any unique ones to
        // the list to be processed
//        List<String> alternatePartNumbersList = new ArrayList<>(/* all alt partNumbers matching the query */);
//
//        // Add the index of any matching the alternatePartNumbersList
//        for (String alternatePartNumber : alternatePartNumbersList) {
//            if (!unprocessedRowIndexes.contains(alternatePartNumber /* the row index */)
//             && !processedList.contains(alternatePartNumber /* the row index */) ) {
//                unprocessedRowIndexes.add(index);
//            }
//        }
        int iterIndex = 0;
        for (ArrayList<String> iterRow : rows) {
            System.err.println("Working on index : " + iterIndex);
            if (rowIndex == iterIndex) {
                // skips checking itself
                System.err.println("Skip self");
                iterIndex++;
                continue;
            } else if (markers[iterIndex] != 0) {
                // the row has already been marked
                System.err.println("Skip marked");
                iterIndex++;
                continue;
            } else if (rowContainsCommonElement(row, iterRow)
                       && !unprocessedRowIndexes.contains(iterIndex)
                       && !processedRowIndexes.contains(iterIndex)) {
                System.err.println("Add if common element and not redundant");
                unprocessedRowIndexes.add(iterIndex);
                markers[iterIndex] = conflictCounter;
                duplicateDetected = true;
            } else {
                System.err.println("The record was not matched or is redundant");
            }
            iterIndex++;
        }

        System.err.println("Found " + unprocessedRowIndexes.size() + " rows matching row index : " + rowIndex);
        for (int foundIndex : unprocessedRowIndexes) {
            System.out.println("Index : " + foundIndex);
        }

        // Since at least (1) duplicate was found, mark the selected row index
        if (duplicateDetected) {
            markers[rowIndex] = conflictCounter;
        }

        // Cycle through the unprocessed indexes, adding and removing rows until stable
        while (unprocessedRowIndexes.size() > 0) {

            // Primary loop is over the unprocessed item indexes
            for (int unprocessedRowIndex : unprocessedRowIndexes) {
                // Initialize derived contents
                List<String> rowItemList = rows.get(unprocessedRowIndex); // The list of items
                List<String> itemsDerivedFromRowItemList = new ArrayList<>(); // Initially empty

                // Loop over the items and add new indexes to the derived container,
                // and add to to derived class if match is found
                iterIndex = 0;
                for (ArrayList<String> iterRow : rows) {
                    if (markers[iterIndex] != 0) {
                        // the row has already been marked
                        iterIndex++;
                        continue;
                    } else if (rowContainsCommonElement(rowItemList, iterRow)
                            && !unprocessedRowIndexes.contains(iterIndex)
                            && !processedRowIndexes.contains(iterIndex)) {
                        unprocessedRowIndexes.add(iterIndex);
                        markers[iterIndex] = conflictCounter;
                    } else {
                        // not matched or redundant
                    }
                    iterIndex++;
                }

                if (!processedRowIndexes.contains(unprocessedRowIndex)) {
                    processedRowIndexes.add(unprocessedRowIndex);
                }

                unprocessedRowIndexes.remove((Object) unprocessedRowIndex);
            }
        }

        // Cycle through the unprocessed list until there are no more rows
//        while (unprocessedRowIndexes.size() > 0) {
//
//            // Primary loop is over the unprocessed item indexes
//            for (int unprocessedIndex : unprocessedRowIndexes) {
//                List<String> altList = new ArrayList<>(/* from the row */); // the list of alternate items
//                List<String> itemsDerivedFromAltList = new ArrayList<>(); // empty
//
//                // Loop over the "alternate" items
//                for (String alt : altList) {
//                    List<String> itemsDerivedFromAlt = new ArrayList<>(/* find items by contains (altItem value) */);
//                    for (String itemDerivedFromAlt : itemsDerivedFromAlt) {
//                        if (!itemsDerivedFromAltList.contains(itemDerivedFromAlt)) {
//                            itemsDerivedFromAltList.add(itemDerivedFromAlt);
//                        }
//                    }
//                    List<String> altItemValuesDerivedFromAltItem = new ArrayList<>(/* find altItem by Num contains (alt.getItemValue) */);
//                    for (String altItemValue : altItemValuesDerivedFromAltItem) {
//                        if (!unprocessedRowIndexes.contains(index) &&
//                            !processedList.contains(index)) {
//                            unprocessedRowIndexes.add(index);
//                        }
//                    }
//                }
//
//                for (String item : itemsDerivedFromAltList) {
//                    if (!unprocessedRowIndexes.contains(index) &&
//                        !processedList.contains(index)) {
//                        unprocessedRowIndexes.add(index);
//                    }
//                }
//
//                if (!processedList.contains(index)) {
//                    processedList.add(index);
//                }
//                unprocessedRowIndexes.remove(index);
//            }
//        }

        if (duplicateDetected) {
            conflictCounter++;
        }

        //return processedList;
        return markers;
    }

    private boolean rowContainsCommonElement(List<String> row, List<String> rowIter) {
        boolean hasCommonElement = false;
        for (String src : row) {
            System.err.println("Checking iterRow for for " + src);
            if (rowIter.contains(src)) {
                hasCommonElement = true;
                break;
            }
        }
        return hasCommonElement;
    }
}
