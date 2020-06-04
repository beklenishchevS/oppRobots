package gui.panels;

import gui.GlobalConstants;
import gui.essences.*;
import gui.essences.Robot;
import gui.windows.GameWindow;
import gui.windows.KeyHolder;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;

public class GameVisualizer extends JPanel
{
    private final Timer timer = initTimer();
    private GameWindow gameOwner;
    private final int id;
    private static Set<Food> food = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public static FoodGenerator foodGenerator = new FoodGenerator(food);
    private static final Color[] colors = new Color[]{Color.blue, Color.red, Color.cyan, Color.MAGENTA, Color.GREEN, Color.orange};
    
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    private Robot robot;
    private Evil evil;
    
    public GameVisualizer(GameWindow gameWindow, int id)
    {
        gameOwner = gameWindow;
        synchronized (GameVisualizer.class) {
            DataTransmitter.registerEvil();
            evil = DataTransmitter.getEvil();
        }
        this.id = id;
        robot = new Robot(200, 300, colors[id], gameWindow, this, id);
        initializeTimer();
        repaint();
        setDoubleBuffered(true);
        setOpaque(true);
    }

    private void initializeTimer()
    {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                synchronized (KeyHolder.paintKey)
                {
                    onRedrawEvent();
                }
            }
        }, 0, 1);
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                genratePoint();
            }
        }, 0, 50);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deleteBlackFood();
            }
        }, 0, 10000);
    }

    private void genratePoint() {
        Point point = getRandomPoint();
        generateFood(point);
    }

    private void deleteBlackFood()
    {
        Set<Food> foodToDelete = new HashSet<>();
        for (Food point: food)
        {
            if (point.isBlack())
                foodToDelete.add(point);
        }
        for (Food point: foodToDelete)
        {
            food.remove(point);
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
        this.repaint();
    }

    protected void onModelUpdateEvent()
    {
        try {
            synchronized (KeyHolder.robotLocationKey) {
                robot.move();
            }
        }
        catch (Exception e)
        {
            //just ignore
        }
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D gd = (Graphics2D) g;
        Image buffer = createImage(getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D)buffer.getGraphics();
        try {
            for (Food food : food) {
                drawTarget(g2d, food);
            }
        } catch (Exception ignored) {

        }
        drawRobot(g2d, robot.getDirection());
        if (evil != null)
            drawEvil(g2d, evil.getDirection());
        gd.drawImage(buffer, 0, 0, null);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, double direction)
    {
        draw(g, direction, robot);
    }

    private void drawEvil(Graphics2D g, double direction)
    {
        synchronized (DataTransmitter.class)
        {
            if (isEvilHere())
                draw(g, direction, evil);
        }
    }

    private void draw(Graphics2D g, double direction, BaseRobot robot)
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
    }
    
    private void drawTarget(Graphics2D g, Food food)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(food.getColor());
        int pointRadius;
        if (food.getPrice() > 0)
            pointRadius = 4 * (food.getPrice() + 1);
        else
            pointRadius = 12;
        fillOval(g, food.getLocationX(), food.getLocationY(), pointRadius, pointRadius);
        g.setColor(Color.BLACK);
        drawOval(g, food.getLocationX(), food.getLocationY(), pointRadius, pointRadius);
    }

    public Food getFoodAt(int x, int y) {
        try {
            for (Food target : food) {
                int foodX = target.getLocationX();
                int foodY = target.getLocationY();
                if (Math.abs(foodX - x) < robot.getGazeLength() / 2 && Math.abs(foodY - y) < robot.getGazeLength() / 2) {
                    return target;
                }
            }
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void deleteTarget(Food target)
    {
        food.remove(target);
        repaint();
    }

    public void dispose()
    {
        robot = null;
    }

    public static Set<Food> getFoodLocation()
    {
        return food;
    }

    public boolean isEvilHere()
    {
        int id = evil.getId();
        if (id == this.id) {
            evil.setOwner(gameOwner);
            return true;
        }
        return false;
    }
}
