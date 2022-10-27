package hqu.edu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hqu.edu.reggie.dto.DishDto;
import hqu.edu.reggie.eneity.Dish;
import sun.awt.SunHints;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品并且插入口味数据
    void saveWithFlavor(DishDto dishDto);

    DishDto selectByIdWithFlavor(Long id);


    void updateWithFlavor(DishDto dishDto);




    void batchDeleteByIds(List<Long> ids);
}
