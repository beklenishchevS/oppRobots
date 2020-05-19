package gui.essences;

import gui.GlobalConstants;
import gui.panels.GameVisualizer;
import gui.windows.GameWindow;
import gui.windows.KeyHolder;

import java.awt.*;
import java.util.*;

public class Robot extends Observable {

    private final Timer timer = initTimer();

    private Timer initTimer() {
        return new Timer("events generator", true);
    }

    private double positionX;
    private double positionY;
    private double robotDirection = 0;
    private int currentScore = 0;

    private int targetPositionX;
    private int targetPositionY;

    private final double maxVelocity;
    private final double maxAngularVelocity;

    private final int gazeLength;

    private boolean targetIsFood = true;

    private Food target;

    private final Color color;

    private final GameWindow gameOwner;

    private final GameVisualizer owner;

    private final int id;

    private static final DataTransmitter dataTransmitter = GlobalConstants.globalDataTransmitter;

    public Robot(double startX, double startY, Color color, GameWindow gameWindow, GameVisualizer owner, int id)
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
        DataTransmitter.registerRobot(id, this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                findTarget();
            }
        }, 0, 50);
        gazeLength = 70 * (id + 1);
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

    public int getGazeLength() {return gazeLength; }

    public void move()
    {
        double distance = distance(targetPositionX, targetPositionY, positionX, positionY);
        if (distance < 10 && targetIsFood)
        {
            synchronized (KeyHolder.scoreKey) {
                currentScore += target.getPrice();
                owner.deleteTarget(target);
                setChanged();
                notifyObservers(this);
                clearChanged();
            }
                return;
        }
        eatNearestFood();
        double velocity = maxVelocity;
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
        moveRobot(velocity, angularVelocity, 10);
        setChanged();
        notifyObservers(this);
        clearChanged();
    }

    private void eatNearestFood()
    {
        Set<Food> foodToDelete = new HashSet<>();
        Set<Food> foodSet = GameVisualizer.getFoodLocation();
        for (Food food: foodSet)
        {
            int foodX = food.getLocationX();
            int foodY = food.getLocationY();
            double remotenessX = Math.abs(foodX - positionX);
            double remotenessY = Math.abs(foodY - positionY);
            if (remotenessX < 20 && remotenessY < 20)
            {
                foodToDelete.add(food);

            }
        }
        for (Food food: foodToDelete)
        {
            currentScore += food.getPrice();
            foodSet.remove(food);
            setChanged();
            notifyObservers(this);
            clearChanged();
        }
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
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

    private static double distance(double x1, double y1, double x2, double y2)
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

    private void findTarget()
    {
        boolean needToFindSaveTarget = countSavingTargetIfNeed();
        if (needToFindSaveTarget) {
            targetIsFood = false;
            return;
        }
        Food target = owner.getFoodAt(getX(), getY());
        if (target != null)
        {
            this.target = target;
            targetPositionX = target.getLocationX();
            targetPositionY = target.getLocationY();
            targetIsFood = true;
            return;
        }
        moveToRandomDirection();
    }

    private void moveToRandomDirection() {
        Random random = new Random();
        int coordX;
        int coordY;
        try
        {
            coordX = random.nextInt(gameOwner.getWidth());
            coordY = random.nextInt(gameOwner.getHeight());
        }
        catch (Exception e)
        {
            coordX = 1;
            coordY = 1;
        }
        targetPositionX = coordX;
        targetPositionY = coordY;
        targetIsFood = false;
    }

    private boolean countSavingTargetIfNeed()
    {
        Point evilCoordinate;
        try {
            evilCoordinate = GlobalConstants.globalEvil.getCoordinate();
        }
        catch (Exception e)
        {
            return false;
        }
        double shiftX = evilCoordinate.x - positionX;
        double shiftY = evilCoordinate.y - positionY;
        if (Math.abs(shiftX) > gazeLength/2 || Math.abs(shiftY) > gazeLength/2)
        {
            return false;
        }
        targetPositionX = (int)(0.9*(positionX - shiftX));
        targetPositionY = (int)((positionY - shiftY)*1.1);
        return true;
    }

    public int getCurrentScore()
    {
        return currentScore;
    }

    public int getId()
    {
        return id;
    }

    public Point getCoordinate()
    {
        return new Point(getX(), getY());
    }

    public void nullifiedScore()
    {
        currentScore = 0;
        setChanged();
        notifyObservers(this);
        clearChanged();
    }
}
