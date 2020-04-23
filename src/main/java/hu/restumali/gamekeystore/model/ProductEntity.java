package hu.restumali.gamekeystore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Product must have a name!")
    private String name;

    private String urlName;

    @NotNull(message = "Product must have a base price!")
    @Positive(message = "Product can't be cheaper than 0")
    private Integer basePrice;


    private Integer salePrice;

    @NotNull
    private ProductAvailabilityType availability;

    @Lob
    private String description;

    private String featuredImageUrl;

    @ElementCollection
    private List<String> imagesUrl;

    @ElementCollection
    private Set<GameStyleType> gameStyle;

    @NotNull
    private PlatformType platform;

}
