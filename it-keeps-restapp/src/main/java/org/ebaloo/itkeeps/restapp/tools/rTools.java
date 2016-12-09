
package org.ebaloo.itkeeps.restapp.tools;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ebaloo.itkeeps.ApiPath;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;


@Path("/")
public class rTools {

	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.TOOLS_PING)
    @Timed
    public Response ping() {
	   		return Response.ok(new Pong()).build();
    }

    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static final class Pong {
    	private static final String RESULT = "pong";

    	@JsonProperty("result")
    	private final Object o1 = RESULT;
    	
    	@JsonProperty("now")
    	private final Object o2 = Instant.now().toString();
    	
    	private Pong() {
    	}
    }
    
    
	private final ObjectMapper STATS_MAPPER = new ObjectMapper().registerModule(
            new MetricsModule(TimeUnit.MINUTES, TimeUnit.MILLISECONDS, false, MetricFilter.ALL));
	
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @PermitAll
    @Path(ApiPath.TOOLS_STATS)
    @Timed
    public Response stats() {
    	
		ObjectNode root = STATS_MAPPER.createObjectNode();

		for(Entry<String, Metric> m : MetricsFactory.getMetricRegistry().getMetrics().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toSet())    ) {
			root.set(m.getKey(), STATS_MAPPER.valueToTree(m.getValue()));
		}
		
		List<String> tag = new ArrayList<>();
		
		try {
			tag.add(InetAddress.getLocalHost().getHostName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tag.add("IT-Keeps");
			
		root.set("tags", STATS_MAPPER.valueToTree(tag));
		
		
	   		return Response.ok(root).build();
    }
}