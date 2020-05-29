package hu.restumali.gamekeystore.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Product must have a name!")
    private String name;

    private String urlName;

    @NotNull(message = "Product must have a base price!")
    @Positive(message = "Product can't be cheaper than 0")
    private Integer basePrice;


    private Integer salePrice;

    @NotNull
    private ProductAvailabilityType availability;

    @ElementCollection
    List<Integer> ratings;

    private String description;

    private String featuredImageUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "game_categories", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<GameCategories> categories = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private PlatformType platform;

    private Boolean featured;

    @Enumerated(EnumType.STRING)
    private AgeLimitType ageLimit;

}
