package com.pbkj.crius.entity.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pbkj.crius.common.utils.PageModel;
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
public class UserQueryParam extends PageModel<CUser> {

    private Long id;
    /**
     * 名称
     */
    private String username;

    /**
     * 年龄
     */
    private Integer age;
}