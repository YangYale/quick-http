package com.ipet.http.enviroment;

import org.springframework.core.env.Environment;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/30 13:54
 */
public class EnvironmentManager {

    private Environment env;

    public static EnvironmentManager INSTANCE = new EnvironmentManager();

    private EnvironmentManager(){

    }

    public Environment getEnv(){
        return env;
    }

    public void setEnv(Environment environment){
        env = environment;
    }
}
