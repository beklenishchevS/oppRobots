package gui.essences;

import gui.GlobalConstants;

import java.util.Random;

public class EvilTread extends Thread {
    private volatile Evil evil = new Evil(100, 100, 4);

    public synchronized Evil getEvil()
    {
        return evil;
    }
}
