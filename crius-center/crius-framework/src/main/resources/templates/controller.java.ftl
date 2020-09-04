package ${package.Controller};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pbkj.cloud.comment.base.BaseResponse;
import com.pbkj.cloud.comment.base.ResultCode;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import ${package.Entity}.dto.${dtoName};

/**
 * 	${table.comment} 
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("/${entity?uncap_first}")
public class ${table.controllerName} {

	@Autowired
	private ${table.serviceName} ${table.serviceName?uncap_first};
	
	// 新增
	@PostMapping("/web/save")
	public ResponseEntity<Object> saveData(@RequestBody ${entity} ${entity?uncap_first}) {
		return BaseResponse.sendMessage(${table.serviceName?uncap_first}.saveData(${entity?uncap_first}), "操作成功");
	}
	
	// 删除-支持批量方式
	@DeleteMapping("/web/removeByIds")
    public ResponseEntity<Object> removeByIds(@RequestParam("ids") String ids) {
        return BaseResponse.sendMessage(${table.serviceName?uncap_first}.removeByIds(ids), "操作成功");
    }
    
    // 修改-支持批量方式
    @PutMapping("/web/updateByIds")
    public ResponseEntity<Object> updateByIds(@RequestParam("ids") String ids) {
        return BaseResponse.sendMessage(${table.serviceName?uncap_first}.updateByIds(ids), "操作成功");
    }
    
    // 查询-根据id获取
	@GetMapping("/app/getById")
    public ResponseEntity<Object> getDataById(@RequestParam("id") Long id) {
        return BaseResponse.sendMessage(ResultCode.SUCCESS, ${table.serviceName?uncap_first}.getDataById(id));
    }

	// 分页查询-返回dto
    @PostMapping("/dto/listByPage")
    public ResponseEntity<Object> queryDtoListByPage(@RequestBody ${dtoName} ${dtoName?uncap_first}) {
        return BaseResponse.sendMessage(ResultCode.SUCCESS, ${table.serviceName?uncap_first}.queryDtoListByPage(${dtoName?uncap_first}));
    }
    
    // 分页查询-返回entity
    @PostMapping("/entity/listByPage")
    public ResponseEntity<Object> queryEntityListByPage(@RequestBody ${dtoName} ${dtoName?uncap_first}) {
        return BaseResponse.sendMessage(ResultCode.SUCCESS, ${table.serviceName?uncap_first}.queryEntityListByPage(${dtoName?uncap_first}));
    }
    
	// 获取所有dto
    @PostMapping("/dto/list")
    public ResponseEntity<Object> queryDtoList(@RequestBody ${dtoName} ${dtoName?uncap_first}) {
        return BaseResponse.sendMessage(ResultCode.SUCCESS, ${table.serviceName?uncap_first}.queryDtoList(${dtoName?uncap_first}));
    }
    
	// 获取所有entity
    @PostMapping("/entity/list")
    public ResponseEntity<Object> queryEntityList(@RequestBody ${dtoName} ${dtoName?uncap_first}) {
        return BaseResponse.sendMessage(ResultCode.SUCCESS, ${table.serviceName?uncap_first}.queryEntityList(${dtoName?uncap_first}));
    }
    
}
