package hqu.edu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hqu.edu.reggie.eneity.Dish;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DishMapper  extends BaseMapper<Dish> {
}
