package com.system.icae02.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.icae02.model.Product;
import com.system.icae02.repository.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> filterProducts(int id) {
        return repo.filterByCat(id);
    }

    public List<Product> searchByDistrict(String district) {
        return repo.findByManufacturingDistrict(district);
    }

    public Optional<Product> findProductById(int id) {
        return repo.findById(id);
    }
}