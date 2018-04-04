package app;

import java.util.ArrayList;
import java.util.List;

public class ItemFinder {

    public ItemFinder() {

    }

    public List<Integer> findItems(String match) {
        List<Integer> unprocessedList = new ArrayList<>();
        List<Integer> processedList = new ArrayList<>();

        int index = 0; // the index of the row in the reference array

        // Get all the (primary and alternate) items which match the search string,
        // and their corresponding indexes, and add any unique ones to
        // the list to be processed
        unprocessedList = new ArrayList<>(/* partNum that matches the query */);
        List<String> alternatePartNumbersList = new ArrayList<>(/* all alt partNumbers matching the query */);

        // Add the index of any matching the alternatePartNumbersList
        for (String alternatePartNumber : alternatePartNumbersList) {
            if (!unprocessedList.contains(alternatePartNumber /* the row index */)
             && !processedList.contains(alternatePartNumber /* the row index */) ) {
                unprocessedList.add(index);
            }
        }

        // Cycle through the unprocessed list until there are no more rows
        while (unprocessedList.size() > 0) {

            // Primary loop is over the unprocessed item indexes
            for (int unprocessedIndex : unprocessedList) {
                List<String> altList = new ArrayList<>(/* from the row */); // the list of alternate items
                List<String> itemsDerivedFromAltList = new ArrayList<>(); // empty

                // Loop over the "alternate" items
                for (String alt : altList) {
                    List<String> itemsDerivedFromAlt = new ArrayList<>(/* find items by contains (altItem value) */);
                    for (String itemDerivedFromAlt : itemsDerivedFromAlt) {
                        if (!itemsDerivedFromAltList.contains(itemDerivedFromAlt)) {
                            itemsDerivedFromAltList.add(itemDerivedFromAlt);
                        }
                    }
                    List<String> altItemValuesDerivedFromAltItem = new ArrayList<>(/* find altItem by Num contains (alt.getItemValue) */);
                    for (String altItemValue : altItemValuesDerivedFromAltItem) {
                        if (!unprocessedList.contains(index) &&
                            !processedList.contains(index)) {
                            unprocessedList.add(index);
                        }
                    }
                }

                for (String item : itemsDerivedFromAltList) {
                    if (!unprocessedList.contains(index) &&
                        !processedList.contains(index)) {
                        unprocessedList.add(index);
                    }
                }

                if (!processedList.contains(index)) {
                    processedList.add(index);
                }
                unprocessedList.remove(index);
            }
        }

        return processedList;
    }
}
