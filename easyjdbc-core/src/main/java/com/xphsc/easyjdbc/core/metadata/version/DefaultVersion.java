package com.xphsc.easyjdbc.core.metadata.version;

/**
 * @author huipei.x
 * @date  2018-8-20
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
            throw new VersionException("默认的 version 只支持 Integer 和 Long 类型的版本号，如果有需要请自行扩展!");
        }
        return newVersion;
    }
}
