package gui.essences;

import gui.windows.GameWindow;
import javafx.util.Pair;

import java.awt.*;

public class MovingFunc {

    public static Pair<Double, Double> moveRobot(double velocity, double angularVelocity, double duration, double maxVelocity, double direction, double positionX, double positionY, GameWindow gameOwner)
    {
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
        return new Pair<>(positionX, positionY);
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
}
