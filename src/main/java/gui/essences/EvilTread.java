package gui.essences;

import java.awt.*;

public class EvilTread extends Thread {

    @Override
    public void run() {
        DataTransmitter.setEvil(new Evil(100, 100, Color.BLACK, null, null, 4));
    }

}
