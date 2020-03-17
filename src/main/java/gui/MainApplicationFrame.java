package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;

import gui.dialogs.CloseDialog;
import gui.dialogs.FrameDialog;
import javafx.util.Pair;
import log.Logger;

/* TODO:
    Что требуется сделать:

 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private GameWindow gameWindow;
    private LogWindow logWindow;
    private Sizes sizes;
    private JInternalFrame selected;

    public MainApplicationFrame() {
        int inset = 50;
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        Pair<Pair<Rectangle, Rectangle>, Pair<Boolean, Boolean>> pair = createSizes();
        logWindow = createLogWindow(pair.getKey().getValue());
        addWindow(logWindow);
        Rectangle gameSize = pair.getKey().getKey();
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameWindow.addInternalFrameListener(new FrameDialog(this, gameWindow));
        try
        {
            setSizes(gameWindow, gameSize.x, gameSize.y, gameSize.width, gameSize.height);
        }
        catch (Exception e)
        {
            setSizes(gameWindow, 220, 10, screenSize.width - 220, screenSize.height - 200);
        }
        addWindow(gameWindow);
        setJMenuBar(generateMenuBar());
        setIconInternalFrames(pair);
        selected = (sizes.isActiveL)? logWindow : gameWindow;
        selected.toFront();

    }

    private void setIconInternalFrames(Pair<Pair<Rectangle, Rectangle>, Pair<Boolean, Boolean>> pair) {
        try
        {
            gameWindow.setIcon(pair.getValue().getKey());
            logWindow.setIcon(pair.getValue().getValue());
        } catch (PropertyVetoException e)
        {
        }
    }

    private void setSizes(JInternalFrame frame, int x, int y, int width, int height) {
        frame.setLocation(x, y);
        frame.setSize(width,  height);
        frame.setResizable(true);
    }

    public GameWindow getGameWindow()
    {
        return gameWindow;
    }

    public  LogWindow getLogWindow()
    {
        return logWindow;
    }
    
    protected LogWindow createLogWindow(Rectangle sizes)
    {
        logWindow = new LogWindow(Logger.getDefaultLogSource());
        try
        {
            setSizes(logWindow, sizes.x, sizes.y, sizes.width, sizes.height);
        }
        catch (Exception e)
        {
            setSizes(logWindow, 10, 10, 210, screenSize.height - 200);
        }
        try {
            logWindow.setIcon(true);
        }
        catch (Exception e)
        {
            //just ignore
        }
        logWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        logWindow.addInternalFrameListener(new FrameDialog(this, logWindow));
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private Pair<Pair<Rectangle, Rectangle>, Pair<Boolean, Boolean>> createSizes()
    {
        Sizes sizes = null;
        try
        {
            FileInputStream inputStream = new FileInputStream("/Users/BeklenishevaT/Desktop/oppRobots/src/main/java/mem.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            sizes = (Sizes) objectInputStream.readObject();
        }
        catch (Exception e)
        {
            //just ignore
        }
        Rectangle rectangleGame = new Rectangle(sizes.xGame, sizes.yGame, sizes.widthG, sizes.heightG);
        Rectangle rectangleLog = new Rectangle(sizes.xLog, sizes.yLog, sizes.widthL, sizes.heightL);
        this.sizes = sizes;
        return new Pair<>(new Pair<>(rectangleGame, rectangleLog), new Pair<>(sizes.iconGame, sizes.iconLog));
    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = makeMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");

        JMenu exitMenu = makeMenu("Выход", KeyEvent.VK_V, "Выход из игры");

        lookAndFeelMenu.add(makeMenuItem("Системная схема",
                (event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                }));
        lookAndFeelMenu.add(makeMenuItem("Универсальная схема",
                (event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                }));

        JMenu testMenu = makeMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        testMenu.add(makeMenuItem("Сообщение в лог",
                (event) -> Logger.debug("Новая строка"))
                );

        exitMenu.add(makeMenuItem("Выход",
                (event) ->
                {
                    CloseDialog closeDialog = new CloseDialog(this);
                    closeDialog.onPushedCloseButton(event);
                }
                ));

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private JMenuItem makeMenuItem(String text, ActionListener listener)
    {
        JMenuItem menuItem = new JMenuItem(text, KeyEvent.VK_S);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    private JMenu makeMenu(String name, int event, String description)
    {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(event);
        menu.getAccessibleContext().setAccessibleDescription(
                description);
        return menu;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
