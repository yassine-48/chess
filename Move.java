public class Move
{
    private Player player;
    private Square startSquare;
    private Square endSquare;   
    private Piece startPiece;
    private Piece endPiece; 
    private Board board;
    private MoveType type;
    private Piece promotionPiece;

    Move(Player player, Square startSquare, Square endSquare, Board board, MoveType type)
    {
        this.player = player;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.startPiece = startSquare.getPiece();
        this.endPiece = endSquare.getPiece();
        this.board = board;
        this.type = type;
    }

    Move(Player player, Square startSquare, Square endSquare, Board board, MoveType type, Piece promotionPiece)
    {
        this.player = player;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.startPiece = startSquare.getPiece();
        this.endPiece = endSquare.getPiece();
        this.board = board;
        this.type = type;
        this.promotionPiece = promotionPiece;
    }

    Move(Player player, Square startSquare, Square endSquare)
    {
        this.player = player;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.startPiece = startSquare.getPiece();
        this.endPiece = endSquare.getPiece();
        this.type = null;
    }

    public void movePiece()
    {
        switch(type)
        {
            case NORMAL:
                moveNormally();
                break;
            case CASTLE:
                castle();
                break;
            case PAWN_PROMOTION:
                promotePawn();
                break;
            case EN_PASSANT:
                enPassant();
                break;
        }
    }

    private void moveNormally()
    {
        startPiece.setMoveCount(startPiece.getMoveCount() + 1);
        endSquare.setPiece(startPiece);
        startSquare.setPiece(null);
    }

    private void castle()
    {
        Square destSquare = getEndSquare();
        Board currBoard = getBoard();
        Move moveRook = null;
        
        if (destSquare.equalsSquare(currBoard.getSquare(0, 6)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(0, 7), board.getSquare(0, 5), getBoard(), MoveType.NORMAL);
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(0, 2)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(0, 0), board.getSquare(0, 3), getBoard(), MoveType.NORMAL);
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(7, 6)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(7, 7), board.getSquare(7, 5), getBoard(), MoveType.NORMAL);
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(7, 2)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(7, 0), board.getSquare(7, 3), getBoard(), MoveType.NORMAL);        
        }

        moveNormally();
        moveRook.moveNormally();
    }

    private void promotePawn()
    {
        moveNormally();
        endSquare.setPiece(promotionPiece);
    }

    private void enPassant()
    {
        moveNormally();

        int destRow = getEndSquare().getROW();
        int destCol = getEndSquare().getCOL();
        int behind = (getPlayer().getColor() == Color.WHITE) ? -1 : 1;
        Square oppPawnSquare = board.getSquare(destRow + behind, destCol);

        oppPawnSquare.setPiece(null);
    }

    private void undoMovePiece()
    {
        switch(type)
        {
            case NORMAL:
                undoMoveNormally();
                break;
            case CASTLE:
                undoCastle();
                break;
            case PAWN_PROMOTION:
                undoPromotePawn();
                break;
            case EN_PASSANT:
                undoEnPassant();
                break;
        }
    }   

    private void undoMoveNormally()
    {
        startPiece.setMoveCount(startPiece.getMoveCount() - 1);
        endSquare.setPiece(endPiece);
        startSquare.setPiece(startPiece);
    }

    private void undoCastle()
    {
        Square destSquare = getEndSquare();
        Board currBoard = getBoard();
        Move moveRook = null;
        
        if (destSquare.equalsSquare(currBoard.getSquare(0, 6)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(0, 7), board.getSquare(0, 5), getBoard(), MoveType.NORMAL);
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(0, 2)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(0, 0), board.getSquare(0, 3), getBoard(), MoveType.NORMAL);
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(7, 6)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(7, 7), board.getSquare(7, 5), getBoard(), MoveType.NORMAL);
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(7, 2)))
        {
            moveRook = new Move(getPlayer(), board.getSquare(7, 0), board.getSquare(7, 3), getBoard(), MoveType.NORMAL);        
        }
        undoMoveNormally();
        moveRook.undoMoveNormally();
    }

    private void undoPromotePawn()
    {
        undoMoveNormally();
        endSquare.setPiece(getEndPiece());
    }

    private void undoEnPassant()
    {
        undoMoveNormally();

        int destRow = getEndSquare().getROW();
        int destCol = getEndSquare().getCOL();

        Color currColor = getPlayer().getColor();
        Color oppColor = (currColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        int behind = (currColor == Color.WHITE) ? -1 : 1;
        Square oppPawnSquare = board.getSquare(destRow + behind, destCol);

        oppPawnSquare.setPiece(new Pawn(oppColor));
        oppPawnSquare.getPiece().setMoveCount(1);
    }

    public Player getPlayer() 
    {
        return player;
    }

    public Square getStartSquare() 
    {
        return startSquare;
    }

    public Square getEndSquare() 
    {
        return endSquare;
    }

    public Piece getStartPiece() 
    {
        return startPiece;
    }

    public Piece getEndPiece() 
    {
        return endPiece;
    }

    public Board getBoard() 
    {
        return board;
    }

    public MoveType getType() 
    {
        return type;
    }

    public void setType(MoveType type) 
    {
        this.type = type;
    }
    
    public Piece getPromotionPiece() 
    {
        return promotionPiece;
    }

    public void setPromotionPiece(Piece promotionPiece) 
    {
        this.promotionPiece = promotionPiece;
    }

    public String toString()
    {
        return (getType() == MoveType.PAWN_PROMOTION) ? promoToString() : elseToString();
    }  

    private String elseToString()
    {
        return startSquare.toString() + "-" + endSquare.toString();
    }  

    private String promoToString()
    {
        return elseToString() + "(" + String.valueOf(promotionPiece.getIcon()) + ")";
    }  

    public boolean isLegal()
    {        
        switch(getType())
        {
            case CASTLE:
                return castleIsLegal();
            default:
                return normalIsLegal();
        }       
    }

    private boolean normalIsLegal()
    {
        return !putsKingIntoCheck();
    }
    
    //private boolean castleIsLegal()
    //{
    //    return !getBoard().kingIsInCheck(getPlayer().getColor()) && !castlesThroughCheck();
    //}    

    private boolean castleIsLegal()
    {
        return !getBoard().squareCanBeAttacked(getEndSquare(), getStartPiece().getColor()) && !castlesThroughCheck();
    }

    private boolean castlesThroughCheck()
    {
        Square destSquare = getEndSquare();
        Board currBoard = getBoard();
        
        if (destSquare.equalsSquare(currBoard.getSquare(0, 6)))
        {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(0, 5), getStartPiece().getColor());
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(0, 2)))
        {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(0, 3), getStartPiece().getColor());
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(7, 6)))
        {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(7, 5), getStartPiece().getColor());
        }
        else if (destSquare.equalsSquare(currBoard.getSquare(7, 2)))
        {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(7, 3), getStartPiece().getColor());
        }
        return false;
    }

    private boolean putsKingIntoCheck()
    {
        movePiece();
        boolean kingIsInCheck = getBoard().kingIsInCheck(getPlayer().getColor());

        undoMovePiece();

        return kingIsInCheck;
    }


    public boolean equalsMove(Move otherMove)
    {
        return getStartSquare().equalsSquare(otherMove.getStartSquare()) && getEndSquare().equalsSquare(otherMove.getEndSquare());
    }
}