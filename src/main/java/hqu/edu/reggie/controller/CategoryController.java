package hqu.edu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.Category;
import hqu.edu.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody Category category){
        log.info("category{}",category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> delete(Long ids){
        log.info("删除的id为{}",ids);

        categoryService.remove(ids);

        return Result.success("删除成功!");
    }

    @PutMapping
    public Result<String> uadate(@RequestBody Category category){
        log.info("修改分类信息{}",category);
        categoryService.updateById(category);
        return Result.success("修改分类信息成功");
    }

    @GetMapping("list")
    public Result<List<Category>> saveDish(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);

    }


}
