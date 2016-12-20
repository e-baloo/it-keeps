package org.ebaloo.itkeeps;

public final class ApiPath {

    private static final String USER = "user/";
    private static final String CRED = "cred/";
    private static final String API = "api/";
    private static final String GROUP = "group/";
    private static final String ENUM = "enum/";
    private static final String ACL = "acl/";
    private static final String AUTH = "auth/";
    private static final String TYPE = "type/";
    private static final String DATA = "data/";
    private static final String OWNER = "owner/";
    private static final String ADMIN = "admin/";
    private static final String ROLE = "role/";
    private static final String LOGIN = "login/";
    private static final String LOGOUT = "logout/";
    private static final String RENEW = "renew/";
    private static final String CHECK = "check/";


    public static final String AUTH_LOGIN = AUTH + LOGIN;
    public static final String AUTH_LOGOUT= AUTH + LOGOUT;
    public static final String AUTH_RENEW = AUTH + RENEW;
    public static final String AUTH_CHECK = AUTH + CHECK;

    public static final String AUTH_TYPE_ENUM = AUTH + TYPE + ENUM;

    private static final String ENTRY_ENC = "entry-enc/";
    private static final String ACL_GRP = "acl-group/";
    private static final String PATH = "path/";
    private static final String ENTRY = "entry/";
    private static final String GET_ID = "get-id/";
    private static final String GET_ALL = "get-all/";
    private static final String UPDATE = "update/";
    private static final String CREATE = "create/";
    private static final String DELETE = "delete/";

    private static final String TOOLS = "";
    public static final String TOOLS_PING = TOOLS + "_ping/";

    public static final String TOOLS_STATS = TOOLS + "_stats/";
    private static final String IMAGE = "img/";

    public static final String IMAGE_GET_ID = IMAGE + GET_ID;

    public static final String API_USER_GET_ALL = API + USER + GET_ALL;
    public static final String API_USER_GET_ID = API + USER + GET_ID;
    public static final String API_USER_UPDATE = API + USER + UPDATE;
    public static final String API_USER_CREATE = API + USER + CREATE;
    public static final String API_USER_DELETE = API + USER + DELETE;
    public static final String API_USER_GET_CRED_ID = API + USER + CRED + GET_ID;

    public static final String API_GROUP_GET_ALL = API + GROUP + GET_ALL;
    public static final String API_GROUP_GET_ID = API + GROUP + GET_ID;
    public static final String API_GROUP_UPDATE = API + GROUP + UPDATE;
    public static final String API_GROUP_CREATE = API + GROUP + CREATE;
    public static final String API_GROUP_DELETE = API + GROUP + DELETE;

    public static final String API_ACL_GET_ID = API + ACL + GET_ID;
    public static final String API_ACL_UPDATE = API + ACL + UPDATE;
    public static final String API_ACL_CREATE = API + ACL + CREATE;
    public static final String API_ACL_DELETE = API + ACL + DELETE;
    public static final String API_ACL_DATA_ENUM = API + ACL + DATA + ENUM;
    public static final String API_ACL_OWNER_ENUM = API + ACL + OWNER + ENUM;
    public static final String API_ACL_ADMIN_ENUM = API + ACL + ADMIN + ENUM;
    public static final String API_ACL_ROLE_ENUM = API + ACL + ROLE + ENUM;

    public static final String API_ACL_GRP_GET_ALL = API + ACL_GRP + GET_ALL;
    public static final String API_ACL_GRP_GET_ID = API + ACL_GRP + GET_ID;
    public static final String API_ACL_GRP_UPDATE = API + ACL_GRP + UPDATE;
    public static final String API_ACL_GRP_CREATE = API + ACL_GRP + CREATE;
    public static final String API_ACL_GRP_DELETE = API + ACL_GRP + DELETE;

    public static final String API_PATH_GET_ALL = API + PATH + GET_ALL;
    public static final String API_PATH_GET_ID = API + PATH + GET_ID;
    public static final String API_PATH_UPDATE = API + PATH + UPDATE;
    public static final String API_PATH_CREATE = API + PATH + CREATE;
    public static final String API_PATH_DELETE = API + PATH + DELETE;

    public static final String API_ENTRY_GET_ALL = API + ENTRY + GET_ALL;
    public static final String API_ENTRY_GET_ID = API + ENTRY + GET_ID;
    public static final String API_ENTRY_UPDATE = API + ENTRY + UPDATE;
    public static final String API_ENTRY_CREATE = API + ENTRY + CREATE;
    public static final String API_ENTRY_DELETE = API + ENTRY + DELETE;

    public static final String API_ENTRY_ENC_GET = API + ENTRY_ENC + GET_ID;
    public static final String API_ENTRY_ENC_UPDATE = API + ENTRY_ENC + UPDATE;

    public static final String API_IMAGE_GET_ALL = API + IMAGE + GET_ALL;
    public static final String API_IMAGE_GET_ID = API + IMAGE + GET_ID;
    public static final String API_IMAGE_UPDATE = API + IMAGE + UPDATE;
    public static final String API_IMAGE_CREATE = API + IMAGE + CREATE;
    public static final String API_IMAGE_DELETE = API + IMAGE + DELETE;

    public static final String API_CRED_GET_ALL =  API + CRED + GET_ALL;
    public static final String API_CRED_GET_ID =  API + CRED + GET_ID;
    public static final String API_CRED_DELETE_ID =  API + CRED + DELETE;
    public static final String API_CRED_CREATE =  API + CRED + CREATE;
    public static final String API_CRED_CREATE_ID =  API + CRED + CREATE + GET_ID;

}

