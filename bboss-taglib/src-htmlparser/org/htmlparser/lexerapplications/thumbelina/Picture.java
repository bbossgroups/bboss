// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexerapplications/thumbelina/Picture.java,v $
// $Author: derrickoswald $
// $Date: 2003/12/16 02:29:56 $
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

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;

/**
 * Class to track pictures within the frame.
 * Maintains an image, an area and the URL for it.
 */
public class Picture extends Rectangle
{
    /**
     * The origin for new points from the zero args constructor.
     */
    public static final Point ORIGIN = new Point (0, 0);

    /**
     * The URL for the picture.
     */
    protected URL mURL;

    /**
     * The image for the picture.
     */
    protected Image mImage;

    /**
     * The upper left hand corner of the image.
     * This doesn't change, even if the image is cropped.
     * For example, if the left half of the image is obscured by another,
     * the <code>Rectangle</code> fields <code>x</code>, <code>y</code>,
     * <code>width</code> and <code>height</code> will change, but the
     * origin remains the same.
     */
    protected Point mOrigin;

    /**
     * Construct a Picture.
     */
    public Picture ()
    {
        setURL (null);
        setImage (null);
        setOrigin (ORIGIN);
    }

    /**
     * Construct a Picture over the area given.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param width The width of the picture.
     * @param height The height of the picture.
     */
    public Picture (final int x, final int y, final int width, final int height)
    {
        super (x, y, width, height);
        setURL (null);
        setImage (null);
        setOrigin (new Point (x, y));
    }

    /**
     * Construct a picture over the rectangle given.
     * @param r The coordinates of the area.
     */
    public Picture (final Rectangle r)
    {
        super (r);
        setURL (null);
        setImage (null);
        setOrigin (new Point (r.x, r.y));
    }

    /**
     * Construct a picture from the one given.
     * @param picture The picture to copy.
     */
    public Picture (final Picture picture)
    {
        super (picture);
        setURL (picture.getURL ());
        setImage (picture.getImage ());
        setOrigin (picture.getOrigin ());
    }

    /**
     * Getter for property URL.
     * @return Value of property URL.
     */
    public URL getURL ()
    {
        return (mURL);
    }

    /**
     * Setter for property URL.
     * @param url New value of property URL.
     */
    public void setURL (final URL url)
    {
        mURL = url;
    }

    /** Getter for property image.
     * @return Value of property image.
     */
    public Image getImage ()
    {
        return (mImage);
    }

    /** Setter for property image.
     * @param image New value of property image.
     */
    public void setImage (final Image image)
    {
        mImage = image;
        if (null != image)
        {
            width = image.getWidth (null);
            height = image.getHeight (null);
        }
    }

    /** Getter for property origin.
     * @return Value of property origin.
     */
    public Point getOrigin ()
    {
        return (mOrigin);
    }

    /** Setter for property origin.
     * @param origin New value of property origin.
     */
    public void setOrigin (final Point origin)
    {
        mOrigin = origin;
    }

    /**
     * Return <code>true</code> if that picture is the same as this one.
     * @param picture The picture to check.
     * @return <code>true</code> if the images match.
     */
    public boolean same (final Picture picture)
    {
        return (mImage == picture.mImage);
    }

    /**
     * Reset the picture to uncropped size.
     */
    public void reset ()
    {
        setBounds (mOrigin.x, mOrigin.y,
            mImage.getWidth (null), mImage.getHeight (null));
    }

    /**
     * Create a string representation of the picture.
     * @return A string that shows this picture URL and size.
     */
    public String toString ()
    {
        StringBuffer ret;

        ret = new StringBuffer ();
        ret.append (getURL ().toString ());
        ret.append ("[x=");
        ret.append (Integer.toString (x));
        ret.append (",y=");
        ret.append (Integer.toString (y));
        ret.append (",width=");
        ret.append (Integer.toString (width));
        ret.append (",height=");
        ret.append (Integer.toString (height));
        ret.append ("]");

        return (ret.toString ());
    }
}

/*
 * Revision Control Modification History
 *
 * $Log: Picture.java,v $
 * Revision 1.2  2003/12/16 02:29:56  derrickoswald
 * Javadoc changes and additions. Stylesheet, overview, build instructions and todo list.
 * Added HTMLTaglet, an inline Javadoc taglet for embedding HTML into javadocs.
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
