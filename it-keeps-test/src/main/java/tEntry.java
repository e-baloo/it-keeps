import java.util.ArrayList;
import java.util.HashMap;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;

public class tEntry {

	public static jEntry entry1 = new jEntry();
	public static jEntry entry2 = new jEntry();
	public static jEntry entry3 = new jEntry();
	public static jEntry entry4 = new jEntry();

	
	
	public static void reload(ItkeepsHttpClient client) {
		
		entry1 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + ParameterEncoder.encoding(entry1.getId()), jEntry.class);
		entry2 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + ParameterEncoder.encoding(entry2.getId()), jEntry.class);
		entry3 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + ParameterEncoder.encoding(entry3.getId()), jEntry.class);
		entry4 = client.callJsonRead(ApiPath.API_ENTRY_GET_ID + ParameterEncoder.encoding(entry4.getId()), jEntry.class);
		
	}	
	
	public static void run(ItkeepsHttpClient client) {
		
		/* Set Name */
		
		entry1.setName("entry 1");
		ArrayList<String> list1 = new ArrayList<>();
		list1.add("xxxxxxxxxxxxxx");
		list1.add("yyyyyyyyyyyyyy");
		entry1.setOtherName(list1);

		entry2.setName("entry 2");
		
		entry3.setName("entry 3");
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("kkkkkkkkkkkkkkk", "vvvvvvvvvvvvvvvvvv");
		entry3.setExternalRef(map1);
		
		entry4.setName("entry 4");

		/* Create */
		
		entry1 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry1, jEntry.class);
		entry2 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry2, jEntry.class);
		entry3 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry3, jEntry.class);
		entry4 = client.callJsonCreat(ApiPath.API_ENTRY_CREATE, entry4, jEntry.class);


        entry1.setPath(tPath.jg_r1_n1_1.getLight());
        entry2.setPath(tPath.jg_r1_n1_2.getLight());
        entry3.setPath(tPath.jg_r2_n1_1.getLight());
        entry4.setPath(tPath.jg_r2_n1.getLight());


		entry1 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry1, jEntry.class);
		entry2 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry2, jEntry.class);
		entry3 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry3, jEntry.class);
		entry4 = client.callJsonUpdate(ApiPath.API_ENTRY_UPDATE, entry4, jEntry.class);
		
		reload(client);
		tPath.reload(client);
		
	}
	
	
}
