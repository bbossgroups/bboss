// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexerapplications/thumbelina/Thumbelina.java,v $
// $Author: derrickoswald $
// $Date: 2005/02/13 20:36:00 $
// $Revision: 1.7 $
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//

package org.htmlparser.lexerapplications.thumbelina;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.ParserException;

/**
 * View images behind thumbnails.
 */
public class Thumbelina
    extends
        JPanel // was: java.awt.Canvas
    implements
        Runnable,
        ItemListener,
        ChangeListener,
        ListSelectionListener
{
    /**
     * Property name for current URL binding.
     */
    public static final String PROP_CURRENT_URL_PROPERTY = "currentURL";
    /**
     * Property name for queue size binding.
     */
    public static final String PROP_URL_QUEUE_PROPERTY = "queueSize";
    /**
     * Property name for visited URL size binding.
     */
    public static final String PROP_URL_VISITED_PROPERTY = "visitedSize";

    /**
     * URL's to visit.
     */
    private ArrayList mUrls;

    /**
     * URL's visited.
     */
    protected HashMap mVisited;

    /**
     * Images requested.
     */
    protected HashMap mRequested;

    /**
     * Images being tracked currently.
     */
    protected HashMap mTracked;

    /**
     * Background thread.
     */
    protected Thread mThread;

    /**
     * Activity state.
     * <code>true</code> means processing URLS, <code>false</code> not.
     */
    protected boolean mActive;

    /**
     * The picture sequencer.
     */
    protected Sequencer mSequencer;

    /**
     * The central area for pictures.
     */
    protected PicturePanel mPicturePanel;

    /**
     * Value returned when no links are discovered.
     */
    protected static final URL[][] NONE = { { }, { } };

    /**
     * Bound property support.
     */
    protected PropertyChangeSupport mPropertySupport;

    /**
     * The URL being currently being examined.
     */
    protected String mCurrentURL;

    /**
     * If <code>true</code>, does not follow links containing cgi calls.
     */
    protected boolean mDiscardCGI;

    /**
     * If <code>true</code>, does not follow links containing queries (?).
     */
    protected boolean mDiscardQueries;

    /**
     * Background thread checkbox in status bar.
     */
    protected JCheckBox mBackgroundToggle;

    /**
     * History list.
     */
    protected JList mHistory;

    /**
     * Scroller for the picture panel.
     */
    protected JScrollPane mPicturePanelScroller;

    /**
     * Scroller for the history list.
     */
    protected JScrollPane mHistoryScroller;

    /**
     * Main panel in central area.
     */
    protected JSplitPane mMainArea;

    /**
     * Status bar.
     */
    protected JPanel mPowerBar;

    /**
     * Image request queue monitor in status bar.
     */
    protected JProgressBar mQueueProgress;

    /**
     * Image ready queue monitor in status bar.
     */
    protected JProgressBar mReadyProgress;

    /**
     * Sequencer thread toggle in status bar.
     */
    protected JCheckBox mRunToggle;

    /**
     * Sequencer speed slider in status bar.
     */
    protected JSlider mSpeedSlider;

    /**
     * URL report in status bar.
     */
    protected JTextField mUrlText;

    /**
     * URL queue size display in status bar.
     */
    protected JLabel mQueueSize;

    /**
     * URL visited count display in status bar.
     */
    protected JLabel mVisitedSize;

    /**
     * Creates a new instance of Thumbelina.
     */
    public Thumbelina ()
    {
        this ((URL)null);
    }

    /**
     * Creates a new instance of Thumbelina.
     * @param url Single URL to enter into the 'to follow' list.
     * @exception MalformedURLException If the url is malformed.
     */
    public Thumbelina (final String url)
        throws
            MalformedURLException
    {
        this (null == url ? null : new URL (url));
    }

    /**
     * Creates a new instance of Thumbelina.
     * @param url URL to enter into the 'to follow' list.
     */
    public Thumbelina (final URL url)
    {
        mUrls = new ArrayList ();
        mVisited = new HashMap ();
        mRequested = new HashMap ();
        mTracked = new HashMap ();
        mThread = null;
        mActive = true;
        mPicturePanel = new PicturePanel (this);
        mSequencer = new Sequencer (this);
        mPropertySupport = new PropertyChangeSupport (this);
        mCurrentURL = null;
        mDiscardCGI = true;
        mDiscardQueries = true;

        // JComponent specific
        setDoubleBuffered (false);
        setLayout (new java.awt.BorderLayout ());
        mPicturePanel.setDoubleBuffered (false);

        mThread = new Thread (this);
        mThread.setName ("BackgroundThread");
        mThread.start ();
        initComponents ();

        mRunToggle.addItemListener (this);
        mBackgroundToggle.addItemListener (this);
        mSpeedSlider.addChangeListener (this);
        mHistory.addListSelectionListener (this);

        memCheck ();

        if (null != url)
            append (url);
    }

    /**
     * Check for low memory situation.
     * Report to the user a bad situation.
     */
    protected void memCheck ()
    {
        Runtime runtime;
        long maximum;

        if (System.getProperty ("java.version").startsWith ("1.4"))
        {
            runtime = Runtime.getRuntime ();
            runtime.gc ();
            maximum = runtime.maxMemory ();
            if (maximum < 67108864L) // 64MB
                JOptionPane.showMessageDialog (
                    null,
                    "Maximum available memory is low (" + maximum + " bytes).\n"
                    + "\n"
                    + "It is strongly suggested to increase the maximum memory\n"
                    + "available by using the JVM command line switch -Xmx with\n"
                    + "a suitable value, such as -Xmx256M for example.",
                    "Thumbelina - Low memory",
                    JOptionPane.WARNING_MESSAGE,
                    null /*Icon*/);
        }
    }

    /**
     * Reset this Thumbelina.
     * Clears the sequencer of pending images, resets the picture panel,
     * emptiies the 'to be examined' list of URLs.
     */
    public void reset ()
    {
        int oldsize;

        synchronized (mUrls)
        {
            mSequencer.reset ();
            mPicturePanel.reset ();
            oldsize = mUrls.size ();
            mUrls.clear ();
        }
        updateQueueSize (oldsize, mUrls.size ());
    }

    /**
     * Append the given URL to the queue.
     * Adds the url only if it isn't already in the queue,
     * and notifys listeners about the addition.
     * @param url The url to add.
     */
    public void append (final URL url)
    {
        String href;
        boolean found;
        URL u;
        int oldsize;

        href = url.toExternalForm ();
        found = false;
        oldsize = -1;
        synchronized (mUrls)
        {
            for (int i = 0; !found && (i < mUrls.size ()); i++)
            {
                u = (URL)mUrls.get (i);
                if (href.equals (u.toExternalForm ()))
                    found = true;
            }
            if (!found)
            {
                oldsize = mUrls.size ();
                mUrls.add (url);
                mUrls.notify ();
            }
        }
        if (-1 != oldsize)
            updateQueueSize (oldsize, mUrls.size ());
    }

    /**
     * Append the given URLs to the queue.
     * @param list The list of URL objects to add.
     */
    public void append (final ArrayList list)
    {
        for (int i = 0; i < list.size (); i++)
            append ((URL)list.get (i));
    }

    /**
     * Filter URLs and add to queue.
     * Removes already visited links and appends the rest (if any) to the
     * visit pending list.
     * @param urls The list of URL's to add to the 'to visit' list.
     * @return Returns the filered list.
     */
    protected ArrayList filter (final URL[] urls)
    {
        ArrayList list;
        URL url;
        String ref;

        list = new ArrayList ();
        for (int i = 0; i < urls.length; i++)
        {
            url = urls[i];
            ref = url.toExternalForm ();
            // ignore cgi
            if (!mDiscardCGI || (-1 == ref.indexOf ("/cgi-bin/")))
                // ignore queries
                if (!mDiscardQueries || (-1 == ref.indexOf ("?")))
                    // ignore duplicates
                    if (!mVisited.containsKey (ref))
                    {
                        try
                        {
                            url.openConnection ();
                            list.add (url);
                        }
                        catch (IOException ioe)
                        {
                            // unknown host or some other problem... discard
                        }
                    }
        }

        return (list);
    }

    /**
     * Initialize the GUI.
     */
    private void initComponents ()
    {
        mPowerBar = new JPanel ();
        mUrlText = new JTextField ();
        mRunToggle = new JCheckBox ();
        mSpeedSlider = new JSlider ();
        mReadyProgress = new JProgressBar ();
        mQueueProgress = new JProgressBar ();
        mBackgroundToggle = new JCheckBox ();
        mMainArea = new JSplitPane ();
        mPicturePanelScroller = new JScrollPane ();
        mHistoryScroller = new JScrollPane ();
        mHistory = new JList ();
        mQueueSize = new JLabel ();
        mVisitedSize = new JLabel ();

        mPowerBar.setLayout (new BoxLayout (mPowerBar, BoxLayout.X_AXIS));

        mPowerBar.setBorder (new BevelBorder (BevelBorder.LOWERED));
        mPowerBar.add (mUrlText);

        mRunToggle.setSelected (true);
        mRunToggle.setText ("On/Off");
        mRunToggle.setToolTipText ("Starts/stops the image presentation.");
        mPowerBar.add (mRunToggle);

        mSpeedSlider.setMajorTickSpacing (1000);
        mSpeedSlider.setMaximum (5000);
        mSpeedSlider.setPaintTicks (true);
        mSpeedSlider.setToolTipText ("Set inter-image delay.");
        mSpeedSlider.setValue (500);
        mSpeedSlider.setInverted (true);
        mPowerBar.add (mSpeedSlider);

        mReadyProgress.setToolTipText ("Pending images..");
        mReadyProgress.setStringPainted (true);
        mPowerBar.add (mReadyProgress);

        mQueueProgress.setToolTipText ("Outstanding image fetches..");
        mQueueProgress.setStringPainted (true);
        mPowerBar.add (mQueueProgress);

        mBackgroundToggle.setSelected (true);
        mBackgroundToggle.setText ("On/Off");
        mBackgroundToggle.setToolTipText ("Starts/stops background fetching.");
        mPowerBar.add (mBackgroundToggle);

        mVisitedSize.setBorder (new BevelBorder (BevelBorder.LOWERED));
        mVisitedSize.setText ("00000");
        mVisitedSize.setToolTipText ("Number of URLs examined.");
        mPowerBar.add (mVisitedSize);
        mQueueSize.setBorder (new BevelBorder (BevelBorder.LOWERED));
        mQueueSize.setText ("00000");
        mQueueSize.setToolTipText ("Number of URLs in queue.");
        mPowerBar.add (mQueueSize);

        mHistory.setModel (new DefaultListModel ());
        mHistory.setToolTipText ("History");
        mHistory.setDoubleBuffered (false);
        mHistory.setSelectionMode (
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        mHistoryScroller.setViewportView (mHistory);
        mHistoryScroller.setDoubleBuffered (false);
        mPicturePanelScroller.setViewportView (mPicturePanel);
        mPicturePanelScroller.setDoubleBuffered (false);
        mPicturePanelScroller.setHorizontalScrollBarPolicy (
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        mPicturePanelScroller.setVerticalScrollBarPolicy (
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add (mMainArea, java.awt.BorderLayout.CENTER);
        mMainArea.setLeftComponent (mHistoryScroller);
        mMainArea.setRightComponent (mPicturePanelScroller);
        add (mPowerBar, java.awt.BorderLayout.SOUTH);
    }

    /**
     * Gets the state of status bar visibility.
     * @return <code>true</code> if the status bar is visible.
     */
    public boolean getStatusBarVisible ()
    {
        boolean ret;

        ret = false;

        for (int i = 0; !ret && (i < getComponentCount ()); i++)
            if (mPowerBar == getComponent (i))
                ret = true;

        return (ret);
    }

    /**
     * Sets the status bar visibility.
     * @param visible The new visibility state.
     * If <code>true</code>, the status bar will be unhidden.
     */
    public void setStatusBarVisible (final boolean visible)
    {
        int index;

        index = -1;
        for (int i = 0; (-1 == index) && (i < getComponentCount ()); i++)
            if (mPowerBar == getComponent (i))
                index = i;
        if (visible)
        {
            if (-1 == index)
            {
                add (mPowerBar, java.awt.BorderLayout.SOUTH);
                invalidate ();
                validate ();
            }
        }
        else
            if (-1 != index)
            {
                remove (index);
                invalidate ();
                validate ();
            }
    }

    /**
     * Gets the state of history list visibility.
     * @return <code>true</code> if the history list is visible.
     */
    public boolean getHistoryListVisible ()
    {
        boolean ret;

        ret = false;

        for (int i = 0; !ret && (i < getComponentCount ()); i++)
            // check indirectly because the history list is in a splitter
            if (mMainArea == getComponent (i))
                ret = true;

        return (ret);
    }

    /**
     * Sets the history list visibility.
     * @param visible The new visibility state.
     * If <code>true</code>, the history list will be unhidden.
     */
    public void setHistoryListVisible (final boolean visible)
    {
        int pictpanel;
        int splitter;
        Component component;

        pictpanel = -1;
        splitter = -1;
        for (int i = 0; i < getComponentCount (); i++)
        {
            component = getComponent (i);
            if (mPicturePanelScroller == component)
                pictpanel = i;
            else if (mMainArea == component)
                splitter = i;
        }
        if (visible)
        {
            if (-1 != pictpanel)
            {
                remove (pictpanel);
                add (mMainArea, java.awt.BorderLayout.CENTER);
                mMainArea.setLeftComponent (mHistoryScroller);
                //mPicturePanelScroller.setViewportView (mPicturePanel);
                mMainArea.setRightComponent (mPicturePanelScroller);
                invalidate ();
                validate ();
            }
        }
        else
            if (-1 != splitter)
            {
                remove (splitter);
                add (mPicturePanelScroller, java.awt.BorderLayout.CENTER);
                invalidate ();
                validate ();
            }
    }

    /**
     * Gets the state of the sequencer thread.
     * @return <code>true</code> if the thread is pumping images.
     */
    public boolean getSequencerActive ()
    {
        return (mSequencer.mActive);
    }

    /**
     * Sets the sequencer activity state.
     * The sequencer is the thread that moves images from the pending list
     * to the picture panel on a timed basis.
     * @param active The new activity state.
     * If <code>true</code>, the sequencer will be turned on.
     * This may alter the speed setting if it is set to zero.
     */
    public void setSequencerActive (final boolean active)
    {
        // check the delay is not zero
        if (0 == getSpeed ())
            setSpeed (Sequencer.DEFAULT_DELAY);
        mSequencer.mActive = active;
        if (active)
            synchronized (mSequencer.mPending)
            {
                mSequencer.mPending.notify ();
            }
        if (active != mRunToggle.isSelected ())
            mRunToggle.setSelected (active);
    }

    /**
     * Gets the state of the background thread.
     * @return <code>true</code> if the thread is examining web pages.
     */
    public boolean getBackgroundThreadActive ()
    {
        return (mActive);
    }

    /**
     * Sets the state of the background thread activity.
     * The background thread is responsible for examining URLs that are on
     * the queue for thumbnails, and starting the image fetch operation.
     * @param active If <code>true</code>,
     * the background thread will be turned on.
     */
    public void setBackgroundThreadActive (final boolean active)
    {
        mActive = active;
        if (active)
            synchronized (mUrls)
            {
                mUrls.notify ();
            }
        if (active != mBackgroundToggle.isSelected ())
            mBackgroundToggle.setSelected (active);
    }

    /**
     * Get the sequencer delay time.
     * @return The number of milliseconds between image additions to the panel.
     */
    public int getSpeed ()
    {
        return (mSequencer.getDelay ());
    }

    /**
     * Set the sequencer delay time.
     * The sequencer is the thread that moves images from the pending list
     * to the picture panel on a timed basis. This value sets the number of
     * milliseconds it waits between pictures.
     * Setting it to zero toggles the running state off.
     * @param speed The sequencer delay in milliseconds.
     */
    public void setSpeed (final int speed)
    {
        if (0 == speed)
            mRunToggle.setSelected (false);
        else
        {
            mRunToggle.setSelected (true);
            mSequencer.setDelay (speed);
        }
        if (speed != mSpeedSlider.getValue ())
            mSpeedSlider.setValue (speed);
    }

    /**
     * Getter for property discardCGI.
     * @return Value of property discardCGI.
     *
     */
    public boolean isDiscardCGI ()
    {
        return (mDiscardCGI);
    }

    /**
     * Setter for property discardCGI.
     * @param discard New value of property discardCGI.
     *
     */
    public void setDiscardCGI (final boolean discard)
    {
        mDiscardCGI = discard;
    }

    /**
     * Getter for property discardQueries.
     * @return Value of property discardQueries.
     *
     */
    public boolean isDiscardQueries ()
    {
        return (mDiscardQueries);
    }

    /**
     * Setter for property discardQueries.
     * @param discard New value of property discardQueries.
     *
     */
    public void setDiscardQueries (final boolean discard)
    {
        mDiscardQueries = discard;
    }

    /**
     * Check if the url looks like an image.
     * @param url The usrl to check for image characteristics.
     * @return <code>true</code> if the url ends in a recognized image
     * extension.
     */
    protected boolean isImage (final String url)
    {
        String lower = url.toLowerCase ();
        return (lower.endsWith (".jpg") || lower.endsWith (".gif")
            || lower.endsWith (".png"));
    }

    /**
     * Get the links of an element of a document.
     * Only gets the links on IMG elements that reference another image.
     * The latter is based on suffix (.jpg, .gif and .png).
     * @param lexer The fully conditioned lexer, ready to rock.
     * @param docbase The url to read.
     * @return The URLs, targets of the IMG links;
     * @exception IOException If the underlying infrastructure throws it.
     * @exception ParserException If there is a problem parsing the url.
     */
    protected URL[][] extractImageLinks (final Lexer lexer, final URL docbase)
        throws
            IOException,
            ParserException
    {
        HashMap images;
        HashMap links;
        boolean ina; // true when within a <A></A> pair
        Node node;
        Tag tag;
        String name;
        Tag startatag;
        Tag imgtag;
        String href;
        String src;
        URL url;
        URL[][] ret;

        images = new HashMap ();
        links = new HashMap ();
        ina = false;
        startatag = null;
        imgtag = null;
        while (null != (node = lexer.nextNode ()))
        {
            if (node instanceof Tag)
            {
                tag = (Tag)node;
                name = tag.getTagName ();
                if ("A".equals (name))
                {
                    if (tag.isEndTag ())
                    {
                        ina = false;
                        if (null != imgtag)
                        {
                            // evidence of a thumb
                            href = startatag.getAttribute ("HREF");
                            if (null != href)
                            {
                                if (isImage (href))
                                {
                                    src = imgtag.getAttribute ("SRC");
                                    if (null != src)
                                        try
                                        {
                                            url = new URL (docbase, href);
                                            // eliminate duplicates
                                            href = url.toExternalForm ();
                                            if (!images.containsKey (href))
                                                images.put (href, url);
                                        }
                                        catch (MalformedURLException murle)
                                        {
                                            // oops, forget it
                                        }
                                }
                            }
                        }
                    }
                    else
                    {
                        startatag = tag;
                        imgtag = null;
                        ina = true;
                        href = startatag.getAttribute ("HREF");
                        if (null != href)
                        {
                            if (!isImage (href))
                                try
                                {
                                    url = new URL (docbase, href);
                                    // eliminate duplicates
                                    href = url.toExternalForm ();
                                    if (!links.containsKey (href))
                                        links.put (href, url);
                                }
                                catch (MalformedURLException murle)
                                {
                                    // well, obviously we don't want this one
                                }
                        }
                    }
                }
                else if (ina && "IMG".equals (name))
                    imgtag = tag;
            }
        }

        ret = new URL[2][];
        ret[0] = new URL[images.size ()];
        images.values ().toArray (ret[0]);
        ret[1] = new URL[links.size ()];
        links.values ().toArray (ret[1]);

        return (ret);
    }

    /**
     * Get the image links from the current URL.
     * @param url The URL to get the links from
     * @return An array of two URL arrays, index 0 is a list of images,
     * index 1 is a list of links to possibly follow.
     */
    protected URL[][] getImageLinks (final URL url)
    {
        Lexer lexer;
        URL[][] ret;

        if (null != url)
        {
            try
            {
                lexer = new Lexer (url.openConnection ());
                ret = extractImageLinks (lexer, url);
            }
            catch (Throwable t)
            {
                System.out.println (t.getMessage ());
                ret = NONE;
            }
        }
        else
            ret =  NONE;

        return (ret);
    }

    /**
     * Get the picture panel object encapsulated by this Thumbelina.
     * @return The picture panel.
     */
    public PicturePanel getPicturePanel ()
    {
        return (mPicturePanel);
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener (
        final PropertyChangeListener listener)
    {
        mPropertySupport.addPropertyChangeListener (listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered for all
     * properties.
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener (
        final PropertyChangeListener listener)
    {
        mPropertySupport.removePropertyChangeListener (listener);
    }

    /**
     * Return the URL currently being examined.
     * This is a bound property. Notifications are available via the
     * PROP_CURRENT_URL_PROPERTY property.
     * @return The size of the 'to be examined' list.
     */
    public String getCurrentURL ()
    {
        return (mCurrentURL);
    }

    /**
     * Set the current URL being examined.
     * @param url The url that is being examined.
     */
    protected void setCurrentURL (final String url)
    {
        String oldValue;

        if (((null != url) && !url.equals (mCurrentURL))
            || ((null == url) && (null != mCurrentURL)))
        {
            oldValue = mCurrentURL;
            mCurrentURL = url;
            mPropertySupport.firePropertyChange (
                PROP_CURRENT_URL_PROPERTY, oldValue, url);
        }
    }

    /**
     * Apply a change in 'to be examined' URL list size.
     * Sends notification via the <code>PROP_URL_QUEUE_PROPERTY</code> property
     * and updates the status bar.
     * @param original The original size of the list.
     * @param current The new size of the list.
     */
    protected void updateQueueSize (final int original, final int current)
    {
        StringBuffer buffer;

        buffer = new StringBuffer ();
        buffer.append (current);
        while (buffer.length () < 5)
            buffer.insert (0, '0');
        mQueueSize.setText (buffer.toString ());
        mPropertySupport.firePropertyChange (
            PROP_URL_QUEUE_PROPERTY, original, current);
    }

    /**
     * Apply a change in 'visited' URL list size.
     * Sends notification via the <code>PROP_URL_VISITED_PROPERTY</code>
     * property and updates the status bar.
     * @param original The original size of the list.
     * @param current The new size of the list.
     */
    protected void updateVisitedSize (final int original, final int current)
    {
        StringBuffer buffer;

        buffer = new StringBuffer ();
        buffer.append (current);
        while (buffer.length () < 5)
            buffer.insert (0, '0');
        mVisitedSize.setText (buffer.toString ());
        mPropertySupport.firePropertyChange (
            PROP_URL_VISITED_PROPERTY, original, current);
    }

    /**
     * Fetch images.
     * Ask the toolkit to make the image from a URL, and add a tracker
     * to handle it when it's received.
     * Add details to the rquested and tracked lists and update
     * the status bar.
     * @param images The list of images to fetch.
     */
    protected void fetch (final URL[] images)
    {
        Image image;
        Tracker tracker;
        int size;

        for (int j = 0; j < images.length; j++)
        {
            if (!mRequested.containsKey (
                images[j].toExternalForm ()))
            {
                image = getToolkit ().createImage (images[j]);
                tracker = new Tracker (images[j]);
                synchronized (mTracked)
                {
                    size = mTracked.size () + 1;
                    if (mQueueProgress.getMaximum () < size)
                    {
                        try
                        {
                            mTracked.wait ();
                        }
                        catch (InterruptedException ie)
                        {
                            // this won't happen, just continue on
                        }
                        }
                    mRequested.put (images[j].toExternalForm (), images[j]);
                    mTracked.put (images[j].toExternalForm (), images[j]);
                    mQueueProgress.setValue (size);
                    image.getWidth (tracker); // trigger the observer
                }
            }
        }
    }

    //
    // Runnable interface
    //

    /**
     * The main processing loop.
     * Pull suspect URLs off the queue one at a time, fetch and parse it,
     * request images and enqueue further links.
     */
    public void run ()
    {
        URL link;
        int original;
        String href;
        URL[][] urls;

        while (true)
        {
            try
            {
                link = null;
                original = -1;
                synchronized (mUrls)
                {
                    if (0 != mUrls.size ())
                    {
                        original = mUrls.size ();
                        link = (URL)mUrls.remove (0);
                    }
                    else
                        // don't spin crazily on an empty list
                        Thread.sleep (100);
                }
                if (null != link)
                {
                    updateQueueSize (original, mUrls.size ());
                    href = link.toExternalForm ();
                    setCurrentURL (href);
                    mVisited.put (href, link);
                    updateVisitedSize (
                        mVisited.size () - 1, mVisited.size ());
                    urls = getImageLinks (link);
                    fetch (urls[0]);
                    //append (filter (urls[1]));
                    synchronized (mEnqueuers)
                    {
                        Enqueuer enqueuer = new Enqueuer (urls[1]);
                        enqueuer.setPriority (Thread.MIN_PRIORITY);
                        mEnqueuers.add (enqueuer);
                        enqueuer.start ();
                    }
                    setCurrentURL (null);
                }
                if (!mActive)
                    synchronized (mUrls)
                    {
                        mUrls.wait ();
                    }
            }
            catch (Throwable t)
            {
                t.printStackTrace ();
            }
        }
    }

    static ArrayList mEnqueuers = new ArrayList ();
    
    class Enqueuer extends Thread
    {
        URL[] mList;

        public Enqueuer (URL[] list)
        {
            mList = list;
        }

        public void run ()
        {
            append (filter (mList));
            synchronized (mEnqueuers)
            {
                mEnqueuers.remove (this);
            }
        }
    }
    //
    // ItemListener interface
    //

    /**
     * Handle checkbox events from the status bar.
     * Based on the thread toggles, activates or deactivates the
     * background thread processes.
     * @param event The event describing the checkbox event.
     */
    public void itemStateChanged (final ItemEvent event)
    {
        Object source;
        boolean checked;

        source = event.getItemSelectable ();
        checked = ItemEvent.SELECTED == event.getStateChange ();
        if (source == mRunToggle)
            setSequencerActive (checked);
        else if (source == mBackgroundToggle)
            setBackgroundThreadActive (checked);
    }

    //
    // ChangeListener interface
    //

    /**
     * Handles the speed slider events.
     * @param event The event describing the slider activity.
     */
    public void stateChanged (final ChangeEvent event)
    {
        JSlider source;

        source = (JSlider)event.getSource ();
        if (!source.getValueIsAdjusting ())
            setSpeed (source.getValue ());
    }

    //
    // ListSelectionListener interface
    //

    /**
     * Handles the history list events.
     * @param event The event describing the list activity.
     */
    public void valueChanged (final ListSelectionEvent event)
    {
        JList source;
        Object[] hrefs;
        Picture picture;
        URL url;
        Image image;
        Tracker tracker;

        source = (JList)event.getSource ();
        if (source == mHistory && !event.getValueIsAdjusting ())
        {
            hrefs = source.getSelectedValues ();
            for (int i = 0; i < hrefs.length; i++)
            {
                picture = mPicturePanel.find ("http://" + (String)hrefs[i]);
                if (null != picture)
                    mPicturePanel.bringToTop (picture);
                else
                    try
                    {
                        url = new URL ("http://" + (String)hrefs[i]);
                        image = getToolkit ().createImage (url);
                        tracker = new Tracker (url);
                        image.getWidth (tracker);
                        System.out.println ("refetching " + hrefs[i]);
                    }
                    catch (MalformedURLException murle)
                    {
                        murle.printStackTrace ();
                    }
            }
        }
    }

    /**
     * Adds the given url to the history list.
     * Also puts the URL in the url text of the status bar.
     * @param url The URL to add to the history list.
     */
    public void addHistory (String url)
    {
        int index;
        DefaultListModel model;

        mUrlText.setText (url);
        // chop off the protocol
        index = url.indexOf ("http://");
        if (-1 != index)
            url = url.substring (index + 7);
        else
            System.out.println ("********* " + url + " ************");
        model = (DefaultListModel)mHistory.getModel ();
        model.addElement (url);
        // this doesn't friggin work:
        // mHistory.ensureIndexIsVisible (model.getSize ());
    }

    /**
     * Open a URL.
     * Resets the urls list and appends the given url as the only item.
     * @param ref The URL to add.
     */
    public void open (String ref)
    {
        URL url;

        try
        {
            if (!ref.startsWith ("http://"))
                ref = "http://" + ref;
            url = new URL (ref);
            reset ();
            append (url);
        }
        catch (Exception e)
        {
            System.out.println (e.getMessage ());
        }
    }

    /**
     * Provide command line help.
     */
    protected static void help ()
    {
        System.out.println ("Thumbelina - Scan and display the images behind thumbnails.");
        System.out.println ("java -Xmx256M -jar thumbelina.jar [url]");
        System.out.println ("It is highly recommended that the maximum heap "
            + "size be increased with -Xmx switch.");
        System.exit (0);
    }

    /**
     * Mainline.
     * @param args the command line arguments.
     * Can be one or more forms of -help to get command line help,
     * or a URL to prime the program with.
     * Checks for JDK 1.4 and if not found runs in crippled mode
     * (no ThumbelinaFrame).
     */
    public static void main (final String[] args)
    {
        URL url;
        String version;
        JFrame frame;
        Thumbelina thumbelina;

        System.setProperty ("sun.net.client.defaultReadTimeout", "7000");
        System.setProperty ("sun.net.client.defaultConnectTimeout", "7000");

        url = null;
        if (0 != args.length)
            try
            {
                if (args[0].equalsIgnoreCase ("help")
                    || args[0].equalsIgnoreCase ("-help")
                    || args[0].equalsIgnoreCase ("-h")
                    || args[0].equalsIgnoreCase ("?")
                    || args[0].equalsIgnoreCase ("-?"))
                    help ();
                else
                    url = new URL (args[0]);
            }
            catch (MalformedURLException murle)
            {
                System.err.println (murle.getMessage ());
                help ();
            }

        version = System.getProperty ("java.version");
        if (version.startsWith ("1.4") || version.startsWith ("1.5"))
            frame = new ThumbelinaFrame (url);
        else
        {
            if (null == url)
                help ();
            System.out.println (
                "Java version is only "
                + version
                + ", entering crippled mode");
            frame = new JFrame ("Thumbelina");
            thumbelina = new Thumbelina (url);
            frame.getContentPane ().add (thumbelina, BorderLayout.CENTER);
            frame.setBounds (10, 10, 640, 480);
            frame.addWindowListener (new WindowAdapter ()
            {
                public void windowClosing (final WindowEvent event)
                {
                    System.exit (0);
                }
            });
        }
        frame.setVisible (true);
    }

    /**
     * Getter for property queue.
     * @return List of URLs that are to be visited.
     */
    public ArrayList getQueue ()
    {
        return (mUrls);
    }

    /**
     * Getter for property queue.
     * This is a bound property. Notifications are available via the
     * <code>PROP_URL_QUEUE_PROPERTY</code> property.
     * @return The size of the list of URLs that are to be visited.
     */
    public int getQueueSize ()
    {
        return (mUrls.size ());
    }

    /**
     * Track incoming asynchronous image reception.
     * On completion, adds the image to the pending list.
     */
    class Tracker implements ImageObserver
    {

        /**
         * The url the image is comming from.
         */
        protected URL mSource;

        /**
         * Create an image tracker.
         * @param source The URL the image is being fetched from.
         */
        public Tracker (final URL source)
        {
            mSource = source;
        }

        //
        // ImageObserver interface
        //

        /**
         * This method is called when information about an image which was
         * previously requested using an asynchronous interface becomes
         * available.  Asynchronous interfaces are method calls such as
         * getWidth(ImageObserver) and drawImage(img, x, y, ImageObserver)
         * which take an ImageObserver object as an argument.  Those methods
         * register the caller as interested either in information about
         * the overall image itself (in the case of getWidth(ImageObserver))
         * or about an output version of an image (in the case of the
         * drawImage(img, x, y, [w, h,] ImageObserver) call).
         *
         * <p>This method
         * should return true if further updates are needed or false if the
         * required information has been acquired.  The image which was being
         * tracked is passed in using the img argument.  Various constants
         * are combined to form the infoflags argument which indicates what
         * information about the image is now available.  The interpretation
         * of the x, y, width, and height arguments depends on the contents
         * of the infoflags argument.
         * <p>
         * The <code>infoflags</code> argument should be the bitwise inclusive
         * <b>OR</b> of the following flags: <code>WIDTH</code>,
         * <code>HEIGHT</code>, <code>PROPERTIES</code>, <code>SOMEBITS</code>,
         * <code>FRAMEBITS</code>, <code>ALLBITS</code>, <code>ERROR</code>,
         * <code>ABORT</code>.
         *
         * @param     image   the image being observed.
         * @param     infoflags   the bitwise inclusive OR of the following
         *               flags:  <code>WIDTH</code>, <code>HEIGHT</code>,
         *               <code>PROPERTIES</code>, <code>SOMEBITS</code>,
         *               <code>FRAMEBITS</code>, <code>ALLBITS</code>,
         *               <code>ERROR</code>, <code>ABORT</code>.
         * @param     x   the <i>x</i> coordinate.
         * @param     y   the <i>y</i> coordinate.
         * @param     width    the width.
         * @param     height   the height.
         * @return    <code>false</code> if the infoflags indicate that the
         *            image is completely loaded; <code>true</code> otherwise.
         *
         * @see #WIDTH
         * @see #HEIGHT
         * @see #PROPERTIES
         * @see #SOMEBITS
         * @see #FRAMEBITS
         * @see #ALLBITS
         * @see #ERROR
         * @see #ABORT
         * @see Image#getWidth
         * @see Image#getHeight
         * @see java.awt.Graphics#drawImage
         */
        public synchronized boolean imageUpdate (
            final Image image,
            final int infoflags,
            final int x,
            final int y,
            final int width,
            final int height)
        {
            boolean done;
            boolean error;
            boolean abort;
            URL url;

            done = (0 != (infoflags & ImageObserver.ALLBITS));
            abort = (0 != (infoflags & ImageObserver.ABORT));
            error = (0 != (infoflags & ImageObserver.ERROR));
            if (done || abort || error)
                synchronized (mTracked)
                {
                    url = (URL)mTracked.remove (mSource.toExternalForm ());
                    mTracked.notify ();
                    mQueueProgress.setValue (mTracked.size ());
                    if (done)
                        mSequencer.add (image, mSource, (null != url));
                }

            return (!done);
        }
    }
}

/*
 * Revision Control Modification History
 *
 * $Log: Thumbelina.java,v $
 * Revision 1.7  2005/02/13 20:36:00  derrickoswald
 * FilterBuilder
 *
 * Revision 1.6  2004/07/31 16:42:30  derrickoswald
 * Remove unused variables and other fixes exposed by turning on compiler warnings.
 *
 * Revision 1.5  2004/05/24 16:18:17  derrickoswald
 * Part three of a multiphase refactoring.
 * The three node types are now fronted by interfaces (program to the interface paradigm)
 * with concrete implementations in the new htmlparser.nodes package. Classes from the
 * lexer.nodes package are moved to this package, and obvious references to the concrete
 * classes that got broken by this have been changed to use the interfaces where possible.
 *
 * Revision 1.4  2004/05/16 17:59:56  derrickoswald
 * Alter bound property name constants to agree with section
 * 8.8 Capitalization of inferred names.
 * in the JavaBeans API specification.
 *
 * Revision 1.3  2003/11/04 01:25:02  derrickoswald
 * Made visiting order the same order as on the page.
 * The 'shouldRecurseSelf' boolean of NodeVisitor could probably
 * be removed since it doesn't make much sense any more.
 * Fixed StringBean, which was still looking for end tags with names starting with
 * a slash, i.e. "/SCRIPT", silly beany.
 * Added some debugging support to the lexer, you can easily base a breakpoint on
 * line number.
 *
 * Revision 1.2  2003/10/26 16:44:01  derrickoswald
 * Get thumbelina working again. The tag.getName() method doesn't include the / of end tags.
 *
 * Revision 1.1  2003/09/21 18:20:56  derrickoswald
 * Thumbelina
 * Created a lexer GUI application to extract images behind thumbnails.
 * Added a task in the ant build script - thumbelina - to create the jar file.
 * You need JDK 1.4.x to build it.  It can be run on JDK 1.3.x in crippled mode.
 * Usage: java -Xmx256M thumbelina.jar [URL]
 *
 *
 */
