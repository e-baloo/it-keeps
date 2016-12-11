

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.enumeration.enAclAdmin;
import org.ebaloo.itkeeps.api.enumeration.enAclData;
import org.ebaloo.itkeeps.api.model.jAclGroup;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.notraverse.eAclNoTraverse;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


/**
 * 
 *
 */
@DatabaseVertrex()
final class vAclGroup extends vBase {

	vAclGroup() {
		super();
	}
	
	/*
	 * CHILD
	 */

    vAclGroup(final jAclGroup j) {
        super(j);


        try {
            this.update(j);
        } catch (Exception e) {
            this.delete();
            throw e;
        }
    }

    final jBaseLight getChild() {
		vAclGroup child = this.getEdge(vAclGroup.class, DirectionType.CHILD, false, eAclNoTraverse.class);
		return child == null ? null : getJBaseLight(child);
	}

	
	/*
	 * PARENTS
	 */
	
	final void setChild(final jBaseLight group) {
		vAclGroup child = get(this.getGraph(), vAclGroup.class, group, false);
		setEdges(this.getGraph(), vAclGroup.class, this, vAclGroup.class, child, DirectionType.CHILD, eAclNoTraverse.class, false);
	}
	
	final List<jBaseLight> getParents() {
		return this.getEdges(
                vAclGroup.class,
                DirectionType.PARENT,
                false,
                eAclNoTraverse.class).stream().map(vBase::getJBaseLight).collect(Collectors.toList());
	}

	
	/*
     * ACL ADMIN
	 */
	
	final void setParents(List<jBaseLight> list) {

        if (list == null)
            list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(
				graph,
				vAclGroup.class,
				this,
				vAclGroup.class,
				list.stream().map(e -> get(graph, vAclGroup.class, e, false)).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}
	
	final List<enAclAdmin> getAclAdmin() {
		return this.getEdges(
                vAclAdmin.class,
                DirectionType.PARENT,
                true,
                eAclNoTraverse.class)
					.stream().map(e -> enAclAdmin.valueOf(e.getName())).collect(Collectors.toList());
	}

	/*
	 * ACL DATA
	 */
	
	final void setAclAdmin(List<enAclAdmin> list) {
		if(list == null)
			list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(
				graph,
                vAclGroup.class,
                this,
                vAclAdmin.class,
				list.stream().map(e -> vAclAdmin.get(graph, vAclAdmin.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}
	
	final List<enAclData> getAclData() {
		return this.getEdges(
                vAclData.class,
                DirectionType.PARENT,
                true,
                eAclNoTraverse.class)
					.stream().map(e -> enAclData.valueOf(e.getName())).collect(Collectors.toList());
	}


    // API

    final void setAclData(List<enAclData> list) {
		if(list == null)
			list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(
				graph,
                vAclGroup.class,
                this,
				vAclData.class,
				list.stream().map(e -> vAclData.get(graph, vAclData.class, e.name())).collect(Collectors.toList()),
				DirectionType.PARENT,
				eAclNoTraverse.class,
				false);
	}

	final jAclGroup read() {
		
		jAclGroup j = new jAclGroup();
		
		this.readBase(j);
		
		j.setAclAdmin(this.getAclAdmin());
		j.setAclData(this.getAclData());
		j.setChild(this.getChild());
		j.setParents(this.getParents());
		
		return j;
	}
	
/*
    public static final jAclGroup create(Rid requesteurRid, jAclGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		vAclGroup aclGroup = new vAclGroup(j);
		
		return vAclGroup.read(requesteurRid, aclGroup.getId());
	}
	*/


	/*
	public static final jAclGroup delete(Rid requesteurRid, Rid guid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		vAclGroup aclGroup = vAclGroup.get(null, vAclGroup.class, guid, false);
		jAclGroup j = aclGroup.read();
		
		aclGroup.delete();
		
		return j;
	}
	*/

	/*
	public static final jAclGroup read(Rid requesteurRid, Rid guid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		vAclGroup aclGroup = vAclGroup.get(null, vAclGroup.class, guid, false);
		
		return aclGroup.read();
	}
	*/

	/*
    public static final jAclGroup update(Rid requesteurRid,  jAclGroup j) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		vAclGroup aclGroup = vAclGroup.get(null, vAclGroup.class, j.getId(), false);

		aclGroup.checkVersion(j);
		aclGroup.update(j);
		
		return vAclGroup.read(requesteurRid, j.getId());
	}
	*/
	
	final void update(jAclGroup j) {
		this.setAclAdmin(j.getAclAdmin());
		this.setAclData(j.getAclData());
		this.setParents(j.getParents());
		this.setChild(j.getChild());
	}

	/*
	public static final List<jAclGroup> readAll(Rid requesteurRid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, Rid.NULL);
		if(!sAcl.isRoleRoot())
			throw ExceptionPermission.NOT_ROOT;

		List<jAclGroup> list = vAclGroup.getAllBase(null, vAclGroup.class, false).stream().map(e -> e.read()).collect(Collectors.toList());
		
		return list;
	}
	*/

}



