import java.io.IOException;
import java.util.*;

public abstract class Player
{
    private Color color;
    private ArrayList<Move> allLegalMoves;

    Player(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }    

    public abstract Move getMove(Board board) throws IOException;

    public abstract Piece getPromotionPiece() throws IOException;
    
    public ArrayList<Move> getAllLegalMoves() 
    {
        return allLegalMoves;
    }

    public void setAllLegalMoves(Board board) 
    {
        ArrayList<Move> allLegalMoves = new ArrayList<Move>();

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                Square currSquare = board.getSquare(i, j);
                Piece piece = currSquare.getPiece();

                if (piece != null && piece.getColor() == getColor())
                {
                    piece.setPossibleMoves(currSquare, board, this);
                    piece.setLegalMoves();
                    allLegalMoves.addAll(piece.getLegalMoves());
                }
            }
        }

        setAllLegalMoves(allLegalMoves);;
    }

    public void setAllLegalMoves(ArrayList<Move> allLegalMoves)
    {
        this.allLegalMoves = allLegalMoves;
    }

    public void printAllLegalMoves()
    {
        for (Move move : getAllLegalMoves())
        {
            System.out.println(move.toString());
        }
    }

    public Move getEquivalentLegalMove(Move userMove)
    {
        for (Move legalMove : getAllLegalMoves())
        {
          
            if (legalMove.equalsMove(userMove))
            {
                return legalMove;
            }
        }
        
        return null;
    } 
    
    public boolean legalMovesContains(Move otherMove)
    {
        for (Move legalMove : getAllLegalMoves())
        {
            if (legalMove.equalsMove(otherMove))
            {
                return true;
            }
        }
        return false;
    }
}