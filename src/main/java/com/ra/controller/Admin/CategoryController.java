package com.ra.controller.Admin;

import com.ra.model.dto.request.CategoryRequest;
import com.ra.model.entity.Category;
import com.ra.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<Page<Category>> findAll(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "name", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        }else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }

        Page<Category> categories = categoryService.getAll(pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Category category = categoryService.findById(id);
        if(category == null) {
            return new ResponseEntity<>("Danh mục không tồn tại!", HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<Category> add(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.add(categoryRequest);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> edit(@PathVariable("id") Long id, @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.edit(categoryRequest, id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>("Xóa danh mục thành công" ,HttpStatus.NO_CONTENT);
    }
}
