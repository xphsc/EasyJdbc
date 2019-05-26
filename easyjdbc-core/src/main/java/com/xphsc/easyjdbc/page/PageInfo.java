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

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public class PageInfo<T> implements Serializable {
    public int pageNum;
    public int pageSize;
    public long total = 1L;
    public List<T> list;
    public int totalPages;
    public int offset=-1;
    public int limit;
    public int prePage;
    public int nextPage;
    public boolean hasPrePage;
    public boolean hasNextPage;

    public PageInfo() {
    }

    public PageInfo(int pageNum, int pageSize) {
        this.pageNum=pageNum;
        this.pageSize=pageSize;
    }


    private  PageInfo(Builder builder){
        this.pageNum=builder.pageNum;
        this.pageSize=builder.pageSize;
        this.offset=builder.offset;
        this.limit=builder.limit;
        this.total=builder.total;
        this.list= (List<T>) builder.list;
        this.totalPages=builder.totalPages;
        this.prePage=builder.prePage;
        this.nextPage=builder.nextPage;
        this.hasPrePage=builder.hasPrePage;
        this.hasNextPage=builder.hasNextPage;

    }

    public static Builder builder(){
        return  new Builder();
    }

    public static class Builder {
        private int pageNum;
        private int pageSize;
        private long total = 1L;
        private List<Object> list;
        private int totalPages;
        private int offset=-1;
        private int limit;
        private int prePage;
        private int nextPage;
        private boolean hasPrePage;
        private boolean hasNextPage;
        public Builder(){}


        public Builder pageNum(int pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder total(long total) {
            this.total = total;
            return this;
        }

        public Builder list(List<Object> list) {
            this.list = list;
            return this;
        }

        public Builder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder prePage(int prePage) {
            this.prePage = prePage;
            return this;
        }

        public Builder nextPage(int nextPage) {
            this.nextPage = nextPage;
            return this;
        }

        public Builder hasPrePage(boolean hasPrePage) {
            this.hasPrePage = hasPrePage;
            return this;
        }

        public Builder hasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
            return this;
        }

        public PageInfo build() {
            return new PageInfo(this);
        }
    }
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public boolean isHasNextPage() {
        return this.hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrePage() {
        return this.hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public int getNextPage() {
        return this.nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return this.prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}