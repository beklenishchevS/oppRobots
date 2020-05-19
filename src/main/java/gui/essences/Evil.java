package gui.essences;

import gui.GlobalConstants;
import gui.panels.GameVisualizer;
import gui.windows.GameWindow;
import gui.windows.KeyHolder;

import java.awt.*;
import java.util.*;

public class Evil {
    private final Timer timer = new Timer("moving generator", true);
    private Timer timerID = new Timer("id changed generator", true);

    private double positionX;
    private double positionY;
    private double direction = 0;

    private int targetPositionX;
    private int targetPositionY;

    private final double maxVelocity;
    private final double maxAngularVelocity;

    private final Random random = new Random();

    private Point target;

    private GameWindow gameOwner;


    private int id;

    public Evil(double startX, double startY, int id)
        {
            this.id = id;
            positionX = startX;
            positionY = startY;
            maxVelocity = 0.016 * (3.5);
            maxAngularVelocity = 0.005 * (3);
            timerID.schedule(new TimerTask() {
                @Override
                public void run() {
                    changeID();
                }
            }, 0, 5000);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    move();
                }
            }, 0, 10);
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
        return direction;
    }

    public void changeID()
    {
        id = random.nextInt(GlobalConstants.numberOfRobots);
        while (DataTransmitter.getKilledRobots().contains(id))
        {
            id = random.nextInt(GlobalConstants.numberOfRobots);
        }
    }

    public boolean canDraw(int robotsID, GameWindow gameWindow)
    {
        if (id == robotsID)
        {
            gameOwner = gameWindow;
            return true;
        }
        return false;
    }

    public void move()
    {
        double angleToTarget = angleTo(positionX, positionY, targetPositionX, targetPositionY);
        double angularVelocity = 0;
        double distance = distance(targetPositionX, targetPositionY, positionX, positionY);
        deleteNearestFood();
        if (distance < 8)
        {
            Robot robot = GlobalConstants.globalDataTransmitter.getRobot(id);
            if (robot == null)
                return;
            synchronized (robot) {
                robot.nullifiedScore();
                changeID();
            }
        }
        if (angleToTarget > direction)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < direction)
        {
            angularVelocity = -maxAngularVelocity;
        }
        turnRobot(angularVelocity, 4);
        moveRobot(maxVelocity, angularVelocity, 10);

    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        findTarget();
        if (gameOwner == null)
            return;
        velocity = applyLimits(velocity, 0, maxVelocity);
        double newX = positionX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) -
                        Math.sin(direction));
        newX = applyLimits(newX, 0, gameOwner.getWidth() - 20);
        if (!Double.isFinite(newX))
        {
            newX = positionX + velocity * duration * Math.cos(direction);
        }

        double newY = positionY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) -
                        Math.cos(direction));
        newY = applyLimits(newY, 0, gameOwner.getHeight() - 40);
        if (!Double.isFinite(newY))
        {
            newY = positionY + velocity * duration * Math.sin(direction);
        }
        positionX = newX;
        positionY = newY;
    }

    private void turnRobot(double angularVelocity, double duration)
    {
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newDirection = asNormalizedRadians(direction + angularVelocity * duration);
        direction = newDirection;
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
        Robot target = GlobalConstants.globalDataTransmitter.getRobot(id);
        if (target != null)
        {
            this.target = target.getCoordinate();
            targetPositionX = this.target.x;
            targetPositionY = this.target.y;
        }
    }

    private void deleteNearestFood()
    {
        Set<Food> foodSet = GameVisualizer.getFoodLocation();
        for (Food food: foodSet)
        {
            int foodX = food.getLocationX();
            int foodY = food.getLocationY();
            double remotenessX = Math.abs(foodX - positionX);
            double remotenessY = Math.abs(foodY - positionY);
            if (remotenessX < 25 && remotenessY < 25)
            {
                food.makeBlack();
            }
        }
    }

    public Point getCoordinate()
    {
        return new Point(getX(), getY());
    }

    @Override
    public String toString() {
        return "Evil{" +
                "id=" + id +
                '}';
    }
}
