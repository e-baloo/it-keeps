import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class PathTest {

	public static jPath jg_r1 = new jPath();
	public static jPath jg_r2 = new jPath();
	public static jPath jg_r1_n1 = new jPath();
	public static jPath jg_r1_n1_1 = new jPath();
	public static jPath jg_r1_n1_2 = new jPath();
	public static jPath jg_r1_n2 = new jPath();
	public static jPath jg_r2_n1 = new jPath();
	public static jPath jg_r2_n1_1 = new jPath();
	public static jPath jg_r2_n1_2 = new jPath();

	
	
	public static final void reload(ItkeepsHttpClient client) {
		
		jg_r1 = client.callJsonRead("/api/path/" + jg_r1.getGuid(), jPath.class);
		jg_r2 = client.callJsonRead("/api/path/" + jg_r2.getGuid(), jPath.class);
		jg_r1_n1 = client.callJsonRead("/api/path/" + jg_r1_n1.getGuid(), jPath.class);
		jg_r1_n2 = client.callJsonRead("/api/path/" + jg_r1_n2.getGuid(), jPath.class);
		jg_r1_n1_1 = client.callJsonRead("/api/path/" + jg_r1_n1_1.getGuid(), jPath.class);
		jg_r1_n1_2 = client.callJsonRead("/api/path/" + jg_r1_n1_2.getGuid(), jPath.class);
		jg_r2_n1 = client.callJsonRead("/api/path/" + jg_r2_n1.getGuid(), jPath.class);
		jg_r2_n1_1 = client.callJsonRead("/api/path/" + jg_r2_n1_1.getGuid(), jPath.class);
		jg_r2_n1_2 = client.callJsonRead("/api/path/" + jg_r2_n1_2.getGuid(), jPath.class);
		
	}	
	
	public static final void run(ItkeepsHttpClient client) {
		
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
		
		jg_r1 = client.callJsonCreat("/api/path", jg_r1, jPath.class);
		jg_r2 = client.callJsonCreat("/api/path", jg_r2, jPath.class);
		jg_r1_n1 = client.callJsonCreat("/api/path", jg_r1_n1, jPath.class);
		jg_r1_n2 = client.callJsonCreat("/api/path", jg_r1_n2, jPath.class);
		jg_r1_n1_1 = client.callJsonCreat("/api/path", jg_r1_n1_1, jPath.class);
		jg_r1_n1_2 = client.callJsonCreat("/api/path", jg_r1_n1_2, jPath.class);
		jg_r2_n1 = client.callJsonCreat("/api/path", jg_r2_n1, jPath.class);
		jg_r2_n1_1 = client.callJsonCreat("/api/path", jg_r2_n1_1, jPath.class);
		jg_r2_n1_2 = client.callJsonCreat("/api/path", jg_r2_n1_2, jPath.class);
		
		
		jg_r1_n1.getChilds().add(jg_r1_n1_1.getJBaseLight());
		jg_r1_n1.getChilds().add(jg_r1_n1_2.getJBaseLight());
		jg_r1_n1 = client.callJsonUpdate("/api/path", jg_r1_n1, jPath.class);
		
		jg_r2_n1.getChilds().add(jg_r2_n1_1.getJBaseLight());
		jg_r2_n1.getChilds().add(jg_r2_n1_2.getJBaseLight());
		jg_r2_n1 = client.callJsonUpdate("/api/path", jg_r2_n1, jPath.class);
		
		jg_r1.getChilds().add(jg_r1_n1.getJBaseLight());
		jg_r1.getChilds().add(jg_r1_n2.getJBaseLight());
		jg_r1 = client.callJsonUpdate("/api/path", jg_r1, jPath.class);
		
		jg_r2.getChilds().add(jg_r2_n1.getJBaseLight());
		jg_r2 = client.callJsonUpdate("/api/path", jg_r2, jPath.class);
		
		reload(client);
		
	}
	
	
}
