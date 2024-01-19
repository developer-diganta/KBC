package gameAdmin;

import lifeLine.AudiencePoll;
import lifeLine.ExpertAdvice;
import lifeLine.FiftyFiftyLifeLine;
import question.Question;
import question.QuestionList;
import user.User;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class GameRunner implements QuestionAdd {
    private final QuestionList questions;
    private final User user;

    private HashMap<String, Boolean> lifeLines;
    Scanner sc = new Scanner(System.in);
    public GameRunner() {
        questions = new QuestionList();
        user = new User();
        lifeLines = new HashMap<>();
        lifeLines.put("Audience Poll", true);
        lifeLines.put("50:50", true);
        lifeLines.put("Expert Advice", true);
    }

    private void acceptAnswer(Question question,int counter){

        System.out.print("Enter your answer: ");
        String userAnswer = sc.nextLine();
        if (question.getAnswer().equalsIgnoreCase(userAnswer)) {
            System.out.println("Correct Answer!!");
            user.addEarnings(PrizeMoney.getByIndex(counter).getAmount());
            System.out.println("Your current prize money is: " + user.getEarnings());
        } else {
            System.out.println("Sorry! The correct answer was " + question.getAnswer());
            System.out.println("Thank You for playing Kaun Banega Crorepati! You have earned a total of Rs." + user.getEarnings());
            return;
        }
    }

    private void loadQuestionsFromFile() {
        Path path = Paths.get("kbc_questions.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            String questionSentence = "";
            ArrayList<String> options = new ArrayList<>();
            String fileAnswer = "";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Q:")) {
                    questionSentence = line.substring(2);
                } else if (line.startsWith("Option")) {
                    options.add(line.substring(9));
                } else if (line.startsWith("CA:")) {
                    fileAnswer = line.substring(3);
                    Question question = new Question(questionSentence, options, fileAnswer);
                    questions.addQuestionToQuestionList(question);
                    questionSentence = "";
                    options = new ArrayList<>();
                    fileAnswer = "";
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addQuestions() {

        int noOfQuestions = 1;
        System.out.println("Welcome To Game Admin Section. Please add 12 questions to be featured in the game");
        System.out.println("Want to load questions from file?(Y/N)");
        String choice = sc.nextLine();
        if(Objects.equals(choice, "Y") || Objects.equals(choice, "y")) {
          loadQuestionsFromFile();
          return;
        }
        while (noOfQuestions > 0) {
            noOfQuestions--;
            String singleQuestion;
            Question question;
            System.out.println("Enter Question:- ");
            singleQuestion = sc.nextLine();

            System.out.println("Enter the options: ");
            ArrayList<String> options = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter option " + (i + 1) + ": ");
                options.add(sc.nextLine());
            }

            System.out.print("Enter Answer: ");
            String answer = sc.nextLine();
            Path path = Paths.get("kbc_questions.txt");
            question = new Question(singleQuestion, options, answer,path);
            questions.addQuestionToQuestionList(question);
        }
    }

    public void showQuestion(Question question) {
        System.out.println("Question: " + question.getQuestion());
        System.out.println("Options:");
        question.getOptions().forEach(System.out::println);
        System.out.println("Choose (A) Answer | (L) Life Line | (Q) Quit");
    }

    public void useLifeLine(String chosenLifeLine, Question question){
        if(lifeLines.get(chosenLifeLine)){
            System.out.println(chosenLifeLine);
            if(Objects.equals(chosenLifeLine, "Audience Poll")){
                HashMap<String,Integer> audiencePollResults = new AudiencePoll().useLifeLine(question);
                audiencePollResults.forEach((option, poll) -> System.out.println(option + "->" + poll));
                lifeLines.put("Audience Poll",false);
            }else if(Objects.equals(chosenLifeLine, "50:50")){
                ArrayList<String> fiftyFiftyOptions = new FiftyFiftyLifeLine().useLifeLine(question);
                for(var x:fiftyFiftyOptions){
                    System.out.println(x);
                }
                lifeLines.put("50:50",false);
            }else if(Objects.equals(chosenLifeLine, "Expert Advice")){
                String expertAnswer = new ExpertAdvice().useLifeLine(question);
                System.out.println("Hmm! I think the correct answer is " + expertAnswer);
                lifeLines.put("Expert Advice",false);
            }
        }else{
            System.out.println("Wrong Choice of Life Line");
        }
    }

    public void startGame() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        user.setName(sc.nextLine());
        System.out.println("Welcome to KBC, " + user.getName());
        System.out.println("Let's begin playing KBC!");
        int counter = 0;
        for (Question question : questions.getQuestionList()) {
            showQuestion(question);
            System.out.println("QQQQ"+question.getAnswer());
            char response = sc.next().charAt(0);

            switch (response) {
                case 'A':
                    acceptAnswer(question,counter);
                    counter++;
                    break;
                case 'L':
                    System.out.println("Available LifeLines:");
                    for(var i:lifeLines.keySet()){
                        if(lifeLines.get(i)){
                            System.out.println(i);
                        }
                    }
                    String chosenLifeLine = sc.nextLine();
                    useLifeLine(chosenLifeLine,question);
                    acceptAnswer(question,counter);
                    counter++;
                    break;
                case 'Q':
                    System.out.println("Thank You for playing Kaun Banega Crorepati! You have earned a total of Rs." + user.getEarnings());
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please Answer the question");
                    acceptAnswer(question,counter);
                    counter++;
                    return;
            }

            System.out.println();
        }
        System.out.println("Congratulations! You have won a prize money of Rs 1 Crore!");
    }
}
