/*
 * Copyright (c) 2018 huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xphsc.easyjdbc.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by ${huipei.x}
 */
public class LogUtil {

    protected Log logger ;

    private LogUtil(Log log4jLogger) {
        logger = log4jLogger;
    }


    public static LogUtil getLogger(Class clazz) {
        return new LogUtil(LogFactory.getLog(clazz));
    }

    public void debug(String message) {

        logger.debug(message);

    }

    public boolean isDebugEnabled(){
        return logger.isDebugEnabled();
    }

    public void fatal(String message) {
        logger.fatal(message);
    }

    public void info(String message) {
        logger.info(message);
    }



    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void debug(String message, Throwable e) {
        logger.debug(message, e);
    }

    public void fatal(String message, Throwable e) {
        logger.fatal(message, e);
    }

    public void info(String message, Throwable e) {
        logger.info(message, e);

    }



    public void warn(String message, Throwable e) {
        logger.warn(message, e);
    }

    public void error(String message, Throwable e) {
        logger.error(message, e);
    }




}
