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
 * Created by ${huipei.x}
 */
public class PageInfoImpl<T> extends PageInfo<T> {

    public PageInfoImpl() {
    }

    public PageInfoImpl(List<T> list, long total, int pageNum, int pageSize) {
        this.pageNum = pageNum;
        if(pageNum < 1) {
            this.pageNum = 1;
        }
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
        this.prePage = this.pageNum - 1;
        if(this.prePage < 1) {
            this.hasPrePage = false;
            this.prePage = 1;
        } else {
            this.hasPrePage = true;
        }

        this.totalPages = (int)Math.ceil((double)total / (double)pageSize);
        if(this.pageNum > this.totalPages) {
            this.pageNum = this.totalPages;
        }

        this.nextPage = this.pageNum + 1;
        if(this.nextPage > this.totalPages) {
            this.hasNextPage = false;
            this.nextPage = this.totalPages;
        } else {
            this.hasNextPage = true;
        }
      this.offset = (this.pageNum * pageSize)-pageSize;
        this.limit=this.pageSize;

    }


}