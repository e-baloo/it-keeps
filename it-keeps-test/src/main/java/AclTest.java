import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class AclTest {

	public static jAcl acl1 = new jAcl();

	
	
	public static final void reload(ItkeepsHttpClient client) {
		
		acl1 = client.callJsonRead(ApiPath.API_ACL_GET_ID + acl1.getGuid(), jAcl.class);
		
	}	
	
	public static final void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		acl1.setName("acl1");
		acl1.setOwner(true);
		acl1.setAclData(enAclData.CREATE);
		acl1.getAclAdmin().add(enAclAdmin.DELEGATE);
		acl1.getChildObjects().add(GroupTest.jg_n1.getJBaseLight());
		acl1.getChildObjects().add(PathTest.jg_r2_n1.getJBaseLight());

		/* Create */
		
		acl1 = client.callJsonCreat(ApiPath.API_ACL_CREATE, acl1, jAcl.class);
		
		
		
		reload(client);
		PathTest.reload(client);
		GroupTest.reload(client);
		
	}
	
	
}
