# Spam Email Detector

## Project Information:

**This project is a spam detector based on the Naive Bayes filtering algorithm. The application has two phases, training and testing.**

-   Upon launching, the user is prompted to specify two directories. Inside each directory, spam emails are stored in a `spam` subfolder, while non-spam (ham) emails are placed in a `ham` subfolder.
-   During the training phase, the application processes all emails in the `train/ham` and `train/spam` folders to build two word-frequency maps: `trainHamFreq` and `trainSpamFreq`, following the `Bag-of-Words` model. These maps track how many files in each category contain each word. Additionally, the program calculates a probability map for words appearing in spam emails using the `Naive Bayes theorem`.
-   In the testing phase, the application analyzes emails from the `test/ham` and `test/spam` folders. It evaluates each file word by word, using the ham and spam word probabilities to determine the likelihood that the file is spam, applying the `Naive Bayes filtering` algorithm.
-   After completing the training and testing phases, the application presents the results in a TableView. Each row includes the filename, the calculated spam probability, and the actual category (based on its directory). Summary statistics are displayed at the bottom, including the accuracy (percentage of correct predictions) and precision (ratio of correct spam predictions to total spam predictions).
-   To address the issue of zero probabilities in the `Naive Bayes` algorithm, the application incorporates `Laplace smoothing`. This technique helps improve both the accuracy and precision of the spam detector. Additionally, the application accounts for case sensitivity during the training and testing phases to further enhance prediction performance.

![ui](ui.PNG)

## How To Run:

1. Clone this repository into your local machine, [instruction](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
2. Recommend using IntelliJ IDEA to run the program, [instruction](https://www.jetbrains.com/idea/download/#section=windows)
3. Set up JavaFX environment in IntelliJ IDEA, [instruction for MacOS](https://www.jetbrains.com/help/idea/javafx.html), [instruction for Windows](https://youtu.be/Ope4icw6bVk)
4. Set up data folder structure: check _assignment1_data_modified.rar_ as an example

-   data
    -   train
        -   spam
        -   ham
    -   test
        -   spam
        -   ham

5. Run the program, choose the data folder that you want the program to train and test on

## Other Resources:

[1] [Naive Bayes spam filtering](https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering)

[2] [Bag-of-words model](https://en.wikipedia.org/wiki/Bag-of-words_model)

[3] [Laplace Smoothing](https://en.wikipedia.org/wiki/Additive_smoothing)
