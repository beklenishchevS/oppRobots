package gui.essences;

import gui.GlobalConstants;
import gui.panels.GameVisualizer;
import gui.windows.GameWindow;
import gui.windows.KeyHolder;

import java.awt.*;
import java.util.*;

public class Evil extends BaseRobot{
    protected double maxVelocity;
    protected final double maxAngularVelocity;

    private final Random random = new Random();

    public Evil(double startX, double startY, Color color, GameWindow gameWindow, GameVisualizer owner, int id)
        {
            super(startX, startY, Color.BLACK, gameWindow, owner, id);
            this.id = id;
            maxVelocity = 0.016 * (3.5);
            maxAngularVelocity = 0.005 * (3);
            timer.schedule(new TimerTask() {
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
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    findTarget();
                }
            }, 0, 50);
        }

    public void changeID()
    {
        id = random.nextInt(GlobalConstants.numberOfRobots);
        while (DataTransmitter.getKilledRobots().contains(id))
        {
            id = random.nextInt(GlobalConstants.numberOfRobots);
        }
    }

    public void setOwner(GameWindow window)
    {
        gameOwner = window;
    }

    public void move()
    {
        double distance = distance(targetPositionX, targetPositionY, positionX, positionY);
        deleteNearestFood();
        if (distance < 8)
        {
            Robot robot = (Robot)DataTransmitter.getRobot(id);
            if (robot == null)
                return;
            synchronized (robot) {
                robot.nullifiedScore();
                changeID();
            }
        }
        super.move();
    }

    protected void findTarget()
    {
        Robot target = (Robot) DataTransmitter.getRobot(id);
        if (target != null)
        {
            targetPositionX = target.getX();
            targetPositionY = target.getY();
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
}
