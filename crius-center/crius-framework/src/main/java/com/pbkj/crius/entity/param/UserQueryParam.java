package com.pbkj.crius.entity.param;

import com.pbkj.crius.common.utils.ResultPageModel;
import com.pbkj.crius.entity.po.SystemUser;
import lombok.Data;

/**
 * @author GZQ
 * @description User查询类$
 * @date 2020/7/24 8:58
 **/
@Data
public class UserQueryParam extends ResultPageModel<SystemUser> {

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