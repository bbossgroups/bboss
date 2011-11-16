SvnAnt
======
You can find latest version of svnant on http://subclipse.tigris.org

Installation
============
Put the svnant.jar and svnClientAdapter.jar files to classpath of your ant build file.
(Or directly to your ANT_HOME/lib directory.)
If you plan to use JavaHL instead of commandline, put the svnjavahl.jar to the classpath too.
(An of course put the appropriate native OS library on your PATH/LD_PATH too)

In the build file, load the <svn> task.
E.g. like this:
  <typedef resource="org/tigris/subversion/svnant/svnantlib.xml" classpathref="svnant.classpath" /> 
(where svnant.jar lies in the "project.classpath")

To provide access to the Subversion API, svnant uses svnClientAdapter on top of 
either the JavaHL native Subversion Java (JNI) bindings or Subverion's
command line programs (which must be installed and in your PATH).
See the subclipse's FAQ <http://subclipse.tigris.org/faq.html#get-javahl">
for info how to get JavaHL for your operating system.

Documentation
=============
Documentation of the <svn> task is in the /doc directory.

Play around
===========
To access the sources, just type "ant" in the directory containing the
build.xml bundled with the distribution.  The default target will
retrieve the sources corresponding to the version you have.  If you
want to get the latest sources, type "ant checkoutLatest".

Once you have the sources, svnant unit tests can be invoked using the
top level build.xml by typing "ant runTests".  These tests provide a
great set of examples of how to use svnant's Ant tasks and data types.

Please send any usage questions to <mailto:users@subclipse.tigris.org>.
