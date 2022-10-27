package hqu.edu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hqu.edu.reggie.eneity.Orders;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
