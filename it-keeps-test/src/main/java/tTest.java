import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.enumeration.enAclOwner;
import org.ebaloo.itkeeps.api.model.jAcl;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;

public class tTest {

	public static jAcl acl1 = new jAcl();

	
	
	public static void reload(ItkeepsHttpClient client) {
		
		acl1 = client.callJsonRead(ApiPath.API_ACL_GET_ID + ParameterEncoder.encoding(acl1.getRid()), jAcl.class);
		
	}	
	
	public static void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		acl1.setName("acl1");
		acl1.setOwner(enAclOwner.OWNER_TRUE);
		acl1.getAclData().add(enAclData.PATH_CREATE);
		acl1.getAclData().add(enAclData.ENTRY_CREATE);
		acl1.getAclAdmin().add(enAclAdmin.DELEGATE);
		acl1.getChildObjects().add(tGroup.jg_n1.getJBaseLight());
		acl1.getChildObjects().add(tPath.jg_r2_n1.getJBaseLight());

		/* Create */
		
		acl1 = client.callJsonCreat(ApiPath.API_ACL_CREATE, acl1, jAcl.class);
		
		
		
		reload(client);
		tPath.reload(client);
		tGroup.reload(client);
		
	}
	
	
}
