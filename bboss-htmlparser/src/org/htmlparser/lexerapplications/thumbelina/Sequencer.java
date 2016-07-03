// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexerapplications/thumbelina/Sequencer.java,v $
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JViewport;


/**
 * Display received images at a constant rate.
 */
public class Sequencer
    extends
        Thread
{
    /**
     * The default delay time, {@value} milliseconds.
     */
    protected static final int DEFAULT_DELAY = 500;

    /**
     * The thumbelina object to drive.
     */
    protected Thumbelina mThumbelina;

    /**
     * Pictures awaiting display.
     */
    protected ArrayList mPending;

    /**
     * Activity state.
     * <code>true</code> means fetching and displaying, <code>false</code> not.
     */
    protected boolean mActive;

    /**
     * Delay between picture displays.
     */
    protected int mDelay;

    /**
     * Random number generator for picture placement.
     */
    protected Random mRandom;

    /**
     * Creates a new instance of a Sequencer.
     * @param thumbelina The object to push images to.
     */
    public Sequencer (final Thumbelina thumbelina)
    {
        mThumbelina = thumbelina;
        mPending = new ArrayList ();
        mActive = true;
        setDelay (DEFAULT_DELAY);
        mRandom = new Random ();
        setName ("Sequencer"); // only good if there's just one of these
        start ();
    }

    /**
     * Clears the pending images list.
     */
    public void reset ()
    {
        synchronized (mPending)
        {
            mPending.clear ();
            mThumbelina.mReadyProgress.setValue (0);
            mPending.notify ();
        }
    }

    /**
     * Compute a random point to load the image.
     * Generate a random point for one of the corners of the image and
     * then condition the numbers so the image is on screen.
     * @param url The url this picture was fetched from.
     * Used in computing the random position, so the picture is always
     * placed in the same location, even when refetched.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return The random point to use.
     */
    protected Point random (final String url, final int width, final int height)
    {
        Component parent;
        Component grandparent;
        Dimension dim;
        Insets insets;
        int minx;
        int miny;
        int maxx;
        int maxy;
        int rndx;
        int rndy;
        int corner;
        Point ret;

        parent = mThumbelina.getPicturePanel ().getParent ();
        if (parent instanceof JViewport)
        {
            grandparent = parent.getParent (); // JScrollPane
            dim = grandparent.getSize ();
        }
        else
            dim = mThumbelina.getPicturePanel ().getSize ();
        insets = mThumbelina.getPicturePanel ().getInsets ();
        dim.width -= (insets.left + insets.right);
        dim.height -= (insets.top + insets.bottom);
        minx = insets.left;
        miny = insets.top;
        maxx = minx + dim.width;
        maxy = miny + dim.height;
        mRandom.setSeed ((((long)(width + height)) << 32) + url.hashCode ());
        rndx = (int)(mRandom.nextDouble () * dim.width);
        rndy = (int)(mRandom.nextDouble () * dim.height);
        corner = (int)(mRandom.nextDouble () * 4); // the panel has four corners
        ret = new Point (0, 0);
        switch (corner)
        {
            case 0: // upper left
                if (rndx + width >= maxx)
                    ret.x = maxx - width;
                else
                    ret.x = rndx;
                if (rndy + height >= maxy)
                    ret.y = maxy - height;
                else
                    ret.y = rndy;
                break;
            case 1: // upper right
                if (rndx - width < minx)
                    ret.x = minx;
                else
                    ret.x = rndx - width;
                if (rndy + height >= maxy)
                    ret.y = maxy - height;
                else
                    ret.y = rndy;
                break;
            case 2: // lower right
                if (rndx - width < minx)
                    ret.x = minx;
                else
                    ret.x = rndx - width;
                if (rndy - height < miny)
                    ret.y = miny;
                else
                    ret.y = rndy - height;
                break;
            case 3: // lower left
                if (rndx + width >= maxx)
                    ret.x = maxx - width;
                else
                    ret.x = rndx;
                if (rndy - height < miny)
                    ret.y = miny;
                else
                    ret.y = rndy - height;
                break;
            default:
                throw new IllegalStateException ("random corner = " + corner);
        }

        // if it's really large stuff it in the upper left hand corner
        if (ret.x < 0)
            ret.x = 0;
        if (ret.y < 0)
            ret.y = 0;


        return (ret);
    }

    /**
     * Add an image to the pending list.
     * @param image The image to add.
     * @param url The url the image came from.
     */
    public void add (final Image image, final URL url)
    {
        add (image, url, true);
    }

    /**
     * Add an image to the panel.
     * @param image The image to add.
     * @param url The url the image came from.
     * @param background If <code>true</code>, just add to pending list.
     */
    public void add (final Image image, final URL url, final boolean background)
    {
        Picture picture;
        int size;

        picture = new Picture ();
        picture.setImage (image);
        picture.setURL (url);
        if (background)
            synchronized (mPending)
            {
                mPending.add (picture);
                size = mPending.size ();
                if (mThumbelina.mReadyProgress.getMaximum () < size)
                    mThumbelina.mReadyProgress.setMaximum (size);
                mThumbelina.mReadyProgress.setValue (size);
                mPending.notify ();
            }
        else
            place (picture, false);
    }

    /**
     * Place a picture in the display area.
     * Places the picture at a random location on screen.
     * @param picture The picture to place on screen.
     * @param add If <code>true</code>, the picture is added to the history.
     */
    protected void place (final Picture picture, final boolean add)
    {
        Point p;

        if (Picture.ORIGIN == picture.getOrigin ())
        {
            // never been placed before
            p = random (
                picture.getURL ().toExternalForm (),
                picture.width,
                picture.height);
            picture.x = p.x;
            picture.y = p.y;
            picture.setOrigin (p);
        }
        mThumbelina.getPicturePanel ().draw (picture, add);
    }

    //
    // Runnable interface
    //

    /**
     * Display pictures from pending list with delay between.
     * If the list is empty it waits on the pending list for new pictures.
     */
    public void run ()
    {
        Picture picture;
        int size;

        while (true)
        {
            try
            {
                picture = null;
                synchronized (mPending)
                {
                    if (mActive && !mPending.isEmpty ())
                        picture = (Picture)mPending.remove (0);
                    else
                        try
                        {
                            mPending.wait ();
                        }
                        catch (InterruptedException ie)
                        {
                            ie.printStackTrace ();
                        }
                    size = mPending.size ();
                    if (mThumbelina.mReadyProgress.getMaximum () < size)
                        mThumbelina.mReadyProgress.setMaximum (size);
                    mThumbelina.mReadyProgress.setValue (size);
                }
                if (null != picture)
                {
                    place (picture, true);
                    if (0 != getDelay ())
                        try
                        {
                            sleep (getDelay ());
                        }
                        catch (InterruptedException ie)
                        {
                            ie.printStackTrace ();
                        }
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace ();
            }
        }
    }

    /**
     * Getter for property delay.
     * @return Value of property delay.
     */
    public int getDelay ()
    {
        return (mDelay);
    }

    /**
     * Setter for property delay.
     * @param delay New value of property delay.
     */
    public void setDelay (final int delay)
    {
        mDelay = delay;
    }

}

/*
 * Revision Control Modification History
 *
 * $Log: Sequencer.java,v $
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
