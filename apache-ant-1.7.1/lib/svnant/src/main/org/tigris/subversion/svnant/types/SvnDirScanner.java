/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 
package org.tigris.subversion.svnant.types;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileUtils;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;

/**
 * This class supports a custom FileSet for ANT. It returns a set of files
 * based on the result of Subversion status command, as opposed to the list
 * of files actually on the file system. Two main differences:
 * <ol>
 * 	<li>Missing and deleted files are provided</li>
 *  <li>Internal subversion file (.svn) are skipped</li>
 * </ol>
 * 
 * This is a copy of the class org.apache.tools.ant.DirectoryScanner,
 * obtained from ANT. This is to support the same level of functionality
 * with regards to includes, excludes, etc. of the regular file set. The changes
 * from the original code are as follows:
 * 1. Modify constructor to accept a ISVNClientAdapter
 * 2. In scandir(), call method list() instead of File.list()
 * 3. In method list(), use ISVNClientAdapter.getStatus to obtain list of files
 * 4. Replace where java.io.File is used for logic based on ISVNStatus 
 * 
 * @author Jean-Pierre Fiset <a href="mailto:jp@fiset.ca">jp@fiset.ca</a>
 */
public class SvnDirScanner extends DirectoryScanner {

    private ISVNClientAdapter svnClient;

    /**
     * Sole constructor.
     * @param clientAdapter
     */
    public SvnDirScanner( ISVNClientAdapter clientAdapter ) {
    	svnClient = clientAdapter;
    }
    
    /**
     * Return a cached result of list performed on file, if
     * available.  Invokes the method and caches the result otherwise.
     *
     * @param file File (dir) to list.
     * @since Ant 1.6
     */
    private ISVNStatus[] list(File file) {
    	ISVNStatus[] files = (ISVNStatus[]) fileListMap.get(file);
        if (files == null) {
            // Obtain the entries from the client adapter
           	try {
           		files = svnClient.getStatus(file,false,true); // descend=false, getAll=true
    		} catch (SVNClientException e) {
    			throw new RuntimeException("Error scanning: " + e, e);
    		}
    		
    		// Count children entries
    		int count = 0;
    		for(int loop=0; loop<files.length; ++loop) {
    			if( false == files[loop].getFile().equals( file ) ) {
    				++count;
    			}
    		}
    		
    		// Copy children entries
    		ISVNStatus[] copy = new ISVNStatus[count];
    		int index = 0;
    		for(int loop=0; loop<files.length; ++loop) {
    			if( false == files[loop].getFile().equals( file ) ) {
    				copy[index] = files[loop];
    				++index;
    			}
    		}
    		files = copy;

    		// Cache
            fileListMap.put(file, files);
        }
        return files;
    }
	
	
    /** Is OpenVMS the operating system we're running on? */
    private static final boolean ON_VMS = Os.isFamily("openvms");

    /**
     * Patterns which should be excluded by default.
     *
     * <p>Note that you can now add patterns to the list of default
     * excludes.  Added patterns will not become part of this array
     * that has only been kept around for backwards compatibility
     * reasons.</p>
     *
     * @deprecated use the {@link #getDefaultExcludes
     * getDefaultExcludes} method instead.
     */
    protected static final String[] DEFAULTEXCLUDES = {
        // Miscellaneous typical temporary files
        "**/*~",
        "**/#*#",
        "**/.#*",
        "**/%*%",
        "**/._*",

        // CVS
        "**/CVS",
        "**/CVS/**",
        "**/.cvsignore",

        // SCCS
        "**/SCCS",
        "**/SCCS/**",

        // Visual SourceSafe
        "**/vssver.scc",

        // Subversion
        "**/.svn",
        "**/.svn/**",

        // Mac
        "**/.DS_Store"
    };

    /**
     * Patterns which should be excluded by default.
     *
     * @see #addDefaultExcludes()
     */
    private static Vector defaultExcludes = new Vector();
    static {
        resetDefaultExcludes();
    }

    /** The base directory to be scanned. */
    protected File basedir;

    /** The patterns for the files to be included. */
    protected String[] includes;

    /** The patterns for the files to be excluded. */
    protected String[] excludes;

    /** Selectors that will filter which files are in our candidate list. */
    protected FileSelector[] selectors = null;

