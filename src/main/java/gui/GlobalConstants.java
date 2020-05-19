package gui;

import gui.essences.DataTransmitter;
import gui.essences.Evil;
import gui.essences.EvilTread;

import java.util.Random;

public class GlobalConstants {
    private final static Random random = new Random();
    public final static int numberOfRobots = 6;
    public final static DataTransmitter globalDataTransmitter = new DataTransmitter();
    private volatile static EvilTread evilThread = new EvilTread();
    public volatile static Evil globalEvil;
    private static boolean was = false;

    static {
        evilThread.start();
        globalEvil = evilThread.getEvil();
    }
}
