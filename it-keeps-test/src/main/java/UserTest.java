import javax.ws.rs.Path;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.annotation.aApplicationRolesAllowed.enSecurityRole;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class UserTest {

	public static jUser gest_1 = new jUser();
	public static jUser user_1 = new jUser();
	public static jUser user_2 = new jUser();
	public static jUser admin_1 = new jUser();

	
	
	public static final void reload(ItkeepsHttpClient client) {

		gest_1 = client.callJsonRead(ApiPath.API_USER_GET_ID + gest_1.getGuid(), jUser.class);
		user_1 = client.callJsonRead(ApiPath.API_USER_GET_ID + user_1.getGuid(), jUser.class);
		user_2 = client.callJsonRead(ApiPath.API_USER_GET_ID + user_2.getGuid(), jUser.class);
		admin_1 = client.callJsonRead(ApiPath.API_USER_GET_ID + admin_1.getGuid(), jUser.class);
		
	}	
	
	public static final void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		gest_1.setName("gest 1");
		user_1.setName("user 1");
		user_2.setName("user_2");
		admin_1.setName("admin_1");

		gest_1.setRole(enSecurityRole.GUEST);
		user_1.setRole(enSecurityRole.USER);
		user_2.setRole(enSecurityRole.USER);
		admin_1.setRole(enSecurityRole.ADMIN);

		/* Create */
		
		gest_1 = client.callJsonCreat(ApiPath.API_USER_CREATE, gest_1, jUser.class);
		user_1 = client.callJsonCreat(ApiPath.API_USER_CREATE, user_2, jUser.class);
		user_2 = client.callJsonCreat(ApiPath.API_USER_CREATE, user_2, jUser.class);
		admin_1 = client.callJsonCreat(ApiPath.API_USER_CREATE, admin_1, jUser.class);
		
		
		user_1.getInGroups().add(GroupTest.jg_n1.getJBaseLight());
		user_2.getInGroups().add(GroupTest.jg_n2.getJBaseLight());
		admin_1.getInGroups().add(GroupTest.jg_r.getJBaseLight());

		user_1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user_1, jUser.class);
		user_2 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, user_2, jUser.class);
		admin_1 = client.callJsonUpdate(ApiPath.API_USER_UPDATE, admin_1, jUser.class);

		
		reload(client);
		GroupTest.reload(client);
	}
	
	
}
