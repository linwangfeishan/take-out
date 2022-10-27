package hqu.edu.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hqu.edu.reggie.eneity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
