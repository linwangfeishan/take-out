package hqu.edu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hqu.edu.reggie.eneity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper  extends BaseMapper<User> {
}
