

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.api.model.JGroup;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.InGroup;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


/**
 * 
 *
 */
@DatabaseVertrex()
@ModelClassAnnotation()
public class Group extends BaseStandard {

	protected Group() {
		super();
	}
	
	protected Group(String name) {
		super(name);
		
	}

	
	
	/*
	 * PARENT GROUP
	 */
	
	public Group getParent() {
		return this.getEdgeByClassesNames(Group.class, DirectionType.CHILD, false, InGroup.class);
	}
	
	public void setParent(final Group group) {
		setEdges(this.getGraph(), Group.class, this, group, DirectionType.CHILD, InGroup.class, false);
	}

	private void setParent(final JBaseLight group) {
		this.setParent(get(this.getGraph(), Group.class, group, false));
	}
	
	
	/*
	 * CHILD GROUP
	 */
	
	public List<Group> getChilds() {
		return this.getEdgesByClassesNames(Group.class, DirectionType.PARENT, false, InGroup.class);
	}
	
	public void setChilds(List<Group> list) {
		setEdges(this.getGraph(), Group.class, this, list, DirectionType.PARENT, InGroup.class, false);
	}

	private void setChildsJBL(List<JBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<JBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setChilds(list.stream().map(e -> get(graph, Group.class, e, false)).collect(Collectors.toList())); 
	}

	
	
	

	
	
	/*
	 * ALL GROUPE IN
	 */
	
	/*
	public static final String ALL_GROUPE_CHILD = PROPERTY.ALL_GROUPE_CHILD;
	
	@SuppressWarnings("unchecked")
	@ModelPropertyAnnotation(name=ALL_GROUPE_CHILD, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = ALL_GROUPE_CHILD)
	public List<Group> getAllGroupChild() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT FROM ");
		sb.append("(TRAVERSE IN('"+ AuthRelation.class.getSimpleName()  +"') FROM " + this.getORID() + ") "); 
		sb.append("WHERE ");
		BaseUtils.WhereClause.classIsntanceOf(Group.class, false, sb);
		sb.append("AND ");
		sb.append("(@rid <> " + this.getORID() + ")");
		
		return (List<Group>) (List<?>) getBaseQuery().commandBaseAbstract(sb.toString());

	}
	*/
	
	/*
	 * ALL GROUPE OUT
	 */
	
	/*
	public static final String ALL_GROUPE_PARENT = PROPERTY.ALL_GROUPE_PARENT;
	
	@SuppressWarnings("unchecked")
	@ModelPropertyAnnotation(name=ALL_GROUPE_PARENT, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = ALL_GROUPE_PARENT)
	public List<Group> getAllGroupParent() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT FROM ");
		sb.append("(TRAVERSE OUT('"+ AuthRelation.class.getSimpleName()  +"') FROM " + this.getORID() + ") "); 
		sb.append("WHERE ");
		BaseUtils.WhereClause.classIsntanceOf(Group.class, false, sb);
		sb.append("AND ");
		sb.append("(@rid <> " + this.getORID() + ")");
		
		return (List<Group>) (List<?>) getBaseQuery().commandBaseAbstract(sb.toString());

	}
	*/

	/*
	 * 
	 * USER IN
	 * 
	 */
	
	/*
	public static final String USERS = PROPERTY.USERS;
	
	@ModelPropertyAnnotation(name=USERS, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = USERS)
	public List<AuthUser> getUsers() {
		return this.getEdgesByClassesNames(AuthUser.class, RelationType.CHILD, false, TraverseInGroup.class);
	}

	@ModelPropertyAnnotation(name=USERS, type=TypeProperty.ADD)
	public void addUser(String user) {
		this.addUser((AuthUser) this.getBaseAbstract(user));
	}

	public void addUser(final AuthUser user) {
		this.addEdge(user, RelationType.CHILD, TraverseInGroup.class);
	}

	@ModelPropertyAnnotation(name=USERS, type=TypeProperty.REMOVE)
	public void removeUser(String user) {
		this.removeUser((AuthUser) this.getBaseAbstract(user));
	}
	
	public void removeUser(final AuthUser user) {
		this.removeEdge(user, RelationType.CHILD, TraverseInGroup.class);
	}
	*/

	/*
	 * ALL_USER IN
	 */
	
