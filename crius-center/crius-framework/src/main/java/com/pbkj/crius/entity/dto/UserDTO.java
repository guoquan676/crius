package com.pbkj.crius.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pbkj.crius.common.constant.SexEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author yuanyang
 * @version 1.0
 * @date 2020/12/16 14:22
 */
@Data
public class UserDTO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;

    private SexEnum sex;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    //Date to string
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    //是将String转换成Date，一般前台给后台传值时用
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
}
