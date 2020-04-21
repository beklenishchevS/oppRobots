package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class GameVisualizer extends JPanel
{
    private final Timer timer = initTimer();
    private GameWindow gameOwner;
    private static Set<Food> food = new LinkedHashSet<>();
    public static FoodGenerator foodGenerator = new FoodGenerator(food);
    private static final Color[] colors = new Color[]{Color.yellow, Color.red, Color.cyan, Color.MAGENTA, Color.GREEN, Color.orange};
    
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    private final Robot robot;
    
    public GameVisualizer(GameWindow gameWindow, int id)
    {
        gameOwner = gameWindow;
        robot = new Robot(200, 300, colors[id], gameWindow, this, id);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                generateFood(e.getPoint());
                repaint();
            }
        });
        initializeTimer();
        repaint();
        setDoubleBuffered(true);
    }

    private void initializeTimer()
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                continueMoving();
            }
        }, 0, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                genratePoint();
            }
        }, 0, 500);
    }

    private void genratePoint() {
        Point point = getRandomPoint();
        generateFood(point);
    }

    private void continueMoving() {
        if (robot.isFoundFood() && !food.isEmpty()) {
            food.remove(robot.getTarget());
        }
    }

    private void generateFood(Point point)
    {
        foodGenerator.addPointsRequest(point);
    }

    private Point getRandomPoint()
    {
        Random random = new Random();
        try {
            int x = random.nextInt(gameOwner.getWidth() - 20);
            int y = random.nextInt(gameOwner.getHeight() - 40);
            return new Point(x, y);
        }
        catch (Exception e)
        {
            return new Point(100, 100);
        }
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent()
    {
        robot.move();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        try {
            for (Food food : food) {
                drawTarget(g2d, food);
            }
        }
        catch (Exception e)
        {

        }
        drawRobot(g2d, robot.getDirection());
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawRect(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawRect(centerX - diam1/2, centerY - diam2/2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, double direction)
    {
        int robotCenterX = robot.getX();
        int robotCenterY = robot.getY();
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(robot.getColor());
        fillOval(g, robotCenterX, robotCenterY, 40, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 40, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        //drawRect(g, robotCenterX, robotCenterY, robot.getGazeLength(), robot.getGazeLength());
    }
    
    private void drawTarget(Graphics2D g, Food food)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(food.getColor());
        int pointRadius = 4 * (food.getPrice() + 1);
        fillOval(g, food.getLocationX(), food.getLocationY(), pointRadius, pointRadius);
        g.setColor(Color.BLACK);
        drawOval(g, food.getLocationX(), food.getLocationY(), pointRadius, pointRadius);
    }

    public Food getFoodAt(int x, int y)
    {
        for (Food target: food)
        {
            int foodX = target.getLocationX();
            int foodY = target.getLocationY();
            if (Math.abs(foodX - x) < robot.getGazeLength()/2 && Math.abs(foodY - y) < robot.getGazeLength()/2)
            {
                return target;
            }
        }
        return null;
    }
}
