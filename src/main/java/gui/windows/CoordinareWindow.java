package gui.windows;

import gui.panels.CoordinatePanel;
import gui.GlobalConstants;
import gui.essences.WindowCreator;
import gui.dialogs.WindowSaver;

import javax.swing.*;
import java.awt.*;

public class CoordinareWindow extends JInternalFrame {
    private final String name = "Coordinate";
    private final CoordinatePanel coordinatePanel = new CoordinatePanel();

    public CoordinareWindow()
    {
        super("Координаты", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        GlobalConstants.globalDataTransmitter.addObserver(coordinatePanel);
        panel.add(coordinatePanel, BorderLayout.CENTER);
        getContentPane().add(panel);
        new WindowCreator(this, name).setSizes();
        show();
    }

    @Override
    public void dispose()
    {
        GlobalConstants.globalDataTransmitter.deleteObserver(coordinatePanel);
        Rectangle bounds = this.getBounds();
        WindowSaver size = new WindowSaver(bounds.x, bounds.y, bounds.width, bounds.height, this.isIcon, this.isSelected);
        size.save(name);
        super.dispose();
    }
}
