package com.pbkj.crius.entity.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pbkj.crius.entity.po.BasePo;
import lombok.Data;

/**
 * @author GZQ
 */
@Data
public class BaseParam<T extends BasePo> {
    private Long current;
    private Long size;
    private Long cursor;
    private Long createdTimeStart;
    private Long createdTimeEnd;

    public QueryWrapper<T> build() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(null != this.createdTimeStart, "created_time", this.createdTimeStart)
                .le(null != this.createdTimeEnd, "created_time", this.createdTimeEnd);
        return queryWrapper;
    }
    public Page<T> getPage() {
        return new Page<>(this.getCurrent(), this.getSize());
    }
}
