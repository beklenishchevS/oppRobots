package gui.essences;

import gui.GlobalConstants;

import java.awt.*;
import java.util.Random;

public class EvilTread extends Thread {
    @Override
    public void run() {
        GlobalConstants.globalEvil = new Evil(100, 100, Color.BLACK, null, null, 4);
    }
}