	/*
	public static final String ALL_USERS = PROPERTY.ALL_USERS;
	
	@SuppressWarnings("unchecked")
	@ModelPropertyAnnotation(name=ALL_USERS, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = ALL_USERS)
	public List<AuthUser> getAllUser() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT FROM ");
		sb.append("(TRAVERSE IN('"+ AuthRelation.class.getSimpleName()  +"') FROM " + this.getORID() + ") "); 
		sb.append("WHERE ");
		BaseUtils.WhereClause.classIsntanceOf(AuthUser.class, false, sb);
		
		return (List<AuthUser>) (List<?>) getBaseQuery().commandBaseAbstract(sb.toString());
		
	}
	*/

	
	/*
	 * 
	 */
	/*
	public static final String ACCESS_RIGHT = PROPERTY.ACCESS_RIGHT;

	
	@ModelPropertyAnnotation(name=ACCESS_RIGHT, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = ACCESS_RIGHT)
	public List<AuthAccessRight> getAccessRight() {
		return super.getAccessRight();
	}

	
	@ModelPropertyAnnotation(name=ACCESS_RIGHT, type=TypeProperty.ADD)
	public void addAccessRight(String accessRight) {
		this.addAccessRight((AuthAccessRight) this.getBaseAbstract(accessRight));
		// TODO Auto-generated method stub
		
	}

	@Override
	@ModelPropertyAnnotation(name=ACCESS_RIGHT, type=TypeProperty.REMOVE)
	public void removeAccessRight(String accessRight) {
		this.removeAccessRight((AuthAccessRight) this.getBaseAbstract(accessRight));
	}

	
	
	public static final String ALL_ACCESS_RIGHT = PROPERTY.ALL_ACCESS_RIGHT;

	@ModelPropertyAnnotation(name=ALL_ACCESS_RIGHT, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = ALL_ACCESS_RIGHT)
	public List<AuthAccessRight> getAllAccessRight() {
		return super.getAllAccessRight();
	}
*/
	
	/*
	 * SCOPE
	 */
/*
	public static final String ALL_SCOPE = PROPERTY.ALL_SCOPE;

	@ModelPropertyAnnotation(name=ALL_SCOPE, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = ALL_SCOPE)
	public List<AuthScope> getAllScope() {
		return super.getAllScope();
	}
	

	public static final String SCOPE = PROPERTY.SCOPE;

	@ModelPropertyAnnotation(name=SCOPE, type=TypeProperty.GET, instanceOf = false) @I18nAnnotation(bundle = CONST.I18N.CLASS + CLASS.AUTH, label = SCOPE)
	public List<AuthScope> getScope() {
		return super.getScope();
	}

	@Override
	@ModelPropertyAnnotation(name=SCOPE, type=TypeProperty.ADD)
	public void addScope(String scope) {
		this.addScope((AuthScope) this.getBaseAbstract(scope));
	}

	@Override
	@ModelPropertyAnnotation(name=SCOPE, type=TypeProperty.REMOVE)
	public void removeScope(String scope) {
		this.removeScope((AuthScope) this.getBaseAbstract(scope));
	}
	*/
	
	// API
	
	public Group(final JGroup j) {
		this(j, true);
	}
	
	protected Group(final JGroup j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();
		
		this.setParent(j.getParent());
		this.setChildsJBL(j.getChilds());

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public <T extends JBase> T read(T j, Guid requesteurGuid) {
		
		if(j == null)
			j = (T) new JGroup();
		
		if(!(j instanceof JGroup))
			throw new RuntimeException("TODO"); //TODO
		
		super.read(j, requesteurGuid);

		JGroup jgroup = (JGroup) j;
		
		jgroup.setParent(getJBaseLight(this.getParent()));
		jgroup.setChilds(this.getChilds().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	@Override
	public <T extends JBase> T update(T j, Guid requesteurGuid) {
		
		if(!(j instanceof JGroup))
			throw new RuntimeException("TODO"); //TODO

		super.update(j, requesteurGuid);
		
		User requesterUser = User.get(this.getGraph(), User.class, requesteurGuid, false);

		switch(requesterUser.getRole()) {

		case ROOT:
			// -> Ok is root
			break;

		case ADMIN:
			//TODO Check it requesterUser have the right to update this
			break;
			
		case USER:
		case GUEST:
		default:
			throw new RuntimeException("TODO"); //TODO
		}
		

		JGroup jgroup = (JGroup) j;
		
		
		if(jgroup.isPresentParent())
			this.setParent(jgroup.getParent());

		if(jgroup.isPresentChilds())
			this.setChildsJBL(jgroup.getChilds());
	
		
		return read(null, requesteurGuid);
		
	}
}



