// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/FilterBuilder.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:42 $
// $Revision: 1.5 $
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
                                                                                                                                                
package org.htmlparser.parserapplications.filterbuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
//import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import org.htmlparser.Parser;
import org.htmlparser.beans.FilterBean;
import org.htmlparser.parserapplications.filterbuilder.layouts.NullLayoutManager;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * The main program for the FilterBuilder programming system.
 * <p>ToDo:
 * <ul>
 * <li>thread the attribute fetching</li>
 * <li>CSS selector filter</li>
 * <li>table row filter</li>
 * <li>table column filter</li>
 * <li>trigger filter</li>
 * <li>undo</li>
 * <li>handle bad URLs</li>
 * <li>StringBean type secondary text output</li>
 * <li>context sensitive menus</li>
 * </ul>
 */
public class FilterBuilder
    extends
        JFrame
    implements
        WindowListener,
        ActionListener,
        MouseListener,
        MouseMotionListener,
        DragGestureListener,
        DragSourceListener,
        DropTargetListener,
        ClipboardOwner
{
    static final String TITLE = "HTML Parser FilterBuilder";

    static final URL mDocumentBase;
    
    static
    {
        
        String p;
        char ps;
        URL base;

        p = System.getProperty ("user.dir");
        // if the system file separator isn't the URL file separator convert it.
        try
        {
            ps = (System.getProperty ("file.separator")).charAt(0);
            if ('/' != ps)
                p.replace (ps, '/');
        }
        catch (StringIndexOutOfBoundsException e)
        {
        }

        try
        {
            base = new URL ("file:///" + p + "/");
        }
        catch (MalformedURLException murle)
        {
            base = null;
        }
        mDocumentBase = base;
    }

    static String mHomeDir;
    
    static
    {
        String dir;
        File file;

        dir = System.getProperty ("user.home")
            + System.getProperty ("file.separator")
            + ".htmlparser";
        file = new File (dir);
        if (!file.exists ())
            if (!file.mkdirs ()) // make the directory if it doesn't exist
                throw new RuntimeException (
                    "cannot create directory "
                    + file.getAbsolutePath ());
        mHomeDir = file.getAbsolutePath ();
    }
    
    /**
     * The relative position of the mouse while dragging.
     */
    protected Point mBasePoint;

    /**
     * Selected commands.
     */
    protected Vector mSelection;

    /**
     * If true selection moved.
     */
    protected boolean mMoved;

    /**
     * This component is a drop target.
     */
    protected DropTarget mDropTarget;
    
    /**
     * Enables this component to be a Drag Source.
     */
    protected DragSource mDragSource;

    /**
     * Kludge: Used by actionPerformed/filterAction to remember the filter menu item.
     */
    protected Component mCurrentComponent;

    /**
     * The main panel GUI component.
     */
    protected JPanel mMainPanel;

    /**
     * The main panel scrolling GUI component.
     */
    protected JScrollPane mMainScroller;

    /**
     * The URL input GUI component.
     */
    protected JTextField mURLField;

    /**
     * The output panel GUI component.
     */
    protected JDesktopPane mOutput;

    /**
     * Create an FilterBuilder programming environment.
     */
    public FilterBuilder ()
    {
        JMenuBar menubar;
        JToolBar toolbar;
        JMenu menu;
        JPanel panel;
        JScrollPane pane;
        JSplitPane split;
        JMenuItem item;

        // drag and drop support
        mMainPanel = new JPanel ();
        mDropTarget = new DropTarget (mMainPanel, this);
        mDragSource = new DragSource ();

        // menu and toolbar
        menubar = new JMenuBar();
        toolbar = new JToolBar ();
        toolbar.setAlignmentY (0.222222F);

        // file menu
        menu = new JMenu ();
        menu.setText ("File");
        menu.setActionCommand ("File");
        menu.setMnemonic ((int)'F');
        makeMenuButton ("New", "Create a new document", "New", 'N', KeyStroke.getKeyStroke (KeyEvent.VK_N, Event.CTRL_MASK), toolbar, menu);
        makeMenuButton ("Open", "Open an existing document", "Open...", 'O', KeyStroke.getKeyStroke (KeyEvent.VK_O, Event.CTRL_MASK), toolbar, menu);
        makeMenuButton ("Save", "Save the active document", "Save...", 'S', KeyStroke.getKeyStroke (KeyEvent.VK_S, Event.CTRL_MASK), toolbar, menu);
        makeMenuButton ("SaveAs", "Save the active document", "Save As...", 'A', KeyStroke.getKeyStroke (KeyEvent.VK_A, Event.CTRL_MASK), null, menu);
        menu.add (new JSeparator ());
        makeMenuButton ("Exit", "Exit the program", "Exit", 'E', KeyStroke.getKeyStroke (KeyEvent.VK_E, Event.CTRL_MASK), null, menu);
        menubar.add (menu);
        
        toolbar.add(new JToolBar.Separator());

        // edit menu
        menu = new JMenu ();
        menu.setText ("Edit");
        menu.setActionCommand ("Edit");
        menu.setMnemonic ((int)'E');
        makeMenuButton ("Cut", "Cut the selection and put it on the Clipboard", "Cut", 'T', KeyStroke.getKeyStroke (KeyEvent.VK_X, Event.CTRL_MASK), toolbar, menu);
        makeMenuButton ("Copy", "Copy the selection and put it on the Clipboard", "Copy", 'C', KeyStroke.getKeyStroke (KeyEvent.VK_C, Event.CTRL_MASK), toolbar, menu);
        makeMenuButton ("Paste", "Insert Clipboard contents", "Paste", 'P', KeyStroke.getKeyStroke (KeyEvent.VK_V, Event.CTRL_MASK), toolbar, menu);
        makeMenuButton ("Delete", "Delete the selection", "Delete", 'D', KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0), toolbar, menu);
        menubar.add (menu);

        // filter menu
        menu = new JMenu ();
        menu.setText ("Filter");
        menu.setActionCommand ("Filter");
        menu.setMnemonic ((int)'F');
        menubar.add (menu);

        toolbar.add (new JToolBar.Separator());

        // filters menu and filters toolbar
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.AndFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.OrFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.NotFilterWrapper");
        menu.addSeparator ();
        toolbar.add (new JToolBar.Separator ());

        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.StringFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.RegexFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.TagNameFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.NodeClassFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.HasAttributeFilterWrapper");
        menu.addSeparator ();
        toolbar.add (new JToolBar.Separator ());

        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.HasParentFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.HasChildFilterWrapper");
        addFilter (menu, toolbar, "org.htmlparser.parserapplications.filterbuilder.wrappers.HasSiblingFilterWrapper");
        menu.addSeparator ();
        toolbar.add (new JToolBar.Separator ());

        // operation menu
        menu = new JMenu ();
        menu.setText ("Operation");
        menu.setActionCommand ("Operation");
        menu.setMnemonic ((int)'r');
        item = new JMenuItem ();
        item.setText ("Expand");
        item.setActionCommand ("expandAction");
        item.addActionListener (this);
        menu.add (item);
        item = new JMenuItem ();
        item.setText ("Collapse");
        item.setActionCommand ("collapseAction");
        item.addActionListener (this);
        menu.add (item);
        menu.addSeparator ();
        item = new JMenuItem ();
        item.setText ("Expand All");
        item.setActionCommand ("expandAllAction");
        item.addActionListener (this);
        menu.add (item);
        item = new JMenuItem ();
        item.setText ("Collapse All");
        item.setActionCommand ("collapseAllAction");
        item.addActionListener (this);
        menu.add (item);
        menu.addSeparator ();
        item = new JMenuItem ("Fetch Page");
        item.setActionCommand ("fetchAction");
        item.addActionListener (this);
        menu.add (item);
        item = new JMenuItem ("Execute Filter");
        item.setActionCommand ("executeAction");
        item.addActionListener (this);
        menu.add (item);
        menubar.add (menu);

        // help menu
        menu = new JMenu ();
        menu.setText ("Help");
        menu.setActionCommand ("Help");
        menu.setMnemonic ((int)'H');
        item = new JMenuItem ("Filtering");
        item.setActionCommand ("filteringAction");
        item.addActionListener (this);
        menu.add (item);
        item = new JMenuItem ("Instructions");
        item.setActionCommand ("instructionsAction");
        item.addActionListener (this);
        menu.add (item);
        item = new JMenuItem ("Tutorial");
        item.setActionCommand ("tutorialAction");
        item.addActionListener (this);
        menu.add (item);
        item = new JMenuItem ("Hints");
        item.setActionCommand ("hintsAction");
        item.addActionListener (this);
        menu.add (item);
        makeMenuButton ("About", "Display program information, version number and copyright", "About", 'B', KeyStroke.getKeyStroke (KeyEvent.VK_H, Event.CTRL_MASK), toolbar, menu);
        menubar.add (menu);

        setJMenuBar (menubar);

        // toolbar panel
        panel = new JPanel ();
        panel.setLayout (new FlowLayout (FlowLayout.LEFT,0,0));
        panel.add (toolbar);
        getContentPane().setLayout (new BorderLayout (0,0));
        getContentPane ().add (BorderLayout.NORTH, panel);

        // URL entry
        mURLField = new JTextField ();
        mURLField.setToolTipText ("Enter the URL to view");
//        mTextField.addActionListener (this);
        mURLField.setText ("http://sourceforge.org/projects/htmlparser");
        getContentPane().add (BorderLayout.SOUTH, mURLField);

        // application setup
        setTitle (TITLE);
        setDefaultCloseOperation (WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize (640, 480);
        setVisible (false);

        // main panel
        mMainPanel.setLayout (new NullLayoutManager ());
        mMainScroller = new JScrollPane (
                mMainPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        split = new JSplitPane ();
        pane = new JScrollPane ();
        pane.setViewportView (mMainScroller);
        split.setLeftComponent (pane);

        mOutput = new JDesktopPane ();
        split.setRightComponent (mOutput);

        getContentPane().add (BorderLayout.CENTER, split);

        // shenanigans to get the splitter bar at the midpoint
        setVisible (true);
        split.setDividerLocation (0.5);
        setVisible (false);

        // listeners
        addWindowListener (this);
        setIconImage (Toolkit.getDefaultToolkit ().getImage ("images/program16.gif"));
        addMouseListener (this);
        addMouseMotionListener (this);

        // clipboard buffer
        mSelection = new Vector ();
    }

    /**
     * Creates a new instance of an FilterBuilder environment with the given title.
     * @param title the title for the new frame.
     * @see #FilterBuilder()
     */
    public FilterBuilder (String title)
    {
        this ();
        setTitle (title);
    }

    /**
     * Makes menu and toolbar items for commands.
     * @param name The name of the command.
     * @param description A description for the tooltip.
     * @param text The text for the menu.
     * @param mnemonic The navigation mnemonic.
     * @param key Accelerator key.
     * @param toolbar The toolbar to add the button to.
     * @param menu The menu to add the menu item to.
     */
    protected void makeMenuButton (
            String name,
            String description,
            String text,
            int mnemonic,
            KeyStroke key,
            JToolBar toolbar,
            JMenu menu)
    {
        JButton button;
        JMenuItem item;
        ImageIcon icon;
        String command;

        command = name.toLowerCase ();
        try
        {
            icon = new ImageIcon (getURL ("images/" + command + ".gif"));
        }
        catch (java.net.MalformedURLException error)
        {
            icon = null;
        }

        item = new JMenuItem ();
        item.setText (text);
        item.setActionCommand (command + "Action");
        item.setAccelerator (key);
        item.setMnemonic (mnemonic);
        item.setIcon (icon);
        item.addActionListener (this);
        menu.add (item);

        if (null != toolbar)
        {
            button = new JButton ();
            button.setDefaultCapable (false);
            button.setToolTipText (description);
            button.setMnemonic (mnemonic);
            button.setActionCommand (command + "Action");
            button.setMargin (new Insets (0, 0, 0, 0));
            button.setIcon (icon);
            button.addActionListener (this);
            toolbar.add (button);
        }
    }

    /**
     * Get a url for the given resource specification.
     * @param spec The name of the resource.
     * @return The fully formed URL.
     * @exception MalformedURLException In the case that the document base
     * or name of the resource cannot be turned into a URL.
     */
    protected URL getURL (String spec)
        throws MalformedURLException
    {
        URL ret;

        if (null == (ret = getClass ().getResource (spec)))
            if ((null != mDocumentBase) && (-1 == spec.indexOf ("//")))
                ret =  new URL (mDocumentBase, spec);
            else
                ret = new URL (spec);

        return ret;
    }

    /**
     * Creates a new button for the given class.
     * @param class_name The name of the Filter class.
     * @return A fully functional button with name, tool tip,
     * icon and drag recognizer.
     */
    public JButton makeFilterButton (String class_name)
    {
        Filter filter;
        JButton ret;

        ret = new JButton ();
        filter = Filter.instantiate (class_name);
        if (null != filter)
        {
            ret.setName (class_name); // filter.getNodeFilter ().getClass ().getName ());
            ret.setToolTipText (filter.getDescription ());
            ret.setMargin (new Insets (0, 0, 0, 0));
            ret.setIcon (filter.getIcon ());
            mDragSource.createDefaultDragGestureRecognizer (
                ret,
                DnDConstants.ACTION_MOVE,
                this);
            ret.setActionCommand ("filterAction");
            ret.addActionListener (this);
        }
        
        return (ret);
    }

    /**
     * Add a filter to the GUI.
     * Adds the filter specified by class_name to the menu, toolbar and
     * starts listening for actions.
     * @param menu The menu to add the filter to.
     * @param toolbar The toolbar to add the filter to.
     * @param class_name The class name for the filter wrapper.
     * From the wrapper, the NodeFilter, description and icon can be obtained.
     */
    public void addFilter (JMenu menu, JToolBar toolbar, String class_name)
    {
        Filter filter;

        filter = Filter.instantiate (class_name);
        if (null != filter)
        {
            String name;
            String description;
            Icon icon;
            String text;
            JMenuItem item;

            name = filter.getNodeFilter ().getClass ().getName ();
            description = filter.getDescription ();
            icon = filter.getIcon ();
            text = name.substring (name.lastIndexOf ('.') + 1);

            item = new JMenuItem ();
            item.setName (class_name);
            item.setText (text);
            item.setActionCommand ("filterAction");
//            item.setAccelerator (key);
//            item.setMnemonic (mnemonic);
            item.setToolTipText (description);
            item.setIcon (icon);
            item.addActionListener (this);
            menu.add (item);
            
            toolbar.add (makeFilterButton (class_name));
        }
    }

    /**
     * Adds a set of filters to the main panel or a sublist.
     * Sets up the GUI components as drop targets and mouse listeners,
     * and performs a relayout to display them.
     * @param filters The filter wrappers to add.
     * @param point The point at which to start adding (list == null).
     * @param list The list to add to (point not used), or <code>null</code>
     * for the main panel.
     */
    protected void insertFilters (Filter[] filters, Point point, SubFilterList list)
    {
        Dimension dimension;

        if (null == list)
        {
            for (int i = 0; i < filters.length; i++)
            {
	            filters[i].setLocation (point);
	            mMainPanel.add (filters[i]);
	            dimension = filters[i].getPreferredSize ();
	            point.y += dimension.height;
            }
        }
        else
            for (int i = 0; i < filters.length; i++)
                list.addFilter (filters[i]);
        setupDropTargets (filters);
        setupMouseListeners (filters);
        relayout ();
    }

    /**
     * Sets the position of the mouse in the component.
     * 
     * @param point The point where the mouse position is.
     */
    protected void setBasePoint (Point point)
    {
        mBasePoint = point;
    }
    
    /**
     * Gets the current base point of the mouse pointer.
     * This value is used to offset the drag position
     * to maintain the mouse position at the same
     * relative position within the card while dragging.
     * 
     * @return The current base point of the mouse pointer.
     */
    protected Point getBasePoint ()
    {
        return (mBasePoint);
    }

    /**
     * Get the enclosing sub filter list if any.
     * @param component The component that's supposedly enclosed.
     * @return The enclosing component or <code>null</code> otherwise.
     */
    protected SubFilterList getEnclosing (Component component)
    {
        do
            component = component.getParent ();
        while (     (null != component)
                && !(component instanceof SubFilterList));

        return ((SubFilterList)component);
    }

    /**
     * Get the enclosed sub filter list if any.
     * @param component The component that's supposedly enclosing the list.
     * @return The enclosed component or <code>null</code> otherwise.
     */
    protected SubFilterList getEnclosed (Component component)
    {
        Component[] list;

        if (component instanceof Container)
        {
            list = ((Container)component).getComponents  ();
            for (int i = 0; i < list.length; i++)
                if (list[i] instanceof SubFilterList)
                    return ((SubFilterList)list[i]);
        }

        return (null);
    }


    /**
     * Makes a program like:
     * <pre>
     * // Generated by FilterBuilder. http://htmlparser.org
     * // [aced0005737200206f72672e68746d6c7061727365722e66696c746572732e416e6446696c74657224c30516b2b7b2120200015b000b6d5072656469636174657374001c5b4c6f72672f68746d6c7061727365722f4e6f646546696c7465723b78707572001c5b4c6f72672e68746d6c7061727365722e4e6f646546696c7465723b8f17479b1d5f7992020000787000000002737200246f72672e68746d6c7061727365722e66696c746572732e5461674e616d6546696c746572b28b2601a614890f0200014c00056d4e616d657400124c6a6176612f6c616e672f537472696e673b78707400044d455441737200296f72672e68746d6c7061727365722e66696c746572732e48617341747472696275746546696c74657296abdfb3b0714cda0200024c000a6d41747472696275746571007e00064c00066d56616c756571007e000678707400046e616d6570]
     *                                                                                                                                                         
     * import org.htmlparser.*;
     * import org.htmlparser.filters.*;
     * import org.htmlparser.beans.*;
     * import org.htmlparser.util.*;
     *                                                                                                                                                         
     * public class Test
     * {
     *     public static void main (String args[])
     *     {
     *         TagNameFilter filter0 = new TagNameFilter ();
     *         filter0.setName ("META");
     *         HasAttributeFilter filter1 = new HasAttributeFilter ();
     *         filter1.setAttributeName ("name");
     *         NodeFilter[] array0 = new NodeFilter[2];
     *         array0[0] = filter0;
     *         array0[1] = filter1;
     *         AndFilter filter2 = new AndFilter ();
     *         filter2.setPredicates (array0);
     *         NodeFilter[] array1 = new NodeFilter[1];
     *         array1[0] = filter2;
     *         FilterBean bean = new FilterBean ();
     *         bean.setFilters (array1);
     *         if (0 != args.length)
     *         {
     *             bean.setURL (args[0]);
     *             System.out.println (bean.getNodes ().toHtml ());
     *         }
     *         else
     *             System.out.println ("Usage: java -classpath .:htmlparser.jar Test <url>");
     *     }
     * }
     * </pre>
     * @param name The name of the class. 
     * @param out The buffer to append to.
     * @param bean The bean to extract the filters from to make the program.
     */
    protected void makeProgram (String name, StringBuffer out, FilterBean bean)
    {
        // so we need to keep track of filters and arrays of filters to give them unique numbers
        // each Filter is responsible for outputting it's code and returning it's variable name
        int[] context; // 0 - indent, 1 - next filter variable #, 2 - next array of filters variable #
        String[] names;
        Filter[] filters;
        String array;

        filters = (Filter[])bean.getFilters ();

        context = new int[3];
        context[0] = 0;

        Filter.spaces (out, context[0]);
        out.append ("// Generated by FilterBuilder. http://htmlparser.org");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("// ");
        try
        {
            out.append (Filter.deconstitute (filters));
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace ();
        }
        Filter.newline (out);
        Filter.newline (out);

        Filter.spaces (out, context[0]);
        out.append ("import org.htmlparser.*;");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("import org.htmlparser.filters.*;");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("import org.htmlparser.beans.*;");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("import org.htmlparser.util.*;");
        Filter.newline (out);
        Filter.newline (out);

        Filter.spaces (out, context[0]);
        out.append ("public class ");
        out.append (name);
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("{");

        context[0] = 4;
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("public static void main (String args[])");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("{");
        Filter.newline (out);
        
        context[0] = 8;
        names = new String [filters.length];
        for (int i = 0; i < names.length; i++)
            names[i] = filters[i].toJavaCode (out, context);

        array = "array" + context[2]++;
        Filter.spaces (out, context[0]);
        out.append ("NodeFilter[] ");
        out.append (array);
        out.append (" = new NodeFilter[");
        out.append (filters.length);
        out.append ("];");
        Filter.newline (out);
        for (int i = 0; i < filters.length; i++)
        {
            Filter.spaces (out, context[0]);
	        out.append (array);
	        out.append ("[");
	        out.append (i);
	        out.append ("] = ");
	        out.append (names[i]);
	        out.append (";");
	        Filter.newline (out);
        }

        Filter.spaces (out, context[0]);
        out.append ("FilterBean bean = new FilterBean ();");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("bean.setFilters (");
        out.append (array);
        out.append (");");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("if (0 != args.length)");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("{");
        Filter.newline (out);
        context[0] = 12;
        Filter.spaces (out, context[0]);
        out.append ("bean.setURL (args[0]);");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("System.out.println (bean.getNodes ().toHtml ());");
        Filter.newline (out);
        context[0] = 8;
        Filter.spaces (out, context[0]);
        out.append ("}");
        Filter.newline (out);
        Filter.spaces (out, context[0]);
        out.append ("else");
        Filter.newline (out);
        context[0] = 12;
        Filter.spaces (out, context[0]);
        out.append ("System.out.println (\"Usage: java -classpath .:htmlparser.jar ");
        out.append (name);
        out.append (" <url>\");");
        Filter.newline (out);
        
        context[0] = 4;
        Filter.spaces (out, context[0]);
        out.append ("}");
        Filter.newline (out);

        context[0] = 0;
        Filter.spaces (out, context[0]);
        out.append ("}");
        Filter.newline (out);
    }

    /**
     * Extracts a java class name from a file name.
     * ToDo: make this package-smart somehow.
     * @param file The name of the file.
     * @return The name of the class.
     */
    protected String classFromFile (String file)
    {
        String filesep;
        int index;

        // remove any path
        filesep = System.getProperty ("file.separator");
        index = file.lastIndexOf (filesep);
        if (-1 != index)
            file = file.substring (index + filesep.length ());
        // remove the extension
        index = file.indexOf ('.');
        if (-1 != index)
            file = file.substring (0, index);

        return (file);
    }

    /**
     * Save the workspace contents to file.
     * @param name The name of the file to save it under.
     */
    public void save (String name)
    {
        Filter[] selections;
        FilterBean bean;
        StringBuffer buffer;
        PrintWriter out;
        String ok = "OK";

        selections = getFilters ();
        if (0 != selections.length)
        {
            bean = new FilterBean ();
            bean.setURL (mURLField.getText ());
            bean.setFilters (selections);
            buffer = new StringBuffer ();
            makeProgram (classFromFile (name), buffer, bean);
            try
            {
                out = new PrintWriter (new FileWriter (name), true);
                try
                {
                    out.write (buffer.toString ());
                    out.flush ();
                }
                finally
                {
                    out.close ();
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace ();
            }
        }
        else // ToDo: grey out save option if nothing to save...
            JOptionPane.showOptionDialog (
                mMainPanel,
                "No filters to save.",
                "Oops",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new String[] { ok },
                ok);
    }

    /**
     * The action to take when "New" menu or button pressed.
     */
    protected void newAction ()
    {
        mMainPanel.removeAll ();
        mSelection.clear ();
        relayout ();
    }

    /**
     * The action to take when "Open" menu or button pressed.
     */
    protected void openAction ()
    {
        FileDialog dialog;
        File file;

        dialog = new FileDialog (this);
        dialog.setMode (FileDialog.LOAD);
        dialog.setTitle ("Open");
        dialog.setDirectory (mHomeDir);
        dialog.setVisible (true);
        if (null != dialog.getFile ())
        {
            mHomeDir = dialog.getDirectory ();
            file = new File (mHomeDir + dialog.getFile ());
            open (file.getAbsolutePath ());
            setTitle (TITLE + " - " + file.getAbsolutePath ());
        }
    }

    /**
     * The action to take when "Save" menu or button pressed.
     */
    protected void saveAction ()
    {
        String title;
        int index;
        File file;
        FileDialog dialog;

        title = getTitle ();
        index = title.indexOf (" - ");
        if (-1 != index)
            file = new File (title.substring (index + 3));
        else
        {
            dialog = new FileDialog (this);
            dialog.setMode (FileDialog.SAVE);
            dialog.setTitle ("Save");
            dialog.setDirectory (mHomeDir);        
            dialog.setVisible (true);
	        if (null != dialog.getFile ())
	        {
	            mHomeDir = dialog.getDirectory ();
	            file = new File (mHomeDir + dialog.getFile ());
	            setTitle (TITLE + " - " + file.getAbsolutePath ());
	        }
	        else
	            file = null;
        }
        if (null != file)
            save (file.getAbsolutePath ());
    }

    /**
     * The action to take when "Save As" menu or button pressed.
     */
    protected void saveasAction ()
    {
        setTitle (TITLE);
        saveAction ();
    }

    /**
     * The action to take when "Exit" menu or button pressed.
     */
    protected void exitAction ()
    {
        exitApplication ();
    }

    /**
     * The action to take when "Cut" menu or button pressed.
     */
    protected void cutAction ()
    {
        String string;
        StringSelection contents;
        Clipboard cb;
            
        // get the selection
        string = serializeSelection ();
        // copy to clipboard
        contents = new StringSelection (string);
        cb = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        cb.setContents (contents, this);
        // delete the selection
        deleteSelection ();
        relayout ();
    }

    /**
     * The action to take when "Copy" menu or button pressed.
     */
    protected void copyAction ()
    {
        String string;
        StringSelection contents;
        Clipboard cb;
            
        // get the selection
        string = serializeSelection ();
        // copy to clipboard
        contents = new StringSelection (string);
        cb = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        cb.setContents (contents, this);
    }

    /**
     * The action to take when "Paste" menu or button pressed.
     */
    protected void pasteAction ()
    {
        Clipboard cb;
        Transferable content;
        String string;
        Filter[] filters;
        Point point;
        SubFilterList list;
            
        // get the text
        cb = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        content = cb.getContents (this);
        if (content.isDataFlavorSupported (DataFlavor.stringFlavor))
        {
            try
            {
                string = (String)content.getTransferData (DataFlavor.stringFlavor);
                // deserialize it and add into the selection
                filters = Filter.reconstitute (string, new Parser (mURLField.getText ()));
                // add it to the (single) selected object or main panel
                if (isSingleSelection ()
                        && (null != (list = getEnclosed (getSelection ()[0]))))
                {
                    for (int i = 0; i < filters.length; i++)
                        list.addFilter (filters[i]);
                }
                else
                {
                    point = new Point (0,0);
                    for (int i = 0; i < filters.length; i++)
                    {
                        filters[i].setLocation (point);
                        mMainPanel.add (filters[i]);
                        point.y += filters[i].getPreferredSize ().height;
                    }
                }
                setupMouseListeners (filters);
                setupDropTargets (filters);
                relayout ();
            }
            catch (Exception e)
            {
                e.printStackTrace ();
            }
        }
    }

    /**
     * The action to take when "Delete" menu or button pressed.
     */
    protected void deleteAction ()
    {
        // delete the selection
        deleteSelection ();
        relayout ();
    }

    /**
     * The action to take when a filter menu or button pressed.
     */
    protected void filterAction ()
    {
        String cls;
        Filter filter;
        SubFilterList list;
        Point point;
        
        // retrieve the source component placed there by actionPerformed
        cls = mCurrentComponent.getName ();
        filter = Filter.instantiate (cls);
        // need this to get the underlying filter prepped?
        try
        {
            filter = Filter.wrap (filter.getNodeFilter (), new Parser (mURLField.getText ()));
        }
        catch (ParserException pe)
        {
            pe.printStackTrace ();
        }
        // add it to the (single) selected object or main panel
        if (isSingleSelection ()
                && (null != (list = getEnclosed (getSelection ()[0]))))
        {
            insertFilters (new Filter[] {filter}, null, list);
        }
        else
        {
            point = new Point (50,50); // find where and who to stick it into
            insertFilters (new Filter[] {filter}, point, null);
        }
    }
    
    /**
     * The action to take when "Fetch" menu pressed.
     */
    protected void fetchAction ()
    {
        JInternalFrame frame;
        Dimension dimension;
        Parser parser;
        NodeList list;

        // set up an internal frame for the results
        frame = new JInternalFrame (mURLField.getText ()); 
        frame.setClosable (true); 
        frame.setResizable (true);
        dimension = mOutput.getSize ();
        frame.setBounds (0, 0, dimension.width, dimension.height);
        list = new NodeList ();
        try
        {
	        parser = new Parser (mURLField.getText ());
	        try
	        {
		        for (NodeIterator iterator = parser.elements (); iterator.hasMoreNodes (); )
		            list.add (iterator.nextNode ());
	        }
	        catch (EncodingChangeException ece)
	        {
	            list.removeAll ();
	            parser.reset ();
		        for (NodeIterator iterator = parser.elements (); iterator.hasMoreNodes (); )
		            list.add (iterator.nextNode ());
	        }
        }
        catch (ParserException pe)
        {
            pe.printStackTrace ();
        }
        JTree tree = new JTree (new HtmlTreeModel (list));
        tree.setRootVisible (false);
        tree.setCellRenderer (new HtmlTreeCellRenderer ());
        JScrollPane treeView = new JScrollPane (tree);
		frame.setContentPane (new JScrollPane (
		    treeView,
		    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        mOutput.add (frame, new Integer (1));   
        try
        {
            frame.setSelected (true); 
        }
        catch (PropertyVetoException pve)
        {
            pve.printStackTrace ();
        }
        frame.show (); 
    }

    /**
     * The action to take when "Execute" menu or button pressed.
     */
    protected void executeAction ()
    {
        Filter[] selections;
        FilterBean bean;
        JInternalFrame frame;
        Dimension dimension;
//        JTextPane text;

        selections = getSelection ();
        if (0 == selections.length)
            selections = getFilters ();
        if (0 != selections.length)
        {
            bean = new FilterBean ();
            bean.setURL (mURLField.getText ());
            bean.setFilters (selections);
            
            // set up an internal frame for the results
            frame = new JInternalFrame (bean.getURL ()); 
            frame.setClosable (true); 
            frame.setResizable (true);
            dimension = mOutput.getSize ();
            frame.setBounds (0, 0, dimension.width, dimension.height / 2);
            JTree tree = new JTree (new HtmlTreeModel (bean.getNodes ()));
            tree.setRootVisible (false);
            tree.setCellRenderer (new HtmlTreeCellRenderer ());
            JScrollPane treeView = new JScrollPane (tree);
			frame.setContentPane (new JScrollPane (
			    treeView,
			    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
//            text = new JTextPane ();
//            text.setText (bean.getNodes ().toHtml ());
//            frame.setContentPane (new JScrollPane (
//                    text,
//                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
            mOutput.add (frame, new Integer(2)); // layer 2?   
            try
            {
                frame.setSelected (true); 
            }
            catch (PropertyVetoException pve)
            {
                pve.printStackTrace ();
            }
            frame.show (); 
        }
    }

    /**
     * The action to take when "Instructions" menu pressed.
     */
    protected void instructionsAction ()
    {
        String instructions =
            "<html>" +
            "Enter the target URL in the text box at the bottom of the window.<br>" +
            "Choose 'Fetch Page' from the Operations menu to see the whole page.<br>" +
            "Pick filters from the Filter menu or drag them from the toolbar.<br>" +
            "Filters such as And, Or, Not, HasParent, HasChild and HasSibling contain other filters:<br>" +
            "<ul><li>drag new filters into their blank areas at the bottom</li>" +
            "<li>cut an existing filter and paste into a selected filter</li></ul>" +
            "Build the filter incrementally, choosing 'Execute Filter' to test the selected filter.<br>" +
            "Save creates a .java file that runs the top level filter.<br>" +
            "Right click on a filter displays a pop-up menu.<br>" +
            "Double click on a blue item in the result pane expands the tree." +
            "</html>";
        String close = "Close";
        JOptionPane.showOptionDialog (
        // not .showMessageDialog(
            mMainPanel,
            instructions,
            "FilterBuilder Instructons",
            // remove this:
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            // and remove rest of these:
            null,
            new String[] { close },
            close);
    }

    /**
     * The action to take when "Filtering" menu pressed.
     */
    protected void filteringAction ()
    {
        String instructions =
            "<html>" +
            "The HTML Parser filter subsystem extracts items from a web page,<br>" +
            "corresponding to the use-case 'I want this little piece of information from http://yadda'.<br>" +
            "The web page is considered a heirarchical tree of nodes. Usually the root node is &lt;html&gt;,<br>" +
            "intermediate level nodes are &lt;div&gt; and &lt;table&gt; for example,<br>" +
            "and leaf nodes are things like text or &lt;img&gt;.<br>" +
            "Any node that isn't the root node has a 'parent' node.<br>" +
            "Leaf nodes, by definition, have no 'children'.<br>" +
            "A filter is a Java class that answers the simple question:<br>" +
            "<pre>Is this node acceptable? True or false.</pre><br>" +
            "Some filters know the answer just by looking at the node,<br>" +
            "while others must ask other filters, sometimes looking up or down the node heirarchy.<br>" +
            "<b>The FilterBuilder is a program for making other programs that use filters.</b><br>" +
            "By combining different types of filters, specific nodes can be isolated from the<br>" +
            "target web page.<br>" +
            "The results are usually passed on to another part of the users program<br>" +
            "that does something useful with them.<br>" +
            "The filters available include:<br>" +
            "<ul>" +
            "<li>AndFilter - The main 'combining' filter, answers <code>true</code> only if<br>" +
            "all it's subfilters (predicates) are <code>true</code>.</li>" +
            "<li>OrFilter - A 'combining' filter that answers <code>true</code> if<br>" +
            "any of it's subfilters (predicates) are <code>true</code>.</li>" +
            "<li>NotFilter - A 'reversing' filter that answers <code>true</code> if<br>" +
            "it's subfilter (predicate) is <code>false</code>.</li>" +
            "<li>StringFilter - A 'leaf' filter that answers <code>true</code> if<br>" +
            "the node is text and it contains a certain sequence of characters.<br>" +
            "It can be made case insensitive, but in this case a 'locale' must be<br>" +
            "supplied as a context for upper-case conversion.</li>" +
            "<li>RegexFilter - A 'leaf' filter that answers <code>true</code> if<br>" +
            "the node is text and it contains a certain pattern (regular expression).<br>" +
            "Regular expressions are descibed in the java.util.regex.Pattern class documentation.</li>" +
            "<li>TagNameFilter - A filter that answers <code>true</code> if<br>" +
            "the node is a tag and it has a certain name," +
            "i.e. &lt;div&gt; would match the name <code>DIV</code>.</li>" +
            "<li>NodeClassFilter - A filter that answers <code>true</code> if<br>" +
            "the node is a certain tag class. Not recommended, use TagNameFilter instead.</li>" +
            "<li>HasAttributeFilter - A filter that answers <code>true</code> if<br>" +
            "the node is a tag and it has a certain attribute,<br>" +
            "i.e. &lt;script language=javascript&gt; would match the attribute <code>LANGUAGE</code>.<br>" +
            "It can be further restricted to have a certain attribute value as well,<br>" +
            "i.e. 'javascript' in this example.</li>" +
            "<li>HasParentFilter - A filter that answers <code>true</code> if<br>" +
            "the node is a child of a node that is acceptable to a certain filter.<br>" +
            "This can be made recursive, which means the acceptable parent can be<br>" +
            "further up the heirarchy than just the immediate parent node.</li>" +
            "<li>HasChildFilter - A filter that answers <code>true</code> if<br>" +
            "the node is a parent of a node that is acceptable to a certain filter.<br>" +
            "This can be made recursive, which means the acceptable child can be<br>" +
            "further down the heirarchy than just the immediate children nodes.</li>" +
            "<li>HasSiblingFilter - A filter that answers <code>true</code> if<br>" +
            "the node is a sibling (they have a common parent) of a node that is<br>" +
            "acceptable to a certain filter.</li>" +
            "</ul>" +
            "</html>";
        String close = "Close";
        JOptionPane.showOptionDialog (
        // not .showMessageDialog(
            mMainPanel,
            instructions,
            "FilterBuilder Instructons",
            // remove this:
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            // and remove rest of these:
            null,
            new String[] { close },
            close);
    }

    /**
     * The action to take when "Tutorial" menu pressed.
     */
    protected void tutorialAction ()
    {
        String instructions =
            "<html>" +
            "To get the title text from a page:<br>" +
            "<ul><li>Choose 'New' from the File menu.</li>" +
            "<li>Choose 'AndFilter' from the Filter menu.</li>" +
            "<li>Select the And filter so it is highlighted.</li>" +
            "<li>Choose 'HasParent' from the Filter menu.</li>" +
            "<li>Toggle the 'Recursive' checkbox on in the HasParent filter.</li>" +
            "<li>Select the HasParent filter so it is highlighted.</li>" +
            "<li>Choose 'TagName' from the Filter menu.<br>" +
            "<i>Alternatively, you can drag the TagName filter (icon Hello-BOB)<br>" +
            "from the toolbar and drop inside the HasParent filter</i></li>" +
            "<li>Choose 'TITLE' from the TagName combo-box.</li>" +
            "<li>Select the And filter and choose 'Execute Filter' from the<br>" +
            "Operations menu to test it.</li>" +
            "<li>If there is unwanted non-text nodes in the result<br>" +
            "select the And filter and choose 'RegexFilter' from the Filter menu.</li>" +
            "<li>Test it again, as above.</li>" +
            "<li>Choose 'Save' from the File menu and enter a filename like GetTitle.java</li>" +
            "<li>Compile the java file and run it.</li></ul>" +
            "</html>";
        String close = "Close";
        JOptionPane.showOptionDialog (
        // not .showMessageDialog(
            mMainPanel,
            instructions,
            "FilterBuilder Tutorial",
            // remove this:
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            // and remove rest of these:
            null,
            new String[] { close },
            close);
    }

    /**
     * The action to take when "Hints" menu pressed.
     */
    protected void hintsAction ()
    {
        String instructions =
            "<html>" +
            "Hints:<br>" +
            "<ul><li>There is no undo yet, so save often.</li>" +
            "<li>Recursive HasParent and HasChild filters can be costly.</li>" +
            "<li>RegexFilter is more expensive than StringFilter.</li>" +
            "<li>The order of predicates in And and Or filters matters for performance,<br>" +
            "put cheap tests first.</li>" +
            "<li>The same node may show up more than once in the results,<br>" +
            "and at more than one nesting depth, depending on the filter used.</li>" +
            "<li>Typing in a tag name in the TagName filter is not recommended,<br>" +
            "since extraneous characters can be added. Use an item from the list instead.</li></ul>" +
            "</html>";
        String close = "Close";
        JOptionPane.showOptionDialog (
        // not .showMessageDialog(
            mMainPanel,
            instructions,
            "FilterBuilder Hints",
            // remove this:
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            // and remove rest of these:
            null,
            new String[] { close },
            close);
    }

    /**
     * The action to take when "About" menu or button pressed.
     */
    protected void aboutAction ()
    {
        String close = "Close";
        JOptionPane.showOptionDialog (
        // not .showMessageDialog(
            mMainPanel,
            "<html><center><font color=black>The HTML Parser <font color=blue><b>FilterBuilder</b></font><br><i>by Derrick Oswald</i>&nbsp;&nbsp;<b>DerrickOswald@users.sourceforge.net</b><br>http://htmlparser.org<br><br><font size=-2>Copyright &copy; 2005</font></center></html>",
            "About FilterBuilder",
            // remove this:
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            // and remove rest of these:
            null,
            new String[] { close },
            close);
    }

    /**
     * The action to take when "Expand" menu chosen.
     */
    public void expandAction ()
    {
        setExpanded (getSelection (), true, false);
    }

    /**
     * The action to take when "Collapse" menu chosen.
     */
    public void collapseAction ()
    {
        setExpanded (getSelection (), false, false);
    }

    /**
     * The action to take when "Expand All" menu chosen.
     */
    public void expandAllAction ()
    {
        setExpanded (getSelection (), true, true);
    }

    /**
     * The action to take when "Collapse" menu chosen.
     */
    public void collapseAllAction ()
    {
        setExpanded (getSelection (), false, true);
    }

    /**
     * Set up mouse listeners.
     * Sets <code>this</code> up to listen to each command
     * in the list as a MouseListener.
     * Recursively descends the tree adding to all contained elements also.
     * @param filters The container with commands in it.
     */
    public void setupMouseListeners (Filter[] filters)
    {
        SubFilterList list;

        for (int i = 0; i < filters.length; i++)
        {
            // set us up as a mouse listener on it
            ((Component)filters[i]).addMouseListener (this);
            ((Component)filters[i]).addMouseMotionListener (this);
            list = getEnclosed (filters[i]);
            if (null != list)
                setupMouseListeners (list.getFilters ());
        }
    }

    /**
     * Set up drop targets.
     * Recursively descends the filter tree and sets up
     * the filter lists as drop targets.
     * @param filters The container with filters in it.
     */
    public void setupDropTargets (Filter[] filters)
    {
        SubFilterList list;
        Component[] components;

        for (int i = 0; i < filters.length; i++)
        {
            list = getEnclosed (filters[i]);
            if (null != list)
            {
                components = list.getDropTargets ();
                for (int j = 0; j < components.length; j++)
                    new DropTarget (components[j], this);
                setupDropTargets (list.getFilters ());
            }
        }
    }

    /**
     * Expand or collapse filters, possibly recursively.
     * @param filters The list of filters to expand or collapse.
     * @param expanded If <code>true</code> the filters are expanded,
     * otherwise they are collapsed.
     * @param recursive If <code>true</code> the filters are processed
     * recursively.
     */
    public void setExpanded (
        Filter[] filters,
        boolean expanded,
        boolean recursive)
    {
        SubFilterList list;

        for (int i = 0; i < filters.length; i++)
        {
            if (recursive && (null != (list = getEnclosed (filters[i]))))
                setExpanded (list.getFilters (), expanded, recursive);
            filters[i].setExpanded (expanded);
        }
    }

    /**
     * Retrieve the top level filters in the main window.
     * @return The top level filters.
     */
    public Filter[] getFilters ()
    {
        Component[] components;
        Filter[] ret;

        components = mMainPanel.getComponents ();
        ret = new Filter[components.length];
        System.arraycopy (components, 0, ret, 0, components.length);
        
        return (ret);
    }

    /**
     * Redo the layout.
     */
    public void relayout ()
    {
        mMainPanel.invalidate ();
        mMainScroller.invalidate ();
        mMainScroller.validate ();
        mMainScroller.repaint ();
    }

    /**
     * Read a workspace from file.
     * The current contents are erased.
     * @param name The name of the file to open.
     */
    public void open (String name)
    {
        LineNumberReader reader;
        String line;
        Filter[] filters;
        Point point;
        Dimension dimension;

        try
        {
            reader = new LineNumberReader (new FileReader (name));
            while (null != (line = reader.readLine ()))
                if (line.startsWith ("// ["))
                {
                    line = line.substring (3);
    	            try
    	            {
    	                filters = Filter.reconstitute (line, new Parser (mURLField.getText ()));
    	                mMainPanel.removeAll ();
    	                point = new Point ();
    	                for (int i = 0; i < filters.length; i++)
    	                {
    	                    dimension = filters[i].getPreferredSize ();
    	                    mMainPanel.add (filters[i]);
    	                    filters[i].setLocation (point);
    	                    point.y += dimension.height;
    	                }
    	                setupMouseListeners (filters);
    	                setupDropTargets (filters);
    	                relayout ();
    	            }
    	            catch (ParserException pe)
    	            {
    	                pe.printStackTrace ();
    	            }
                    break;
                }
            reader.close ();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace ();
        }
    }

    /**
     * Exit back to the operating system.
     */
    void exitApplication ()
    {
        this.setVisible (false);    // hide the Frame
        this.dispose ();            // free the system resources
        System.exit (0);            // close the application
    }

    /**
     * Show a pop up context menu.
     * Shows a context sensitive popup menu at the location of the
     * mouse event.
     * @param event The mouse event that initiates the popup.
     */
    public void showContextMenu (MouseEvent event)
    {
        JPopupMenu menu;
        JMenuItem item;

        menu = new JPopupMenu ();
        menu.setName ("Popup");

        item = new JMenuItem ("Expand");
        item.setActionCommand ("expandAction");
        item.addActionListener (this);
        menu.add (item);

        item = new JMenuItem ("Collapse");
        item.setActionCommand ("collapseAction");
        item.addActionListener (this);
        menu.add (item);

        menu.addSeparator ();

        item = new JMenuItem ("Expand All");
        item.setActionCommand ("expandAllAction");
        item.addActionListener (this);
        menu.add (item);

        item = new JMenuItem ("CollapseAll");
        item.setActionCommand ("collapseAllAction");
        item.addActionListener (this);
        menu.add (item);

        menu.addSeparator ();

        item = new JMenuItem ("Cut");
        item.setActionCommand ("cutAction");
        item.addActionListener (this);
        menu.add (item);

        item = new JMenuItem ("Copy");
        item.setActionCommand ("copyAction");
        item.addActionListener (this);
        menu.add (item);

        item = new JMenuItem ("Paste");
        item.setActionCommand ("pasteAction");
        item.addActionListener (this);
        menu.add (item);

        item = new JMenuItem ("Delete");
        item.setActionCommand ("deleteAction");
        item.addActionListener (this);
        menu.add (item);

        menu.addSeparator ();

        item = new JMenuItem ("Execute Filter");
        item.setActionCommand ("executeAction");
        item.addActionListener (this);
        menu.add (item);

        menu.show (event.getComponent (), event.getX (), event.getY ());
    }

    //
    // selection manipulation
    //

    /**
     * Add a filter to the current selection set.
     * @param filter The filter to add.
     */
    protected void addSelection (Filter filter)
    {
        if (!selectionContains (filter))
            mSelection.addElement (filter);
        filter.setSelected (true);
        mMoved = false;
    }

    /**
     * Remove a filter from the current selection set.
     * @param filter The filter to remove.
     */
    protected void removeSelection (Filter filter)
    {
        mSelection.removeElement (filter); // no harm if not contained
        filter.setSelected (false);
    }

    /**
     * Select(highlight)/deselect the current selection set.
     * @param select If <code>true</code> turn on highlighting,
     * turn it off otherwise.
     */
    protected void selectSelection (boolean select)
    {
        int count;
        Filter filter;
        
        count = mSelection.size ();
        for (int i = 0; i < count; i++)
        {
            filter = (Filter)mSelection.elementAt (i);
            filter.setSelected (select);
        }
    }

    /**
     * Clear (empty) the current selection set.
     */
    protected void clearSelection ()
    {
        selectSelection (false);
        mSelection.removeAllElements ();
    }

    /**
     * Move the current selection set as a group.
     * @param translation The displacement to move them all by.
     */
    protected void moveSelection (Point translation)
    {
        int count;
        Filter filter;
        Point point;
        
        count = mSelection.size ();
        for (int i = 0; i < count; i++)
        {
            filter = (Filter)mSelection.elementAt (i);
            point = filter.getLocation ();
            point.translate (translation.x, translation.y);
            synchronized (filter.getTreeLock ())
            {
                filter.setLocation (point.x, point.y);
            }
        }
        mMoved = true;
    }

    /**
     * Check if the current selection set contains the given filter.
     * @param filter The filter to check.
     * @return <code>true</code> if the filter is a member,
     * <code>false</code> otherwise.
     */
    protected boolean selectionContains (Filter filter)
    {
        return (mSelection.contains (filter));
    }

    /**
     * Return the last filter added to the selection set.
     * @return The last filter added or <code>null</code> if the current
     * selection set is empty.
     */
    protected Filter lastSelected ()
    {
        Filter ret;
        
        ret = null;

        if (0 < mSelection.size ())
            ret = (Filter)mSelection.lastElement ();
            
        return (ret);
    }

    /**
     * Return the current selection set as an array.
     * @return The array of selected filters.
     */
    protected Filter[] getSelection ()
    {
        Filter[] ret;

        ret = new Filter[mSelection.size ()];
        mSelection.copyInto (ret);

        return (ret);
    }

    /**
     * Serialize the current selection set.
     * @return The serialized form of the set of filters.
     */
    public String serializeSelection ()
    {
        Filter[] filters;
        StringWriter writer;
        PrintWriter out;

        filters = getSelection ();
        writer = new StringWriter (200);
        out = new PrintWriter (writer, false);
        try
        {
            out.println (Filter.deconstitute (filters));
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace ();
        }
        finally
        {
            out.close ();
        }
        
        return (writer.getBuffer ().toString ());
    }

    /**
     * Delete the current selection set from the filters in the GUI.
     */
    public void deleteSelection ()
    {
        Filter[] filters;
        SubFilterList list;

        filters = getSelection ();
        for (int i = 0; i < filters.length; i++)
        {
            list = getEnclosing (filters[i]);
            if (null != list)
                list.removeFilter (filters[i]);
            else
                mMainPanel.remove (filters[i]);
        }
        mSelection.clear ();
    }
    
    /**
     * Check if there is more than one filter selected.
     * @return <code>true</code> if only one filter is selected,
     * <code>false</code> otherwise.
     */
    public boolean isSingleSelection ()
    {
        return (1 == mSelection.size());
    }

    //
    // MouseListener interface
    //

    /**
     * Invoked when the mouse has been clicked on a component.
     * @param event The mouse clicked event.
     */
    public void mouseClicked (MouseEvent event)
    {
        Object component;
        Filter filter;
        SubFilterList list;
        int modifiers;
        boolean contained;
        Filter[] filters;
        
        component = event.getSource ();
        if (component instanceof Filter)
        {
            filter = (Filter)component;
            modifiers = event.getModifiers ();
            contained = selectionContains (filter);
            
            if (0 != (modifiers & InputEvent.SHIFT_MASK))
            {
                // add everything from last selected to this command
                list = getEnclosed (filter);
                if (null != list)
                    filters = list.getFilters ();
                else
                    filters = getFilters ();
                Filter last = lastSelected ();
                if (null == last)
                    addSelection (filter);
                else
                {
                    int current = -1;
                    int recent = -1;
                    for (int i = 0; i < filters.length; i++)
                    {
                        if (filters[i] == filter)
                            current = i;
                        if (filters[i] == last)
                            recent = i;
                    }
                    if ((current != -1) && (recent != -1))
                        for (int i = Math.min (current, recent);
                            i <= Math.max (current, recent); i++)
                        addSelection (filters[i]);
                }
            }
            else if (0 != (modifiers & InputEvent.CTRL_MASK))
            {
                // add just the new command
                if (contained)
                    removeSelection (filter);
                else
                    addSelection (filter);
            }
            else if (0 != (modifiers & InputEvent.BUTTON3_MASK))
            {
                if (!selectionContains (filter))
                {
	                clearSelection ();
	                addSelection (filter);
                }
                showContextMenu (event);
            }
            else
            {
                clearSelection ();
                addSelection (filter);
            }
        }
        else
            clearSelection ();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * @param event The mouse released event.
     */
    public void mouseReleased (MouseEvent event)
    {
    }

    /**
     * Invoked when the mouse enters a component.
     * @param event The mouse entered event.
     */
    public void mouseEntered (MouseEvent event)
    {
    }

    /**
     * Invoked when the mouse exits a component.
     * @param event The mouse exited event.
     */
    public void mouseExited (MouseEvent event)
    {
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param event The mouse pressed event.
     */
    public void mousePressed (MouseEvent event)
    {
        Object component;
        Point newpoint;
        Point upperleft;
        
        component = event.getSource ();
        if (component instanceof Filter)
        {
            // translate the point relative to the enclosing container
            newpoint = event.getPoint ();
            upperleft = ((Component)component).getLocation ();
            newpoint.translate (upperleft.x, upperleft.y);
            setBasePoint (newpoint);
        }
        else
            setBasePoint (null);
    }

    //
    // MouseMotionListener interface
    //
    
    /**
     * Mouse drag notification.
     * Invoked when a mouse button is pressed on a component and
     * then dragged. Mouse drag events will continue to be
     * delivered to the component where the first originated
     * until the mouse button is released (regardless of whether
     * the mouse position is within the bounds of the component).
     * @param event The mouse drag event.
     */
    public synchronized void mouseDragged (MouseEvent event)
    {
        Object component;
        Filter filter;
        Point base;
        Point newpoint;
        Point upperleft;
        Point translation;
        
        component = event.getSource ();
        if (component instanceof Filter)
        {
            filter = (Filter)component;
            if (selectionContains (filter)) // drag on a selected item
            {
                if (null == getEnclosing (filter)) // not contained
                    try
                    {
                        base = getBasePoint ();
                        if (null != base)
                        {
                            newpoint = event.getPoint ();
                            // translate the point relative to the enclosing container
                            upperleft = filter.getLocation ();
                            newpoint.translate (upperleft.x, upperleft.y);
                            // get the difference between this point and the old base
                            translation = new Point (
                                newpoint.x - base.x,
                                newpoint.y - base.y);
                            // update the base point
                            setBasePoint (newpoint);
                            // apply this difference to the selection
                            moveSelection (translation);
                        }    
                    }
                    catch (Exception e)
                    {
                    }
            }
            else
                mouseClicked (event); // a small slip shouldn't stop a click
        }
    }

    /**
     * Mouse move notification.
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).    
     * @param event The mouse moved event.
     */
    public void mouseMoved (MouseEvent event)
    {
    }

    //
    // WindowListener interface
    //

    /**
     * Invoked the first time a window is made visible.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowOpened (WindowEvent event) {}

    /**
     * Handles window closing event.
     * Performs function <code>exitApplication()</code>.
     * @param event The window event.
     */
    public void windowClosing (WindowEvent event)
    {
        if (event.getSource () == this)
            exitApplication ();
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowClosed (WindowEvent event) {}

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window 
     * is displayed as the icon specified in the window's 
     * iconImage property.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowIconified (WindowEvent event) {}

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowDeiconified (WindowEvent event) {}

    /**
     * Invoked when the window is set to be the user's
     * active window, which means the window (or one of its
     * subcomponents) will receive keyboard events.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowActivated (WindowEvent event) {}

    /**
     * Invoked when a window is no longer the user's active
     * window, which means that keyboard events will no longer
     * be delivered to the window or its subcomponents.
     * <i>Not used.</i>
     * @param event The window event.
     */
    public void windowDeactivated (WindowEvent event) {}

    //
    // ActionListener interface
    //

    /**
     * Handles menu and toolbar item choices.
     * @param event The action even that triggers this function.
     */
    public void actionPerformed (ActionEvent event)
    {
        Object object;
        String action;
        
        object = event.getSource();
//        if (object instanceof JButton)
//        {
//            String url;
//            url = mURLField.getText ();
//            mURLField.selectAll ();
//            //setURL (url);
//        }
        if (object instanceof JButton)
            action = ((JButton)object).getActionCommand ();
        else if (object instanceof JMenuItem)
            action = ((JMenuItem)object).getActionCommand ();
        else
            action = null;

        if (object instanceof Component)
            mCurrentComponent = (Component)object;

        if (null != action)
            try
            {
                Method method = this.getClass ().getDeclaredMethod (action, new Class[0]);
                method.invoke (this, new Object[0]);
            }
            catch (NoSuchMethodException nsme)
            {
                System.out.println ("no " + action + " method found");
            }
            catch (Exception e)
            {
                e.printStackTrace ();
            }
    }

    //
    // ClipboardOwner interface
    //

    /**
     * Notifies this object that it is no longer the owner
     * of the contents of the clipboard.
     * @param clipboard The clipboard that is no longer owned.
     * @param contents The contents which this owner had placed on the clipboard.
     */
    public void lostOwnership (Clipboard clipboard, Transferable contents)
    {
        System.out.println ("lost clipboard ownership");
    }

    //
    // DragGestureListener interface
    //

    /**
     * A DragGestureRecognizer has detected a platform-dependent drag initiating gesture.
     * It is notifying this listener in order for it to initiate the action for the user.
     * @param event The DragGestureEvent describing the gesture that has just occurred.
     */
    public void dragGestureRecognized (DragGestureEvent event)
    {
        Component component;
        String cls;
        Filter filter;
        StringSelection text;
        
        
        component = event.getComponent ();
        try
        {
            cls = component.getName ();  // (String)Filter.mWrappers.get (component.getName ());
            if (null != cls)
            {
                filter = Filter.instantiate (cls);
                text = new StringSelection (Filter.deconstitute (new Filter[] { filter })); 
                mDragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
    }

    //
    // DragSourceListener interface
    //

    /**
     * This message goes to DragSourceListener,
     * informing it that the dragging has ended.
     * @param event Details about the drop event.
     */
    public void dragDropEnd (DragSourceDropEvent event)
    {   
        if (event.getDropSuccess ())
        {
            // System.out.println ("added new class");
        }
    }

    /**
     * This message goes to DragSourceListener,
     * informing it that the dragging has entered the DropSite.
     * @param event Details about the drag event.
     */
    public void dragEnter (DragSourceDragEvent event)
    {
        // System.out.println ("dragEnter");
    }

    /**
     * This message goes to DragSourceListener, informing it that the dragging 
     * has exited the DropSite.
     * @param event Details about the drag event.
     */
    public void dragExit (DragSourceEvent event)
    {
        // System.out.println( "dragExit");
    }

    /**
     * This message goes to DragSourceListener, informing it that the dragging is currently 
     * ocurring over the DropSite.
     * @param event Details about the drag event.
     */
    public void dragOver (DragSourceDragEvent event)
    {
        // System.out.println( "dragExit");
    }

    /**
     * This is invoked when the user changes the dropAction.
     * @param event Details about the drop action event.
     */
    public void dropActionChanged (DragSourceDragEvent event)
    {
        // System.out.println( "dropActionChanged"); 
    }

    //
    // DropTargetListener interface
    //

    /**
     * This is invoked when you are dragging over the DropSite.
     * @param event Details about the drag event.
     */
    public void dragEnter (DropTargetDragEvent event)
    {
        // debug messages for diagnostics 
        // event.acceptDrag (DnDConstants.ACTION_MOVE);
        // System.out.println ("dragEnter");
        DropTargetContext context;
        Component component;
        SubFilterList list;

        // find the enclosing filter
        context = event.getDropTargetContext ();
        component = context.getComponent ();
        while (     (null != component)
                && !(component instanceof SubFilterList)
                && !(component == mMainPanel))
            component = component.getParent ();
        if (component instanceof SubFilterList)
            list = (SubFilterList)component;
        else
            list = null;
        // so either list is the enclosing list,
        // or list is null and the target component is the main panel

        if (null != list)
            if (!list.canAccept ())
                event.rejectDrag ();
            else
                list.setSelected (true);
    }

    /**
     * Thi ss invoked when you are exit the DropSite without dropping.
     * @param event Details about the drag event.
     */
    public void dragExit (DropTargetEvent event)
    {
        // debug messages for diagnostics 
        // event.acceptDrag (DnDConstants.ACTION_MOVE);
        // System.out.println ("dragEnter");
        DropTargetContext context;
        Component component;
        SubFilterList list;

        // find the enclosing filter
        context = event.getDropTargetContext ();
        component = context.getComponent ();
        while (     (null != component)
                && !(component instanceof SubFilterList)
                && !(component == mMainPanel))
            component = component.getParent ();
        if (component instanceof SubFilterList)
            list = (SubFilterList)component;
        else
            list = null;
        // so either list is the enclosing list,
        // or list is null and the target component is the main panel

        if (null != list)
            list.setSelected (false);
    }

    /**
     * This is invoked when a drag operation is going on.
     * @param event Details about the drag event.
     */
    public void dragOver (DropTargetDragEvent event)
    {
        // System.out.println( "dragOver");
    }

    /**
     * This is invoked when a drop has occurred.
     * @param event The drop event.
     */
    public void drop (DropTargetDropEvent event)
    {
        DropTargetContext context;
        Component component;
        SubFilterList list;
        String s;
        Point point;
        Filter[] filters;
        boolean accept;

        // find the enclosing filter
        context = event.getDropTargetContext ();
        component = context.getComponent ();
        while (     (null != component)
                && !(component instanceof SubFilterList)
                && !(component == mMainPanel))
            component = component.getParent ();
        if (component instanceof SubFilterList)
            list = (SubFilterList)component;
        else
            list = null;
        // so either list is the enclosing list,
        // or list is null and the target component is the main panel

        try 
        {
            accept = false;
            Transferable transferable = event.getTransferable();
                                   
            // we accept only Strings      
            if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor))
            {
                accept = true;
                event.acceptDrop (DnDConstants.ACTION_MOVE);
                s = (String)transferable.getTransferData (DataFlavor.stringFlavor);
                point = event.getLocation ();
                try
                {
                    // get the filter and add into the target
                    filters = Filter.reconstitute (s, new Parser (mURLField.getText ()));
                    if (0 < filters.length)
                        insertFilters (filters, point, list);
                    if (null != list)
                        list.setSelected (false);
                }
                catch (Exception e)
                {
                    e.printStackTrace ();
                }
                // signal the drop was successful
                context.dropComplete (accept);
            } 
            else
                event.rejectDrop();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            System.err.println( "Exception" + exception.getMessage());
            event.rejectDrop();
        } 
        catch (UnsupportedFlavorException ufException)
        {
            ufException.printStackTrace();
            System.err.println( "Exception" + ufException.getMessage());
            event.rejectDrop();
        }
    }

    /**
     * This is invoked if the user modifies the current drop gesture.
     * @param event Details about the drop action change event.
     */
    public void dropActionChanged (DropTargetDragEvent event)
    {
        // System.out.println( "dropActionChanged");
    }

    /**
     * The entry point for this application.
     * Creates a new FilterBuilder and makes it visible.
     * @param args [0] optional URL to operate on.
     */
    public static void main (String args[])
    {
        try
        {
            // set the Look and Feel to the the native system
//            try
//            {
//                javax.swing.UIManager.setLookAndFeel (javax.swing.UIManager.getSystemLookAndFeelClassName ());
//            } 
//            catch (Exception e)
//            { 
//            }

            // create a new instance of our application's frame, and make it visible
            FilterBuilder builder = new FilterBuilder ();
            if (0 != args.length)
                builder.mURLField.setText (args[0]);
            builder.setVisible (true);
        } 
        catch (Throwable t)
        {
            t.printStackTrace ();
            // ensure the application exits with an error condition
            System.exit (1);
        }
    }

}
