package gui.windows;

import gui.essences.DataTransmitter;
import gui.GlobalConstants;
import gui.panels.ScorePanel;
import gui.essences.WindowCreator;
import gui.dialogs.WindowSaver;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JInternalFrame {
    private final String name = "Score";
    private final ScorePanel scorePanel = new ScorePanel();
    private final DataTransmitter dataTransmitter = GlobalConstants.globalDataTransmitter;

    public ScoreWindow()
    {
        super("Очки", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        dataTransmitter.addObserver(scorePanel);
        panel.add(scorePanel, BorderLayout.CENTER);
        getContentPane().add(panel);
        new WindowCreator(this, name).setSizes();
        show();
    }

    @Override
    public void dispose()
    {
        dataTransmitter.deleteObserver(scorePanel);
        Rectangle bounds = this.getBounds();
        WindowSaver size = new WindowSaver(bounds.x, bounds.y, bounds.width, bounds.height, this.isIcon, this.isSelected);
        size.save(name);
        super.dispose();
    }
}
