package hqu.edu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.Employee;
import hqu.edu.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
    员工登录
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if(emp==null){
            return Result.error("登陆失败");
        }

        if(!emp.getPassword().equals(password)){
            return Result.error("登陆失败");
        }

        if(emp.getStatus()==0){
            return Result.error("该账号已被禁用");
        }


        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @PostMapping
    public Result<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息{}",employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long)request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);

        return Result.success("新增员工成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("线程id为{}",id);
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return Result.success(employee);
        }
        return Result.error("查询失败");
    }





}
