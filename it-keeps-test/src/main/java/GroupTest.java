import org.ebaloo.itkeeps.api.model.JGroup;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class GroupTest {

	public static JGroup jg_r = new JGroup();
	public static JGroup jg_n1 = new JGroup();
	public static JGroup jg_n1_1 = new JGroup();
	public static JGroup jg_n1_2 = new JGroup();
	public static JGroup jg_n2 = new JGroup();
	public static JGroup jg_n2_1 = new JGroup();
	public static JGroup jg_n2_2 = new JGroup();

	
	
	public static final void reload(ItkeepsHttpClient client) {
		
		jg_r = client.callJsonRead("/api/group/" + jg_r.getGuid(), JGroup.class);
		jg_n1 = client.callJsonRead("/api/group/" + jg_n1.getGuid(), JGroup.class);
		jg_n1_1 = client.callJsonRead("/api/group/" + jg_n1_1.getGuid(), JGroup.class);
		jg_n1_2 = client.callJsonRead("/api/group/" + jg_n1_2.getGuid(), JGroup.class);
		jg_n2 = client.callJsonRead("/api/group/" + jg_n2.getGuid(), JGroup.class);
		jg_n2_1 = client.callJsonRead("/api/group/" + jg_n2_1.getGuid(), JGroup.class);
		jg_n2_2 = client.callJsonRead("/api/group/" + jg_n2_2.getGuid(), JGroup.class);
		
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
		
		jg_r = client.callJsonCreat("/api/group", jg_r, JGroup.class);
		jg_n1 = client.callJsonCreat("/api/group", jg_n1, JGroup.class);
		jg_n2 = client.callJsonCreat("/api/group", jg_n2, JGroup.class);
		jg_n1_1 = client.callJsonCreat("/api/group", jg_n1_1, JGroup.class);
		jg_n1_2 = client.callJsonCreat("/api/group", jg_n1_2, JGroup.class);
		jg_n2_1 = client.callJsonCreat("/api/group", jg_n2_1, JGroup.class);
		jg_n2_2 = client.callJsonCreat("/api/group", jg_n2_2, JGroup.class);
		
		
		
		jg_r.getChildGroups().add(jg_n1.getJBaseLight());
		jg_r.getChildGroups().add(jg_n2.getJBaseLight());
		jg_r = client.callJsonUpdate("/api/group", jg_r, JGroup.class);
		
		
		reload(client);
		
		jg_n1.getChildGroups().add(jg_n1_1.getJBaseLight());
		jg_n1.getChildGroups().add(jg_n1_2.getJBaseLight());
		jg_n1 = client.callJsonUpdate("/api/group", jg_n1, JGroup.class);

		jg_n2.getChildGroups().add(jg_n2_1.getJBaseLight());
		jg_n2.getChildGroups().add(jg_n2_2.getJBaseLight());
		jg_n2 = client.callJsonUpdate("/api/group", jg_n2, JGroup.class);

		reload(client);
	}
	
	
}
