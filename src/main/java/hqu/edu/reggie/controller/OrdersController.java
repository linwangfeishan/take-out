package hqu.edu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hqu.edu.reggie.common.BaseContext;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.OrderDetail;
import hqu.edu.reggie.eneity.Orders;
import hqu.edu.reggie.eneity.OrdersDto;
import hqu.edu.reggie.service.OrderDetailService;
import hqu.edu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return Result.success("下单成功");
    }



    @GetMapping("/userPage")
    public Result<Page> getUserPage(Integer page, Integer pageSize){
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> pageDto = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.orderByDesc(Orders::getOrderTime);
        this.ordersService.page(pageInfo,queryWrapper);

        LambdaQueryWrapper<OrdersDto> queryWrapper2 = new LambdaQueryWrapper();


        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();

            Long orderId = item.getId();
            List<OrderDetail> orderDetailList =  this.ordersService.getOrderDetailsByOrderId(orderId);
            BeanUtils.copyProperties(item,ordersDto);
            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;

        }).collect(Collectors.toList());

        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        pageDto.setRecords(ordersDtoList);
        return Result.success(pageDto);


    }

}
