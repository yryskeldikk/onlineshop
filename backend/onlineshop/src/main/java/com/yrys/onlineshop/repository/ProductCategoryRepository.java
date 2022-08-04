package com.yrys.onlineshop.repository;

import com.yrys.onlineshop.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "productCategory", path = "product-category")
// @CrossOrigin
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
