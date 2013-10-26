// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexerapplications/thumbelina/ThumbelinaFrame.java,v $
// $Author: derrickoswald $
// $Date: 2004/09/02 02:28:14 $
// $Revision: 1.4 $
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import org.htmlparser.lexer.Lexer;

/**
 * Encapsulate a Thumbelina bean and add menu and preferences support.
 * Provides a JFrame base in which to place a Thumbelina bean, and
 * adds a menu system with MRU (Most Recently Used) list.
 * Also provides a Google search capability.
 * Will eventually provide Javahelp too.
 */
public class ThumbelinaFrame
    extends
        JFrame
    implements
        WindowListener,
        ActionListener,
        ItemListener,
        PropertyChangeListener
{
    /**
     * Window title.
     */
    private static final String TITLE = "Thumbelina";

    /**
     * Preference name for frame location and size.
     */
    private static final String FRAMESIZE = "FrameSize";

    /**
     * Percent of screen to leave as border when no preferences available.
     */
    private static final int BORDERPERCENT = 5;

    /**
     * Preference name for most recently used count.
     */
    private static final String MRULENGTH = "MRULength";

    /**
     * Preference name for most recently used maximum count.
     */
    private static final String MRUMAX = "MRUMax";

    /**
     * Preference prefix for most recently used list items.
     */
    private static final String MRUPREFIX = "MRUListItem";

    /**
     * Preference name for google query.
     */
    private static final String GOOGLEQUERY = "GoogleQuery";

    /**
     * Default google query when no preferences are available.
     */
    private static final String DEFAULTGOOGLEQUERY = "thumbs";

    /**
     * List of URLs to prime the MRU list with.
     */
    private static final String[] DEFAULTMRULIST =
    {
        "www.a2zcds.com",
        "www.stoneschool.com/Japan/",
        "www.tommys-bookmarks.com",
        "www.unitedmedia.com/comics/dilbert/archive",
        "www.pastelartists.com",
    };

    /**
     * Send Mozilla headers in request if <code>true</code>.
     */
    private static final boolean USE_MOZILLA_HEADERS = false;

    /**
     * Preference name for status bar visibility state.
     */
    private static final String STATUSBARSTATE = "StatusBarVisible";

    /**
     * Preference name for history list visibility state.
     */
    private static final String HISTORYLISTSTATE = "HistoryListVisible";

    /**
     * Preference name for sequencer active state.
     */
    private static final String SEQUENCERACTIVE = "SequencerActive";

    /**
     * Preference name for background thread active state.
     */
    private static final String BACKGROUNDTHREADACTIVE =
        "BackgroundThreadActive";

    /**
     * Preference name for sequencer display speed.
     */
    private static final String DISPLAYSPEED = "DisplaySpeed";

    /**
     * Main menu.
     */
    protected JMenuBar mMenu;

    /**
     * URL submenu.
     */
    protected JMenu mURL;

    /**
     * Open menu item.
     */
    protected JMenuItem mOpen;

    /**
     * Google menu item.
     */
    protected JMenuItem mGoogle;

    /**
     * MRU list separator #1.
     */
    protected JSeparator mSeparator1;

    /**
     * MRU list separator #2.
     */
    protected JSeparator mSeparator2;

    /**
     * Exit menu item.
     */
    protected JMenuItem mExit;

    /**
     * View submenu.
     */
    protected JMenu mView;

    /**
     * Status bar visible menu item.
     */
    protected JCheckBoxMenuItem mStatusVisible;

    /**
     * History list visible menu item.
     */
    protected JCheckBoxMenuItem mHistoryVisible;

    /**
     * Vommand menu.
     */
    protected JMenu mCommand;

    /**
     * Reset menu item.
     */
    protected JMenuItem mReset;

    /**
     * Clear menu item
     */
    protected JMenuItem mClear;

    /**
     * Help submenu.
     */
    protected JMenu mHelp;

    /**
     * About menu item.
     */
    protected JMenuItem mAbout;

    /**
     * Construct a new Thumbelina frame with an idle Thumbelina.
     */
    public ThumbelinaFrame ()
    {
        this (new Thumbelina ());
    }

    /**
     * Construct a new Thumbelina frame with a Thumbelina primed with one URL.
     * @param url The URL to prime the Thumbelina with.
     * @exception MalformedURLException If the given string doesn't represent
     * a valid url.
     */
    public ThumbelinaFrame (final String url)
        throws
            MalformedURLException
    {
        this (new Thumbelina (url));
    }

    /**
     * Construct a new Thumbelina frame with a Thumbelina primed with one URL.
     * @param url The URL to prime the Thumbelina with.
     */
    public ThumbelinaFrame (final URL url)
    {
        this (new Thumbelina (url));
    }

    /**
     * Construct a new Thumbelina frame with a given Thumbelina.
     * @param thumbelina The Thumbelina to encapsulate.
     */
    public ThumbelinaFrame (final Thumbelina thumbelina)
    {
        setTitle (TITLE);
        thumbelina.addPropertyChangeListener (this);
        getContentPane ().add (thumbelina, BorderLayout.CENTER);
        addWindowListener (this);
        makeMenu ();
        setJMenuBar (mMenu);
        restoreSize ();
        initState ();
        updateMenu ();
    }

    /**
     * Access the Thumbelina object contained in the frame.
     * @return The Thumbelina bean.
     */
    public Thumbelina getThumbelina ()
    {
        return ((Thumbelina)getContentPane ().getComponent (0));
    }

    /**
     * Initialize the user preferences.
     * Reads from the existing user preferences,
     * or initializes values from the bean directly if they don't exist.
     * Sets the state of the view checkboxes to match.
     */
    public void initState ()
    {
        Preferences prefs;

        prefs = Preferences.userNodeForPackage (getClass ());
        if (-1 == prefs.getInt (MRULENGTH, -1))
            for (int i = 0; i < DEFAULTMRULIST.length; i++)
                updateMRU (DEFAULTMRULIST[i]);
        getThumbelina ().setStatusBarVisible (
            prefs.getBoolean (STATUSBARSTATE,
                getThumbelina ().getStatusBarVisible ()));
        mStatusVisible.setSelected (getThumbelina ().getStatusBarVisible ());
        getThumbelina ().setHistoryListVisible (
            prefs.getBoolean (HISTORYLISTSTATE,
                getThumbelina ().getHistoryListVisible ()));
        mHistoryVisible.setSelected (getThumbelina ().getHistoryListVisible ());
        getThumbelina ().setSequencerActive (
            prefs.getBoolean (SEQUENCERACTIVE,
                getThumbelina ().getSequencerActive ()));
        getThumbelina ().setBackgroundThreadActive (
            prefs.getBoolean (BACKGROUNDTHREADACTIVE,
                getThumbelina ().getBackgroundThreadActive ()));
        getThumbelina ().setSpeed (
            prefs.getInt (DISPLAYSPEED, getThumbelina ().getSpeed ()));
    }

    /**
     * Saves the current settings in the user preferences.
     * By default this writes to the thumbelina subdirectory under
     * .java in the users home directory.
     */
    public void saveState ()
    {
        Preferences prefs;

        prefs = Preferences.userNodeForPackage (getClass ());
        // don't save size unless we're in normal state
        if (NORMAL == getExtendedState ())
            prefs.put (FRAMESIZE, toString (getBounds ()));
        prefs.putBoolean (STATUSBARSTATE,
            getThumbelina ().getStatusBarVisible ());
        prefs.putBoolean (HISTORYLISTSTATE,
            getThumbelina ().getHistoryListVisible ());
        prefs.putBoolean (SEQUENCERACTIVE,
            getThumbelina ().getSequencerActive ());
        prefs.putBoolean (BACKGROUNDTHREADACTIVE,
            getThumbelina ().getBackgroundThreadActive ());
        prefs.putInt (DISPLAYSPEED,
            getThumbelina ().getSpeed ());
        try
        {
            prefs.flush ();
        }
        catch (BackingStoreException bse)
        {
            bse.printStackTrace ();
        }
    }

    /**
     * Sets the frame size if no previous preference has been stored.
     * It creates a window covering all but <code>BORDERPERCENT</code>
     * margins.
     */
    public void initSize ()
    {
        Toolkit tk;
        Dimension dim;
        int borderx;
        int bordery;

        tk = getToolkit ();
        dim = tk.getScreenSize ();
        borderx = dim.width * BORDERPERCENT / 100;
        bordery = dim.height * BORDERPERCENT / 100;
        setBounds (
            borderx,
            bordery,
            dim.width - (2 * borderx),
            dim.height - (2 * bordery));
    }

    /**
     * Restores the window size based on stored preferences.
     * If no preferences exist, it calls <code>initSize()</code>.
     */
    public void restoreSize ()
    {
        Preferences prefs;
        String size;
        Rectangle rectangle;

        prefs = Preferences.userNodeForPackage (getClass ());
        size = prefs.get (FRAMESIZE, "");
        if ("".equals (size))
            initSize ();
        else
            try
            {
                rectangle = fromString (size);
                if (rational (rectangle))
                    setBounds (
                        rectangle.x,
                        rectangle.y,
                        rectangle.width,
                        rectangle.height);
                else
                    initSize ();
            }
            catch (IllegalArgumentException iae)
            {
                initSize ();
            }
    }

    /**
     * Converts the rectangle to a string.
     * The rectangle is converted into a string that is of the form
     * <pre>
     * [x,y,width,height].
     * </pre>
     * @return The string equivalent of the rectangle.
     * @param r The rectangle containing the window position and size,
     * as returned by <code>getBounds()</code>.
     */
    protected String toString (final Rectangle r)
    {
        return ("[" + r.x + "," + r.y + "," + r.width + "," + r.height + "]");
    }

    /**
     * Convert the given string to a valid rectangle.
     * The string is converted to a Rectangle.
     * @param value The value to parse.
     * @exception IllegalArgumentException if the format does not match the
     * form "[x,y,width,height]" with all values integers.
     * @return Returns the rectangle extracted from the string.
     */
    protected Rectangle fromString (final String value)
        throws
            IllegalArgumentException
    {
        String guts;
        int current;
        int[] values;
        int index;
        Rectangle ret;

        try
        {
            // parse "[x,y,width,height]"
            if (value.startsWith ("[") && value.endsWith ("]"))
            {
                guts = value.substring (1, value.length () - 1) + ",";
                current = 0;
                values = new int[4];
                for (int i = 0; i < 4; i++)
                {
                    index = guts.indexOf (",", current);
                    if (-1 == index)
                        throw new IllegalArgumentException (
                            "invalid format \"" + value + "\"");
                    else
                    {
                        values[i] = Integer.parseInt (
                            guts.substring (current, index));
                        current = index + 1;
                    }
                }
                ret = new Rectangle (
                    values[0], values[1], values[2], values[3]);
            }
            else
                throw new IllegalArgumentException (
                    "invalid format \"" + value + "\"");
        }
        catch (NumberFormatException nfe)
        {
            throw new IllegalArgumentException (nfe.getMessage ());
        }

        return (ret);
    }

    /**
     * Check if the rectangle represents a valid screen position and size.
     * @param r The rectangle to check.
     * @return <code>true</code> if this could be a valid frame bounds.
     */
    private boolean rational (final Rectangle r)
    {
        Toolkit tk;
        Dimension winsize;

        tk = getToolkit ();
        winsize = tk.getScreenSize();
        // all elements must be not stupid w.r.t. the screen size
        // we assume here that that means no more than 10% off screen
        // on the left, right and bottom sides
        return (   (r.x >= r.width / -10)
                && (r.y >= 0)
                && (r.width > 0)
                && (r.height > 0)
                && (r.x + r.width <= winsize.width + r.width / 10)
                && (r.y + r.height <= winsize.height + r.height / 10));
    }

    /**
     * Create the menu.
     * Initializes the menu and adds it to the frame.
     */
    public void makeMenu ()
    {
        mMenu = new JMenuBar ();
        mURL = new JMenu ();
        mOpen = new JMenuItem ();
        mGoogle = new JMenuItem ();
        mSeparator1 = new JSeparator ();
        mSeparator2 = new JSeparator ();
        mExit = new JMenuItem ();

        mView = new JMenu ();
        mStatusVisible = new JCheckBoxMenuItem ();
        mHistoryVisible = new JCheckBoxMenuItem ();

        mHelp = new JMenu ();
        mAbout = new JMenuItem ();

        mCommand = new JMenu ();
        mReset = new JMenuItem ();
        mClear = new JMenuItem ();

        mURL.setMnemonic ('U');
        mURL.setText ("URL");
        mOpen.setMnemonic ('O');
        mOpen.setText ("Open");
        mOpen.setToolTipText ("Open a URL.");
        mURL.add (mOpen);

        mGoogle.setMnemonic ('G');
        mGoogle.setText ("Google");
        mGoogle.setToolTipText ("Search Google.");
        mURL.add (mGoogle);

        mURL.add (mSeparator1);
        mURL.add (mSeparator2);

        mExit.setMnemonic ('E');
        mExit.setText ("Exit");
        mExit.setToolTipText ("Quit Thumbelina.");
        mURL.add (mExit);

        mMenu.add (mURL);

        mView.setMnemonic ('V');
        mView.setText ("View");
        mStatusVisible.setMnemonic ('S');
        mStatusVisible.setSelected (getThumbelina ().getStatusBarVisible ());
        mStatusVisible.setText ("Status Bar");
        mStatusVisible.setToolTipText ("Show/Hide the status bar.");
        mView.add (mStatusVisible);
        mHistoryVisible.setMnemonic ('H');
        mHistoryVisible.setSelected (getThumbelina ().getHistoryListVisible ());
        mHistoryVisible.setText ("History List");
        mHistoryVisible.setToolTipText ("Show/Hide the history list.");
        mView.add (mHistoryVisible);
        mMenu.add (mView);

        mCommand.setMnemonic ('C');
        mCommand.setText ("Command");
        mReset.setMnemonic ('R');
        mReset.setText ("Reset");
        mReset.setToolTipText ("Reset Thumbelina.");
        mClear.setMnemonic ('L');
        mClear.setText ("Clear");
        mClear.setToolTipText ("Clear display.");
        mCommand.add (mReset);
        mCommand.add (mClear);
        mCommand.add (mHelp);
        mMenu.add (mCommand);

        mHelp.setMnemonic ('H');
        mHelp.setText ("Help");
        mAbout.setMnemonic ('A');
        mAbout.setText ("About");
        mAbout.setToolTipText ("Information about Thumbelina.");
        mHelp.add (mAbout);
        mMenu.add (mHelp);

        mOpen.addActionListener (this);
        mGoogle.addActionListener (this);
        mExit.addActionListener (this);
        mStatusVisible.addItemListener (this);
        mHistoryVisible.addItemListener (this);
        mReset.addActionListener (this);
        mClear.addActionListener (this);
        mAbout.addActionListener (this);
    }

    /**
     * Adjusts the menu, by inserting the current MRU list.
     * Removes the old MRU (Most Recently Used) items and inserts new
     * ones betweeen the two separators.
     */
    public void updateMenu ()
    {
        Preferences prefs;
        int start;
        int end;
        Component component;
        JMenuItem item;
        int count;
        String string;

        prefs = Preferences.userNodeForPackage (getClass ());
        start = -1;
        end = -1;
        for (int i = 0; i < mURL.getItemCount (); i++)
        {
            component = mURL.getMenuComponent (i);
            if (component == mSeparator1)
                start = i + 1;
            else if (component == mSeparator2)
                end = i;
        }

        if ((-1 != start) && (-1 != end))
        {
            for (int i = start; i < end; i++)
                mURL.remove (start);

            count = prefs.getInt (MRULENGTH, 0);
            for (int i = 0; i < count; i++)
            {
                string = prefs.get (MRUPREFIX + i, "");
                if (!"".equals (string))
                {
                    item = new JMenuItem ();
                    item.setActionCommand (string);
                    if (string.length () > 40)
                        string = string.substring (0, 38) + "...";
                    item.setText (string);
                    item.addActionListener (this);
                    mURL.add (item, start++);
                }
            }
        }
    }

    //
    // WindowListener interface
    //

    /**
     * Invoked the first time a window is made visible.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowOpened (final WindowEvent event)
    {
    }

    /**
     * Handles window closing event.
     * Performs function <code>exitApplication()</code>.
     * @param event The window event.
     */
    public void windowClosing (final WindowEvent event)
    {
        exit ();
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowClosed (final WindowEvent event)
    {
    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowIconified (final WindowEvent event)
    {
    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowDeiconified (final WindowEvent event)
    {
    }

    /**
     * Invoked when the window is set to be the user's
     * active window, which means the window (or one of its
     * subcomponents) will receive keyboard events.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowActivated (final WindowEvent event)
    {
    }

    /**
     * Invoked when a window is no longer the user's active
     * window, which means that keyboard events will no longer
     * be delivered to the window or its subcomponents.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowDeactivated (final WindowEvent event)
    {
    }

    //
    // ActionListener interface
    //

    /**
     * Handles events from the menu.
     * Based on the action of the event, executes the necessary subroutine.
     * @param actionEvent The event describing the user action.
     */
    public void actionPerformed (final ActionEvent actionEvent)
    {
        String action;

        action = actionEvent.getActionCommand ();
        if (action.equals ("Open"))
            open ();
        else if (action.equals ("Google"))
            googlesearch ();
        else if (action.equals ("Reset"))
            getThumbelina ().reset ();
        else if (action.equals ("Clear"))
            getThumbelina ().getPicturePanel ().reset ();
        else if (action.equals ("About"))
            about ();
        else if (action.equals ("Exit"))
            exit ();
        else
        {
            // must be a URL from the most recently used list
            getThumbelina ().open (action);
            updateMRU (action);
            updateMenu ();
        }
    }

    //
    // ItemListener interface
    //

    /**
     * Handles selections on the view state checkboxes.
     * @param event The event describing the checkbox affected.
     */
    public void itemStateChanged (final ItemEvent event)
    {
        Object source;
        boolean visible;

        source = event.getItemSelectable ();
        visible = ItemEvent.SELECTED == event.getStateChange ();
        if (source == mStatusVisible)
            getThumbelina ().setStatusBarVisible (visible);
        else if (source == mHistoryVisible)
            getThumbelina ().setHistoryListVisible (visible);
    }

    //
    // PropertyChangeListener
    //

    /**
     * Handle a property change.
     * @param event The property old and new values.
     */
    public void propertyChange (final PropertyChangeEvent event)
    {
        String url;

        if (event.getPropertyName ().equals (
            Thumbelina.PROP_CURRENT_URL_PROPERTY))
        {
            url = (String)event.getNewValue ();
            if (null == url)
                setTitle ("Thumbelina");
            else
                setTitle ("Thumbelina - " + url);
        }
    }

    /**
     * Updates the user preferences based on the most recently used list.
     * @param url The URL that is to be placed at the top of the MRU list.
     */
    public void updateMRU (String url)
    {
        Preferences prefs;
        int count;
        ArrayList list;
        String string;
        int max;

        if (url.startsWith ("http://"))
            url = url.substring (7);
        prefs = Preferences.userNodeForPackage (getClass ());
        count = prefs.getInt (MRULENGTH, -1);
        list = new ArrayList ();
        for (int i = 0; i < count; i++)
        {
            string = prefs.get (MRUPREFIX + i, "");
            if (!"".equals (string) && !url.equalsIgnoreCase (string))
                list.add (string);
        }
        list.add (0, url);
        max = prefs.getInt (MRUMAX, -1);
        if (-1 == max)
            max = 8;
        while (list.size () > max)
            list.remove (max);
        prefs.putInt (MRULENGTH, list.size ());
        prefs.putInt (MRUMAX, max);
        for (int i = 0; i < list.size (); i++)
            prefs.put (MRUPREFIX + i, (String)list.get (i));
        try
        {
            prefs.flush ();
        }
        catch (BackingStoreException bse)
        {
            bse.printStackTrace ();
        }
    }

    /**
     * Opens a user specified URL.
     */
    public void open ()
    {
        String result;

        result = JOptionPane.showInputDialog (
            this,
            "Enter the URL:",
            "Open URL",
            JOptionPane.PLAIN_MESSAGE);
        if (null != result)
        {
            getThumbelina ().open (result);
            updateMRU (result);
            updateMenu ();
        }
    }

    /**
     * Query google via user specified keywords and queue results.
     * Asks the user for keywords, and then submits them as input to the
     * usual google form:
     * <pre>
     * <form action="/search" name=f>
     * <span id=hf></span>
     * <table cellspacing=0 cellpadding=0>
     * <tr valign=middle>
     * <td width=75>&nbsp;</td>
     * <td align=center>
     * <input maxLength=256 size=55 name=q value="">
     * <input type=hidden name=ie value="UTF-8">
     * <input type=hidden name=oe value="UTF-8">
     * <input name=hl type=hidden value=en><br>
     * <input type=submit value="Google Search" name=btnG>
     * <input type=submit value="I'm Feeling Lucky" name=btnI>
     * </td>
     * <td valign=top nowrap><font size=-2>
     *   &nbsp;&#8226; <a href=/advanced_search?hl=en>Advanced Search</a>
     *   <br>&nbsp;&#8226; <a href=/preferences?hl=en>Preferences</a>
     *   <br>&nbsp;&#8226; <a href=/language_tools?hl=en>Language Tools</a>
     * </font>
     * </td>
     * </tr>
     * <tr>
     * <td colspan=3 align=center><font size=-1>
     * Search: <input id=all type=radio name=meta value="" checked>
     * <label for=all> the web</label>
     * <input id=cty type=radio name=meta value="cr=countryCA" >
     * <label for=cty>pages from Canada</label>
     * </font>
     * </td>
     * </tr>
     * </table>
     * </form>
     * </pre>
     * Creates a query of the form:
     * <pre>
     * http://www.google.ca/search?hl=en&ie=UTF-8&oe=UTF-8&q=thumbs&btnG=Google+Search&meta=
     * </pre>
     */
    public void googlesearch ()
    {

        Preferences prefs;
        String query;
        String terms;
        StringBuffer buffer;
        HttpURLConnection connection;
        URL url;
        Lexer lexer;
        URL[][] results;

        prefs = Preferences.userNodeForPackage (getClass ());
        query = prefs.get (GOOGLEQUERY, DEFAULTGOOGLEQUERY);
        try
        {
            query = (String)JOptionPane.showInputDialog (
                this,
                "Enter the search term:",
                "Search Google",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                query);
            if (null != query)
            {
                // replace spaces with +
                terms = query.replace (' ', '+');
                buffer = new StringBuffer (1024);
                buffer.append ("http://www.google.ca/search?");
                buffer.append ("q=");
                buffer.append (terms);
                buffer.append ("&ie=UTF-8");
                buffer.append ("&oe=UTF-8");
                buffer.append ("&hl=en");
                buffer.append ("&btnG=Google+Search");
                buffer.append ("&meta=");
                url = new URL (buffer.toString ());
                connection = (HttpURLConnection)url.openConnection ();
                if (USE_MOZILLA_HEADERS)
                {
                    // These are the Mozilla header fields:
                    //Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,video/x-mng,image/png,image/jpeg,image/gif;q=0.2,text/css,*/*;q=0.1
                    //Accept-Language: en-us, en;q=0.50
                    //Connection: keep-alive
                    //Host: grc.com
                    //Referer: https://grc.com/x/ne.dll?bh0bkyd2
                    //User-Agent: Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.2.1) Gecko/20030225
                    //Content-Length: 27
                    //Content-Type: application/x-www-form-urlencoded
                    //Accept-Encoding: gzip, deflate, compress;q=0.9
                    //Accept-Charset: ISO-8859-1, utf-8;q=0.66, *;q=0.66
                    //Keep-Alive: 300

                    connection.setRequestProperty ("Referer", "http://www.google.ca");
                    connection.setRequestProperty ("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,video/x-mng,image/png,image/jpeg,image/gif;q=0.2,text/css,*/*;q=0.1");
                    connection.setRequestProperty ("Accept-Language", "en-us, en;q=0.50");
                    connection.setRequestProperty ("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.2.1) Gecko/20030225");
                    connection.setRequestProperty ("Accept-Charset", "ISO-8859-1, utf-8;q=0.66, *;q=0.66");
                }
                else
                {
                    // These are the IE header fields:
                    //Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*
                    //Accept-Language: en-ca
                    //Connection: Keep-Alive
                    //Host: grc.com
                    //User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; trieste; .NET CLR 1.1.4322; .NET CLR 1.0.3705)
                    //Content-Length: 32
                    //Content-Type: application/x-www-form-urlencoded
                    //Accept-Encoding: gzip, deflate
                    //Cache-Control: no-cache

                    connection.setRequestProperty ("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                    connection.setRequestProperty ("Accept-Language", "en-ca");
                    connection.setRequestProperty ("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; trieste; .NET CLR 1.1.4322; .NET CLR 1.0.3705)");
                }
                connection.setDoOutput (true);
                connection.setDoInput (true);
                connection.setUseCaches (false);
                lexer = new Lexer (connection);
                results = getThumbelina ().extractImageLinks (lexer, url);
                // add 'em
                getThumbelina ().reset ();
                // remove google links, not just append (results[1]);
                for (int i = 0; i < results[1].length; i++)
                {
                    String found = results[1][i].toExternalForm ();
                    if (-1 == found.indexOf ("google"))
                        getThumbelina ().append (results[1][i]);
                }
                prefs.put (GOOGLEQUERY, query);
                try
                {
                    prefs.flush ();
                }
                catch (BackingStoreException bse)
                {
                    bse.printStackTrace ();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println (e.getMessage ());
        }
    }

    /**
     * Display information about Thumbelina.
     */
    public void about ()
    {
        URL url;

        try
        {
            url = new URL ("http://sourceforge.net/sflogo.php?group_id=24399");
        }
        catch (MalformedURLException murle)
        {
            url = null;
        }
        JOptionPane.showMessageDialog (
            this,
            "Scan and display the images behind thumbnails.\n"
            + "\n"
            + "An example application using the HTML Parser project.\n"
            + "Visit http://htmlparser.sourceforge.org for the latest\n"
            + "version and source code.\n",
            "Thumbelina - About",
            JOptionPane.PLAIN_MESSAGE,
            new ImageIcon (url));
    }

    /**
     * Exits the application.
     * Saves user preferences before exiting.
     */
    public void exit ()
    {
        saveState ();
        System.exit (0);
    }

    /**
     * Alternate mainline for Thumbelina.
     * Similar code exists in the Thumbelina class, but this version doesn't
     * worry about java version.
     * @param args The command line arguments.
     * Optionally, arg[0] can be the URL to preload the Thumeblina bean with.
     */
    public static void main (final String[] args)
    {
        String url;
        ThumbelinaFrame thumbelina;

        System.setProperty ("sun.net.client.defaultReadTimeout", "7000");
        System.setProperty ("sun.net.client.defaultConnectTimeout", "7000");

        url = null;
        if (0 != args.length)
            if (args[0].equalsIgnoreCase ("help")
                || args[0].equalsIgnoreCase ("-help")
                || args[0].equalsIgnoreCase ("-h")
                || args[0].equalsIgnoreCase ("?")
                || args[0].equalsIgnoreCase ("-?"))
                Thumbelina.help ();
            else
                url = args[0];

        try
        {
            thumbelina = new ThumbelinaFrame (url);
            thumbelina.setVisible (true);
        }
        catch (MalformedURLException murle)
        {
            System.err.println (murle.getMessage ());
            Thumbelina.help ();
        }
    }
}
