package gui.dialogs;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class WindowSaver implements Serializable {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean icon;
    public boolean isActive;

    public WindowSaver(int x, int y, int width, int heigth, boolean icon, boolean isActive)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = heigth;
        this.icon = icon;
        this.isActive = isActive;
    }

    public void save(String name)
    {
        try
        {
            FileOutputStream outputStream = new FileOutputStream("/Users/BeklenishevaT/Desktop/oppRobots/src/main/java/"+name+".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }
        catch (Exception e){
            //just ignore
        }
    }


}
