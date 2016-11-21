import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class GroupTest {

	public static jGroup jg_r = new jGroup();
	public static jGroup jg_n1 = new jGroup();
	public static jGroup jg_n1_1 = new jGroup();
	public static jGroup jg_n1_2 = new jGroup();
	public static jGroup jg_n2 = new jGroup();
	public static jGroup jg_n2_1 = new jGroup();
	public static jGroup jg_n2_2 = new jGroup();

	
	
	public static final void reload(ItkeepsHttpClient client) {
		
		jg_r = client.callJsonRead("/api/group/" + jg_r.getGuid(), jGroup.class);
		jg_n1 = client.callJsonRead("/api/group/" + jg_n1.getGuid(), jGroup.class);
		jg_n1_1 = client.callJsonRead("/api/group/" + jg_n1_1.getGuid(), jGroup.class);
		jg_n1_2 = client.callJsonRead("/api/group/" + jg_n1_2.getGuid(), jGroup.class);
		jg_n2 = client.callJsonRead("/api/group/" + jg_n2.getGuid(), jGroup.class);
		jg_n2_1 = client.callJsonRead("/api/group/" + jg_n2_1.getGuid(), jGroup.class);
		jg_n2_2 = client.callJsonRead("/api/group/" + jg_n2_2.getGuid(), jGroup.class);
		
	}	
	
	public static final void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		jg_r.setName("root");
		jg_n1.setName("node 1");
		jg_n2.setName("node 2");
		jg_n1_1.setName("node 1.1");
		jg_n1_2.setName("node 1.2");
		jg_n2_1.setName("node 2.1");
		jg_n2_2.setName("node 2.2");

		/* Create */
		
		jg_r = client.callJsonCreat("/api/group", jg_r, jGroup.class);
		jg_n1 = client.callJsonCreat("/api/group", jg_n1, jGroup.class);
		jg_n2 = client.callJsonCreat("/api/group", jg_n2, jGroup.class);
		jg_n1_1 = client.callJsonCreat("/api/group", jg_n1_1, jGroup.class);
		jg_n1_2 = client.callJsonCreat("/api/group", jg_n1_2, jGroup.class);
		jg_n2_1 = client.callJsonCreat("/api/group", jg_n2_1, jGroup.class);
		jg_n2_2 = client.callJsonCreat("/api/group", jg_n2_2, jGroup.class);
		
		
		
		jg_r.getChilds().add(jg_n1.getJBaseLight());
		jg_r.getChilds().add(jg_n2.getJBaseLight());
		jg_r = client.callJsonUpdate("/api/group", jg_r, jGroup.class);
		
		
		reload(client);
		
		jg_n1.getChilds().add(jg_n1_1.getJBaseLight());
		jg_n1.getChilds().add(jg_n1_2.getJBaseLight());
		jg_n1 = client.callJsonUpdate("/api/group", jg_n1, jGroup.class);

		jg_n2.getChilds().add(jg_n2_1.getJBaseLight());
		jg_n2.getChilds().add(jg_n2_2.getJBaseLight());
		jg_n2 = client.callJsonUpdate("/api/group", jg_n2, jGroup.class);

		reload(client);
	}
	
	
}
