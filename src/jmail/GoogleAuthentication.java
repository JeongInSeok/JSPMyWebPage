package jmail;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class GoogleAuthentication extends Authenticator {
	PasswordAuthentication passAuth;
    
    public GoogleAuthentication(){
        passAuth = new PasswordAuthentication("kxv1031", "ffqfryknfduylhum");
    }
 
    public PasswordAuthentication getPasswordAuthentication() {
        return passAuth;
    }
}
