package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Dialog extends JDialog
{
    private boolean closed = false;

    public Dialog(JFrame owner)
    {
        super(owner, "Подтверждение", true);

        JButton yes = new JButton("ДА");
        yes.addActionListener(event -> {
            setVisible(false);
            closed = true;
        });

        JButton no = new JButton("HET");
        no.addActionListener(actionEvent -> setVisible(false));


        JPanel panel = new JPanel();
        panel.add(yes);
        panel.add(no);
        add(panel, BorderLayout.SOUTH);
        JLabel label = new JLabel("Хотите ли выйте?");
        label.setLocation(200, 200);
        add(label);
        setSize(260, 160);
        setLocation(400, 400);
        setResizable(false);
    }

    public boolean is_closed()
    {
        return closed;
    }


}
