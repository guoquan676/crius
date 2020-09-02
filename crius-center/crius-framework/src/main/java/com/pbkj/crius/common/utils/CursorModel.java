package com.pbkj.crius.common.utils;

import com.github.phantomthief.util.ThrowableFunction;
import com.google.common.collect.Lists;
import com.pbkj.crius.entity.po.BasePo;
import com.pbkj.crius.entity.vo.BaseVo;

import java.util.List;

/**
 * @author GZQ
 * 无条件滑动模块，这样比分页查数据库快
 * @date 2020/9/1 10:45
 **/
public class CursorModel<U extends BaseVo> {

    private static final Integer DEFAULT_LIMIT = 10;

    private Long cursor;
    private List<U> records;

    public List<U> getRecords() {
        return records;
    }

    public void setRecords(List<U> records) {
        this.records = records;
    }

    public Long getCursor() {
        return cursor;
    }

    public void setCursor(Long cursor) {
        if (cursor == null) {
            cursor = Long.MAX_VALUE;
        }
        this.cursor = cursor;
    }

    public static <T extends BasePo, U, X extends Throwable> CursorModel getCursorModel(List<T> list, ThrowableFunction<T, U, X> function) {
        List<U> objects = Lists.newArrayList();
        CursorModel cursorModel = new CursorModel();
        if (list != null && list.size() != 0) {
            list.forEach(e -> {
                U apply = null;
                try {
                    apply = function.apply(e);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                objects.add(apply);
            });
            if (list.size() < DEFAULT_LIMIT) {
                cursorModel.setCursor(-1L);
            } else {
                cursorModel.setCursor(list.get(list.size() - 1).getId());
            }
        } else {
            cursorModel.setCursor(-1L);
        }
        cursorModel.setRecords(objects);
        return cursorModel;
    }
}