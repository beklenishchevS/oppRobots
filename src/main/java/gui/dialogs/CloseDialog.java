package gui.dialogs;

import gui.GameWindow;
import gui.MainApplicationFrame;
import javafx.scene.effect.Reflection;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CloseDialog extends WindowAdapter{
    private MainApplicationFrame owner;

    public CloseDialog(MainApplicationFrame owner){
        this.owner = owner;
    }
    public void onPushedCloseButton(ActionEvent event)
    {
        close();
    }

    @Override
    public void windowClosing(WindowEvent windowEvent)
    {
        close();
    }

    private void close()
    {

        Dialog dialog = new Dialog(owner);
        dialog.setVisible(true);
        if (dialog.is_closed())
        {
            owner.getScoreWindow().dispose();
            owner.getLogWindow().dispose();
            for (GameWindow gameWindow: owner.getGameWindows())
            {
                gameWindow.dispose();
            }
            System.exit(0);
        }
    }

}
