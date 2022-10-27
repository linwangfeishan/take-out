package hqu.edu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.common.CustomException;
import hqu.edu.reggie.eneity.Category;
import hqu.edu.reggie.eneity.Dish;
import hqu.edu.reggie.eneity.Setmeal;
import hqu.edu.reggie.mapper.CategoryMapper;
import hqu.edu.reggie.service.CategoryService;
import hqu.edu.reggie.service.DishService;
import hqu.edu.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {


    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1>0){
            throw new CustomException("当前分类对象关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> mealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(mealLambdaQueryWrapper);
        if(count2>0){
            throw new CustomException("当前分类对象关联了套餐，不能删除");
        }

        super.removeById(id);

    }




}
