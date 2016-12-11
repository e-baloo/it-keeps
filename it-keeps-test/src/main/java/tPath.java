import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;

public class tPath {

	public static jPath jg_r1 = new jPath();
	public static jPath jg_r2 = new jPath();
	public static jPath jg_r1_n1 = new jPath();
	public static jPath jg_r1_n1_1 = new jPath();
	public static jPath jg_r1_n1_2 = new jPath();
	public static jPath jg_r1_n2 = new jPath();
	public static jPath jg_r2_n1 = new jPath();
	public static jPath jg_r2_n1_1 = new jPath();
	public static jPath jg_r2_n1_2 = new jPath();

	
	
	public static void reload(ItkeepsHttpClient client) {
		
		jg_r1 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r1.getRid()), jPath.class);
		jg_r2 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r2.getRid()), jPath.class);
		jg_r1_n1 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r1_n1.getRid()), jPath.class);
		jg_r1_n2 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r1_n2.getRid()), jPath.class);
		jg_r1_n1_1 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r1_n1_1.getRid()), jPath.class);
		jg_r1_n1_2 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r1_n1_2.getRid()), jPath.class);
		jg_r2_n1 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r2_n1.getRid()), jPath.class);
		jg_r2_n1_1 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r2_n1_1.getRid()), jPath.class);
		jg_r2_n1_2 = client.callJsonRead(ApiPath.API_PATH_GET_ID + ParameterEncoder.encoding(jg_r2_n1_2.getRid()), jPath.class);
		
	}	
	
	public static void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		jg_r1.setName("root 1");
		jg_r2.setName("root 2");
		jg_r1_n1.setName("r1 - n 1");
		jg_r1_n2.setName("r1 - n 2");
		jg_r1_n1_1.setName("r1 - n 1.1");
		jg_r1_n1_2.setName("r1 - n 1.2");
		jg_r2_n1.setName("r2 - n 1");
		jg_r2_n1_1.setName("r2 - n 1.1");
		jg_r2_n1_2.setName("r2 - n 1.2");

		/* Create */
		
		jg_r1 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r1, jPath.class);
		jg_r2 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r2, jPath.class);
		jg_r1_n1 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r1_n1, jPath.class);
		jg_r1_n2 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r1_n2, jPath.class);
		jg_r1_n1_1 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r1_n1_1, jPath.class);
		jg_r1_n1_2 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r1_n1_2, jPath.class);
		jg_r2_n1 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r2_n1, jPath.class);
		jg_r2_n1_1 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r2_n1_1, jPath.class);
		jg_r2_n1_2 = client.callJsonCreat(ApiPath.API_PATH_CREATE, jg_r2_n1_2, jPath.class);


        jg_r1_n1.getChilds().add(jg_r1_n1_1.getLight());
        jg_r1_n1.getChilds().add(jg_r1_n1_2.getLight());
        jg_r1_n1 = client.callJsonUpdate(ApiPath.API_PATH_UPDATE, jg_r1_n1, jPath.class);

        jg_r2_n1.getChilds().add(jg_r2_n1_1.getLight());
        jg_r2_n1.getChilds().add(jg_r2_n1_2.getLight());
        jg_r2_n1 = client.callJsonUpdate(ApiPath.API_PATH_UPDATE, jg_r2_n1, jPath.class);

        jg_r1.getChilds().add(jg_r1_n1.getLight());
        jg_r1.getChilds().add(jg_r1_n2.getLight());
        jg_r1 = client.callJsonUpdate(ApiPath.API_PATH_UPDATE, jg_r1, jPath.class);

        jg_r2.getChilds().add(jg_r2_n1.getLight());
        jg_r2 = client.callJsonUpdate(ApiPath.API_PATH_UPDATE, jg_r2, jPath.class);
		
		reload(client);
		
	}
	
	
}
