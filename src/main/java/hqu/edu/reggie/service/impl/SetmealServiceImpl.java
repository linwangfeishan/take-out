package hqu.edu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.common.CustomException;
import hqu.edu.reggie.dto.SetmealDto;
import hqu.edu.reggie.eneity.Setmeal;
import hqu.edu.reggie.eneity.SetmealDish;
import hqu.edu.reggie.mapper.SetmealMapper;
import hqu.edu.reggie.service.SetmealDishService;
import hqu.edu.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();


        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void updateStatus(Integer status, List<Long> ids) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids!=null,Setmeal::getId,ids);
        List<Setmeal> setmeals = this.list(queryWrapper);

        if(setmeals!=null){
            for(Setmeal setmeal:setmeals){
                setmeal.setStatus(status);
                this.updateById(setmeal);
            }
        }
    }

    @Override
    @Transactional
    public void removeWithDishes(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count>0){
            throw  new CustomException("商品正在售卖不可删除");
        }
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);


    }

    @Override
    public void batchDeleteByIds(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids!=null,Setmeal::getId,ids);
        List<Setmeal> setmeals = this.list(queryWrapper);

        for(Setmeal setmeal:setmeals){
            if(setmeal.getStatus()==0){
                this.removeById(setmeal.getId());
            }else {
                throw new CustomException("此商品正在售卖不可删除");
            }
        }
    }
}
