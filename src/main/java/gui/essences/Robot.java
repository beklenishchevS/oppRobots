package gui.essences;

import gui.GlobalConstants;
import gui.panels.GameVisualizer;
import gui.windows.GameWindow;
import gui.windows.KeyHolder;

import java.awt.*;
import java.util.*;

public class Robot extends BaseRobot {
    private int currentScore = 0;

    private final int gazeLength;

    private boolean targetIsFood = true;

    public Robot(double startX, double startY, Color color, GameWindow gameWindow, GameVisualizer owner, int id)
    {
        super(startX, startY, color, gameWindow, owner, id);
        DataTransmitter.registerRobot(id, this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                findTarget();
            }
        }, 0, 50);
        gazeLength = 70 * (id + 1);
        addObserver(dataTransmitter);
    }

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
        super.move();
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

    public void nullifiedScore()
    {
        currentScore = 0;
        setChanged();
        notifyObservers(this);
        clearChanged();
    }
}
