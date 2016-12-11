import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;

public class tGroup {

	public static jGroup jg_r = new jGroup();
	public static jGroup jg_n1 = new jGroup();
	public static jGroup jg_n1_1 = new jGroup();
	public static jGroup jg_n1_2 = new jGroup();
	public static jGroup jg_n2 = new jGroup();
	public static jGroup jg_n2_1 = new jGroup();
	public static jGroup jg_n2_2 = new jGroup();

	
	
	public static void reload(ItkeepsHttpClient client) {
		
		jg_r = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_r.getRid()), jGroup.class);
		jg_n1 = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_n1.getRid()), jGroup.class);
		jg_n1_1 = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_n1_1.getRid()), jGroup.class);
		jg_n1_2 = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_n1_2.getRid()), jGroup.class);
		jg_n2 = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_n2.getRid()), jGroup.class);
		jg_n2_1 = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_n2_1.getRid()), jGroup.class);
		jg_n2_2 = client.callJsonRead(ApiPath.API_GROUP_GET_ID + ParameterEncoder.encoding(jg_n2_2.getRid()), jGroup.class);
		
	}	
	
	public static void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		jg_r.setName("root");
		jg_n1.setName("node 1");
		jg_n2.setName("node 2");
		jg_n1_1.setName("node 1.1");
		jg_n1_2.setName("node 1.2");
		jg_n2_1.setName("node 2.1");
		jg_n2_2.setName("node 2.2");

		/* Create */
		
		jg_r = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_r, jGroup.class);
		jg_n1 = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_n1, jGroup.class);
		jg_n2 = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_n2, jGroup.class);
		jg_n1_1 = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_n1_1, jGroup.class);
		jg_n1_2 = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_n1_2, jGroup.class);
		jg_n2_1 = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_n2_1, jGroup.class);
		jg_n2_2 = client.callJsonCreat(ApiPath.API_GROUP_CREATE, jg_n2_2, jGroup.class);


        jg_n1.getChilds().add(jg_n1_1.getLight());
        jg_n1.getChilds().add(jg_n1_2.getLight());
        jg_n1 = client.callJsonUpdate(ApiPath.API_GROUP_UPDATE, jg_n1, jGroup.class);

        jg_n2.getChilds().add(jg_n2_1.getLight());
        jg_n2.getChilds().add(jg_n2_2.getLight());
        jg_n2 = client.callJsonUpdate(ApiPath.API_GROUP_UPDATE, jg_n2, jGroup.class);


        jg_r.getChilds().add(jg_n1.getLight());
        jg_r.getChilds().add(jg_n2.getLight());
        jg_r = client.callJsonUpdate(ApiPath.API_GROUP_UPDATE, jg_r, jGroup.class);
		
		reload(client);
	}
	
	
}
