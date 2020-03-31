package gui;

import gui.dialogs.WindowSaver;

import javax.swing.*;
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
        try {
            inputStream = new FileInputStream("/Users/BeklenishevaT/Desktop/oppRobots/src/main/java/"+ownerName+".txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            windowParameters = (WindowSaver) objectInputStream.readObject();
            owner.setLocation(windowParameters.x, windowParameters.y);
            owner.setSize(windowParameters.width, windowParameters.height);
            owner.setIcon(windowParameters.icon);
        } catch (Exception e) {
            owner.setLocation(0, 0);
            owner.setSize(1000, 800);
        }


    }
}
