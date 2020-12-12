public class Board
{
    private Square[][] board;
    private Piece lastPieceToMove;
    
    Board()
    {
        setNewBoard();
    }
    
    public Piece getLastPieceToMove() 
    {
        return lastPieceToMove;
    }

    public void setLastPieceToMove(Piece lastPieceToMove) 
    {
        this.lastPieceToMove = lastPieceToMove;
    }

    private void setNewBoard()
    {     
        board = new Square[8][8];

        board[0][0] = new Square(0, 0, new Rook(Color.WHITE));
        board[0][1] = new Square(0, 1, new Knight(Color.WHITE));
        board[0][2] = new Square(0, 2, new Bishop(Color.WHITE));
        board[0][3] = new Square(0, 3, new Queen(Color.WHITE));
        board[0][4] = new Square(0, 4, new King(Color.WHITE));
        board[0][5] = new Square(0, 5, new Bishop(Color.WHITE));
        board[0][6] = new Square(0, 6, new Knight(Color.WHITE));
        board[0][7] = new Square(0, 7, new Rook(Color.WHITE));

        board[1][0] = new Square(1, 0, new Pawn(Color.WHITE));
        board[1][1] = new Square(1, 1, new Pawn(Color.WHITE));
        board[1][2] = new Square(1, 2, new Pawn(Color.WHITE));
        board[1][3] = new Square(1, 3, new Pawn(Color.WHITE));
        board[1][4] = new Square(1, 4, new Pawn(Color.WHITE));
        board[1][5] = new Square(1, 5, new Pawn(Color.WHITE));
        board[1][6] = new Square(1, 6, new Pawn(Color.WHITE));
        board[1][7] = new Square(1, 7, new Pawn(Color.WHITE));

        board[7][0] = new Square(7, 0, new Rook(Color.BLACK));
        board[7][1] = new Square(7, 1, new Knight(Color.BLACK));
        board[7][2] = new Square(7, 2, new Bishop(Color.BLACK));
        board[7][3] = new Square(7, 3, new Queen(Color.BLACK));
        board[7][4] = new Square(7, 4, new King(Color.BLACK));
        board[7][5] = new Square(7, 5, new Bishop(Color.BLACK));
        board[7][6] = new Square(7, 6, new Knight(Color.BLACK));
        board[7][7] = new Square(7, 7, new Rook(Color.BLACK));

        board[6][0] = new Square(6, 0, new Pawn(Color.BLACK));
        board[6][1] = new Square(6, 1, new Pawn(Color.BLACK));
        board[6][2] = new Square(6, 2, new Pawn(Color.BLACK));
        board[6][3] = new Square(6, 3, new Pawn(Color.BLACK));
        board[6][4] = new Square(6, 4, new Pawn(Color.BLACK));
        board[6][5] = new Square(6, 5, new Pawn(Color.BLACK));
        board[6][6] = new Square(6, 6, new Pawn(Color.BLACK));
        board[6][7] = new Square(6, 7, new Pawn(Color.BLACK));              

        for (int i = 2; i <= 5; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j] = new Square(i, j, null);
            }
        }
    }

    public void printBoard()
    {
        String out = "";
        for (int i = 7; i >= 0; i--)
        {
            String line = "";
            for (int j = 0; j < 8; j++)
            {
                if (j == 0)
                {
                    line += "" + (i + 1) + " ";
                }
                if (board[i][j].getPiece() == null)
                {
                    line += "- ";
                }
                else 
                {
                    String s = String.valueOf(board[i][j].getPiece().getIcon());
                    if (board[i][j].getPiece().getColor() == Color.BLACK)
                    {
                        s = s.toLowerCase();
                    }
                    line += s + " ";
                }
            }
            out += line + "\n";
        }
        out += "  a b c d e f g h\n";
        System.out.println(out);
    }
    
    public Square getSquare(int row, int col)
    {
        return board[row][col];
    }

    public Square findKingSquare(Color color)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                Square currSquare = getSquare(i, j);
                Piece currPiece = currSquare.getPiece();

                if (currPiece != null && currPiece instanceof King && currPiece.getColor() == color)
                {
                    return currSquare;
                }
            }
        }

        return null;
    }    

    public boolean squareCanBeAttacked(Square square, Color friendlyColor)
    {
        
        return canBeAttackedLinear(1, 0, square, friendlyColor) || 
               canBeAttackedLinear(-1, 0, square, friendlyColor) ||
               canBeAttackedLinear(0, 1, square, friendlyColor) || 
               canBeAttackedLinear(0, -1, square, friendlyColor) || 
               canBeAttackedLinear(1, 1, square, friendlyColor) || 
               canBeAttackedLinear(-1, 1, square, friendlyColor) || 
               canBeAttackedLinear(1, -1, square, friendlyColor) || 
               canBeAttackedLinear(-1, -1, square, friendlyColor) || 
               canBeAttackedKnight(square, friendlyColor);
    }

    public boolean kingIsInCheck(Color color)
    {
        return squareCanBeAttacked(findKingSquare(color), color);
    }

    private boolean canBeAttackedLinear(int rowDir, int colDir, Square square, Color friendlyColor)
    {
        int row = square.getROW();
        int col = square.getCOL();

        int i = 1;
        while (i < 8)
        {
            int dRow = row + (i * rowDir);
            int dCol = col + (i * colDir);
            
            if (dRow >= 0 && dRow < 8 && dCol < 8 && dCol >= 0)
            { 
                Square destSquare = getSquare(dRow, dCol);
                Piece destPiece = destSquare.getPiece();
            
                if (destPiece != null)
                {
                    if (destPiece.getColor() != friendlyColor)
                    {
                        if (rowDir == 0 || colDir == 0)
                        {
                            return straightLineThreats(destPiece, i);
                        }
                        else
                        {
                            return diagonalThreats(destPiece, i, rowDir, friendlyColor);
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                
                
            }
            else 
            {
                break;
            }
            i++;
        }
        return false;
    }

    
    private boolean straightLineThreats(Piece destPiece, int i)
    {
        if (i == 1)
        {
            return destPiece instanceof Rook || destPiece instanceof Queen || destPiece instanceof King;
        }

        return destPiece instanceof Rook || destPiece instanceof Queen;
    }

    private boolean diagonalThreats(Piece destPiece, int i, int rowDir, Color friendlyColor)
    {
       
        if (i == 1)
        {            
            if (friendlyColor == Color.WHITE && rowDir == 1 || friendlyColor == Color.BLACK && rowDir == -1)
            {
                return destPiece instanceof Bishop || destPiece instanceof Queen || destPiece instanceof King || destPiece instanceof Pawn;
            }
            else
            {
                return destPiece instanceof Bishop || destPiece instanceof Queen || destPiece instanceof King;
            }
        }

        return destPiece instanceof Bishop || destPiece instanceof Queen;
    }

    private boolean canBeAttackedKnight(Square square, Color color)
    {
        int row = square.getROW();
        int col = square.getCOL();
       
        int[] dRows = new int[]{1, -1,  1, -1, 2, -2,  2, -2};
        int[] dCols = new int[]{2,  2, -2, -2, 1,  1, -1, -1};
        
        for (int i = 0; i < 8; i++)
        {
            int dRow = row + dRows[i];
            int dCol = col + dCols[i];            

            if (dRow < 8 && dRow >= 0 && dCol < 8 && dCol >= 0)
            {
                Square destSquare = getSquare(dRow, dCol);
                Piece destPiece = destSquare.getPiece();

                if (destPiece != null && destPiece.getColor() != color && destPiece instanceof Knight)
                {
                    return true;
                }
            }
        }

        return false;
    }
}