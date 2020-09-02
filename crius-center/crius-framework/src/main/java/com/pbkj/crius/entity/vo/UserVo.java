package com.pbkj.crius.entity.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.pbkj.crius.entity.po.CUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserVo extends BaseVo {

    public UserVo(CUser user) {
        BeanUtils.copyProperties(user, this);
        Long createdTime = user.getCreatedTime();
        this.createdTime = DateUtil.format(new Date(createdTime), DatePattern.NORM_DATETIME_PATTERN);
    }

    private String username;
    private Integer age;
    private String createdTime;
}
