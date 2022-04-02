package org.thinh;

import java.io.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.DecimalFormat;

public class SpamDetector {
    private static DecimalFormat df = new DecimalFormat("0.00000");
    private static int numTruePos, numFalsePos, numTrueNeg, numFalseNeg, numFiles;
    private static double accuracy, precision;
    private static File trainDir, testDir;
    private Map<String, Integer> trainSpamFreq, trainHamFreq;
    private Map<String, Double> trainSpamProb;
    private static ObservableList<TestFile> testedFiles;
    private static boolean flag = false; // if training phase fail, will not proceed testing phase

    public SpamDetector(File mainDir) {
        trainHamFreq = new TreeMap<>();
        trainSpamFreq = new TreeMap<>();
        trainSpamProb = new TreeMap<>();
        testedFiles = FXCollections.observableArrayList();
        numTruePos = 0;
        numFalsePos = 0;
        numTrueNeg = 0;
        numFalseNeg = 0;
        accuracy = 0.0;
        precision = 0.0;

        start(mainDir);
    }

    public void start(File mainDir) {
        System.out.println("Starting parsing the directory: " + mainDir.getAbsolutePath());
        File[] content = mainDir.listFiles(File::isDirectory);
        if (content.length == 0) {
            System.out.println("Main folder is empty!");
        } else {
            for (File subDir : content) {
                if (subDir.getName().equals("train"))
                    trainDir = subDir;
                if (subDir.getName().equals("test"))
                    testDir = subDir;
            }
            try {
                trainSpam(trainDir);
                if (!flag)
                    testSpam(testDir);
                summary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void summary() {
        accuracy = (double) (numTruePos + numTrueNeg) / numFiles;
        precision = (double) numTruePos / (numFalsePos + numTruePos);
    }

    private boolean isValidWord(String word) {
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters
        // otherwise returns false;
        return word.matches(allLetters);
    }

    private void countWord(File dir, Map<String, Integer> trainWordFreq) throws IOException {
        System.out.println("Starting parsing the directory: " + dir.getAbsolutePath());
        File[] content = dir.listFiles(File::isFile);
        if (content.length == 0) {
            System.err.println(dir.getName() + " folder is empty!");
            flag = true;
            return;
        }
        Set<String> words;
        for (File file : content) {
            words = new HashSet<>();
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()){
                String token = scanner.next();
                if (isValidWord(token)){
                    // examine case sensitive
                    words.add(token.toLowerCase());
                    //words.add(token);
                }
            }
            scanner.close();
            for (String word : words) {
                if (trainWordFreq.containsKey(word)) {
                    int previousCount = trainWordFreq.get(word);
                    trainWordFreq.put(word, previousCount + 1);
                } else {
                    trainWordFreq.put(word, 1);
                }
            }
            words.clear();
        }
    }

    private void trainSpam(File trainDir) throws IOException {
        File spamDir = null, hamDir = null;
        File[] content = null;
        try {
            content = trainDir.listFiles(File::isDirectory);
        } catch (NullPointerException e) {
            System.err.println("Invalid train directory!");
            flag = true;
            return;
        }
        System.out.println("Starting parsing the directory: " + trainDir.getAbsolutePath());
        if (content.length == 0) {
            System.err.println("Train folder is empty!");
            flag = true;
            return;
        }
        for (File subDir : content) {
            if (subDir.getName().equals("ham"))
                hamDir = subDir;
            if (subDir.getName().equals("spam"))
                spamDir = subDir;
        }
        int totalNumHamFiles = 0, totalNumSpamFiles = 0;
        try {
            totalNumHamFiles = hamDir.listFiles().length;
            totalNumSpamFiles = spamDir.listFiles().length;
        } catch (NullPointerException e) {
            System.err.println("Invalid spam and ham directories in training phase!");
            flag = true;
            return;
        }
        countWord(hamDir, trainHamFreq);
        countWord(spamDir, trainSpamFreq);
        Set<String> words = new HashSet<>();
        words.addAll(trainHamFreq.keySet());
        words.addAll(trainSpamFreq.keySet());
        for (String word : words) {
            trainSpamProb.put(word, 0.0);
        }
        // examine with overall probability
        //double overallProbHam = (double) totalNumHamFiles / (totalNumHamFiles + totalNumSpamFiles);
        //double overallProbSpam = (double) totalNumSpamFiles / (totalNumSpamFiles + totalNumHamFiles);
        for (String word : trainSpamProb.keySet()) {
            int numHamFiles = 0, numSpamFiles = 0;
            double probHam, probSpam, probWord;
            if (trainHamFreq.containsKey(word))
                numHamFiles = trainHamFreq.get(word);
            if (trainSpamFreq.containsKey(word))
                numSpamFiles = trainSpamFreq.get(word);
            // add 1 to numerator and add 2 to denominator to avoid 0 and 1 probability
            probHam = (double) (numHamFiles + 1) / (totalNumHamFiles + 2);
            probSpam = (double) (numSpamFiles + 1) / (totalNumSpamFiles + 2);
            // examine with overall probability
            probWord = probSpam / (probSpam + probHam);
            //probWord = (probSpam * overallProbSpam) / (probSpam * overallProbSpam + probHam * overallProbHam);
            trainSpamProb.put(word, probWord);
        }
    }

    private void testFile(File dir) throws IOException {
        System.out.println("Starting parsing the directory: " + dir.getAbsolutePath());
        File[] content = dir.listFiles(File::isFile);
        if (content.length == 0) {
            System.err.println(dir.getName() + " folder is empty!");
            return;
        }
        for (File file : content) {
            double n = 0.0;
            int wordCount = 0;
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()){
                String token = scanner.next();
                wordCount++;
                // examine case sensitive
                //if (trainSpamProb.containsKey(token.toLowerCase())) {
                //    n += Math.log(1 - trainSpamProb.get(token.toLowerCase())) - Math.log(trainSpamProb.get(token.toLowerCase()));
                //}
                if (trainSpamProb.containsKey(token)) {
                    n += Math.log(1 - trainSpamProb.get(token)) - Math.log(trainSpamProb.get(token));
                }
            }
            scanner.close();
            // examine spam probability threshold
            double spamProbability = 1 / (1 + Math.pow(Math.E, n));
            if (spamProbability >= 0.5 && dir.getName().equals("spam")) { numTruePos += 1; }
            if (spamProbability >= 0.5 && dir.getName().equals("ham")) { numFalsePos += 1; }
            if (spamProbability < 0.5 && dir.getName().equals("ham")) { numTrueNeg += 1; }
            if (spamProbability < 0.5 && dir.getName().equals("spam")) { numFalseNeg += 1; }
            String detectedClass = null;
            if (spamProbability >= 0.5) {
                detectedClass = "spam";
            } else {
                detectedClass = "ham";
            }
            testedFiles.add(new TestFile(file.getName(), dir.getName(), detectedClass, wordCount, spamProbability));
        }
    }

    public void testSpam(File testDir) throws IOException {
        File spamDir = null, hamDir = null;
        File[] content = null;
        try {
            content = testDir.listFiles(File::isDirectory);
        } catch (NullPointerException e) {
            System.err.println("Invalid test directory");
            return;
        }
        System.out.println("Starting parsing the directory: " + testDir.getAbsolutePath());
        if (content.length == 0) {
            System.err.println("Test folder is empty!");
            return;
        }
        for (File subDir : content) {
            if (subDir.getName().equals("ham"))
                hamDir = subDir;
            if (subDir.getName().equals("spam"))
                spamDir = subDir;
        }
        try {
            numFiles = hamDir.listFiles().length + spamDir.listFiles().length;
        } catch (NullPointerException e) {
            System.err.println("Invalid spam and ham directories in testing phase!");
            return;
        }
        testFile(hamDir);
        testFile(spamDir);
    }

    public static ObservableList<TestFile> getTestedFiles() {
        return testedFiles;
    }

    public static int getNumTruePos() { return numTruePos; }
    public static String getNumTruePosString() { return Integer.toString(numTruePos); }

    public static int getNumFalsePos() { return numFalsePos; }
    public static String getNumFalsePosString() { return Integer.toString(numFalsePos); }

    public static int getNumTrueNeg() { return numTrueNeg; }
    public static String getNumTrueNegString() { return Integer.toString(numTrueNeg); }

    public static int getNumFalseNeg() { return numFalseNeg; }
    public static String getNumFalseNegString() { return Integer.toString(numFalseNeg); }

    public static int getNumFiles() { return numFiles; }
    public static String getNumFilesString() { return Integer.toString(numFiles); }

    public static double getAccuracy() { return accuracy; }
    public static String getAccuracyRounded() { return df.format(accuracy); }

    public static double getPrecision() { return precision; }
    public static String getPrecisionRounded() { return df.format(precision); }
}
