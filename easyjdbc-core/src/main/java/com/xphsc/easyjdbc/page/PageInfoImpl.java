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
package com.xphsc.easyjdbc.page;


import java.util.List;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 通用的分页信息实现类，用于封装分页查询的结果
 * 实现了Serializable接口以支持序列化，便于在网络传输或存储
 */
public class PageInfoImpl<T> extends PageInfo<T> {

    public PageInfoImpl() {
    }

    public PageInfoImpl(List<T> list, long total, int pageNum, int pageSize) {
        this.setPageNum(pageNum);
        if(pageNum < 1) {
            this.setPageNum(1);
        }
        this.setPageSize(pageSize);
        this.setTotal(total);
        this.setList(list);
        this.setPrePage(this.getPageNum()-1);
        if(this.getPrePage() < 1) {
            this.setHasPrePage(false);
            this.setPrePage(1);
        } else {
            this.setHasPrePage(true);
        }
        this.setTotalPages((int)Math.ceil((double)total / (double)pageSize));
        if(this.getPageNum() > this.getTotalPages()) {
            this.setPageNum(this.getTotalPages());
        }
        this.setNextPage(this.getPageNum()+1);
        if(this.getNextPage() > this.getTotalPages()) {
            this.setHasNextPage(false);
            this.setNextPage(this.getTotalPages());
        } else {
            this.setHasNextPage(true);
        }
        this.setOffset((this.getPageNum() * pageSize)-pageSize);
        this.setLimit(this.getPageSize());

    }


}