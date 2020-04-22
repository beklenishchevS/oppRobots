package gui;

import gui.dialogs.FrameDialog;

import javax.swing.*;

public class WindowThread extends Thread{
    private final int id;
    private final MainApplicationFrame owner;

    public WindowThread(int id, MainApplicationFrame owner)
    {
        this.id = id;
        this.owner = owner;
    }

    @Override
    public void run() {
        GameWindow gameWindow = new GameWindow(id);
        gameWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameWindow.addInternalFrameListener(new FrameDialog(owner, gameWindow));
        owner.addWindow(gameWindow);
    }
}