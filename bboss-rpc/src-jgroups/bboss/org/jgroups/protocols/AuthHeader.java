package bboss.org.jgroups.protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bboss.org.jgroups.Header;
import bboss.org.jgroups.auth.AuthToken;
import bboss.org.jgroups.util.Util;
/**
 * AuthHeader is a holder object for the token that is passed from the joiner to the coordinator
 * @author Chris Mills
 */
public class AuthHeader extends Header {
    private AuthToken token=null;

    public AuthHeader(){
    }
    /**
     * Sets the token value to that of the passed in token object
     * @param token the new authentication token
     */
    public void setToken(AuthToken token){
        this.token = token;
    }

    /**
     * Used to get the token from the AuthHeader
     * @return the token found inside the AuthHeader
     */
    public AuthToken getToken(){
        return this.token;
    }


    public void writeTo(DataOutputStream out) throws IOException {
        Util.writeAuthToken(this.token, out);
    }

    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
        this.token = Util.readAuthToken(in);
    }
    public int size(){
        //need to fix this
        return Util.sizeOf(this);
    }
}

