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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class GameRunner implements QuestionAdd {
    private final QuestionList questions;
    private final User user;

    private static final String URL = "jdbc:mysql://localhost:3306/kbc";

    private static final String USERNAME="root";
    private static final String PASSWORD = "password";

    private final Connection CONN;

    private final static String insertQuery = "INSERT INTO questions(question,Option1,Option2,Option3,Option4,CA) VALUES(?,?,?,?,?,?)";

    private final static String getQuery = "SELECT * FROM questions";

    private final static String deleteQuery = "DELETE * from questions WHERE id=?";

    ResultSet getAllQuestionsResults;
    private HashMap<String, Boolean> lifeLines;
    Scanner sc = new Scanner(System.in);
    public GameRunner() throws SQLException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        CONN = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        Statement statement = CONN.createStatement();

        questions = new QuestionList();
        user = new User();
        lifeLines = new HashMap<>();
        lifeLines.put("Audience Poll", true);
        lifeLines.put("50:50", true);
        lifeLines.put("Expert Advice", true);
    }

    private void acceptAnswer(Question question,int counter) throws SQLException {

        System.out.print("Enter your answer: ");
        String userAnswer = sc.nextLine();
        if (question.getAnswer().equalsIgnoreCase(userAnswer)) {
            System.out.println("Correct Answer!!");
            user.addEarnings(PrizeMoney.getByIndex(counter).getAmount());
            System.out.println("Your current prize money is: " + user.getEarnings());
        } else {
            System.out.println("Sorry! The correct answer was " + question.getAnswer());
            System.out.println("Thank You for playing Kaun Banega Crorepati! You have earned a total of Rs." + user.getEarnings());
            CONN.close();
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

    public void viewQuestions() throws SQLException{
        Statement statement = CONN.createStatement();
        getAllQuestionsResults = statement.executeQuery(getQuery);
        while(getAllQuestionsResults.next()){
            String question = getAllQuestionsResults.getString("question");
            String Option1 = getAllQuestionsResults.getString("Option1");
            String Option2 = getAllQuestionsResults.getString("Option2");
            String Option3 = getAllQuestionsResults.getString("Option3");
            String Option4 = getAllQuestionsResults.getString("Option4");
            String correctAnswer = getAllQuestionsResults.getString("CA");
            System.out.println("Q: "+ question);
            System.out.println("1: "+ Option1);
            System.out.println("2: "+ Option2);
            System.out.println("3: "+ Option3);
            System.out.println("4: "+ Option4);
            System.out.println("CA: "+ correctAnswer);
            System.out.println("--------------------------------------------------------------------");
        }
    }
    public void addQuestions() throws SQLException{
        int noOfQuestions = 1;
        PreparedStatement preparedStatement = CONN.prepareStatement(insertQuery);
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
//            Path path = Paths.get("kbc_questions.txt");
            question = new Question(singleQuestion, options, answer, preparedStatement);
            questions.addQuestionToQuestionList(question);
        }
        preparedStatement.close();
    }
    public void adminSection() throws SQLException {
        //        System.out.println("Want to load questions from file?(Y/N)");
//        String choice = sc.nextLine();
//        if(Objects.equals(choice, "Y") || Objects.equals(choice, "y")) {
//          loadQuestionsFromFile();
//          return;
//        }
        System.out.println("Welcome To Game Admin Section.");
        System.out.println("Press V to view existing questions, A to add question, D to delete question, any other key to start the game");
        String choice = sc.next();
        if(choice.equals("V")){
            //view questions
            viewQuestions();
        }else if(choice.equals("A")){
            addQuestions();
        }else{
            return;
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

    public void startGame() throws SQLException {
        Scanner sc = new Scanner(System.in);
        Statement statement = CONN.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        getAllQuestionsResults = statement.executeQuery(getQuery);
        getAllQuestionsResults.last();
        int rows = getAllQuestionsResults.getRow();
        if(rows<12){
            System.out.println("Sorry! Not enough questions");
            return;
        }
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
        CONN.close();
    }
}
