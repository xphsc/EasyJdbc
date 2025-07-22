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
package com.xphsc.easyjdbc.core.entity;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: LikeType
 */
public enum LikeType {
    /**
     *
     */
    LEFT("left", "左边%"),
    RIGHT("right", "右边%"),
    CUSTOM("custom", "定制"),
    DEFAULT("default", "两边%");

    public final String type;
    public final String name;

    private LikeType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }
}
