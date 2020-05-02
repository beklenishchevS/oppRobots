package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CoordinatePanel extends JPanel {
    private Point[] coordinates = new Point[GlobalConstants.numberOfRobots];
    private boolean updated = false;
    private final java.util.Timer timer = initTimer();

    private static java.util.Timer initTimer()
    {
        java.util.Timer timer = new Timer("events generator", true);
        return timer;
    }

    public CoordinatePanel()
    {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 100);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateRobotInfo();
            }
        }, 0, 10);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        if (!updated)
            return;
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(Color.BLACK);
        g2d.drawString("сосиска #:", 20, 20);
        g2d.drawString("координаты:", 90, 20);
        for (int i = 0; i < DataTransmitter.numberOfRobot(); i++)
        {
            g2d.drawString(String.valueOf(i), 20, (i + 2) * 20);
            g2d.drawString(coordinates[i].x + " " + coordinates[i].y, 100, (i + 2) * 20);
        }
    }

    private void updateRobotInfo()
    {
        for (int i = 0; i < GlobalConstants.numberOfRobots; i++) {
            if (DataTransmitter.robotCoordinateWasChanged(i)) {
                coordinates[i] = DataTransmitter.getRobotCoordinate(i);
                updated = true;
            }
        }
    }
}
