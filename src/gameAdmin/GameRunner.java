package gameAdmin;

import question.Question;
import question.QuestionList;

import java.util.ArrayList;
import java.util.Scanner;

public class GameRunner implements QuestionAdd{
    private QuestionList questions;
    public GameRunner(){
        questions = new QuestionList();
    }

    public void addQuestions(){
        Scanner sc = new Scanner(System.in);
        while(true){
            String singleQuestion;
            Question question;
            System.out.println("Enter Question:- ");
            singleQuestion = sc.nextLine();
            if(singleQuestion.equals("EXIT")){
                break;
            }
            System.out.println("Enter the options: ");
            ArrayList<String> options = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter option " + (i + 1) + ": ");
                options.add(sc.nextLine());
            }

            System.out.print("Enter Answer: ");
            String answer = sc.nextLine();
            question = new Question(singleQuestion,options, answer);
            questions.addQuestionToQuestionList(question);
        }
    }

    public void showQuestions(){
        for (Question question : questions.getQuestionList()) {
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Options:");
            ArrayList<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println("  Option " + (i + 1) + ": " + options.get(i));
            }
            System.out.println("Answer: " + question.getAnswer());
            System.out.println();
        }
    }
}
