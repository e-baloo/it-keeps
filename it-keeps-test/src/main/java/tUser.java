
import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.enumeration.enAuthenticationType;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;
import org.ebaloo.itkeeps.tools.Base64;

public class tUser {

	public static jUser guest1 = new jUser();
	public static jUser user1 = new jUser();
	public static jUser user2 = new jUser();
	public static jUser admin1 = new jUser();
	public static jCredential credGuest1 = null;
	public static jCredential credUser1 = null;
	public static jCredential credUser2 = null; 
	public static jCredential credAdmin1 = null; 
	
	
	public static void reload(ItkeepsHttpClient client) {

		guest1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(guest1.getId()), jUser.class);
		user1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(user1.getId()), jUser.class);
		user2 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(user2.getId()), jUser.class);
		admin1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(admin1.getId()), jUser.class);
		
	}	
	
	public static void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		guest1.setName("guest1");
		user1.setName("user1");
		user2.setName("user2");
		admin1.setName("admin1");

		guest1.setRole(enAclRole.GUEST);
		user1.setRole(enAclRole.USER);
		user2.setRole(enAclRole.USER);
		admin1.setRole(enAclRole.ADMIN);

		/* Create */
		
		guest1 = client.callJsonCreat(ApiPath.API_USER_CREATE, guest1, jUser.class);
		user1 = client.callJsonCreat(ApiPath.API_USER_CREATE, user1, jUser.class);
		user2 = client.callJsonCreat(ApiPath.API_USER_CREATE, user2, jUser.class);
		admin1 = client.callJsonCreat(ApiPath.API_USER_CREATE, admin1, jUser.class);


        user1.getGroups().add(tGroup.jg_n1.getLight());
        user2.getGroups().add(tGroup.jg_n2.getLight());
        admin1.getGroups().add(tGroup.jg_r.getLight());

		user1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user1, jUser.class);
		user2 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user2, jUser.class);
		admin1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, admin1, jUser.class);

		
		reload(client);
		tGroup.reload(client);

		credGuest1 = new jCredential();
		credGuest1.setName(guest1.getName());
		credGuest1.setPassword64(Base64.encodeAsString(credGuest1.getName()));
		credGuest1.setAuthenticationType(enAuthenticationType.BASIC);

		credAdmin1 = new jCredential();
		credAdmin1.setName(admin1.getName());
		credAdmin1.setPassword64(Base64.encodeAsString(credAdmin1.getName()));
		credAdmin1.setAuthenticationType(enAuthenticationType.BASIC);

		credUser1 = new jCredential();
		credUser1.setName(user1.getName());
		credUser1.setPassword64(Base64.encodeAsString(credUser1.getName()));
		credUser1.setAuthenticationType(enAuthenticationType.BASIC);

		credUser2 = new jCredential();
		credUser2.setName(user2.getName());
		credUser2.setPassword64(Base64.encodeAsString(credUser2.getName()));
		credUser2.setAuthenticationType(enAuthenticationType.BASIC);


		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(guest1.getId()), credGuest1, jCredential.class);
		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(admin1.getId()), credAdmin1, jCredential.class);
		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(user1.getId()), credUser1, jCredential.class);
		client.callJsonCreat(ApiPath.API_CRED_CREATE_ID + ParameterEncoder.encoding(user2.getId()), credUser2, jCredential.class);
		
		reload(client);

		
	}
	
	
}
