package gui;

import gui.dialogs.WindowSaver;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JInternalFrame {
    private final String name = "Score";
    private final ScorePanel scorePanel = new ScorePanel();

    public ScoreWindow()
    {
        super("Очки", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scorePanel, BorderLayout.CENTER);
        getContentPane().add(panel);
        new WindowCreator(this, name).setSizes();
        show();
    }

    @Override
    public void dispose()
    {
        Rectangle bounds = this.getBounds();
        WindowSaver size = new WindowSaver(bounds.x, bounds.y, bounds.width, bounds.height, this.isIcon, this.isSelected);
        size.save(name);
        super.dispose();
    }
}
