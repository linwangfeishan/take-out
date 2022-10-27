package hqu.edu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hqu.edu.reggie.common.BaseContext;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.ShoppingCart;
import hqu.edu.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据为{}",shoppingCart);

        //设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);


        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);


        if(dishId!=null){

            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        //查询菜品或套餐有没有再购物车里
        if (cart!=null){
            Integer number =cart.getNumber();
            cart.setNumber(number+1);
            shoppingCartService.updateById(cart);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cart =shoppingCart;
        }
        return Result.success(cart);
    }
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return Result.success(list);
    }
    @DeleteMapping("/clean")
    public Result<String> clean(){
        log.info("清空购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return Result.success("清空购物车成功");
    }
}
