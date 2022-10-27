package hqu.edu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.dto.DishDto;
import hqu.edu.reggie.eneity.Category;
import hqu.edu.reggie.eneity.Dish;
import hqu.edu.reggie.eneity.DishFlavor;
import hqu.edu.reggie.service.CategoryService;
import hqu.edu.reggie.service.DishFlavorService;
import hqu.edu.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;



    @GetMapping("/page")
    public Result<Page> getPage(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);

        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item)->{

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
     }

     @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
     }

     @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.selectByIdWithFlavor(id);
        return Result.success(dishDto);
     }


    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return Result.success("修改菜品操作成功！");
    }
//
//    @GetMapping("/list")
//    public Result<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//        return Result.success(list);
//
//    }

    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long itemCategoryId = item.getCategoryId();
            Category category = categoryService.getById(itemCategoryId);
            if (category!=null){
                dishDto.setCategoryName(category.getName());
            }
            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(flavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(dishDtoList);

    }

    @PostMapping("/status/{status}")
    public Result<String> updateSaleStatus(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids!=null,Dish::getId,ids);
        List<Dish> dishList = dishService.list(queryWrapper);

        if(dishList!=null){
            for(Dish dish:dishList){
                    dish.setStatus(status);
                    dishService.updateById(dish);

            }
            return Result.success("菜品售卖状态已更改");
        }
        return Result.error("菜品售卖状态不可更改");
    }



    @DeleteMapping
    public Result<String> delete(@RequestParam("ids") List<Long> ids){

        dishService.batchDeleteByIds(ids);

        return Result.success("删除菜品成功");

    }









}
