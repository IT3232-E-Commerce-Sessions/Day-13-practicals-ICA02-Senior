package com.system.icae02.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.system.icae02.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.seller.district = :district")
    List<Product> findByManufacturingDistrict(@Param("district") String district);
    
    
    @Query("SELECT p FROM Category c JOIN c.products p WHERE c.id = :cid")
    List<Product> filterByCat(@Param("cid") int cid);
}