package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.ProductEntity;
import hu.restumali.gamekeystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void save(ProductEntity productEntity) {productRepository.save(productEntity);}

    public void delete(ProductEntity productEntity) {productRepository.delete(productEntity);}

    public List<ProductEntity> findAll(){ return productRepository.findAll(); }


}
