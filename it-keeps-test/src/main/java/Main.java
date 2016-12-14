
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.api.enumeration.enAuthenticationType;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jCredential;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.api.model.jObject;
import org.ebaloo.itkeeps.api.model.jToken;
import org.ebaloo.itkeeps.api.model.jUser;
import org.ebaloo.itkeeps.commons.ConfigFactory;
import org.ebaloo.itkeeps.commons.LogFactory;
import org.ebaloo.itkeeps.core.database.DatabaseFactory;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.vertex.fEntry;
import org.ebaloo.itkeeps.core.domain.vertex.fPath;
import org.ebaloo.itkeeps.httpclient.ItkeepsHttpClient;
import org.ebaloo.itkeeps.httpclient.ParameterEncoder;
import org.ebaloo.itkeeps.restapp.ApplicationConfig;
import org.ebaloo.itkeeps.tools.Base64;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class Main {

	private static String srvUriPort = "8080";
	private static URI srvBaseUri = null;

	public static void main(String[] args) throws JsonProcessingException {

		try {

			startHttpServer();

			// TODO Auto-generated method stub

			ConfigFactory.init();
			LogFactory.init(Main.class);

			LogFactory.getMain().info("START");

			ItkeepsHttpClient guestClient = new ItkeepsHttpClient();

			{

				{
					JsonNode jn = guestClient.callJsonRead(ApiPath.AUTH_TYPE_ENUM);
					LogFactory.getMain().debug(jObject.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jn));
				}

				{
					JsonNode jn = guestClient.callJsonRead(ApiPath.TOOLS_PING);
					LogFactory.getMain().debug(jObject.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jn));
				}

				{
					JsonNode jn = guestClient.callJsonRead(ApiPath.TOOLS_STATS);
					LogFactory.getMain().debug(jObject.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jn));
				}

			}

			jCredential cred = new jCredential();
			cred.setName("marc");
			cred.setPassword64(Base64.encodeAsString(cred.getName()));
			cred.setAuthenticationType(enAuthenticationType.BASIC);
			cred.setUserName("Marc DONVAL");

			ItkeepsHttpClient clientRoot = new ItkeepsHttpClient(cred);

			{
				jUser rootUser = clientRoot.callJsonRead(ApiPath.API_USER_GET_CRED_ID + cred.getName(), jUser.class);
				LogFactory.getMain()
						.debug(jObject.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(rootUser));

				jToken token = clientRoot.callJsonRead(ApiPath.AUTH_RENEW, jToken.class);
				LogFactory.getMain().debug(jObject.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(token));
			}

			tAclGroup.run(clientRoot);

			tGroup.run(clientRoot);
			tPath.run(clientRoot);
			tUser.run(clientRoot);
			tEntry.run(clientRoot);
			tTest.run(clientRoot);

			/*
			 * for(OrientVertex ov : GraphFactory.command(null, "SELECT FROM V"
			 * )) { System.out.println(ov.getRecord().toJSON()); }
			 *
			 */

			LogFactory.getMain().info(">>-------------------------------------------------------");

			List<jBaseLight> list = fPath.readAll(tUser.admin1.getId());
			LogFactory.getMain().info(String.format("%s - %s", list.size(), list));

			List<jBaseLight> list2 = fEntry.readAll(tUser.admin1.getId());
			LogFactory.getMain().info(String.format("%s - %s", list2.size(), list2));

			LogFactory.getMain().info("---------------------------------------------------------");

			jEncryptedEntry jen = new jEncryptedEntry();
			jen.setName("test1 password");
			jen.setMediaType("text");
			jen.setData64(Base64.encodeAsString("Marc DONVAL"));

			LogFactory.getMain().info("xxxx");

            fEntry.updateEncrypted(tUser.admin1.getId(), list2.get(0).getId(), jen);
            LogFactory.getMain().info("---------------------------------------------------------");

			LogFactory.getMain().info("=> " + Base64
                    .decodeAsString(fEntry.readEncrypted(tUser.admin1.getId(), list2.get(0).getId()).getData64()));

			LogFactory.getMain().info("---------------------------------------------------------");

			for (OrientVertex ov : GraphFactory.command(null, "SELECT FROM V")) {
				LogFactory.getMain().info(ov.getRecord().toJSON());
			}

			LogFactory.getMain().info("---------------------------------------------------------");

			{

				jCredential c = new jCredential();
				c.setAuthenticationType(enAuthenticationType.BASIC);
				c.setName(tUser.credUser1.getName());
				c.setPassword64(Base64.encodeAsString(c.getName()));

				ItkeepsHttpClient clientUser1 = new ItkeepsHttpClient(c);

				JsonNode j = clientUser1.callJsonRead(ApiPath.API_CRED_GET_ALL);

				String rid = j.iterator().next().get("id").asText();

				jCredential jc = clientUser1.callJsonRead(ApiPath.API_CRED_GET_ID + ParameterEncoder.encoding(rid),
						jCredential.class);
				LogFactory.getMain().info(jc.toString());

			}

			LogFactory.getMain().info("---------------------------------------------------------");

			{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Enter String");
				br.readLine();
			}


			LogFactory.getMain().info("END");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private static void startHttpServer() {

		ConfigFactory.init();

		LogFactory.init(Main.class);
		LogFactory.getMain().info("E) LogFactory.init()");

		DatabaseFactory.init();
		LogFactory.getMain().info("E) DatabaseFactory.init()");

		// new Image("icon:default", "image/png",
		// "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");

		srvUriPort = ConfigFactory.getString("http.port", srvUriPort);

		srvBaseUri = URI.create("http://localhost:" + srvUriPort + "/");
		ConfigFactory.getManiLogger().trace(srvBaseUri.toString());

		ConfigFactory.getManiLogger().info("Application Starting...");

		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(srvBaseUri, new ApplicationConfig());
		Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

		ConfigFactory.getManiLogger().info("Application Started!");
	}

}
