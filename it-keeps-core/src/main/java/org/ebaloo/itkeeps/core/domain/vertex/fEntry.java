

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.commons.LogFactory;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.RID;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;



/**
 * 
 *
 */
@DatabaseVertrex()
public final class fEntry {


	public static final jEntry create(Rid requesteurRid, jEntry j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		// TODO Security
		
		vEntry entry = new vEntry(j);
		
		return fEntry.read(requesteurRid, entry.getRid());
	}


	public static final jEntry delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, rid);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		// TODO Security
		
		vEntry entry = vBaseAbstract.get(null, vEntry.class, rid, false);
		jEntry j = entry.read();
		
		entry.delete();
		
		return j;
	}

	public static final jEntry read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, rid);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vEntry entry = vBaseAbstract.get(null, vEntry.class, rid, false);
		
		return entry.read();
	}

	public static final jEncryptedEntry readEncrypted(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, rid);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vEntry entry = vBaseAbstract.get(null, vEntry.class, rid, false);
		
		return entry.readEncrypted(sAcl);
	}

	public static final jEntry update(Rid requesteurRid,  jEntry j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, j.getRid());
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vEntry entry = vBaseAbstract.get(null, vEntry.class, j.getRid(), false);

		entry.checkVersion(j);
		entry.update(j);
		
		return fEntry.read(requesteurRid, j.getRid());
	}
	
	public static final void updateEncrypted(Rid requesteurRid, Rid rid, jEncryptedEntry j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, rid);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		
		// TODO Security

		vEntry entry = vBaseAbstract.get(null, vEntry.class, rid, false);
		
		entry.updateEncrypted(sAcl, j);
	}
	


	public static final List<jBaseLight> readAll(Rid requesteurRid) {
		
		long timed = System.currentTimeMillis();
		
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		
		List<jBaseLight> list = new ArrayList<jBaseLight>();
		if(sAcl.isRoleRoot()) {
			list = vBase.getAllBase(null, vEntry.class, false).stream().map(e -> e.read().getJBaseLight()).collect(Collectors.toList());
		} else {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT FROM (TRAVERSE IN('");
			sb.append(eAclRelation.class.getSimpleName());
			sb.append("') FROM (SELECT FROM (TRAVERSE OUT('");
			sb.append(eAclRelation.class.getSimpleName());
			sb.append("') FROM ");
			sb.append(requesteurRid.get());
			sb.append(") WHERE @class = '");
			sb.append(vAcl.class.getSimpleName());
			sb.append("')) WHERE @class = '");
			sb.append(vEntry.class.getSimpleName());
			sb.append("'");
			
			//    String cmd = "SELECT FROM (TRAVERSE IN('eAclRelation') FROM (SELECT FROM (TRAVERSE OUT('eAclRelation') FROM " + requesteurRid.get() + ") WHERE @class = 'vAcl')) WHERE @class = 'vPath'";
			
			OrientBaseGraph graph = GraphFactory.getOrientBaseGraph();
			
			for(OrientVertex ov : GraphFactory.command(graph, sb.toString())) {
				Rid rid = RID.get(ov);
				SecurityAcl sAclOv = SecurityFactory.getSecurityAcl(requesteurRid, rid);
				if(sAclOv.isDataEntryRead()) {
					vEntry entry = vBaseAbstract.get(graph, vEntry.class, rid, false);
					list.add(entry.read().getJBaseLight());
				}
			}
		}
		
		timed = System.currentTimeMillis() - timed;
		LogFactory.getMain().debug(String.format("fEntry.readAll() executed in %s", timed));
		
		
		return list;
	}
	
}



