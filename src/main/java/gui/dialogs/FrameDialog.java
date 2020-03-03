package gui.dialogs;

import gui.dialogs.Dialog;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class FrameDialog implements InternalFrameListener {
    private JFrame owner;

    public FrameDialog(JFrame owner)
    {
        this.owner = owner;
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent internalFrameEvent) {

    }

    @Override
    public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
        Dialog dialog = new Dialog(owner);
        dialog.setVisible(true);
        if (dialog.is_closed())
        {
            internalFrameEvent.getInternalFrame().setVisible(false);
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent internalFrameEvent) {

    }

    @Override
    public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {

    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {

    }

    @Override
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {

    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {

    }
}
