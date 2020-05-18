package gui.essences;

import gui.dialogs.WindowSaver;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class WindowCreator {
    private JInternalFrame owner;
    private final String ownerName;

    public WindowCreator(JInternalFrame owner, String name)
    {
        this.owner = owner;
        ownerName = name;
    }

    public void setSizes(){
        WindowSaver windowParameters = null;
        FileInputStream inputStream = null;
        String filename = "./src/main/java/"+ownerName+".txt";
        if (new File(filename).exists()) {
            try {
                inputStream = new FileInputStream(filename);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                windowParameters = (WindowSaver) objectInputStream.readObject();
                owner.setLocation(windowParameters.x, windowParameters.y);
                owner.setSize(windowParameters.width, windowParameters.height);
                owner.setIcon(windowParameters.icon);
            } catch (Exception e) {
                //just ignore
            }
        } else
        {
            owner.setSize(400, 400);
            owner.setLocation(10, 10);
        }
    }
}
