import java.io.*;

public class HumanPlayer extends Player
{
    HumanPlayer(Color color) 
    {
        super(color);
    }
    
    @Override
    public Move getMove(Board board) throws IOException
    {
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(in);

        String colourName = (getColor() == Color.WHITE) ? "White" : "Black";
        String prompt =  colourName + " - Please enter a move (ex. E2-E4, H7-H6, etc.)";

        String moveAsString;
        do
        {
            System.out.println(prompt);
            moveAsString = input.readLine().toUpperCase().trim();
        
        } while (!matchesNormalMove(moveAsString) && !matchesPromotionMove(moveAsString));      

        return toMove(moveAsString, board);
    }
    
    private boolean matchesNormalMove(String moveAsString)
    {
        return moveAsString.matches("[ABCDEFGH][12345678]-[ABCDEFGH][12345678]");
    }
    
    private boolean matchesPromotionMove(String moveAsString)
    {
        return moveAsString.matches("[ABCDEFGH][12345678]-[ABCDEFGH][12345678][(][QRBN][)]");
    }    

    public Move toMove(String s, Board board)
    {        
        String alphs = "ABCDEFGH";
        
        int startRow = Integer.parseInt(String.valueOf(s.charAt(1))) - 1;
        int startCol = alphs.indexOf(String.valueOf(s.charAt(0)));
        Square startSquare = board.getSquare(startRow, startCol);

        int endRow = Integer.parseInt(String.valueOf(s.charAt(4))) - 1;
        int endCol = alphs.indexOf(String.valueOf(s.charAt(3)));    
        Square endSquare = board.getSquare(endRow, endCol);
      
        return new Move(this, startSquare, endSquare);
    }

    @Override
    public Piece getPromotionPiece() throws IOException
    {
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(in);

        String colourName = (getColor() == Color.WHITE) ? "White" : "Black";
        String prompt =  colourName + " - Please enter a piece to promote to (Q, R, B, N)";
        String possPieceString = "QRBN";

        String pieceAsString;
        do
        {
            System.out.println(prompt);
            pieceAsString = input.readLine().toUpperCase().trim();
        
        } while (pieceAsString.length() != 1 || !possPieceString.contains(pieceAsString)); 
        System.out.println();    

        return parseRightPiece(pieceAsString);
    }

    private Piece parseRightPiece(String s)
    {
        switch(s)
        {
            case "Q":
                return new Queen(getColor());
            case "R":
                return new Rook(getColor());
            case "B":
                return new Bishop(getColor());
            case "N":
                return new Knight(getColor());
            default:
                return null;
        }
    }
}