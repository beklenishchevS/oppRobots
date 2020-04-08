package gui.dialogs;

import gui.MainApplicationFrame;

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
            owner.getLogWindow().dispose();
            owner.getGameWindow().dispose();
            System.exit(0);
        }
    }

}
