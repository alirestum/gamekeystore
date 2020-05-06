package hu.restumali.gamekeystore.repository;

import hu.restumali.gamekeystore.model.GameCategories;
import hu.restumali.gamekeystore.model.PlatformType;
import hu.restumali.gamekeystore.model.ProductAvailabilityType;
import hu.restumali.gamekeystore.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByName(String name);

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByCategoriesIn(List<GameCategories> categories);

    Page<ProductEntity> findAllByPlatformInAndCategoriesInAndSalePriceIsLessThanEqualAndAvailabilityOrPlatformInAndCategoriesInAndBasePriceIsLessThanEqualAndAvailability(List<PlatformType> platforms1,
                                                                                                                                            List<GameCategories> categories1,
                                                                                                                                            Integer salePrice,
                                                                                                                                            ProductAvailabilityType availability1,
                                                                                                                                            List<PlatformType> platforms2,
                                                                                                                                            List<GameCategories> categories2,
                                                                                                                                            Integer basePrice, ProductAvailabilityType availability2,
                                                                                                                                                                              Pageable pageable);


    Page<ProductEntity> findAllByAvailability(Pageable pageable, ProductAvailabilityType availability);

    List<ProductEntity> findTop5ByNameIsLikeIgnoreCase(String name);

    //List<ProductEntity> filterByPriceAndPlatformAndCategory();

    ProductEntity findOneById(Long id);
}
