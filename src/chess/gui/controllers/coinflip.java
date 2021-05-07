
package chess.gui.controllers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class coinflip
{
    private Boolean isPlayerTurn = false;

    public void imageClicked(MouseEvent event){
        /* Decide whether or not the player starts as white (which starts first in Chess). */
        String choice = event.getPickResult().getIntersectedNode().getId();

        switch (choice){
            case "playerWhite" -> isPlayerTurn = true;
            case "playerBlack" -> isPlayerTurn = false;
            case "playerRand" -> isPlayerTurn = (Math.random() < 0.5) ? true : false;
        }

        closeStage(event);
    }

    public Boolean getPlayerTurnChoice()
    {
        return isPlayerTurn;
    }

    private void closeStage(MouseEvent event){
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
