package bboss.org.jgroups.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Represents an array of deprecated Protocol properties 
 * 
 *
 * @author Vladimir Blagojevic
 * @version $Id: DeprecatedProperty.java,v 1.1 2008/05/29 13:53:04 vlada Exp $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE})
public @interface DeprecatedProperty {

    String [] names() default"";    
}
