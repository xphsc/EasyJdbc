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
package com.xphsc.easyjdbc.core.factory;

import com.xphsc.easyjdbc.core.binding.DaoProxy;
import org.springframework.beans.factory.FactoryBean;
import java.lang.reflect.Proxy;



/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:  EasyDaoBeanFactory是一个实现了FactoryBean接口的类，用于创建和管理DAO接口的代理实例
 * 它通过使用Java的动态代理机制来创建DAO接口的实现类实例，从而实现对DAO层的统一管理和代理
 * @param <T> 表示DAO接口的类型
 */
public class EasyDaoBeanFactory<T> implements FactoryBean<T> {

    private Class<T> daoInterface;

    private DaoProxy proxy;

    @Override
    public T getObject() throws Exception {
        proxy.setDaoInterface(daoInterface);
        return newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return daoInterface;
    }

    public DaoProxy getProxy() {
        return proxy;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private T newInstance() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{daoInterface}, proxy);
    }

    public void setProxy(DaoProxy proxy) {
        this.proxy = proxy;
    }

    public void setDaoInterface(Class<T> daoInterface) {
        this.daoInterface = daoInterface;
    }

}
