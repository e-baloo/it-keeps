
import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.enumeration.enAuthentication;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;
import org.ebaloo.itkeeps.tools.Base64;

public class tUser {

	public static jUser gest1 = new jUser();
	public static jUser user1 = new jUser();
	public static jUser user2 = new jUser();
	public static jUser admin1 = new jUser();
	public static jCredential credUser1 = null; 
	public static jCredential credUser2 = null; 
	public static jCredential credAdmin1 = null; 
	
	
	public static void reload(ItkeepsHttpClient client) {

		gest1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(gest1.getRid()), jUser.class);
		user1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(user1.getRid()), jUser.class);
		user2 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(user2.getRid()), jUser.class);
		admin1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(admin1.getRid()), jUser.class);
		
	}	
	
	public static void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		gest1.setName("gest1");
		user1.setName("user1");
		user2.setName("user2");
		admin1.setName("admin1");

		gest1.setRole(enAclRole.GUEST);
		user1.setRole(enAclRole.USER);
		user2.setRole(enAclRole.USER);
		admin1.setRole(enAclRole.ADMIN);

		/* Create */
		
		gest1 = client.callJsonCreat(ApiPath.API_USER_CREATE, gest1, jUser.class);
		user1 = client.callJsonCreat(ApiPath.API_USER_CREATE, user1, jUser.class);
		user2 = client.callJsonCreat(ApiPath.API_USER_CREATE, user2, jUser.class);
		admin1 = client.callJsonCreat(ApiPath.API_USER_CREATE, admin1, jUser.class);
		
		
		user1.getGroups().add(tGroup.jg_n1.getJBaseLight());
		user2.getGroups().add(tGroup.jg_n2.getJBaseLight());
		admin1.getGroups().add(tGroup.jg_r.getJBaseLight());

		user1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user1, jUser.class);
		user2 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user2, jUser.class);
		admin1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, admin1, jUser.class);

		
		reload(client);
		tGroup.reload(client);
		
		
		credAdmin1 = new jCredential();
		credAdmin1.setCred(admin1.getName());
		credAdmin1.setPassword64(Base64.encodeAsString(credAdmin1.getCred()));
		credAdmin1.setAuthenticationType(enAuthentication.BASIC);

		credUser1 = new jCredential();
		credUser1.setCred(user1.getName());
		credUser1.setPassword64(Base64.encodeAsString(credUser1.getCred()));
		credUser1.setAuthenticationType(enAuthentication.BASIC);

		credUser2 = new jCredential();
		credUser2.setCred(user2.getName());
		credUser2.setPassword64(Base64.encodeAsString(credUser2.getCred()));
		credUser2.setAuthenticationType(enAuthentication.BASIC);


		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(admin1.getRid()), credAdmin1, jCredential.class);
		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(user1.getRid()), credUser1, jCredential.class);
		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(user2.getRid()), credUser2, jCredential.class);
		
		reload(client);

		
	}
	
	
}
