package com.ra.controller.User;

import com.ra.model.entity.EOrderStatus;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import com.ra.service.OrderDetailService;
import com.ra.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ra.controller.User.accountController.getUserId;

@RestController
@RequestMapping("/v1/user/history")
public class HistoryController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("")
    public ResponseEntity<List<Orders>> getAll() {
        Long userId = getUserId();
        List<Orders> orders = orderService.getAll(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{serial}")
    public ResponseEntity<?> getOrderDetailbySerialNum(@PathVariable("serial") String serial) {
        Long userId = getUserId();
        Orders order = orderService.getbySerial(userId, serial);
        if (order == null) {
            return new ResponseEntity<>("không tìm thấy đơn hàng của bạn",HttpStatus.BAD_REQUEST);
        }

        List<OrderDetail> orderDetails = orderDetailService.getByOrderId(order.getId());
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @GetMapping("/{orderStatus}")
    public ResponseEntity<?> getByOrderStatus(@PathVariable("orderStatus") EOrderStatus orderStatus) {
        Long userId = getUserId();
        List<Orders> orders = orderService.getByStatus(userId, orderStatus);

        if (orders.isEmpty()) {
            return new ResponseEntity<>("không tìm thấy đơn hàng của bạn", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long orderId) {
        Long userId = getUserId();
        Orders order = orderService.getByIdAndStatus(userId, orderId, EOrderStatus.WAITING);
        if (order == null) {
            return new ResponseEntity<>("Không tìm thấy đơn hàng của bạn hoặc đơn hàng không ở trạng thái chờ xác nhận", HttpStatus.BAD_REQUEST);
        }
        order.setStatus(EOrderStatus.CANCEL);
        orderService.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
