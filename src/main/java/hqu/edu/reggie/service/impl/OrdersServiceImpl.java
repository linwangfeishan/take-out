package hqu.edu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.common.BaseContext;
import hqu.edu.reggie.common.CustomException;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.*;
import hqu.edu.reggie.mapper.OrdersMapper;
import hqu.edu.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService {


    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;




    @Transactional
    public void submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        if (list == null ||list.size()==0) {
            throw new CustomException("购物车为空，不能下单");
        }

        User user = userService.getById(userId);

        Long addressBookId = orders.getAddressBookId();
        if(addressBookId==null){
            throw new CustomException("地址信息有误,不能下单");
        }
        AddressBook addressBook = addressBookService.getById(addressBookId);
        long id = IdWorker.getId();


        //  购物车中 商品 的总金额 需要保证在多线程的情况下 也是能计算正确的，故需要使用原子类
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = list.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrderId(id);
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());

            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());

            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

            return orderDetail;

        }).collect(Collectors.toList());

        orders.setId(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额，需要 遍历购物车，计算相关金额来得到
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(id));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));


        // 向订单表插入数据,一条数据，插入数据之前，需要填充如上属性
        this.save(orders);    //  --> ordersService.save(orders);

        // 向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车数据
        shoppingCartService.remove(queryWrapper);



    }

}
