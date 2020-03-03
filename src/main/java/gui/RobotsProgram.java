package gui;

import gui.dialogs.CloseDialog;

import java.awt.Frame;

import javax.swing.*;

public class RobotsProgram
{

    public static void main(String[] args) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      SwingUtilities.invokeLater(() -> {
        MainApplicationFrame frame = new MainApplicationFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        CloseDialog closeDialog = new CloseDialog(frame);
        frame.addWindowListener(closeDialog);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}
