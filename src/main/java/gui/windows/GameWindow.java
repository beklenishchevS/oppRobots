package gui.windows;

import gui.essences.DataTransmitter;
import gui.panels.GameVisualizer;
import gui.essences.WindowCreator;
import gui.dialogs.WindowSaver;

import java.awt.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final String name;
    private final GameVisualizer gameVisualizer;
    private final int id;
    public GameWindow(int id)
    {

        super("Игровое поле"+id, true, true, true, true);
        this.name = "game" + id;
        this.id = id;
        gameVisualizer = new GameVisualizer(this, id);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        new WindowCreator(this, name).setSizes();
        show();
        setResizable(false);
    }


    @Override
    public void dispose()
    {
        Rectangle bounds = this.getBounds();
        DataTransmitter.killRobot(id);
        gameVisualizer.dispose();
        WindowSaver size = new WindowSaver(bounds.x, bounds.y, bounds.width, bounds.height, this.isIcon, this.isSelected);
        size.save(name);
        super.dispose();
    }
}
