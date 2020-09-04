package ${package.Mapper};

import java.util.HashMap;
import java.util.List;

import ${package.Entity}.${entity};
import ${package.Entity}.dto.${dtoName};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 	${table.comment}
 * @author ${author}
 * @since ${date}
 */
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

	List<${dtoName}> queryListDto(HashMap<String, Object> param);

	${dtoName} queryDataById(Long id);

}
