package com.pbkj.crius.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.pbkj.crius.common.constant.SexEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author yuanyang
 * @version 1.0
 * @date 2020/12/18 13:20
 */
@Data
public class UserTest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private SexEnum sex;
    private Integer age;
    private String email;
    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
