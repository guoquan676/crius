package com.pbkj.crius.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageModel<T> implements Serializable {
    /**
     * 每页显示记录数
     */
    private Integer size = 10;
    /**
     * 偏移量
     */
    private Integer offset;
    /**
     * 当前页
     */
    private Integer current = 1;

    public int getOffset() {
        if (current == 0) {
            offset = 0;
        } else {
            offset = (current - 1) * this.size;
        }
        return offset;
    }

    public PageModel<T> setOffset(int offset) {
        this.offset = offset;
        return this;
    }


    public int getCurrent() {
        return current;
    }

    public PageModel<T> setCurrent(Integer current) {
        if (current != null) this.current = current;
        return this;
    }


    public Integer getSize() {
        if (size == null) {
            size = 10;
        }
        return size;
    }

    public PageModel<T> setSize(Integer size) {
        if (size == null) {
            size = 10;
        }
        this.size = size;
        return this;
    }
    public Page<T> getPage(){
        return new Page<>( this.current, this.size);
    }
}
