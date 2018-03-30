
package com.ipet.http.utils;

import com.ipet.http.enviroment.EnvironmentManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/30 14:11
 */
public class UrlPropertyResolveUtil {

    private static Logger logger = LoggerFactory.getLogger(UrlPropertyResolveUtil.class);

    private static final String EL_TAG_REGEX = "(?<=\\$\\{).*(?=})";

    public static String resolveProperty(String elTag) throws Exception{
        if(StringUtils.isBlank(elTag)){
            return elTag;
        }
        if(EnvironmentManager.INSTANCE.getEnv() == null){
            throw new Exception("Current Environment hasn't been initialed.");
        }

        Pattern pattern = Pattern.compile(EL_TAG_REGEX);
        Matcher matcher = pattern.matcher(elTag);
        if(matcher.find()){
            String key = matcher.group();
            return EnvironmentManager.INSTANCE.getEnv().getProperty(key);
        }
        return elTag;
    }
}
