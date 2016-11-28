

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jPath;
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
public final class fPath {


	public static final jPath create(Rid requesteurRid, jPath j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		// TODO Security
		
		vPath path = new vPath(j);
		
		return fPath.read(requesteurRid, path.getRid());
	}


	public static final jPath delete(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, rid);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		if(!sAcl.isDataPathCreate())
			throw ExceptionPermission.DENY;

		vPath path = vBaseAbstract.get(null, vPath.class, rid, false);
		jPath j = path.read();
		
		path.delete();
		
		return j;
	}

	public static final jPath read(Rid requesteurRid, Rid rid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, rid);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		if(!sAcl.isDataPathRead())
			throw ExceptionPermission.DENY;
			
		
		// TODO Security

		vPath path = vBaseAbstract.get(null, vPath.class, rid, false);
		
		return path.read();
	}

	public static final jPath update(Rid requesteurRid,  jPath j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, j.getRid());
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;
		if(!sAcl.isDataPathUpdate())
			throw ExceptionPermission.DENY;
		
		// TODO Security

		vPath path = vBaseAbstract.get(null, vPath.class, j.getRid(), false);

		path.checkVersion(j);
		path.update(j);
		
		return fPath.read(requesteurRid, j.getRid());
	}
	
	


	public static final List<jBaseLight> readAll(Rid requesteurRid) {
		
		long timed = System.currentTimeMillis();
		
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleUser())
			throw ExceptionPermission.NOT_USER;

		
		List<jBaseLight> list = new ArrayList<jBaseLight>();
		if(sAcl.isRoleRoot()) {
			list = vBase.getAllBase(null, vPath.class, false).stream().map(e -> e.read().getJBaseLight()).collect(Collectors.toList());
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
			sb.append(vPath.class.getSimpleName());
			sb.append("'");
			
			//    String cmd = "SELECT FROM (TRAVERSE IN('eAclRelation') FROM (SELECT FROM (TRAVERSE OUT('eAclRelation') FROM " + requesteurRid.get() + ") WHERE @class = 'vAcl')) WHERE @class = 'vPath'";
			
			OrientBaseGraph graph = GraphFactory.getOrientBaseGraph();
			
			for(OrientVertex ov : GraphFactory.command(graph, sb.toString())) {
				Rid rid = RID.get(ov);
				SecurityAcl sAclOv = SecurityFactory.getSecurityAcl(requesteurRid, rid);
				if(sAclOv.isDataPathRead()) {
					vPath path = vBaseAbstract.get(graph, vPath.class, rid, false);
					list.add(path.read().getJBaseLight());
				}
			}
		}
		
		timed = System.currentTimeMillis() - timed;
		LogFactory.getMain().debug(String.format("fPath.readAll() executed in %s", timed));
		
		
		return list;
	}
}


/*
vUser -> Acls
SELECT FROM (TRAVERSE OUT('eAclRelation') FROM #107:0) WHERE @class = 'vAcl'

Acls -> vPath
SELECT FROM (TRAVERSE IN('eAclRelation') FROM (SELECT FROM (TRAVERSE OUT('eAclRelation') FROM #107:0) WHERE @class = 'vAcl')) WHERE @class = 'vPath'


SELECT FROM (TRAVERSE OUT('eAclNoTraverse') FROM #73:0) WHERE @class INSTANCEOF 'vAclEnum'


SELECT FROM (TRAVERSE OUT('eAclNoTraverse') FROM (SELECT FROM (TRAVERSE OUT('eAclRelation') FROM #107:0) WHERE @rid IN (SELECT @rid FROM (TRAVERSE OUT('eAclRelation') FROM #103:1) WHERE @class = 'vAcl') AND @class = 'vAcl')) WHERE @class INSTANCEOF 'vAclEnum' 
AND (
      	(@rid IN [#137:0, #141:0]) //OWNER or ROOT
      OR 
      	( 
          	(@rid IN [#133:1, #135:1, #136:1]) // PATH READ / UPDATE / CREATE
          AND 
            NOT (@rid IN [#133:0, #134:0]) // PATH DENY / DEMY
        )
)
*/



