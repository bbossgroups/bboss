package org.tigris.subversion.svnant.conditions;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
import org.tigris.subversion.svnant.ISvnAntProjectComponent;
import org.tigris.subversion.svnant.SvnAntException;

/**
 * This is an abstract class that implements functions common to all subclasses.
 * It handles the generic 'javahl' and 'svnkit' properties.
 * 
 * Conditions are from a special type in ANT where they can be used to set values
 * of properties. See taks Condition in ANT documentation.
 * 
 * @author Jean-Pierre Fiset <a href="mailto:jp@fiset.ca">jp@fiset.ca</a>
 *
 */
public abstract class SvnCondition extends ConditionBase implements Condition, ISvnAntProjectComponent {

    private boolean javahl = true;
    
    private boolean svnkit = true;

    /* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.ISvnAntProjectComponent#getJavahl()
	 */
	public boolean getJavahl() {
		return javahl;
	}

	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.ISvnAntProjectComponent#getSvnKit()
	 */
	public boolean getSvnKit() {
		return svnkit;
	}

	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.ISvnAntProjectComponent#getProjectComponent()
	 */
	public ProjectComponent getProjectComponent() {
		return this;
	}

    /**
     * Accessor method to 'javahl' property. If reset (false),
     * JavaHL is not used.
     * @param javahl_ New value for javahl property.
     */
    public void setJavahl(boolean javahl_) {
        javahl = javahl_;
    }

    /**
     * Accessor method to 'svnkit' property. If reset (false),
     * SVNKit is not used.
     * @param svnkit_ New value for svnkit property.
     */
    public void setSvnkit(boolean svnkit_) {
        svnkit = svnkit_;
    }
    
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.taskdefs.condition.Condition#eval()
	 */
	public boolean eval() throws BuildException {
		try {
			return internalEval();
		} catch (SvnAntException e) {
			throw new BuildException(e.getMessage(), e);
		}
	}
	
	/**
	 * Method called internally by eval(). Must be implemented by all subclasses.
	 * @return True if the condition is met. False, otherwsie.
	 * @throws SvnAntException exception that should be used in case of problems.
	 */
	public abstract boolean internalEval() throws SvnAntException;
}
