/*
 * Copyright (c) 2018-2019  huipei.x
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
package com.xphsc.easyjdbc.core.binding;


import com.xphsc.easyjdbc.EasyJdbcTemplate;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description  :
 */
public class DaoProxy<T> implements InvocationHandler, Serializable {

    private  Class<T> daoInterface;
    private EasyJdbcTemplate easyJdbcTemplate ;
    public DaoProxy(EasyJdbcTemplate easyJdbcTemplate) {
        this.easyJdbcTemplate=easyJdbcTemplate;
    }

    public void setDaoInterface(Class<T> daoInterface) {
        this.daoInterface = daoInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        DaoMethod daoMethod = new DaoMethod(daoInterface,this::getEasyJdbcTemplate, method, args);
        return daoMethod.doExecute();
    }

    private EasyJdbcTemplate getEasyJdbcTemplate() {
        return easyJdbcTemplate;
    }
}
