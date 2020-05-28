package gui.essences;

import gui.GlobalConstants;
import gui.panels.GameVisualizer;
import gui.windows.GameWindow;

import java.awt.*;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseRobot extends Observable {

    protected final Timer timer = initTimer();

    private Timer initTimer() {
        return new Timer("events generator", true);
    }

    protected double positionX;
    protected double positionY;
    protected double robotDirection = 0;

    protected int targetPositionX;
    protected int targetPositionY;

    protected final double maxVelocity;
    protected final double maxAngularVelocity;

    protected Food target;

    protected final Color color;

    protected GameWindow gameOwner;

    protected final GameVisualizer owner;

    private int id;

    protected static final DataTransmitter dataTransmitter = GlobalConstants.globalDataTransmitter;

    public BaseRobot(double startX, double startY, Color color, GameWindow gameWindow, GameVisualizer owner, int id)
    {
        this.id = id;
        this.owner = owner;
        positionX = startX;
        positionY = startY;
        targetPositionX = getX();
        targetPositionY = getY();
        target = new Food(getX(), getY(), Color.yellow, 0);
        this.color = color;
        gameOwner = gameWindow;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                findTarget();
            }
        }, 0, 50);
        maxVelocity = 0.016 * (id + 1);
        maxAngularVelocity = 0.005 * (id + 1);
        addObserver(dataTransmitter);
    }

    public int getX()
    {
        return (int)(positionX + 0.5);
    }

    public int getY()
    {
        return (int)(positionY + 0.5);
    }

    public double getDirection()
    {
        return robotDirection;
    }

    public Color getColor() { return color; }

    public void move()
    {
        double angleToTarget = angleTo(positionX, positionY, targetPositionX, targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        turnRobot(angularVelocity, 4);
        moveRobot(maxVelocity, angularVelocity, 10);
        setChanged();
        notifyObservers(this);
        clearChanged();
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        if (gameOwner == null)
            return;
        velocity = applyLimits(velocity, 0, maxVelocity);
        double newX = positionX + velocity / angularVelocity *
                (Math.sin(robotDirection  + angularVelocity * duration) -
                        Math.sin(robotDirection));
        newX = applyLimits(newX, 0, gameOwner.getWidth() - 20);
        if (!Double.isFinite(newX))
        {
            newX = positionX + velocity * duration * Math.cos(robotDirection);
        }

        double newY = positionY - velocity / angularVelocity *
                (Math.cos(robotDirection  + angularVelocity * duration) -
                        Math.cos(robotDirection));
        newY = applyLimits(newY, 0, gameOwner.getHeight() - 40);
        if (!Double.isFinite(newY))
        {
            newY = positionY + velocity * duration * Math.sin(robotDirection);
        }
        positionX = newX;
        positionY = newY;
    }

    private void turnRobot(double angularVelocity, double duration)
    {
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
        robotDirection = newDirection;
    }

    protected static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
            angle += 2*Math.PI;
        while (angle >= 2*Math.PI)
            angle -= 2*Math.PI;
        return angle;
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    protected abstract void findTarget();

    public int getId()
    {
        return id;
    }

    public Point getCoordinate()
    {
        return new Point(getX(), getY());
    }

}