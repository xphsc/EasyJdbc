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
package com.xphsc.easyjdbc.builder;


/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: SQL类继承自AbstractSQL，用于构建SQL查询
 * 它提供了一种链式调用的方式来构造SQL语句
 */
public class SQL extends AbstractSQL<SQL> {
    public SQL(){}
    public static SQL BUILD(){
        return new SQL();
    }
    @Override
    public SQL getSelf() {
        return this;
    }
}