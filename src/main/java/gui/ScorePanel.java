package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ScorePanel extends JPanel{
    private final Robot[] robots = new Robot[GlobalConstants.numberOfRobots];
    private final Timer timer = initTimer();
    private int[] scores = new int[GlobalConstants.numberOfRobots];
    private boolean updated = false;

    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public ScorePanel()
    {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 10);
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
        g2d.drawString("очки:", 90, 20);
        for (int i = 0; i < DataTransmitter.numberOfRobot(); i++)
        {
            g2d.drawString(String.valueOf(i), 20, (i + 2) * 20);
            g2d.drawString(String.valueOf(scores[i]), 100, (i + 2) * 20);
        }
    }

    private void updateRobotInfo()
    {
        for (int i = 0; i < GlobalConstants.numberOfRobots; i++) {
            if (DataTransmitter.robotScoreWasChanged(i)) {
                scores[i] = DataTransmitter.getRobotScore(i);
                updated = true;
            }
        }
    }
}
