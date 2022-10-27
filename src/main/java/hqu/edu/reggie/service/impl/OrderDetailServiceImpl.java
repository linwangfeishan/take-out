package hqu.edu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.eneity.OrderDetail;
import hqu.edu.reggie.mapper.OrderDetailMapper;
import hqu.edu.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
