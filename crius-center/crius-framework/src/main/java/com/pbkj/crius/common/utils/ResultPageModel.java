package com.pbkj.crius.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具
 * @author GZQ
 */
@Data
public class ResultPageModel<T> implements Serializable {
    /**
     * 数据列表
     */
    private List<T> records;
    /**
     * 每页显示记录数
     */
    private Integer size;
    /**
     * 总条数
     */
    private Integer total;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 是否是最后一页
     */
    private boolean isLastPage;

    public ResultPageModel(){

    }
    public ResultPageModel(List<T> records, Integer current, Integer size, Integer total){
        this.records=records;
        this.current=current;
        this.size=size;
        this.total=total;
        if (size != null && total != null) {
            pages = total % size == 0 ? (total / size) : (total / size) + 1;
        }
        if (pages != null && current != null) {
            isLastPage = pages.equals(current) ;
        }
    }

    public void setCurrent(Integer current) {
        this.current = current;
        if(pages!=null&&current!=null){
            isLastPage=pages.equals(current);
        }
    }

    public void setTotal(Integer total) {
        this.total = total;
        setTotalPageAndIsLastPage(pages, total);
    }

    public void setPages(Integer pages) {
        this.pages = pages;
        setTotalPageAndIsLastPage(pages, total);
    }
    public Integer getSize() {
        if (size == null) {
            size = 10;
        }
        return size;
    }

    public void setSize(Integer size) {
        if (size == null) {
            size = 10;
        }
        this.size = size;
    }
    public Page<T> getPage(){
        return new Page<>( this.current, this.size);
    }

    private void setTotalPageAndIsLastPage(Integer size, Integer total) {
        if (size != null && total != null) {
            pages = total % size == 0 ? (total / size) : (total / size) + 1;
        }
        if (pages != null && current != null) {
            isLastPage = pages.equals(current);
        }
    }
}
