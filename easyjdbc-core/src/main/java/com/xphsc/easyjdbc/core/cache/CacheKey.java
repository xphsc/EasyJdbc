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



import com.xphsc.easyjdbc.util.StringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author huipei.x
 * @date  2019-3-18
 * @description
 */

public class CacheKey implements Cloneable, Serializable {

    private static final long serialVersionUID = 3997169102080140762L;

    private static final int DEFAULT_MULTIPLYER = 37;
    private static final int DEFAULT_HASHCODE = 17;

    private final int multiplyer;
    private int hashcode;
    private int checksum;
    private int count;

    private List<Object> updateList;

    public CacheKey() {
        this.hashcode = DEFAULT_HASHCODE;
        this.multiplyer = DEFAULT_MULTIPLYER;
        this.count = 0;
        this.updateList = new ArrayList<>();
    }

    public CacheKey(Object... objects) {
        this();
        updateAll(objects);
    }

    public int getUpdateList() {
        return updateList.size();
    }

    public void update(Object object) {
        int baseHashcode = object == null ? 1 : StringUtil.hashcode(object);
        count++;
        checksum += baseHashcode;
        baseHashcode *= count;
        hashcode = multiplyer * hashcode + baseHashcode;
        updateList.add(object);

    }

    public void updateAll(Object[] objects) {
        for (Object object : objects) {
            update(object);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CacheKey)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        CacheKey cacheKey = (CacheKey) obj;
        if (hashcode != cacheKey.hashcode) {
            return false;
        }
        if (checksum != cacheKey.checksum) {
            return false;
        }
        if (count != cacheKey.count) {
            return false;
        }

        for (int i = 0, l = updateList.size(); i < l; i++) {
            if (!Objects.equals(updateList.get(i), cacheKey.updateList.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    protected CacheKey clone() throws CloneNotSupportedException {
        CacheKey cloneCacheKey = (CacheKey) super.clone();
        cloneCacheKey.updateList = new ArrayList<>(updateList);
        return cloneCacheKey;
    }

    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder().append(hashcode).append(':').append(checksum);
        for (Object object : updateList) {
            returnValue.append(':').append(object.toString());
        }
        return returnValue.toString();
    }
}
