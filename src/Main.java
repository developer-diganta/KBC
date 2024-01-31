import gameAdmin.GameRunner;

import java.sql.SQLException;

//import user.User;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        GameRunner game = new GameRunner();
        game.adminSection();
        clearConsole();

        // Output after clearing
        System.out.println("WELCOME TO KBC! KBC mein apka swagat hai!");
        game.startGame();
    }
    public static void clearConsole() {
        for(int i=0;i<50;i++){
            System.out.println();
        }
    }

}