    /** The files which matched at least one include and no excludes
     *  and were selected.
     */
    protected Vector filesIncluded;

    /** The files which did not match any includes or selectors. */
    protected Vector filesNotIncluded;

    /**
     * The files which matched at least one include and at least
     * one exclude.
     */
    protected Vector filesExcluded;

    /** The directories which matched at least one include and no excludes
     *  and were selected.
     */
    protected Vector dirsIncluded;

    /** The directories which were found and did not match any includes. */
    protected Vector dirsNotIncluded;

    /**
     * The directories which matched at least one include and at least one
     * exclude.
     */
    protected Vector dirsExcluded;

    /** The files which matched at least one include and no excludes and
     *  which a selector discarded.
     */
    protected Vector filesDeselected;

    /** The directories which matched at least one include and no excludes
     *  but which a selector discarded.
     */
    protected Vector dirsDeselected;

    /** Whether or not our results were built by a slow scan. */
    protected boolean haveSlowResults = false;

    /**
     * Whether or not the file system should be treated as a case sensitive
     * one.
     */
    protected boolean isCaseSensitive = true;

    /**
     * Whether or not symbolic links should be followed.
     *
     * @since Ant 1.5
     */
    private boolean followSymlinks = true;

    /** Helper. */
    private static final FileUtils fileUtils = FileUtils.newFileUtils();

