package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CoordinatePanel extends JPanel {
    private final Robot[] robots = new Robot[6];
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
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawString("сосиска #:", 20, 20);
        g2d.drawString("координаты:", 90, 20);
        for (int i = 0; i < DataTransmitter.numberOfRobot(); i++)
        {
            try
            {
                g2d.setPaint(DataTransmitter.getRobot(i).getColor());
                g2d.drawString(String.valueOf(i), 20, (i + 2) * 20);
                String coodinate = (String.valueOf(DataTransmitter.getRobot(i).getX()) +
                        " " +
                        DataTransmitter.getRobot(i).getY());
                g2d.drawString(coodinate, 100, (i + 2) * 20);
            }
            catch (Exception e)
            {
                g2d.setPaint(Color.BLACK);
                g2d.drawString(String.valueOf(i), 20, (i + 2) * 20);
                g2d.drawString("Нет данных", 100, (i + 2) * 20);
            }
        }
    }
}
