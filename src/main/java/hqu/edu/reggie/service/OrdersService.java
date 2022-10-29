package hqu.edu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hqu.edu.reggie.eneity.OrderDetail;
import hqu.edu.reggie.eneity.Orders;

import java.util.List;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
}
