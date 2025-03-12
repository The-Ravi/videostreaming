package com.api.videostreaming.utilities;

public class URIConstants {
    private URIConstants() {}

    public static final String API_VERSION = "/v1";

    public static final String GET = "/get";
    public static final String GET_ALL = "/getAll";
    
    /*
     * ADMIN CONTROLLER
     */
    public static final String ADMIN_BASE_URL = "/admin";
    public static final String GET_TOKEN = "/get-token";
    public static final String REFRESH_TOKEN = "/token-refresh";

    /*
     * VIDEO CONTROLLER
    */
    public static final String VIDEO_BASE_URL = "/api/video";
    public static final String PUBLISH_VIDEO = "/{videoContentId}/publish";
    public static final String ADD_OR_EDIT_META_DATA = "/metadata/{videoContentId}";
    public static final String VIDEO_CONTENT_ID = "/{videoContentId}";
    public static final String LOAD_VIDEO = "/{videoContentId}/load";
    public static final String PLAY_VIDEO = "/{videoContentId}/play";
    public static final String SEARCH = "/search";

    /*
     * ENGAGEMENT STATISTIC CONTROLLER
    */
    public static final String ES_BASE_URL = "/engagement";
    //public static final String NOTIFY_EVENT = "";
}