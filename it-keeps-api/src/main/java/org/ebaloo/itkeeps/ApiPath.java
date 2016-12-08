package org.ebaloo.itkeeps;

public final class ApiPath {
	
	private static final String ALL = "all/";
	private static final String ID = "id/";
	private static final String UPDATE = "update/";
	private static final String CREATE = "create/";
	private static final String DELETE = "delete/";
	
	private static final String TOOLS = "";
	public  static final String TOOLS_PING = TOOLS + "_ping/";
	public  static final String TOOLS_STATS = TOOLS + "_stats/";

	
	private static final String IMAGE = "img/";
	public  static final String IMAGE_GET_ID = IMAGE + ID;
	

	private static final String API = "api/";
	private static final String API_USER = API + "user/";
	public  static final String API_USER_GET_ALL = API_USER + ALL;
	public  static final String API_USER_GET_ID = API_USER + ID;
	public  static final String API_USER_UPDATE = API_USER + UPDATE;
	public  static final String API_USER_CREATE = API_USER + CREATE;
	public  static final String API_USER_DELETE = API_USER + DELETE;

	private static final String API_GROUP = API + "group/";
	public  static final String API_GROUP_GET_ALL = API_GROUP + ALL;
	public  static final String API_GROUP_GET_ID = API_GROUP + ID;
	public  static final String API_GROUP_UPDATE = API_GROUP + UPDATE;
	public  static final String API_GROUP_CREATE = API_GROUP + CREATE;
	public  static final String API_GROUP_DELETE = API_GROUP + DELETE;

	private static final String API_ENUM = API + "enum/";
	public  static final String API_ENUM_ACL = API_ENUM + "acl/";
	public  static final String API_ENUM_AUTH = API_ENUM + "auth/";
	
	
	private static final String API_ACL = API + "acl/";
	public  static final String API_ACL_GET_ALL = API_ACL + ALL;
	public  static final String API_ACL_GET_ID = API_ACL + ID;
	public  static final String API_ACL_UPDATE = API_ACL + UPDATE;
	public  static final String API_ACL_CREATE = API_ACL + CREATE;
	public  static final String API_ACL_DELETE = API_ACL + DELETE;

	
	private static final String API_ACL_GRP = API + "aclgroup/";
	public  static final String API_ACL_GRP_GET_ALL = API_ACL_GRP + ALL;
	public  static final String API_ACL_GRP_GET_ID = API_ACL_GRP + ID;
	public  static final String API_ACL_GRP_UPDATE = API_ACL_GRP + UPDATE;
	public  static final String API_ACL_GRP_CREATE = API_ACL_GRP + CREATE;
	public  static final String API_ACL_GRP_DELETE = API_ACL_GRP + DELETE;

	private static final String API_PATH = API + "path/";
	public  static final String API_PATH_GET_ALL = API_PATH + ALL;
	public  static final String API_PATH_GET_ID = API_PATH + ID;
	public  static final String API_PATH_UPDATE = API_PATH + UPDATE;
	public  static final String API_PATH_CREATE = API_PATH + CREATE;
	public  static final String API_PATH_DELETE = API_PATH + DELETE;

	private static final String API_ENTRY = API + "entry/";
	public  static final String API_ENTRY_GET_ALL = API_ENTRY + ALL;
	public  static final String API_ENTRY_GET_ID = API_ENTRY + ID;
	public  static final String API_ENTRY_UPDATE = API_ENTRY + UPDATE;
	public  static final String API_ENTRY_CREATE = API_ENTRY + CREATE;
	public  static final String API_ENTRY_DELETE = API_ENTRY + DELETE;

	private static final String API_ENTRY_ENC = API + "entryenc/";
	public  static final String API_ENTRY_ENC_GET = API_ENTRY_ENC;
	public  static final String API_ENTRY_ENC_UPDATE = API_ENTRY_ENC;


	private static final String API_IMAGE = API + "image/";
	public  static final String API_IMAGE_GET_ALL = API_IMAGE + ALL;
	public  static final String API_IMAGE_GET_ID = API_IMAGE + ID;
	public  static final String API_IMAGE_UPDATE = API_IMAGE + UPDATE;
	public  static final String API_IMAGE_CREATE = API_IMAGE + CREATE;
	public  static final String API_IMAGE_DELETE = API_IMAGE + DELETE;

	private static final String API_CRED = API + "cred/";
	public static final String API_CRED_GET_ALL = API_CRED + ALL;
	public static final String API_CRED_GET_ID = API_CRED + ID;
	public static final String API_CRED_DELETE_ID = API_CRED + DELETE;
	public  static final String API_CRED_CREATE = API_CRED + CREATE;
	public  static final String API_CRED_CREATE_ID = API_CRED + CREATE + ID;
	
	private static final String AUTH = "auth/";
	public static final String AUTH_LOGIN = AUTH + "login/";
	public static final String AUTH_RENEW = AUTH + "renew/";
	public static final String AUTH_CHECK = AUTH + "check/";
	
	
	

}
