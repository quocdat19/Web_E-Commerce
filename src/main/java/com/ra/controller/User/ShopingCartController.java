package com.ra.controller.User;

import com.ra.model.dto.request.QuantityRequest;
import com.ra.model.dto.request.ShopingCartRequest;
import com.ra.model.entity.*;
import com.ra.service.*;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ra.controller.User.accountController.getUserId;

@RestController
@RequestMapping("/v1/user/shopping-cart")
public class ShopingCartController {
    @Autowired
    private ShopingCartService shopingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<ShopingCart>> getAll() {
        Long userId = getUserId();
        List<ShopingCart> shoppingCarts = shopingCartService.getAll(userId);
        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody ShopingCartRequest shopingCartRequest) {
        Long userId = getUserId();
        Product product =productService.findById(shopingCartRequest.getProductId());
        if(product == null){
            return new ResponseEntity<>("Mã sản phẩm không tồn tai, vui lòng nhập đúng mã sản phẩm", HttpStatus.BAD_REQUEST);
        }
        ShopingCart shopingCart = shopingCartService.add(shopingCartRequest, userId);
        return new ResponseEntity<>(shopingCart, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuantity(@RequestBody QuantityRequest quantity, @PathVariable("id") int id) {
        Long userId = getUserId();
        ShopingCart shopingCart = shopingCartService.findById(id);
        if(shopingCart != null) {
            if(shopingCart.getUsers().getId().equals(userId)) {
                shopingCart.setQuantity(quantity.getQuantity());
                shopingCartService.save(shopingCart);
                return new ResponseEntity<>(shopingCart, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("bạn không có quyền thay đổi giỏ hàng!",HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("không tồn tại gỏ hàng!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> delete(@PathVariable("productId") Long id) {
        Long userId = getUserId();
        ShopingCart shopingCart = shopingCartService.findByProductId(userId, id);
        if (shopingCart != null) {
            shopingCartService.delete(shopingCart.getId());
            return new ResponseEntity<>("Sản phẩm đã được xóa khỏi giỏ hàng của bạn", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Sản phẩm không tồn tại trong giỏ hàng của bạn", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAll() {
        Long userId = getUserId();
        List<ShopingCart> shopingCarts = shopingCartService.getAll(userId);
        shopingCarts.forEach(shopingCart -> shopingCartService.delete(shopingCart.getId()));
        return new ResponseEntity<>("Tất cả sản phẩm đã được xóa khỏi giỏ hàng của bạn", HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkOut() {
        Long userId = getUserId();
        List<ShopingCart> shopingCarts = shopingCartService.getAll(userId);

        if (shopingCarts.isEmpty()) {
            return new ResponseEntity<>("giỏ hàng trống", HttpStatus.BAD_REQUEST);
        }

        Users user = userService.findById(userId);

        double totalPrice = shopingCarts.stream()
                .mapToDouble(shopingCart -> shopingCart.getProduct().getPrice())
                .sum();

        Orders order = orderService.add(user, totalPrice);

        for (ShopingCart shopingCart: shopingCarts) {
            int orderQuantity = shopingCart.getQuantity();
            Product product = shopingCart.getProduct();
            orderDetailService.add(product, order, orderQuantity);
        }

        return new ResponseEntity<>("Đặt hàng thành công!", HttpStatus.OK);
    }
}
