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
package com.xphsc.easyjdbc.core.metadata.version;

/**
 * @author huipei.x
 * @date  2019-3-20
 * @description
 */
public class DefaultVersion implements NextVersion {
    @Override
    public Object nextVersion(Object versionValue) throws VersionException {
        Object newVersion=null;
        if(versionValue instanceof Integer ) {
            newVersion = (Integer)versionValue+1;

        } else if (versionValue instanceof Long ) {
            newVersion = (Long)versionValue+1;
        } else {
            throw new VersionException("The default version only supports version numbers of Integer and Long types. If you need to, please expand!");
        }
        return newVersion;
    }
}
