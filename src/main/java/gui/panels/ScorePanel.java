package gui.panels;

import gui.essences.BaseRobot;
import gui.essences.DataTransmitter;
import gui.GlobalConstants;
import gui.essences.Robot;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScorePanel extends JPanel implements Observer {
    private int[] scores = new int[GlobalConstants.numberOfRobots];

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(Color.BLACK);
        g2d.drawString("сосиска #:", 20, 20);
        g2d.drawString("очки:", 90, 20);
        for (int i = 0; i < DataTransmitter.numberOfRobot(); i++)
        {
            g2d.drawString(String.valueOf(i), 20, (i + 2) * 20);
            g2d.drawString(String.valueOf(scores[i]), 100, (i + 2) * 20);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        Robot robot;
        if (GlobalConstants.canSetRobot((BaseRobot)o)) {
            robot = (Robot)o;
        }
        else
        {
            return;
        }
        int id = robot.getId();
        int score = robot.getCurrentScore();
        scores[id] = score;
        this.repaint();
    }
}