    /** Whether or not everything tested so far has been included. */
    protected boolean everythingIncluded = true;

    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     *
     * @return whether or not a given path matches the start of a given
     * pattern up to the first "**".
     */
    protected static boolean matchPatternStart(String pattern, String str) {
        return SelectorUtils.matchPatternStart(pattern, str);
    }
    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     * @return whether or not a given path matches the start of a given
     * pattern up to the first "**".
     */
    protected static boolean matchPatternStart(String pattern, String str,
                                               boolean isCaseSensitive) {
        return SelectorUtils.matchPatternStart(pattern, str, isCaseSensitive);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     *
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    protected static boolean matchPath(String pattern, String str) {
        return SelectorUtils.matchPath(pattern, str);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    protected static boolean matchPath(String pattern, String str,
                                       boolean isCaseSensitive) {
        return SelectorUtils.matchPath(pattern, str, isCaseSensitive);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern The pattern to match against.
     *                Must not be <code>null</code>.
     * @param str     The string which must be matched against the pattern.
     *                Must not be <code>null</code>.
     *
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    public static boolean match(String pattern, String str) {
        return SelectorUtils.match(pattern, str);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern The pattern to match against.
     *                Must not be <code>null</code>.
     * @param str     The string which must be matched against the pattern.
     *                Must not be <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     *
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    protected static boolean match(String pattern, String str,
                                   boolean isCaseSensitive) {
        return SelectorUtils.match(pattern, str, isCaseSensitive);
    }


    /**
     * Get the list of patterns that should be excluded by default.
     *
     * @return An array of <code>String</code> based on the current
     *         contents of the <code>defaultExcludes</code>
     *         <code>Vector</code>.
     *
     * @since Ant 1.6
     */
    public static String[] getDefaultExcludes() {
        return (String[]) defaultExcludes.toArray(new String[defaultExcludes
                                                             .size()]);
    }

    /**
     * Add a pattern to the default excludes unless it is already a
     * default exclude.
     *
     * @param s   A string to add as an exclude pattern.
     * @return    <code>true</code> if the string was added
     *            <code>false</code> if it already
     *            existed.
     *
     * @since Ant 1.6
     */
    public static boolean addDefaultExclude(String s) {
        if (defaultExcludes.indexOf(s) == -1) {
            defaultExcludes.add(s);
            return true;
        }
        return false;
    }

    /**
     * Remove a string if it is a default exclude.
     *
     * @param s   The string to attempt to remove.
     * @return    <code>true</code> if <code>s</code> was a default
     *            exclude (and thus was removed),
     *            <code>false</code> if <code>s</code> was not
     *            in the default excludes list to begin with
     *
     * @since Ant 1.6
     */
    public static boolean removeDefaultExclude(String s) {
        return defaultExcludes.remove(s);
    }

    /**
     *  Go back to the hard wired default exclude patterns
     *
     * @since Ant 1.6
     */
    public static void resetDefaultExcludes() {
    defaultExcludes = new Vector();

        for (int i = 0; i < DEFAULTEXCLUDES.length; i++) {
            defaultExcludes.add(DEFAULTEXCLUDES[i]);
        }
    }

    /**
     * Sets the base directory to be scanned. This is the directory which is
     * scanned recursively. All '/' and '\' characters are replaced by
     * <code>File.separatorChar</code>, so the separator used need not match
     * <code>File.separatorChar</code>.
     *
     * @param basedir The base directory to scan.
     *                Must not be <code>null</code>.
     */
    public void setBasedir(String basedir) {
        setBasedir(new File(basedir.replace('/', File.separatorChar).replace(
                '\\', File.separatorChar)));
    }

    /**
     * Sets the base directory to be scanned. This is the directory which is
     * scanned recursively.
     *
     * @param basedir The base directory for scanning.
     *                Should not be <code>null</code>.
     */
    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    /**
     * Returns the base directory to be scanned.
     * This is the directory which is scanned recursively.
     *
     * @return the base directory to be scanned
     */
    public File getBasedir() {
        return basedir;
    }

    /**
     * Find out whether include exclude patterns are matched in a
     * case sensitive way
     * @return whether or not the scanning is case sensitive
     * @since ant 1.6
     */
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }
    /**
     * Sets whether or not include and exclude patterns are matched
     * in a case sensitive way
     *
     * @param isCaseSensitive whether or not the file system should be
     *                        regarded as a case sensitive one
     */
    public void setCaseSensitive(boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }

    /**
     * gets whether or not a DirectoryScanner follows symbolic links
     *
     * @return flag indicating whether symbolic links should be followed
     *
     * @since ant 1.6
     */
    public boolean isFollowSymlinks() {
        return followSymlinks;
    }

    /**
     * Sets whether or not symbolic links should be followed.
     *
     * @param followSymlinks whether or not symbolic links should be followed
     */
    public void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }

    /**
     * Sets the list of include patterns to use. All '/' and '\' characters
     * are replaced by <code>File.separatorChar</code>, so the separator used
     * need not match <code>File.separatorChar</code>.
     * <p>
     * When a pattern ends with a '/' or '\', "**" is appended.
     *
     * @param includes A list of include patterns.
     *                 May be <code>null</code>, indicating that all files
     *                 should be included. If a non-<code>null</code>
     *                 list is given, all elements must be
     * non-<code>null</code>.
     */
    public void setIncludes(String[] includes) {
        if (includes == null) {
            this.includes = null;
        } else {
            this.includes = new String[includes.length];
            for (int i = 0; i < includes.length; i++) {
                String pattern;
                pattern = includes[i].replace('/', File.separatorChar).replace(
                        '\\', File.separatorChar);
                if (pattern.endsWith(File.separator)) {
                    pattern += "**";
                }
                this.includes[i] = pattern;
            }
        }
    }


    /**
     * Sets the list of exclude patterns to use. All '/' and '\' characters
     * are replaced by <code>File.separatorChar</code>, so the separator used
     * need not match <code>File.separatorChar</code>.
     * <p>
     * When a pattern ends with a '/' or '\', "**" is appended.
     *
     * @param excludes A list of exclude patterns.
     *                 May be <code>null</code>, indicating that no files
     *                 should be excluded. If a non-<code>null</code> list is
     *                 given, all elements must be non-<code>null</code>.
     */
    public void setExcludes(String[] excludes) {
        if (excludes == null) {
            this.excludes = null;
        } else {
            this.excludes = new String[excludes.length];
            for (int i = 0; i < excludes.length; i++) {
                String pattern;
                pattern = excludes[i].replace('/', File.separatorChar).replace(
                        '\\', File.separatorChar);
                if (pattern.endsWith(File.separator)) {
                    pattern += "**";
                }
                this.excludes[i] = pattern;
            }
        }
    }


    /**
     * Sets the selectors that will select the filelist.
     *
     * @param selectors specifies the selectors to be invoked on a scan
     */
    public void setSelectors(FileSelector[] selectors) {
        this.selectors = selectors;
    }


    /**
     * Returns whether or not the scanner has included all the files or
     * directories it has come across so far.
     *
     * @return <code>true</code> if all files and directories which have
     *         been found so far have been included.
     */
    public boolean isEverythingIncluded() {
        return everythingIncluded;
    }

    /**
     * Scans the base directory for files which match at least one include
     * pattern and don't match any exclude patterns. If there are selectors
     * then the files must pass muster there, as well.
     *
     * @exception IllegalStateException if the base directory was set
     *            incorrectly (i.e. if it is <code>null</code>, doesn't exist,
     *            or isn't a directory).
     */
    public void scan() throws IllegalStateException {
        if (basedir == null) {
            throw new IllegalStateException("No basedir set");
        }
        if (!basedir.exists()) {
            throw new IllegalStateException("basedir " + basedir
                                            + " does not exist");
        }
        if (!basedir.isDirectory()) {
            throw new IllegalStateException("basedir " + basedir
                                            + " is not a directory");
        }

        if (includes == null) {
            // No includes supplied, so set it to 'matches all'
            includes = new String[1];
            includes[0] = "**";
        }
        if (excludes == null) {
            excludes = new String[0];
        }

        filesIncluded    = new Vector();
        filesNotIncluded = new Vector();
        filesExcluded    = new Vector();
        filesDeselected  = new Vector();
        dirsIncluded     = new Vector();
        dirsNotIncluded  = new Vector();
        dirsExcluded     = new Vector();
        dirsDeselected   = new Vector();

        if (isIncluded("")) {
            if (!isExcluded("")) {
                if (isSelected("", basedir)) {
                    dirsIncluded.addElement("");
                } else {
                    dirsDeselected.addElement("");
                }
            } else {
                dirsExcluded.addElement("");
            }
        } else {
            dirsNotIncluded.addElement("");
        }
        checkIncludePatterns();
        clearCaches();
    }

    /**
     * this routine is actually checking all the include patterns in
     * order to avoid scanning everything under base dir
     * @since ant1.6
     */
    private void checkIncludePatterns() {
        Hashtable newroots = new Hashtable();
        // put in the newroots vector the include patterns without
        // wildcard tokens
        for (int icounter = 0; icounter < includes.length; icounter++) {
            String newpattern =
                SelectorUtils.rtrimWildcardTokens(includes[icounter]);
            newroots.put(newpattern, includes[icounter]);
        }

        if (newroots.containsKey("")) {
            // we are going to scan everything anyway
            scandir(basedir, "", true);
        } else {
            // only scan directories that can include matched files or
            // directories
            Enumeration enum2 = newroots.keys();

            File canonBase = null;
            try {
                canonBase = basedir.getCanonicalFile();
            } catch (IOException ex) {
                throw new BuildException(ex);
            }

            while (enum2.hasMoreElements()) {
                String currentelement = (String) enum2.nextElement();
                String originalpattern = (String) newroots.get(currentelement);
                File myfile = new File(basedir, currentelement);

                if (myfile.exists()) {
                    // may be on a case insensitive file system.  We want
                    // the results to show what's really on the disk, so
                    // we need to double check.
                    try {
                        File canonFile = myfile.getCanonicalFile();
                        String path = fileUtils.removeLeadingPath(canonBase,
                                                                  canonFile);
                        if (!path.equals(currentelement) || ON_VMS) {
                            myfile = findFile(basedir, currentelement);
                            if (myfile != null) {
                                currentelement =
                                    fileUtils.removeLeadingPath(basedir,
                                                                myfile);
                            }
                        }
                    } catch (IOException ex) {
                        throw new BuildException(ex);
                    }
                }

                if ((myfile == null || !myfile.exists()) && !isCaseSensitive) {
                    File f = findFileCaseInsensitive(basedir, currentelement);
                    if (f.exists()) {
                        // adapt currentelement to the case we've
                        // actually found
                        currentelement = fileUtils.removeLeadingPath(basedir,
                                                                     f);
                        myfile = f;
                    }
                }

                if (myfile != null && myfile.exists()) {
                    if (!followSymlinks
                        && isSymlink(basedir, currentelement)) {
                        continue;
                    }

                    if (myfile.isDirectory()) {
                        if (isIncluded(currentelement)
                            && currentelement.length() > 0) {
                            accountForIncludedDir(currentelement, myfile, true);
                        }  else {
                            if (currentelement.length() > 0) {
                                if (currentelement.charAt(currentelement
                                                          .length() - 1)
                                    != File.separatorChar) {
                                    currentelement =
                                        currentelement + File.separatorChar;
                                }
                            }
                            scandir(myfile, currentelement, true);
                        }
                    } else {
                        if (isCaseSensitive
                            && originalpattern.equals(currentelement)) {
                            accountForIncludedFile(currentelement, myfile);
                        } else if (!isCaseSensitive
                                   && originalpattern
                                   .equalsIgnoreCase(currentelement)) {
                            accountForIncludedFile(currentelement, myfile);
                        }
                    }
                }
            }
        }
    }

    /**
     * Top level invocation for a slow scan. A slow scan builds up a full
     * list of excluded/included files/directories, whereas a fast scan
     * will only have full results for included files, as it ignores
     * directories which can't possibly hold any included files/directories.
     * <p>
     * Returns immediately if a slow scan has already been completed.
     */
    protected void slowScan() {
        if (haveSlowResults) {
            return;
        }

        String[] excl = new String[dirsExcluded.size()];
        dirsExcluded.copyInto(excl);

        String[] notIncl = new String[dirsNotIncluded.size()];
        dirsNotIncluded.copyInto(notIncl);

        for (int i = 0; i < excl.length; i++) {
            if (!couldHoldIncluded(excl[i])) {
                scandir(new File(basedir, excl[i]),
                        excl[i] + File.separator, false);
            }
        }

        for (int i = 0; i < notIncl.length; i++) {
            if (!couldHoldIncluded(notIncl[i])) {
                scandir(new File(basedir, notIncl[i]),
                        notIncl[i] + File.separator, false);
            }
        }

        haveSlowResults  = true;
    }

    /**
     * Scans the given directory for files and directories. Found files and
     * directories are placed in their respective collections, based on the
     * matching of includes, excludes, and the selectors.  When a directory
     * is found, it is scanned recursively.
     *
     * @param dir   The directory to scan. Must not be <code>null</code>.
     * @param vpath The path relative to the base directory (needed to
     *              prevent problems with an absolute path when using
     *              dir). Must not be <code>null</code>.
     * @param fast  Whether or not this call is part of a fast scan.
     *
     * @see #filesIncluded
     * @see #filesNotIncluded
     * @see #filesExcluded
     * @see #dirsIncluded
     * @see #dirsNotIncluded
     * @see #dirsExcluded
     * @see #slowScan
     */
    protected void scandir(File dir, String vpath, boolean fast) {
        // avoid double scanning of directories, can only happen in fast mode
        if (fast && hasBeenScanned(vpath)) {
            return;
        }
        ISVNStatus[] newfiles = list(dir);

        if (newfiles == null) {
            /*
             * two reasons are mentioned in the API docs for File.list
             * (1) dir is not a directory. This is impossible as
             *     we wouldn't get here in this case.
             * (2) an IO error occurred (why doesn't it throw an exception
             *     then???)
             */
            throw new BuildException("IO error scanning directory "
                                     + dir.getAbsolutePath());
        }

        for (int i = 0; i < newfiles.length; i++) {
            String name = vpath + newfiles[i].getFile().getName();
            File file = new File(dir, newfiles[i].getFile().getName());
            if( SVNNodeKind.DIR == newfiles[i].getNodeKind() 
             || (SVNNodeKind.UNKNOWN == newfiles[i].getNodeKind() && newfiles[i].getFile().isDirectory()) ) {
                if (isIncluded(name)) {
                    accountForIncludedDir(name, file, fast);
                } else {
                    everythingIncluded = false;
                    dirsNotIncluded.addElement(name);
                    if (fast && couldHoldIncluded(name)) {
                        scandir(file, name + File.separator, fast);
                    }
                }
                if (!fast) {
                    scandir(file, name + File.separator, fast);
                }
            } else if( SVNNodeKind.FILE == newfiles[i].getNodeKind()  
                    || (SVNNodeKind.UNKNOWN == newfiles[i].getNodeKind() && newfiles[i].getFile().isFile()) ) {
                if (isIncluded(name)) {
                    accountForIncludedFile(name, file);
                } else {
                    everythingIncluded = false;
                    filesNotIncluded.addElement(name);
                }
            }
        }
    }
    /**
     * process included file
     * @param name  path of the file relative to the directory of the fileset
     * @param file  included file
     */
    private void accountForIncludedFile(String name, File file) {
        if (!filesIncluded.contains(name)
            && !filesExcluded.contains(name)
            && !filesDeselected.contains(name)) {

            if (!isExcluded(name)) {
                if (isSelected(name, file)) {
                    filesIncluded.addElement(name);
                } else {
                    everythingIncluded = false;
                    filesDeselected.addElement(name);
                }
            } else {
                everythingIncluded = false;
                filesExcluded.addElement(name);
            }
        }
    }

    /**
     *
     * @param name path of the directory relative to the directory of
     * the fileset
     * @param file directory as file
     * @param fast
     */
    private void accountForIncludedDir(String name, File file, boolean fast) {
        if (!dirsIncluded.contains(name)
            && !dirsExcluded.contains(name)
            && !dirsDeselected.contains(name)) {

            if (!isExcluded(name)) {
                if (isSelected(name, file)) {
                    dirsIncluded.addElement(name);
                    if (fast) {
                        scandir(file, name + File.separator, fast);
                    }
                } else {
                    everythingIncluded = false;
                    dirsDeselected.addElement(name);
                    if (fast && couldHoldIncluded(name)) {
                        scandir(file, name + File.separator, fast);
                    }
                }

            } else {
                everythingIncluded = false;
                dirsExcluded.addElement(name);
                if (fast && couldHoldIncluded(name)) {
                    scandir(file, name + File.separator, fast);
                }
            }
        }
    }
    /**
     * Tests whether or not a name matches against at least one include
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against at least one
     *         include pattern, or <code>false</code> otherwise.
     */
    protected boolean isIncluded(String name) {
        for (int i = 0; i < includes.length; i++) {
            if (matchPath(includes[i], name, isCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether or not a name matches the start of at least one include
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against the start of at
     *         least one include pattern, or <code>false</code> otherwise.
     */
    protected boolean couldHoldIncluded(String name) {
        for (int i = 0; i < includes.length; i++) {
            if (matchPatternStart(includes[i], name, isCaseSensitive)) {
                if (isMorePowerfulThanExcludes(name, includes[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  find out whether one particular include pattern is more powerful
     *  than all the excludes
     *  note : the power comparison is based on the length of the include pattern
     *  and of the exclude patterns without the wildcards
     *  ideally the comparison should be done based on the depth
     *  of the match, that is to say how many file separators have been matched
     *  before the first ** or the end of the pattern
     *
     *  IMPORTANT : this function should return false "with care"
     *
     *  @param name the relative path that one want to test
     *  @param includepattern  one include pattern
     *  @return true if there is no exclude pattern more powerful than this include pattern
     *  @since ant1.6
     */
    private boolean isMorePowerfulThanExcludes(String name, String includepattern) {
        String soughtexclude = name + File.separator + "**";
        for (int counter = 0; counter < excludes.length; counter++) {
            if (excludes[counter].equals(soughtexclude))  {
                return false;
            }
        }
        return true;
    }
    /**
     * Tests whether or not a name matches against at least one exclude
     * pattern.
     *
     * @param name The name to match. Must not be <code>null</code>.
     * @return <code>true</code> when the name matches against at least one
     *         exclude pattern, or <code>false</code> otherwise.
     */
    protected boolean isExcluded(String name) {
        for (int i = 0; i < excludes.length; i++) {
            if (matchPath(excludes[i], name, isCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether a name should be selected.
     *
     * @param name the filename to check for selecting
     * @param file the java.io.File object for this filename
     * @return <code>false</code> when the selectors says that the file
     *         should not be selected, <code>true</code> otherwise.
     */
    protected boolean isSelected(String name, File file) {
        if (selectors != null) {
            for (int i = 0; i < selectors.length; i++) {
                if (!selectors[i].isSelected(basedir, name, file)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the names of the files which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the files which matched at least one of the
     *         include patterns and none of the exclude patterns.
     */
    public String[] getIncludedFiles() {
        String[] files = new String[filesIncluded.size()];
        filesIncluded.copyInto(files);
        Arrays.sort(files);
        return files;
    }

    /**
     * Returns the names of the files which matched none of the include
     * patterns. The names are relative to the base directory. This involves
     * performing a slow scan if one has not already been completed.
     *
     * @return the names of the files which matched none of the include
     *         patterns.
     *
     * @see #slowScan
     */
    public String[] getNotIncludedFiles() {
        slowScan();
        String[] files = new String[filesNotIncluded.size()];
        filesNotIncluded.copyInto(files);
        return files;
    }

    /**
     * Returns the names of the files which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     * The names are relative to the base directory. This involves
     * performing a slow scan if one has not already been completed.
     *
     * @return the names of the files which matched at least one of the
     *         include patterns and at least one of the exclude patterns.
     *
     * @see #slowScan
     */
    public String[] getExcludedFiles() {
        slowScan();
        String[] files = new String[filesExcluded.size()];
        filesExcluded.copyInto(files);
        return files;
    }

    /**
     * <p>Returns the names of the files which were selected out and
     * therefore not ultimately included.</p>
     *
     * <p>The names are relative to the base directory. This involves
     * performing a slow scan if one has not already been completed.</p>
     *
     * @return the names of the files which were deselected.
     *
     * @see #slowScan
     */
    public String[] getDeselectedFiles() {
        slowScan();
        String[] files = new String[filesDeselected.size()];
        filesDeselected.copyInto(files);
        return files;
    }

    /**
     * Returns the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     */
    public String[] getIncludedDirectories() {
        String[] directories = new String[dirsIncluded.size()];
        dirsIncluded.copyInto(directories);
        Arrays.sort(directories);
        return directories;
    }

    /**
     * Returns the names of the directories which matched none of the include
     * patterns. The names are relative to the base directory. This involves
     * performing a slow scan if one has not already been completed.
     *
     * @return the names of the directories which matched none of the include
     * patterns.
     *
     * @see #slowScan
     */
    public String[] getNotIncludedDirectories() {
        slowScan();
        String[] directories = new String[dirsNotIncluded.size()];
        dirsNotIncluded.copyInto(directories);
        return directories;
    }

    /**
     * Returns the names of the directories which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     * The names are relative to the base directory. This involves
     * performing a slow scan if one has not already been completed.
     *
     * @return the names of the directories which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     *
     * @see #slowScan
     */
    public String[] getExcludedDirectories() {
        slowScan();
        String[] directories = new String[dirsExcluded.size()];
        dirsExcluded.copyInto(directories);
        return directories;
    }

    /**
     * <p>Returns the names of the directories which were selected out and
     * therefore not ultimately included.</p>
     *
     * <p>The names are relative to the base directory. This involves
     * performing a slow scan if one has not already been completed.</p>
     *
     * @return the names of the directories which were deselected.
     *
     * @see #slowScan
     */
    public String[] getDeselectedDirectories() {
        slowScan();
        String[] directories = new String[dirsDeselected.size()];
        dirsDeselected.copyInto(directories);
        return directories;
    }

    /**
     * Adds default exclusions to the current exclusions set.
     */
    public void addDefaultExcludes() {
        int excludesLength = excludes == null ? 0 : excludes.length;
        String[] newExcludes;
        newExcludes = new String[excludesLength + defaultExcludes.size()];
        if (excludesLength > 0) {
            System.arraycopy(excludes, 0, newExcludes, 0, excludesLength);
        }
        String[] defaultExcludesTemp = getDefaultExcludes();
        for (int i = 0; i < defaultExcludesTemp.length; i++) {
            newExcludes[i + excludesLength] =
                defaultExcludesTemp[i].replace('/', File.separatorChar)
                .replace('\\', File.separatorChar);
        }
        excludes = newExcludes;
    }

    /**
     * Get the named resource
     * @param name path name of the file relative to the dir attribute.
     *
     * @return the resource with the given name.
     * @since Ant 1.5.2
     */
    public Resource getResource(String name) {
        File f = fileUtils.resolveFile(basedir, name);
        return new Resource(name, f.exists(), f.lastModified(),
                            f.isDirectory());
    }

    /**
     * temporary table to speed up the various scanning methods below
     *
     * @since Ant 1.6
     */
    private Map fileListMap = new HashMap();

    /**
     * From <code>base</code> traverse the filesystem in a case
     * insensitive manner in order to find a file that matches the
     * given name.
     *
     * @return File object that points to the file in question.  if it
     * hasn't been found it will simply be <code>new File(base,
     * path)</code>.
     *
     * @since Ant 1.6
     */
    private File findFileCaseInsensitive(File base, String path) {
        File f = findFileCaseInsensitive(base,
                                         SelectorUtils.tokenizePath(path));
        return  f == null ? new File(base, path) : f;
    }

    /**
     * From <code>base</code> traverse the filesystem in a case
     * insensitive manner in order to find a file that matches the
     * given stack of names.
     *
     * @return File object that points to the file in question or null.
     *
     * @since Ant 1.6
     */
    private File findFileCaseInsensitive(File base, Vector pathElements) {
        if (pathElements.size() == 0) {
            return base;
        } else {
            if (!base.isDirectory()) {
                return null;
            }
            ISVNStatus[] files = list(base);
            if (files == null) {
                throw new BuildException("IO error scanning directory "
                                         + base.getAbsolutePath());
            }
            String current = (String) pathElements.remove(0);
            for (int i = 0; i < files.length; i++) {
            	String name = files[i].getFile().getName();
                if( name.equals(current) ) {
                    return findFileCaseInsensitive(new File(base, name), pathElements);
                }
            }
            for (int i = 0; i < files.length; i++) {
            	String name = files[i].getFile().getName();
                if( name.equalsIgnoreCase(current) ) {
                    return findFileCaseInsensitive(new File(base, name), pathElements);
                }
            }
        }
        return null;
    }

    /**
     * From <code>base</code> traverse the filesystem in order to find
     * a file that matches the given name.
     *
     * @return File object that points to the file in question or null.
     *
     * @since Ant 1.6
     */
    private File findFile(File base, String path) {
        return findFile(base, SelectorUtils.tokenizePath(path));
    }

    /**
     * From <code>base</code> traverse the filesystem in order to find
     * a file that matches the given stack of names.
     *
     * @return File object that points to the file in question or null.
     *
     * @since Ant 1.6
     */
    private File findFile(File base, Vector pathElements) {
        if (pathElements.size() == 0) {
            return base;
        } else {
            if (!base.isDirectory()) {
                return null;
            }
            ISVNStatus[] files = list(base);
            if (files == null) {
                throw new BuildException("IO error scanning directory "
                                         + base.getAbsolutePath());
            }
            String current = (String) pathElements.remove(0);
            for (int i = 0; i < files.length; i++) {
            	String name = files[i].getFile().getName();
                if( name.equals(current) ) {
                    return findFile(new File(base, name), pathElements);
                }
            }
        }
        return null;
    }

    /**
     * Do we have to traverse a symlink when trying to reach path from
     * basedir?
     * @since Ant 1.6
     */
    private boolean isSymlink(File base, String path) {
        return isSymlink(base, SelectorUtils.tokenizePath(path));
    }

    /**
     * Do we have to traverse a symlink when trying to reach path from
     * basedir?
     * @since Ant 1.6
     */
    private boolean isSymlink(File base, Vector pathElements) {
        if (pathElements.size() > 0) {
            String current = (String) pathElements.remove(0);
            try {
                if (fileUtils.isSymbolicLink(base, current)) {
                    return true;
                } else {
                    return isSymlink(new File(base, current), pathElements);
                }
            } catch (IOException ioe) {
                String msg = "IOException caught while checking "
                    + "for links, couldn't get canonical path!";
                // will be caught and redirected to Ant's logging system
                System.err.println(msg);
                return false;
            }
        }
        return false;
    }

    /**
     * List of all scanned directories.
     *
     * @since Ant 1.6
     */
    private Set scannedDirs = new HashSet();

    /**
     * Has the directory with the given path relative to the base
     * directory already been scanned?
     *
     * <p>Registers the given directory as scanned as a side effect.</p>
     *
     * @since Ant 1.6
     */
    private boolean hasBeenScanned(String vpath) {
        return !scannedDirs.add(vpath);
    }

    /**
     * Clear internal caches.
     *
     * @since Ant 1.6
     */
    private void clearCaches() {
        fileListMap.clear();
        scannedDirs.clear();
    }
}
