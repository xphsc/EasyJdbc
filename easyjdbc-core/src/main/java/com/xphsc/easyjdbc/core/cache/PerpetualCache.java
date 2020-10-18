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

package com.xphsc.easyjdbc.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huipei.x
 * @date  2019-3-18
 * @description
 */

public class PerpetualCache implements Cache {

    private  Map<Object, Object> cache = new ConcurrentHashMap<Object, Object>();

    private final String id;
    private ReentrantLock reentrantLock;

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getOject(Object key) {
        Object object;
        synchronized(cache) {
            object = cache.get(key);
        }
        return object;
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    @Override
    public ReentrantLock getReadWriteLock() {
        return reentrantLock;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cache)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Cache cache = (Cache) obj;
        return getId().equals(cache.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
