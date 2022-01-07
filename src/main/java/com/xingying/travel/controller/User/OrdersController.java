package com.xingying.travel.controller.User;

import com.xingying.travel.common.PageResult;
import com.xingying.travel.common.Result;
import com.xingying.travel.common.StatusCode;
import com.xingying.travel.pojo.Hotel_orders;
import com.xingying.travel.pojo.Orders;
import com.xingying.travel.pojo.User;
import com.xingying.travel.service.Hotel_ordersService;
import com.xingying.travel.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@Controller
@CrossOrigin
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


    @Autowired
    private Hotel_ordersService hotel_ordersService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", ordersService.findAll());
    }

    /**
     * 查询全部订单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allorders", method = RequestMethod.POST)
    public String findAllOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        System.out.println(user.getId() + "=========" + ordersService.findOrders(user.getId()));
        model.addAttribute("orders", ordersService.findOrders(user.getId()));
        return "index_header::table_refresh";
    }

    /**
     * 查询全部订单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allorder", method = RequestMethod.POST)
    public List<Orders> findAllOrder(HttpSession session) {
        User user = (User) session.getAttribute("user");
        //List<Object> objects=new ArrayList<>();
        System.out.println(user.getId() + "=========" + ordersService.findOrders(user.getId()));
        //List<Hotel_orders> hotel_orders=hotel_ordersService.hotel_orders(user.getId());
        //objects.addAll(ordersService.findOrders(user.getId()));
        //objects.addAll(hotel_orders);
        return ordersService.findOrders(user.getId());
    }


    /**
     * 查询全部订单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allorderhotel", method = RequestMethod.POST)
    public List<Hotel_orders> findAllOrderHotel(HttpSession session) {
        User user = (User) session.getAttribute("user");
        //List<Object> objects=new ArrayList<>();
        //System.out.println(user.getId()+"========="+ordersService.findOrders(user.getId()));
        List<Hotel_orders> hotel_orders = hotel_ordersService.hotel_orders(user.getId());
        //objects.addAll(ordersService.findOrders(user.getId()));
        //objects.addAll(hotel_orders);
        return hotel_orders;
    }


    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", ordersService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @ResponseBody
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Orders> pageList = ordersService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Orders>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", ordersService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param orders
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Orders orders, HttpSession session) {

        //获取数量
        System.out.println("id为--->" + orders.getId());
        System.out.println("数量为--->" + orders.getQty());
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new Result(false, StatusCode.ACCESSERROR, "请登录");
        }

        return ordersService.add(orders, user.getId(), orders.getId(), orders.getQty());
    }

    /**
     * 修改
     *
     * @param
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id) {

        System.out.println(id);
        ordersService.updateStatus(id);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        ordersService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/ordersList")
    public String ordersList() {
        return "admin/ordersmanage/orderslist";
    }


}
