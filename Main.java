import java.io.IOException;


public class Main
{
public static void main(String[] args){
     int Choice;
        Scanner saisir=new Scanner(System.in);
        System.out.println("------------Chess Game----------");
        System.out.println("1- 2 joueurs");
        System.out.println("2- 1 joueur vs cpu");
        Choice=saisir.nextInt();
        switch(Choice) {
            case 1:
                Player whitePlayer = new HumanPlayer(Color.WHITE);
                Player blackPlayer = new HumanPlayer(Color.BLACK);
                Game game = new Game(whitePlayer, blackPlayer);
                game.play();
                break;
            case 2:
                Player whitePlayer1 = new HumanPlayer(Color.WHITE);
                Player blackPlayer1 = new CPUPlayer(Color.BLACK);
                Game game1 = new Game(whitePlayer1, blackPlayer1);
                game1.play();
        }                      
    
}

