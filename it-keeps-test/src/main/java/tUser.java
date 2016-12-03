
import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAclRole;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;
import org.glassfish.jersey.internal.util.Base64;

public class tUser {

	public static jUser gest_1 = new jUser();
	public static jUser user_1 = new jUser();
	public static jUser user_2 = new jUser();
	public static jUser admin_1 = new jUser();

	
	
	public static final void reload(ItkeepsHttpClient client) {

		gest_1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(gest_1.getRid()), jUser.class);
		user_1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(user_1.getRid()), jUser.class);
		user_2 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(user_2.getRid()), jUser.class);
		admin_1 = client.callJsonRead(ApiPath.API_USER_GET_ID + ParameterEncoder.encoding(admin_1.getRid()), jUser.class);
		
	}	
	
	public static final void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		gest_1.setName("gest 1");
		user_1.setName("user 1");
		user_2.setName("user_2");
		admin_1.setName("admin_1");

		gest_1.setRole(enAclRole.GUEST);
		user_1.setRole(enAclRole.USER);
		user_2.setRole(enAclRole.USER);
		admin_1.setRole(enAclRole.ADMIN);

		/* Create */
		
		gest_1 = client.callJsonCreat(ApiPath.API_USER_CREATE, gest_1, jUser.class);
		user_1 = client.callJsonCreat(ApiPath.API_USER_CREATE, user_1, jUser.class);
		user_2 = client.callJsonCreat(ApiPath.API_USER_CREATE, user_2, jUser.class);
		admin_1 = client.callJsonCreat(ApiPath.API_USER_CREATE, admin_1, jUser.class);
		
		
		user_1.getGroups().add(tGroup.jg_n1.getJBaseLight());
		user_2.getGroups().add(tGroup.jg_n2.getJBaseLight());
		admin_1.getGroups().add(tGroup.jg_r.getJBaseLight());

		user_1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user_1, jUser.class);
		user_2 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user_2, jUser.class);
		admin_1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, admin_1, jUser.class);

		
		reload(client);
		tGroup.reload(client);
		
		
		jCredential credAdmin1 = new jCredential();
		credAdmin1.setId("admin1");
		credAdmin1.setPassword64(Base64.encodeAsString("admin1"));
		//credAdmin1.set
		//admin_1.getCredentials().add()
		
		
		
	}
	
	
}
