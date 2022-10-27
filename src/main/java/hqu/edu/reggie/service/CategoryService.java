package hqu.edu.reggie.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import hqu.edu.reggie.eneity.Category;

public interface CategoryService extends IService<Category> {


    public void remove(Long id);
}
