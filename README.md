# Recursive File Merger
Use this application to recursively mark collections of strings with duplicate items.

## Usage
```
java -jar app.jar FILE_NAME.csv
```

The parameter FILE_NAME is a CSV format file with two columns. The first column acts as a primary name, and the 2nd acts as a secondary name, which may be multi-valued.

The output will be a new CSV deposited in the same directory, with any indexes sequentially marked.
