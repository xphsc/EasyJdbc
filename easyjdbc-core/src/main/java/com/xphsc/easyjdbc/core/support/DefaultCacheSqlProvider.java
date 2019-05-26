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
package com.xphsc.easyjdbc.core.support;

import com.xphsc.easyjdbc.core.cache.CacheKey;
import com.xphsc.easyjdbc.core.cache.CachekeyBuiler;

/**
 * @author huipei.x
 * @date  2019-3-18
 * @description
 */
public class DefaultCacheSqlProvider implements SqlCacheProvider, CachekeyBuiler {

    private final String sql;
    private final Object[] objects;

    public DefaultCacheSqlProvider(String sql, Object[] objects) {
        this.sql = sql;
        this.objects=objects;

    }
    @Override
    public CacheKey createCachekey() {
        CacheKey cacheKey = new CacheKey(sql);
        if(objects!=null){
            for (Object object : objects) {
                cacheKey.update(object);
            }
        }
        return cacheKey;
    }


}
