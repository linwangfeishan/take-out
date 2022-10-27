package hqu.edu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.common.CustomException;
import hqu.edu.reggie.dto.DishDto;
import hqu.edu.reggie.eneity.Dish;
import hqu.edu.reggie.eneity.DishFlavor;
import hqu.edu.reggie.mapper.DishMapper;
import hqu.edu.reggie.service.DishFlavorService;
import hqu.edu.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;



    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((flavor)->{  //  flavor 为遍历出来的 每个DishFlavor对象
            flavor.setDishId(dishId);
            return flavor;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto selectByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish的基本信息
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((flavor)->{  //  flavor 为遍历出来的 每个DishFlavor对象
            flavor.setDishId(dishDto.getId());
            return flavor;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void batchDeleteByIds(List<Long> ids) {

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids!=null,Dish::getId,ids);
        List<Dish> list = this.list(queryWrapper);

        if(list != null){
            for(Dish dish:list){
                if(dish.getStatus()==0){
                    this.removeById(dish.getId());
                }else {
                    throw new CustomException("有商品正在售卖，无法删除");
                }
            }
        }



    }
}
