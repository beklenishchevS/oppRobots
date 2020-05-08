package gui.Panels;

import gui.Essences.DataTransmitter;
import gui.GlobalConstants;
import gui.Essences.Robot;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatePanel extends JPanel implements Observer {
    private Point[] coordinates = new Point[GlobalConstants.numberOfRobots];

    @Override
    public void paint(Graphics g)
    {
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

    @Override
    public void update(Observable observable, Object o)
    {
        Robot robot = (Robot)o;
        int id = robot.getId();
        Point coordinate = robot.getCoordinate();
        coordinates[id] = coordinate;
        this.repaint();
    }
}
