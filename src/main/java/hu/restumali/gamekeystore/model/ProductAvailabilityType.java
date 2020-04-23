package hu.restumali.gamekeystore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProductAvailabilityType {

    InStock("In stock"),
    OutOfStock("Out of stock"),
    Unavailable("Unavailable"),
    FewItemsLeft("Only few items left");

    @Getter
    private String friendlyName;

}
