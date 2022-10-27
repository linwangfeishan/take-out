package hqu.edu.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.dto.SetmealDto;
import hqu.edu.reggie.eneity.Category;
import hqu.edu.reggie.eneity.Setmeal;
import hqu.edu.reggie.eneity.SetmealDish;
import hqu.edu.reggie.service.CategoryService;
import hqu.edu.reggie.service.SetmealDishService;
import hqu.edu.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.pkcs11.P11TlsKeyMaterialGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public Result<Page> getPage(int page,int pageSize,String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> list = pageInfo.getRecords();

        List<SetmealDto> setmealDtos =list.stream().map((item)->{

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

        return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtos);
        return Result.success(dtoPage);
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return Result.success(list);
    }


    @PostMapping
    public Result<String> save(@RequestBody SetmealDto dto){
        setmealService.saveWithDish(dto);
        return Result.success("新增成功");
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDishes(ids);
        return Result.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        setmealService.updateStatus(status,ids);
        return Result.success("修改状态成功");
    }

    


}
