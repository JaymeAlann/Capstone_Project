package chess.game;

import java.io.IOException;
import java.util.*;

public class AiBrain {
    private Board board;
    private int kingVulnerableScore;
    private String aiTeamColor;
    private ArrayList<Integer> kingVulnerableDestinations;
    private ArrayList<Piece> AI_kingCorp;
    private ArrayList<Piece> AI_kingsBishopCorp;
    private ArrayList<Piece> AI_queensBishopCorp;

    public void AI_turn(Board board) throws IOException {
        this.board = board;
        aiTeamColor = board.getAiTeamColor();
        checkKingStatus(board); // Get the kings vulnerability score
        checkCorpCommandersStatus(); // See if all commanders are still on the board
        setupCorps();
    }

    // Evaluate board
    private int evaluateBoard(final Board board, final Board movedBoard){
        // Negative score if AI is losing. Positive Score if winning
        return (aiTeamColor.equals("white")) ? ((board.getWhiteTeamScore() - board.getBlackTeamScore()) -
                (movedBoard.getWhiteTeamScore() - movedBoard.getBlackTeamScore())) :
                ((board.getBlackTeamScore() - board.getWhiteTeamScore()) - (movedBoard.getBlackTeamScore() - movedBoard.getWhiteTeamScore()));
    }

    public MoveHandler getAiMove() throws IOException {
        int kingScore = Integer.MIN_VALUE;
        int kingBishopScore = Integer.MIN_VALUE;
        int queenBishopScore = Integer.MIN_VALUE;


        HashMap<Integer, MoveHandler> kingMoves = determineBestCorpMove(AI_kingCorp);
        if(board.getAIKingCorpAvailability()){
            if(Collections.max(kingMoves.keySet()) != null){
                if(Collections.max(kingMoves.keySet()) > kingScore){
                    kingScore = Collections.max(kingMoves.keySet());
                }
            }
        }

        HashMap<Integer, MoveHandler> kingBishopMoves = determineBestCorpMove(AI_kingsBishopCorp);
        if(board.getAIKingBishopAvailability()){
            if(Collections.max(kingBishopMoves.keySet()) != null){
                if(Collections.max(kingBishopMoves.keySet()) > kingBishopScore){
                    kingBishopScore = Collections.max(kingBishopMoves.keySet());
                }
            }
        }

        HashMap<Integer, MoveHandler> queenBishopMoves = determineBestCorpMove(AI_queensBishopCorp);
        if(board.getAIQueensBishopAvailability()){
            if(Collections.max(queenBishopMoves.keySet()) != null){
                if(Collections.max(queenBishopMoves.keySet()) > queenBishopScore){
                    queenBishopScore = Collections.max(queenBishopMoves.keySet());
                }
            }
        }

        if(board.getAIKingCorpAvailability()){
            if(kingScore >= kingBishopScore && kingScore >= queenBishopScore){
                board.switchAIKingCorpCommand();
                return kingMoves.get(kingScore);
            }
        }
        // return best King Bishop Move
        if(kingBishopScore >= kingScore && kingBishopScore >= queenBishopScore){
            board.switchAIKingBishopCorpCommand();
            return kingBishopMoves.get(kingBishopScore);
        }
        // return best Queens Bishop Move
        if(queenBishopScore >= kingScore && queenBishopScore >= kingBishopScore){
            board.switchAIQueenBishopCorpCommand();
            return queenBishopMoves.get(queenBishopScore);
        }


        return null;
    }

    private HashMap<Integer, MoveHandler> determineBestCorpMove(ArrayList<Piece> corp) throws IOException {
        HashMap<Integer, MoveHandler> moveList = new HashMap<>();

        for(Piece piece : corp){
            ArrayList<MoveHandler> pieceMoves  = piece.determineMoves(board);
            int movescore;
            for(MoveHandler moves: pieceMoves){
                final Board boardHolder = board;
                board = moves.temporaryExecuteMove();

                // Move Score is equal to (AI Team Score - Player Team Score) + Center Control Score - Kings Vulnerability
                movescore = evaluateBoard(board, boardHolder);
                movescore += centerControlStatus(board);

                // If the kings vulnerable locations are in range, increase location importance
                if(kingVulnerableDestinations.contains(moves.getDestination())){
                    movescore += 75;
                }
                // Make moving the king have to be more important
                if(moves.getMovingPiece().getName().equals("King")){
                    movescore -= 50;
                }

                board = moves.unexecuteMove();

                Random random = new Random();
                int randomInt = random.nextInt(5 + 5);
                if(moveList.containsKey(movescore)){
                    moveList.put(movescore + randomInt, moves);
                }
                else{
                    moveList.put(movescore + randomInt, moves);
                }
            }
        }
        return moveList;
    }

    // Calculate Kings Vulnerability Score
    // If score is > 30% pieces need to move to protect
    private int checkKingStatus(final Board board){
        kingVulnerableDestinations = new ArrayList<>();
        Piece king = board.getAIKing();
        kingVulnerableScore = 0;
        List<Piece> playerTeam = (aiTeamColor.equals("white")) ? board.getBlackPieces() : board.getWhitePieces();
        for(Piece piece : playerTeam){
            ConquerSet vulnerabilityScore = new ConquerSet(piece, king);
            for(MoveHandler enemyPieceMoves : piece.determineMoves(board)){
                if(enemyPieceMoves.getDestination() == king.getCoordinates()){
                    kingVulnerableDestinations.add(piece.getCoordinates());
                    double value = ((double)(6 - vulnerabilityScore.getConquerSet()) / 6);
                    kingVulnerableScore += (int) value;
                }
            }
        }
        return kingVulnerableScore;
    }

    // Score for center control
    // If the score is negative, the AI does not have control
    private int centerControlStatus(final Board board){
        int playerControl = 0;
        int aiControl = 0;
        for(Piece piece : board.getPlayerPieces()){
            if(piece.getCoordinates() >= 25 && piece.getCoordinates() <= 30 ||
                    piece.getCoordinates() >= 33 && piece.getCoordinates() <= 38){
                playerControl += 15;
            }
        }
        for(Piece piece : board.getAIPieces()){
            if(piece.getCoordinates() >= 25 && piece.getCoordinates() <= 30 ||
                    piece.getCoordinates() >= 33 && piece.getCoordinates() <= 38){
                aiControl += 15;
            }
        }
        return aiControl - playerControl;
    }

    // Check is Bishop Commanders where captured during players last turn
    // If so revert the command of the pieces back to the king
    private void checkCorpCommandersStatus(){
        ArrayList<Piece> commanders = board.getAICorpCommanders();
        for(Piece piece : commanders){
            if(piece.getCaptureStatus()){
                board.revertCommand(piece.getCorp());
            }
        }
    }

    // King may delegate subordinates to be commanded by either of bishops
    private void delegateKingCorpPieceToBishopCorp(Piece pieceToDelegate){

    }

    private void setupCorps(){
        AI_kingCorp = (aiTeamColor.equals("white")) ? board.getWhiteCorpPieces("AI_king") :
                board.getBlackCorpPieces("AI_king");
        AI_kingsBishopCorp = (aiTeamColor.equals("white")) ? board.getWhiteCorpPieces("AI_kingsBishop") :
                board.getBlackCorpPieces("AI_kingsBishop");
        AI_queensBishopCorp = (aiTeamColor.equals("white")) ? board.getWhiteCorpPieces("AI_queensBishop") :
                board.getBlackCorpPieces("AI_queensBishop");
    }

    public String getAiTeamColor(){
        return aiTeamColor;
    }

}
