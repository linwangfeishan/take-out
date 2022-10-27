package hqu.edu.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hqu.edu.reggie.mapper.AddressBookMapper;
import hqu.edu.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;
import hqu.edu.reggie.eneity.AddressBook;


@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {

}
