package hqu.edu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hqu.edu.reggie.eneity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
