package hqu.edu.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.eneity.ShoppingCart;
import hqu.edu.reggie.mapper.ShoppingCartMapper;
import hqu.edu.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {


}
