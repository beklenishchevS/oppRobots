package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;

import javax.swing.*;

import gui.dialogs.CloseDialog;
import gui.dialogs.FrameDialog;
import log.Logger;

/* TODO:
    Что требуется сделать:

 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private GameWindow[] gameWindows = new GameWindow[6];
    private LogWindow logWindow;

    public MainApplicationFrame() {
        int inset = 50;
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);
        setContentPane(desktopPane);
        logWindow = createLogWindow();
        addWindow(logWindow);
        for (int i=0; i<gameWindows.length; i++)
        {
            GameWindow gameWindow = new GameWindow(i);
            gameWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            gameWindow.addInternalFrameListener(new FrameDialog(this, gameWindow));
            addWindow(gameWindow);
            gameWindows[i] = gameWindow;
        }
        setJMenuBar(generateMenuBar());

    }

    public GameWindow getGameWindow(int index)
    {
        return gameWindows[index];
    }

    public GameWindow[] getGameWindows()
    {
        return gameWindows;
    }

    public  LogWindow getLogWindow()
    {
        return logWindow;
    }
    
    protected LogWindow createLogWindow()
    {
        logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        logWindow.addInternalFrameListener(new FrameDialog(this, logWindow));
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
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
