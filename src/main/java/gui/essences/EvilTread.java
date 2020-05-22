package gui.essences;

import gui.GlobalConstants;

import java.awt.*;
import java.util.Random;

public class EvilTread extends Thread {
    private Evil evil = null;

    @Override
    public void run() {
        evil = new Evil(100, 100, Color.BLACK, null, null, 4);
        super.run();

    }

    public synchronized Evil getEvil()
    {
        return evil;
    }
}
