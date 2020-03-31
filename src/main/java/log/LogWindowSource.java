package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 *
 *
 * объекты которые не будут больше использоваться но не удалены сборщиком мусора
 */
public class LogWindowSource
{
    private int m_iQueueLength;
    
    private ArrayList<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;
    private ConcurrentLinkedQueue<LogEntry> messages;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayList<>(iQueueLength);
        m_listeners = new ArrayList<>();
        messages = new ConcurrentLinkedQueue<>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        synchronized (messages)
        {
            addMessage(entry);
        }
        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }


        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }


    public void cleanMessages()
    {
        messages = new ConcurrentLinkedQueue<>();
    }

    public int size()
    {
        return messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return m_messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return messages;
    }

    private void addMessage(LogEntry entry)
    {
        if (messages.size() == m_iQueueLength)
        {
            messages.poll();
        }
        messages.add(entry);
    }
}
