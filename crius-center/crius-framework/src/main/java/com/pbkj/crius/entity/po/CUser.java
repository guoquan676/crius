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
public class CUser extends BasePo {
    /**
     * 名称
     */
    private String username;

    /**
     * 年龄
     */
    private Integer age;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Long createdTime;
}
