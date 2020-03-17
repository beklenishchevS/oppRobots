package gui.dialogs;

import gui.MainApplicationFrame;
import gui.Sizes;
import gui.dialogs.Dialog;
import org.w3c.dom.events.Event;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

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
        save();
        Dialog dialog = new Dialog(owner);
        dialog.setVisible(true);
        if (dialog.is_closed())
        {
            System.exit(0);
        }
    }

    private void save()
    {
        Rectangle gameWindow = owner.getGameWindow().getBounds();
        Rectangle logWindow = owner.getLogWindow().getBounds();
        Sizes sizes = new Sizes(gameWindow.x,
                gameWindow.y,
                logWindow.x,
                logWindow.y,
                gameWindow.width,
                gameWindow.height,
                logWindow.width,
                logWindow.height,
                owner.getGameWindow().isIcon(),
                owner.getLogWindow().isIcon(),
                owner.getGameWindow().isSelected(),
                owner.getLogWindow().isSelected());
        try
        {
            FileOutputStream outputStream = new FileOutputStream("/Users/BeklenishevaT/Desktop/oppRobots/src/main/java/mem.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(sizes);
            objectOutputStream.close();
        }
        catch (Exception e){
           //just ignore
        }
    }
}
