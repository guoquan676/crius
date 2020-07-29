package com.pbkj.crius.entity.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pbkj.crius.entity.po.CUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author GZQ
 * @description User查询类$
 * @date 2020/7/24 8:58
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryParam extends BaseParam<CUser> {

    private Long id;
    /**
     * 名称
     */
    private String username;

    /**
     * 年龄
     */
    private Integer age;

    public QueryWrapper<CUser> queryBuild() {
        QueryWrapper<CUser> build = this.build();
        build.eq(StringUtils.isNotBlank(username),"username",username);
        build.eq(Objects.nonNull(age),"age",age);
        build.eq(Objects.nonNull(id),"id",id);
        return build;
    }
}