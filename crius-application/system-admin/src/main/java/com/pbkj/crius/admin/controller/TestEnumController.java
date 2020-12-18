package com.pbkj.crius.admin.controller;

import com.pbkj.crius.entity.dto.UserDTO;
import com.pbkj.crius.entity.po.UserTest;
import com.pbkj.crius.mapper.UserTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuanyang
 * @version 1.0
 * @date 2020/12/18 13:33
 */
@RestController
@RequestMapping("/demo")
public class TestEnumController {
    @Autowired
    private UserTestMapper userPoMapper;
    /**
     * 1.测试传入字符串日期-controller获取日期类
     * 2.controller日期类型-返给前端字符串
     * @param userDTO
     * @return
     */
    @GetMapping("/hello")
    public ResponseEntity<Object> hello(UserDTO userDTO){
        String s = "hello";
        System.out.println(userDTO);
        System.out.println(userDTO.getName());
        System.out.println(userDTO.getBirthday());

        return ResponseEntity.status(200).body(userDTO);

    }
    /**
     * @NonNull可以标注在方法、字段、参数之上，表示对应的值不可以为空
     * @Nullable注解可以标注在方法、字段、参数之上，表示对应的值可以为空
     */
    @PostMapping("/hello")
    public ResponseEntity<Object> helloPost(@RequestBody UserDTO userDTO){
        String s = "hello";
        System.out.println(userDTO);
        System.out.println(userDTO.getName());
        System.out.println(userDTO.getBirthday());
        List<UserTest> users = userPoMapper.selectList(null);

        return ResponseEntity.status(200).body(users);

    }
}
