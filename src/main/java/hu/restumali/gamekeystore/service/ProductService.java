package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void save(ProductEntity productEntity) {productRepository.save(productEntity);}

    public void delete(ProductEntity productEntity) {productRepository.delete(productEntity);}

    public void deleteById(Long id){
        ProductEntity managedProduct = productRepository.findOneById(id);
        managedProduct.setAvailability(ProductAvailabilityType.Unavailable);
        productRepository.save(managedProduct);
    }

    public List<ProductEntity> findAll(){ return productRepository.findAll(); }

    public ProductEntity findById(Long id){ return productRepository.findOneById(id); }

    public List<ProductEntity> findByCategories(List<GameCategories> categories){
        return productRepository.findAllByCategoriesIn(categories);
    }

    public Page<ProductEntity> filter(List<PlatformType> platforms, List<GameCategories> categories, Integer maxPrice, List<AgeLimitType> ageLimits, Pageable pageable){
        return productRepository.filteredProducts(maxPrice, platforms, categories, ProductAvailabilityType.InStock, ageLimits, pageable);
    }

    public List<ProductEntity> searchByName(String name){
        return  productRepository.findTop5ByNameIsLikeIgnoreCase(name);
    }

    public void updateProductById(Long id, ProductEntity product){
        ProductEntity managedProduct = productRepository.findOneById(id);
        managedProduct.setName(product.getName());
        managedProduct.setBasePrice(product.getBasePrice());
        managedProduct.setSalePrice(product.getSalePrice());
        managedProduct.setDescription(product.getDescription());
        if (product.getFeaturedImageUrl() != null)
            managedProduct.setFeaturedImageUrl(product.getFeaturedImageUrl());
        managedProduct.setCategories(product.getCategories());
        managedProduct.setPlatform(product.getPlatform());
        managedProduct.setAgeLimit(product.getAgeLimit());
    }

    public Integer highestPrice(){
        List<ProductEntity> products = productRepository.findAll();
        Integer maxValue = 0;
        for (ProductEntity product : products){
            if (product.getSalePrice() != null && product.getSalePrice() > maxValue)
                maxValue = product.getSalePrice();
            else if(product.getBasePrice() > maxValue)
                maxValue = product.getBasePrice();
        }
        return maxValue;
    }

    public Page<ProductEntity> getByAvailabilityWithPages(Pageable pageable){
        return productRepository.findAllByAvailability(pageable, ProductAvailabilityType.InStock);
    }

    public void exportAllProducts() {
    }
}
