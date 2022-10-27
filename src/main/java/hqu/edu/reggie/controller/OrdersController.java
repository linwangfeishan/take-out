package hqu.edu.reggie.controller;

import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.Orders;
import hqu.edu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return Result.success("下单成功");
    }
}
