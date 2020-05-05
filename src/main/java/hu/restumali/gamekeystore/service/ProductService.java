package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.GameCategories;
import hu.restumali.gamekeystore.model.PlatformType;
import hu.restumali.gamekeystore.model.ProductAvailabilityType;
import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void save(ProductEntity productEntity) {productRepository.save(productEntity);}

    public void delete(ProductEntity productEntity) {productRepository.delete(productEntity);}

    public void deleteById(Long id){ productRepository.deleteById(id);}

    public List<ProductEntity> findAll(){ return productRepository.findAll(); }

    public ProductEntity findById(Long id){ return productRepository.findOneById(id); }

    public List<ProductEntity> findByCategories(List<GameCategories> categories){
        return productRepository.findAllByCategoriesIn(categories);
    }

    public Page<ProductEntity> filter(List<PlatformType> platforms, List<GameCategories> categories, Integer maxPrice, Pageable pageable){
        return productRepository
                .findAllByPlatformInAndCategoriesInAndSalePriceIsLessThanEqualAndAvailabilityOrPlatformInAndCategoriesInAndBasePriceIsLessThanEqualAndAvailability(platforms,
                        categories, maxPrice, ProductAvailabilityType.InStock, platforms, categories, maxPrice, ProductAvailabilityType.InStock, pageable);


    }

/*    public Map<String, Object> createResponseForProductsPage(){

    }*/

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
}
