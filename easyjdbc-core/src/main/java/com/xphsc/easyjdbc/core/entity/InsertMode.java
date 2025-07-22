package com.xphsc.easyjdbc.core.entity;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @since 2.0.5
 */
public enum InsertMode {
    /**
     *
     */
    RETURNKEY,
    IGNORENULL;

    private InsertMode() {

    }

    private InsertMode(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

