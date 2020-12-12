import java.util.ArrayList;

public abstract class Piece
{
    private Color color; 
    private char icon;
    private int moveCount;
    private ArrayList<Move> possibleMoves;
    private ArrayList<Move> legalMoves;

    public Piece(Color color)
    {
        this.color = color;
        this.moveCount = 0;
    } 

    public Color getColor() 
    {
        return color;
    }

    public char getIcon() 
    {
        return icon;
    }

    public void setIcon(char icon) 
    {
        this.icon = icon;
    }

    public int getMoveCount()
    {
        return moveCount;
    }

    public void setMoveCount(int moveCount)
    {
        this.moveCount = moveCount;
    }
    
    public ArrayList<Move> oneDirection(int rowDir, int colDir, Square currSquare, Board board, Player currPlayer)
    {
        ArrayList<Move> oneDirection = new ArrayList<Move>();
        int row = currSquare.getROW();
        int col = currSquare.getCOL();

        int i = 1;
        while (i < 8)
        {
            int dRow = row + (i * rowDir);
            int dCol = col + (i * colDir);
            
            if (dRow >= 0 && dRow < 8 && dCol < 8 && dCol >= 0)            
            { 
                Square destSquare = board.getSquare(row + (i * rowDir), col + (i * colDir));
                Piece destPiece = destSquare.getPiece();
            
                if (destPiece == null)
                {
                    oneDirection.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
                }
                else if (destPiece.getColor() != getColor())
                {
                    oneDirection.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
                    break;
                   
                }
                else 
                {
                    break;
                   
                }
            }
            else 
            {
                break;
            }
            i++;
        }

        return oneDirection;
    }

    public ArrayList<Move> getPossibleMoves() 
    {
        return possibleMoves;
    }

    public abstract void setPossibleMoves(Square curr, Board board, Player currPlayer);  

    public void setPossibleMoves(ArrayList<Move> possibleMoves)
    {
        this.possibleMoves = possibleMoves;
    }
    
    public void printPossibleMoves()
    {
        for (Move move : getLegalMoves())
        {
            System.out.println(move.toString());
        }
    }

    public ArrayList<Move> getLegalMoves() 
    {
        return legalMoves;
    }

    public void setLegalMoves()
    {
        ArrayList<Move> legalMoves = new ArrayList<Move>();

        for (Move possMove : getPossibleMoves())
        {
            if (possMove.isLegal())
            {
                legalMoves.add(possMove);
            }
        }

        this.legalMoves = legalMoves;
    }  

    public void setLegalMoves(ArrayList<Move> legalMoves)
    {
        this.legalMoves = legalMoves;
    }
    
    public void printLegalMoves()
    {
        for (Move move : getLegalMoves())
        {
            System.out.println(move.toString());
        }
    }
}
