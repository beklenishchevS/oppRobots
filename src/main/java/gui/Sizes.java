package gui;

import java.io.Serializable;

public class Sizes implements Serializable {
    private static final long serialVersionUID = 1L;
    public int xGame;
    public int yGame;
    public int xLog;
    public int yLog;
    public int widthG;
    public int heightG;
    public int widthL;
    public int heightL;
    public boolean iconGame;
    public boolean iconLog;
    public boolean isActiveG;
    public boolean isActiveL;


    public Sizes(int xGame, int yGame, int xLog, int yLog, int widthG, int heightG, int widthL, int heightL, boolean iconGame, boolean iconLog, boolean isActiveG, boolean isActiveL)
    {
        this.xGame = xGame;
        this.yGame = yGame;
        this.xLog = xLog;
        this.yLog = yLog;
        this.widthG = widthG;
        this.heightG = heightG;
        this.widthL = widthL;
        this.heightL = heightL;
        this.iconGame = iconGame;
        this.iconLog = iconLog;
        this.isActiveG = isActiveG;
        this.isActiveL = isActiveL;
    }
}
