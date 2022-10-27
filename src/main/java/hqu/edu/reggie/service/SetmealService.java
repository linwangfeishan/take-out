package hqu.edu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hqu.edu.reggie.dto.SetmealDto;
import hqu.edu.reggie.eneity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void batchDeleteByIds(List<Long> ids);

    void removeWithDishes(List<Long> ids);

    void updateStatus(Integer status,List<Long> ids);
}
