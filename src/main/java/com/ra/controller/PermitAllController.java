package com.ra.controller;

import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.service.CategoryService;
import com.ra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class PermitAllController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> findCategories(){
        List<Category> categories = categoryService.getbyStatus();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(name = "name") String query) {
        List<Product> products = productService.getByNameOrDes(query, query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProduct(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "name", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }
        Page<Product> products = productService.getByCategoryStatus(pageable, true);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/new-products")
    public ResponseEntity<?> getAllNewProduct(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "id", name = "sort") String sort) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        Page<Product> product = productService.getByCategoryStatus(pageable, true);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products/categories/{categoryId}")
    public ResponseEntity<?> getbyCategory(@PathVariable("categoryId") Long id) {
        List<Product> products = productService.getByCategoryId(id);
        if(products.isEmpty()){
            return new ResponseEntity<>("Danh mục không tồn tại sản phẩm nào", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
