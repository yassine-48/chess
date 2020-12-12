import java.util.*;

public class Knight extends Piece
{
    public Knight(Color color)
    {
        super(color);
        setIcon('N');
    }    

    @Override
    public void setPossibleMoves(Square currSquare, Board board, Player currPlayer) 
    {
        ArrayList<Move> possibleMoves = new ArrayList<Move>();
        int row = currSquare.getROW();
        int col = currSquare.getCOL();
        //not the basketball player
        int[] dRows = new int[]{1, -1,  1, -1, 2, -2,  2, -2};
        int[] dCols = new int[]{2,  2, -2, -2, 1,  1, -1, -1};
        
        for (int i = 0; i < 8; i++)
        {
            int dRow = row + dRows[i];
            int dCol = col + dCols[i];            

            if (dRow < 8 && dRow >= 0 && dCol < 8 && dCol >= 0)
            {
                Square destSquare = board.getSquare(dRow, dCol);
                Piece destPiece = destSquare.getPiece();

                if (destPiece == null || destPiece.getColor() != getColor())
                {
                    possibleMoves.add(new Move(currPlayer, currSquare, destSquare, board, MoveType.NORMAL));
                }
            }
        }

        setPossibleMoves(possibleMoves);
    }
}