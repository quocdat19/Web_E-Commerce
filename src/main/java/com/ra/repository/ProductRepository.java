package com.ra.repository;

import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT pro from Product pro WHERE pro.name like %?1% ")
    List<Product> findByNameOrDescription(String name, String description);
    boolean existsByName(String name);
    @Query("select p from Product p where p.category.status = :status")
    Page<Product> findByCategoryStatus(Pageable pageable, Boolean status);
    @Query("select p from Product p where p.category.id = :id")
    List<Product> findByCategoryId(Long id);
}
