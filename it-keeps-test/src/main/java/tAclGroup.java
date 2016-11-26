import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.model.jAclGroup;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class tAclGroup {

	public static jAclGroup aclGrp_dataPathCreate = new jAclGroup();
	public static jAclGroup aclGrp_dataPathUpdate = new jAclGroup();
	public static jAclGroup aclGrp_dataPathRead = new jAclGroup();
	public static jAclGroup aclGrp_dataPathDeny = new jAclGroup();

	public static jAclGroup aclGrp_dataEntryCreate = new jAclGroup();
	public static jAclGroup aclGrp_dataEntryUpdate = new jAclGroup();
	public static jAclGroup aclGrp_dataEntryRead = new jAclGroup();
	public static jAclGroup aclGrp_dataEntryDeny = new jAclGroup();
	
	
	public static jAclGroup aclGrp_dataCreate = new jAclGroup();
	public static jAclGroup aclGrp_dataUpdate = new jAclGroup();
	public static jAclGroup aclGrp_dataRead = new jAclGroup();
	public static jAclGroup aclGrp_dataDeny = new jAclGroup();

	public static jAclGroup aclGrp_TestDelete = new jAclGroup();
	
	
	public static final void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		aclGrp_dataPathCreate.setName("aclGrp Data Path CREATE");
		aclGrp_dataPathCreate.getAclData().add(enAclData.PATH_CREATE);
		aclGrp_dataPathCreate = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataPathCreate, jAclGroup.class);

		aclGrp_dataPathUpdate.setName("aclGrp Data Path UPDATE");
		aclGrp_dataPathUpdate.getAclData().add(enAclData.PATH_UPDATE);
		aclGrp_dataPathUpdate = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataPathUpdate, jAclGroup.class);

		aclGrp_dataPathRead.setName("aclGrp Data Path READ");
		aclGrp_dataPathRead.getAclData().add(enAclData.PATH_READ);
		aclGrp_dataPathRead = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataPathRead, jAclGroup.class);

		aclGrp_dataPathDeny.setName("aclGrp Data Path DENY");
		aclGrp_dataPathDeny.getAclData().add(enAclData.PATH_DENY);
		aclGrp_dataPathDeny = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataPathDeny, jAclGroup.class);
		
		
		
		aclGrp_dataEntryCreate.setName("aclGrp Data Entry CREATE");
		aclGrp_dataEntryCreate.getAclData().add(enAclData.ENTRY_CREATE);
		aclGrp_dataEntryCreate = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataEntryCreate, jAclGroup.class);

		aclGrp_dataEntryUpdate.setName("aclGrp Data Entry UPDATE");
		aclGrp_dataEntryUpdate.getAclData().add(enAclData.ENTRY_UPDATE);
		aclGrp_dataEntryUpdate = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataEntryUpdate, jAclGroup.class);

		aclGrp_dataEntryRead.setName("aclGrp Data Entry READ");
		aclGrp_dataEntryRead.getAclData().add(enAclData.ENTRY_READ);
		aclGrp_dataEntryRead = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataEntryRead, jAclGroup.class);

		aclGrp_dataEntryDeny.setName("aclGrp Data Entry DENY");
		aclGrp_dataEntryDeny.getAclData().add(enAclData.ENTRY_DENY);
		aclGrp_dataEntryDeny = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataEntryDeny, jAclGroup.class);
		
		
		
		aclGrp_dataCreate.setName("aclGrp Data CREATE");
		aclGrp_dataCreate.getParents().add(aclGrp_dataPathCreate.getJBaseLight());
		aclGrp_dataCreate.getParents().add(aclGrp_dataEntryCreate.getJBaseLight());
		aclGrp_dataCreate = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataCreate, jAclGroup.class);

		aclGrp_dataUpdate.setName("aclGrp Data UPDATE");
		aclGrp_dataUpdate.getParents().add(aclGrp_dataPathUpdate.getJBaseLight());
		aclGrp_dataUpdate.getParents().add(aclGrp_dataEntryUpdate.getJBaseLight());
		aclGrp_dataUpdate = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataUpdate, jAclGroup.class);

		aclGrp_dataRead.setName("aclGrp Data READ");
		aclGrp_dataRead.getParents().add(aclGrp_dataPathRead.getJBaseLight());
		aclGrp_dataRead.getParents().add(aclGrp_dataEntryRead.getJBaseLight());
		aclGrp_dataRead = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataRead, jAclGroup.class);

		aclGrp_dataDeny.setName("aclGrp Data DENY");
		aclGrp_dataDeny = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_dataDeny, jAclGroup.class);
		aclGrp_dataDeny.getAclData().add(enAclData.DENY);
		aclGrp_dataDeny = client.callJsonUpdate(ApiPath.API_ACL_GRP_UPDATE, aclGrp_dataDeny, jAclGroup.class);
		aclGrp_dataDeny.getParents().add(aclGrp_dataPathDeny.getJBaseLight());
		aclGrp_dataDeny = client.callJsonUpdate(ApiPath.API_ACL_GRP_UPDATE, aclGrp_dataDeny, jAclGroup.class);
		aclGrp_dataDeny.getParents().add(aclGrp_dataEntryDeny.getJBaseLight());
		aclGrp_dataDeny = client.callJsonUpdate(ApiPath.API_ACL_GRP_UPDATE, aclGrp_dataDeny, jAclGroup.class);

		
		aclGrp_TestDelete.setName("aclGrp Test Delete");

		
		
		/* Create */
		
		
		aclGrp_TestDelete = client.callJsonCreat(ApiPath.API_ACL_GRP_CREATE, aclGrp_TestDelete, jAclGroup.class);
		aclGrp_TestDelete = client.callJsonDelete(ApiPath.API_ACL_GRP_DELETE + aclGrp_TestDelete.getGuid(), jAclGroup.class);
		
		
		
	}
	
	
}
