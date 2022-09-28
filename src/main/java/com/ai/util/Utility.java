package com.ai.util;

import javax.servlet.http.HttpServletRequest;

public class Utility {

    public static String getSiteUrl(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        return url.replace(request.getServletPath(),"");
    }
}
