import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Game 
{
    private int turn;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currPlayer;
    private ArrayList<Move> allMovesInGame;
    private Board board;
    private GameState gameState;
    private int turnsWithoutCapture;
    private int turnsWithoutPawnMove;

    Game(Player whitePlayer, Player blackPlayer)
    {
        setTurn(1);
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        allMovesInGame = new ArrayList<Move>();
        this.board = new Board();
    }

    public int getTurn() 
    {
        return turn;
    }

    public void setTurn(int turn) 
    {
        this.turn = turn;
    }

    public Player getWhitePlayer() 
    {
        return whitePlayer;
    }

    public Player getBlackPlayer() 
    {
        return blackPlayer;
    }

    public ArrayList<Move> getAllMovesInGame() 
    {
        return allMovesInGame;
    }

    public void addMoveToAllMovesInGame(Move move)
    {
        allMovesInGame.add(move);
    }

    public void setAllMovesInGame(ArrayList<Move> allMovesInGame) 
    {
        this.allMovesInGame = allMovesInGame;
    }

    public Player getCurrPlayer() 
    {
        return currPlayer;
    }

    public void setCurrPlayer(Player currPlayer) 
    {
        this.currPlayer = currPlayer;
    }

    public Board getBoard() 
    {
        return board;
    }

    public GameState getGameState() 
    {
        return gameState;
    }

    public void setGameState(GameState gameState) 
    {
        this.gameState = gameState;
    }
    
    public int getTurnsWithoutCapture() 
    {
        return turnsWithoutCapture;
    }

    public void setTurnsWithoutCapture(int turnsWithoutCapture) 
    {
        this.turnsWithoutCapture = turnsWithoutCapture;
    }

    public void setTurnsWithoutCapture(Move realMove)
    {
       
        setTurnsWithoutCapture((realMove.getEndPiece() == null || realMove.getType() == MoveType.EN_PASSANT) ? getTurnsWithoutCapture() + 1 : 0);
    }

    public int getTurnsWithoutPawnMove() 
    {
        return turnsWithoutPawnMove;
    }

    public void setTurnsWithoutPawnMove(int turnsWithoutPawnMove) 
    {
        this.turnsWithoutPawnMove = turnsWithoutPawnMove;
    }

    public void setTurnsWithoutPawnMove(Move realMove)
    {
        setTurnsWithoutPawnMove((realMove.getStartPiece() instanceof Pawn) ? 0 : getTurnsWithoutPawnMove() + 1);
    }

    public void play() throws IOException
    {
        while (true)
        {
            setCurrPlayer((getTurn() % 2 == 1) ? getWhitePlayer() : getBlackPlayer());   

            getCurrPlayer().setAllLegalMoves(getBoard());
            
            //System.out.println(getCurrPlayer().getAllLegalMoves().toString());            
            getBoard().printBoard();                   

           
            setGameState();
            if (getGameState() != GameState.ONGOING)
            {
                break;
            }            
           
            Move realMove = currPlayer.getEquivalentLegalMove(getValidMoveFromUser());
            
            if (realMove.getType() == MoveType.PAWN_PROMOTION)
            {
                realMove.setPromotionPiece(currPlayer.getPromotionPiece());
            }

            if (getCurrPlayer() instanceof CPUPlayer)
            {
                printCPUMoveAfterTurn(realMove);
            }

            realMove.movePiece();

            getBoard().setLastPieceToMove(realMove.getStartPiece());
            addMoveToAllMovesInGame(realMove);;
            setTurn(getTurn() + 1);
            setTurnsWithoutCapture(realMove);
            setTurnsWithoutPawnMove(realMove);
        }

        printEndGameMessage();
        getBoard().printBoard();
        writeAllMovesInGameToFile();
    }

    private void printEndGameMessage()
    {
        switch(getGameState())
        {
            case WHITE_WIN:
                System.out.println("White has won the game by checkmate!");
                break;
            case BLACK_WIN:
                System.out.println("Black has won the game by checkmate!");
                break;
            case STALEMATE:
                System.out.println("The game is a stalemate!");
                break;
            case FIFTY_MOVE_RULE:
                System.out.println("The game is a draw by 50 move rule!");;
                break;      
             
            case ONGOING:
                System.out.println("The game is still ongoing!");
        }
    }

    private Move getValidMoveFromUser() throws IOException
    {
        Move userMove;
        do
        {
            userMove =  getCurrPlayer().getMove(getBoard());
        }
        while(!currPlayer.legalMovesContains(userMove));
        return userMove;
    }
    
    private void writeAllMovesInGameToFile() throws IOException
    {
        FileWriter fileWriter = new FileWriter("AllMovesInGame.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Move move : getAllMovesInGame())
        {
            bufferedWriter.write(move.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.write(gameResultString());
        bufferedWriter.close();
    }

    private String gameResultString()
    {
        switch(getGameState())
        {
            case WHITE_WIN:
                return "1-0";
            case BLACK_WIN:
                return "0-1";
            default:
                return "0.5-0.5";
        }
    }

    private void setGameState()
    {
        if (inCheckMate())
        {
            setGameState((getCurrPlayer().getColor() == Color.WHITE) ? GameState.BLACK_WIN : GameState.WHITE_WIN);
        }
        else if (inStalemate())
        {
            setGameState(GameState.STALEMATE);
        }
        //IS 100 BECAUSE BOTH PLAYERS HAVE TO MOVE TO COUNT FOR 1
        else if (getTurnsWithoutCapture() >= 100 && getTurnsWithoutPawnMove() >= 100)
        {
            setGameState(GameState.FIFTY_MOVE_RULE);
        }
        else
        {
            setGameState(GameState.ONGOING);
        }
    }

    private boolean inCheckMate()
    {
        return getBoard().kingIsInCheck(getCurrPlayer().getColor()) && getCurrPlayer().getAllLegalMoves().isEmpty();
    }

    private boolean inStalemate()
    {
        return !getBoard().kingIsInCheck(getCurrPlayer().getColor()) && getCurrPlayer().getAllLegalMoves().isEmpty();
    }

    private void printCPUMoveAfterTurn(Move realMove)
    {        
        System.out.println("The CPU played: " + realMove.toString() + "\n");
    }
}