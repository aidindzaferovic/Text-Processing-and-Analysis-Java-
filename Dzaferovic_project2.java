import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
public class Dzaferovic_project2 {

private static HashSet<String> stopwords;
private static final String INPUT_FILE = "/Users/alice29.txt"; 
private static final String STOPWORDS_FILE = "/Users/stopwords.txt"; 
private static final String OUTPUT_FILE_TASK1 = "Task1_output.txt";
private static final String OUTPUT_FILE_TASK2 = "Task2_output.txt";

public static void main(String[] args) {
	try {
            stopwords = readStopwords(STOPWORDS_FILE);
            Object[] results = processText(INPUT_FILE);
            HashMap<String, Integer> numberOfWords = (HashMap<String, Integer>) results[0];
            long numberOfStopWords = (long) results[1];
            long numberOfPuncutation = (long) results[2];
            printWordFrequencies(numberOfWords, OUTPUT_FILE_TASK1);
            printWordFrequencies(numberOfWords, OUTPUT_FILE_TASK2);
            System.out.println("Size of the dataset (all tokens): " + sizeOfDataset(INPUT_FILE));
            System.out.println("Number of unique words: " + numberOfWords.size());
            System.out.println("Number of stopwords in the dataset: " + numberOfStopWords);
            System.out.println("Number of punctuation in the dataset: " + numberOfPuncutation);
          } catch (IOException e) {
            System.out.println("Error processing: " + e.getMessage());
     }
    }
    static HashSet<String> readStopwords(String filePath) throws IOException { 
    return Files.lines(Paths.get(filePath))
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toCollection(HashSet::new));
         }
    static Object[] processText(String filePath) throws IOException {
    HashMap<String, Integer> frequencies = new HashMap<>();
    long stopwordsCount = 0;
    long punctuationCount = 0;

    try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
        for (String line : lines.collect(Collectors.toList())) {
           for (String token : line.split("\\s+")) { 
              if (!token.isEmpty()) {
              String cleanedToken = token.replaceAll("\\p{Punct}", "");
              if (stopwords.contains(cleanedToken.toLowerCase())) {
                  stopwordsCount++;
                  } else if (!cleanedToken.isEmpty()) {
                    frequencies.merge(cleanedToken.toLowerCase(), 1, Integer::sum);
                    }
                     punctuationCount += token.length() - cleanedToken.length(); 
                  }
                 }
                }
               }
        return new Object[]{frequencies, stopwordsCount, punctuationCount};
    }
    static void printWordFrequencies(HashMap<String, Integer> frequencies, String outputFilePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
        frequencies.entrySet().stream()
             .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
             .forEach(e -> {
              try {
                  writer.write(e.getKey() + ": " + e.getValue() + System.lineSeparator());
                  } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                        }
                    });
        }
    } 
   static long sizeOfDataset(String filePath) throws IOException {
    return Files.lines(Paths.get(filePath))
    .flatMap(line -> Arrays.stream(line.split("\\s+")))
    .count();
    
    
}
}
