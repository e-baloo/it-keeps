import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;

public class EntryTest {

	public static jEntry entry1 = new jEntry();
	public static jEntry entry2 = new jEntry();
	public static jEntry entry3 = new jEntry();
	public static jEntry entry4 = new jEntry();

	
	
	public static final void reload(ItkeepsHttpClient client) {
		
		entry1 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + entry1.getGuid(), jEntry.class);
		entry2 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + entry2.getGuid(), jEntry.class);
		entry3 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + entry3.getGuid(), jEntry.class);
		entry4 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + entry4.getGuid(), jEntry.class);
		
	}	
	
	public static final void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		entry1.setName("entry 1");
		entry2.setName("entry 2");
		entry3.setName("entry 3");
		entry4.setName("entry 4");

		/* Create */
		
		entry1 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry1, jEntry.class);
		entry2 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry2, jEntry.class);
		entry3 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry3, jEntry.class);
		entry4 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry4, jEntry.class);
		
		
		entry1.setPath(PathTest.jg_r1_n1_1.getJBaseLight());
		entry2.setPath(PathTest.jg_r1_n1_2.getJBaseLight());
		entry3.setPath(PathTest.jg_r2_n1_1.getJBaseLight());
		entry4.setPath(PathTest.jg_r2_n1.getJBaseLight());


		entry1 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry1, jEntry.class);
		entry2 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry2, jEntry.class);
		entry3 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry3, jEntry.class);
		entry4 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry4, jEntry.class);
		
		reload(client);
		PathTest.reload(client);
		
	}
	
	
}
