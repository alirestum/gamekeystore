package hu.restumali.gamekeystore.repository;

import hu.restumali.gamekeystore.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByName(String name);

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByCategoriesIn(List<GameCategories> categories);


    @Query("select distinct product from ProductEntity product join product.categories c where (product.salePrice <= :maxPrice or product.basePrice <= :maxPrice) and " +
            "(product.platform in :platforms) and (c in :categories ) and (product.availability = :availability) " +
            "and (product.ageLimit in :ageLimits)")
    Page<ProductEntity> filteredProducts(
            @Param("maxPrice") Integer maxPrice,
            @Param("platforms") List<PlatformType> platforms,
            @Param("categories") List<GameCategories> categories,
            @Param("availability") ProductAvailabilityType availabilityType,
            @Param("ageLimits") List<AgeLimitType> ageLimits,
            Pageable pageable
    );

    Page<ProductEntity> findAllByAvailability(Pageable pageable, ProductAvailabilityType availability);

    List<ProductEntity> findTop5ByNameIsLikeIgnoreCase(String name);

    ProductEntity findOneById(Long id);
}
