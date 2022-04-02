package org.thinh;

import java.text.DecimalFormat;

public class TestFile {
    private String filename;
    private String actualClass;
    private String detectedClass;
    private int wordCount;
    private double spamProbability;

    public TestFile(String filename, String actualClass, String detectedClass, int wordCount, double spamProbability) {
        this.filename = filename;
        this.actualClass = actualClass;
        this.detectedClass = detectedClass;
        this.wordCount = wordCount;
        this.spamProbability = spamProbability;
    }

    public String getFilename() { return this.filename; }

    public String getActualClass() { return this.actualClass; }

    public String getDetectedClass() { return this.detectedClass; }

    public int getWordCount() { return this.wordCount; }

    public double getSpamProbability() { return this.spamProbability; }

    public String getSpamProbabilityRounded() {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability);
    }

    public void setFilename(String value) { this.filename = value; }

    public void setActualClass(String value) { this.actualClass = value; }

    public void setSpamProbability(double val) { this.spamProbability = val; }
}