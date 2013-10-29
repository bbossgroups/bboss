// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexerapplications/thumbelina/TileSet.java,v $
// $Author: derrickoswald $
// $Date: 2004/07/31 16:42:30 $
// $Revision: 1.2 $
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

import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class to track picture regions.
 */
public class TileSet
/*
    extends
        java.awt.Canvas
    implements
        java.awt.event.ActionListener,
        java.awt.event.MouseListener,
        java.awt.event.WindowListener
*/
{
    /**
     * The list of Pictures.
     */
    protected Vector mRegions;

    /**
     * Construct a tile set.
     */
    public TileSet ()
    {
        mRegions = new Vector ();
    }

    /**
     * Get the number of tiles in this collection.
     * @return The number of pictures showing.
     * Note that the same image and URL may be showing
     * (different pieces) in several locations.
     */
    public int getSize ()
    {
        return (mRegions.size ());
    }

    /**
     * Get the list of pictures.
     * @return An enumeration over the picture objects in this set.
     */
    public Enumeration getPictures ()
    {
        return (mRegions.elements ());
    }

    /**
     * Add a single picture to the list.
     * @param r The picture to add.
     */
    public void add (final Picture r)
    {
        Vector regions; // this will be the new set
        Enumeration e;
        Picture rover;
        Rectangle intersection;
        Vector splits;
        Enumeration frags;

        regions = new Vector ();
        for (e = getPictures (); e.hasMoreElements (); )
        {
            rover = (Picture)e.nextElement ();
            if (rover.intersects (r))
            {
                intersection = rover.intersection (r);
                if (!intersection.equals (rover))
                {
                    // incoming lies completely within the existing picture
                    // or touches the existing picture somehow
                    splits = split (r, rover, false);
                    for (frags = splits.elements (); frags.hasMoreElements (); )
                        regions.addElement (frags.nextElement ());
                }
                else
                    // incoming covers existing... drop the existing picture
                    // but be sure to release the image memory
                    rover.setImage (null);
            }
            else
                // no conflict, keep the existing
                regions.addElement (rover);
        }
        regions.addElement (r);
        mRegions = regions;
    }

    /**
     * Split the large picture.
     * Strategy: split horizontally (full width strips top and bottom).
     * NOTE: top and bottom make sense only in terms of AWT coordinates.
     * @param small The incoming picture.
     * @param large The encompassing picture. The attributes of this one
     * are propagated to the fragments.
     * @param keep If <code>true</code>, the center area is kept,
     * otherwise discarded.
     * @return The fragments from the large picture.
     */
    private Vector split (
        final Picture small,
        final Picture large,
        final boolean keep)
    {
        Picture m;
        Vector ret;

        ret = new Vector ();

        if (large.intersects (small))
        {
            Rectangle intersection = large.intersection (small);

            // if tops don't match split off the top
            if ((intersection.y + intersection.height)
                != (large.y + large.height))
            {
                m = new Picture (large);
                m.y = (intersection.y + intersection.height);
                m.height = (large.y + large.height) - m.y;
                ret.addElement (m);
            }

            // if left sides don't match make a left fragment
            if (intersection.x != large.x)
            {
                m = new Picture (large);
                m.y = intersection.y;
                m.width = intersection.x - large.x;
                m.height = intersection.height;
                ret.addElement (m);
            }

            // the center bit
            if (keep)
            {
                m = new Picture (large);
                m.x = intersection.x;
                m.y = intersection.y;
                m.width = intersection.width;
                m.height = intersection.height;
                ret.addElement (m);
            }

            // if right sides don't match make a right fragment
            if ((intersection.x + intersection.width)
                != (large.x + large.width))
            {
                m = new Picture (large);
                m.x = intersection.x + intersection.width;
                m.y = intersection.y;
                m.width = (large.x + large.width) - m.x;
                m.height = intersection.height;
                ret.addElement (m);
            }

            // if bottoms don't match split off the bottom
            if (intersection.y != large.y)
            {
                m = new Picture (large);
                m.height = (intersection.y - large.y);
                ret.addElement (m);
            }
        }

        return (ret);
    }

    /**
     * Find the Picture at position x,y
     * @param x The x coordinate of the point to examine.
     * @param y The y coordinate of the point to examine.
     * @return The picture at that point, or <code>null</code>
     * if there are none.
     */
    public Picture pictureAt (final int x, final int y)
    {
        Picture m;
        Picture ret;

        ret = null;

        for (int i = 0; (null == ret) && (i < mRegions.size ()); i++)
        {
            m = (Picture)mRegions.elementAt (i);
            if (m.contains (x, y))
                ret = m;
        }

        return (ret);
    }

    /**
     * Move the given picture to the top of the Z order.
     * @param picture The picture to add.
     */
    public void bringToTop (final Picture picture)
    {
        Picture m;
        Picture ret;

        ret = null;

        for (int i = 0; (null == ret) && (i < mRegions.size ()); )
        {
            m = (Picture)mRegions.elementAt (i);
            if (picture.same (m))
                mRegions.removeElementAt (i);
            else
                i++;
        }
        add (picture);

    }

//    //
//    // Unit test.
//    //
//
//    // and need to add:
//    extends
//        java.awt.Canvas
//    implements
//        java.awt.event.ActionListener,
//        java.awt.event.MouseListener,
//        java.awt.event.WindowListener
//    // to the class definition
//
//    boolean mVerbose;
//    int mCounter;
//    java.awt.Point origin;
//    Rectangle last;
//    int type;
//
//  static java.awt.MenuBar menuMain;
//  static java.awt.Menu Options;
//  static java.awt.MenuItem repeat;
//  static java.awt.MenuItem clear;
//    static java.awt.TextField status;
//
//    // checks if adding the rectangle causes an overlap
//    boolean checkAdd (Rectangle r, Vector v)
//    {
//        Enumeration e;
//        boolean ret;
//        ret = false;
//
//        for (e = v.elements (); !ret && e.hasMoreElements (); )
//            ret = r.intersects ((Rectangle)e.nextElement ());
//
//        return (ret);
//    }
//
//    void paintwait ()
//    {
//        java.awt.Graphics g = getGraphics ();
//        if (null != g)
//            paint (g);
//        Thread.yield ();
//        try
//        {
//            Thread.sleep (1000);
//        }
//        catch (Exception exception)
//        {
//        }
//    }
//
//    void add ()
//    {
//        if (null != last)
//        {
//            Picture m = new Picture (last);
//            try
//            {
//                m.setURL (new URL ("http://localhost/image#" + mCounter++));
//            }
//            catch (java.net.MalformedURLException murle)
//            {
//                murle.printStackTrace ();
//            }
//            this.add (m);
//            repaint ();
//        }
//    }
//
//    //
//    // WindowListener interface
//    //
//    public void windowOpened (java.awt.event.WindowEvent e) {}
//    public void windowClosing (java.awt.event.WindowEvent e)
//    {
//        System.exit (0);
//    }
//    public void windowClosed (java.awt.event.WindowEvent e) {}
//    public void windowIconified (java.awt.event.WindowEvent e) {}
//    public void windowDeiconified (java.awt.event.WindowEvent e) {}
//    public void windowActivated (java.awt.event.WindowEvent e) {}
//    public void windowDeactivated (java.awt.event.WindowEvent e) {}
//
//    //
//  // ActionListener interface
//  //
//  public void actionPerformed (java.awt.event.ActionEvent event)
//  {
//      Object object = event.getSource();
//        if (object == repeat)
//            add ();
//        else if (object == clear)
//        {
//            mRegions = new Vector ();
//            repaint ();
//        }
//    }
//
//    //
//    // MouseListener Interface
//    //
//
//    public void mouseClicked (java.awt.event.MouseEvent event)
//    {
//        if (mVerbose)
//            System.out.println ("DrawTarget.mouseClicked " + event);
//    }
//
//  public void mouseReleased (java.awt.event.MouseEvent event)
//    {
//        if (mVerbose)
//            System.out.println ("DrawTarget.mouseReleased " + event);
//        if (null != origin)
//        {
//            last = new Rectangle (
//                Math.min (origin.x, event.getX ()),
//                Math.min (origin.y, event.getY ()),
//                Math.abs (event.getX () - origin.x),
//                Math.abs (event.getY () - origin.y));
//            add ();
//            origin = null;
//        }
//    }
//
//  public void mouseEntered (java.awt.event.MouseEvent event)
//    {
//        if (mVerbose)
//            System.out.println ("DrawTarget.mouseEntered " + event);
//    }
//
//  public void mouseExited (java.awt.event.MouseEvent event)
//    {
//        if (mVerbose)
//            System.out.println ("DrawTarget.mouseExited " + event);
//    }
//
//    public void mousePressed (java.awt.event.MouseEvent event)
//    {
//        if (mVerbose)
//            System.out.println ("DrawTarget.mousePressed " + event);
//        if (event.isMetaDown ())
//        {
//            status.setText (getDetails (event.getX (), event.getY ()));
//        }
//        else
//            origin = new java.awt.Point (event.getX (), event.getY ());
//    }
//
//    public void update (java.awt.Graphics graphics)
//    {
//        paint (graphics);
//    }
//
//    static final java.awt.Color[] mColours =
//    {
//        java.awt.Color.blue,
//        java.awt.Color.cyan,
//        java.awt.Color.gray,
//        java.awt.Color.green,
//        java.awt.Color.orange,
//        java.awt.Color.pink,
//        java.awt.Color.red,
//        java.awt.Color.yellow,
//        java.awt.Color.lightGray,
//        java.awt.Color.darkGray,
//    };
//
//    public void paint (java.awt.Graphics graphics)
//    {
//        java.awt.Dimension size = getSize ();
//        graphics.setColor (getBackground ());
//        graphics.fillRect (0, 0, size.width + 1, size.height + 1);
//
//        if (0 == mRegions.size ())
//        {
//            graphics.setColor (getForeground ());
//            graphics.drawString (
//                "Click and drag to create a picture.", 10, 20);
//            graphics.drawString (
//                "Right click a picture for details.", 10, 40);
//        }
//        else
//        {
//            Enumeration e = getPictures ();
//            while (e.hasMoreElements ())
//            {
//                Picture m = (Picture)e.nextElement ();
//                String url = m.getURL ().toExternalForm ();
//                int n = url.indexOf ('#');
//                n = Integer.parseInt (url.substring (n + 1))
//                java.awt.Color colour = mColours[n % mColours.length];
//                graphics.setColor (colour);
//                graphics.fillRect (m.x, m.y, m.width + 1, m.height + 1);
//                graphics.setColor (java.awt.Color.black);
//                graphics.drawRect (m.x, m.y, m.width, m.height);
//            }
//            checkOverlap (graphics);
//        }
//    }
//
//    void checkOverlap (java.awt.Graphics graphics)
//    {
//        Picture m;
//        Picture _m;
//        Rectangle r;
//
//        graphics.setColor (java.awt.Color.magenta);
//        for (int i = 0; i < mRegions.size (); i++)
//        {
//            m = (Picture)mRegions.elementAt (i);
//            for (int j = i + 1; j < mRegions.size (); j++)
//            {
//                _m = (Picture)mRegions.elementAt (j);
//                if (m.intersects (_m))
//                {
//                    r = m.intersection (_m);
//                    System.out.println (
//                        "overlap ("
//                        + r.x
//                        + ","
//                        + r.y
//                        + ") ("
//                        + (r.x + r.width)
//                        + ","
//                        + (r.y + r.height)
//                        + ")");
//                    graphics.fillRect (r.x, r.y, r.width + 1, r.height + 1);
//                }
//            }
//        }
//    }
//
//    String getDetails (int x, int y)
//    {
//        Picture m;
//        String ret;
//
//        ret = null;
//
//        // find the Picture
//        for (int i = 0; (null == ret) && (i < mRegions.size ()); i++)
//        {
//            m = (Picture)mRegions.elementAt (i);
//            if (m.contains (x, y))
//                ret = m.toString ();
//        }
//        if (null == ret)
//            ret = "";
//
//        return (ret);
//    }
//
//    public static void main (String[] args)
//    {
//        java.awt.Frame frame;
//
//        frame = new java.awt.Frame ();
//        frame.setSize (400,400);
//      menuMain = new java.awt.MenuBar();
//      Options = new java.awt.Menu ("Options");
//        repeat = new java.awt.MenuItem("Repeat");
//      Options.add (repeat);
//      clear = new java.awt.MenuItem("Clear");
//      Options.add (clear);
//
//      menuMain.add (Options);
//      frame.setMenuBar (menuMain);
//
//        java.awt.Insets insets = frame.getInsets ();
//
//        TileSet buffy = new TileSet ();
//        buffy.setLocation (insets.left + 10, insets.top + 10);
//        buffy.setBackground (java.awt.Color.lightGray.brighter ());
//        buffy.setVisible (true);
//
//        frame.add (buffy, "Center");
//        status = new java.awt.TextField ();
//        frame.add (status, "South");
//
//        frame.addWindowListener (buffy);
//        buffy.addMouseListener (buffy);
//      repeat.addActionListener (buffy);
//      clear.addActionListener (buffy);
//
//        frame.setVisible (true);
//
//    }
}

/*
 * Revision Control Modification History
 *
 * $Log: TileSet.java,v $
 * Revision 1.2  2004/07/31 16:42:30  derrickoswald
 * Remove unused variables and other fixes exposed by turning on compiler warnings.
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
