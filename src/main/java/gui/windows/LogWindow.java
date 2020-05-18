package gui.windows;

import java.awt.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import gui.essences.WindowCreator;
import gui.dialogs.WindowSaver;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private LogWindowSource logSource;
    private TextArea logContent;
    private final String name = "log";

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        updateLogContent();
        new WindowCreator(this, name).setSizes();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void unregisterListener() {
        logSource.unregisterListener(this);
    }

    @Override
    public void dispose()
    {
        Rectangle bounds = this.getBounds();
        WindowSaver size = new WindowSaver(bounds.x, bounds.y, bounds.width, bounds.height, this.isIcon, this.isSelected);
        size.save(name);
        unregisterListener();
        super.dispose();
    }
}
