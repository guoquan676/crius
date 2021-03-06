package com.pbkj.crius.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemUser extends BasePo {
    /**
     * 名称
     */
    private String username;

    /**
     * 年龄
     */
    private Integer password;
}
