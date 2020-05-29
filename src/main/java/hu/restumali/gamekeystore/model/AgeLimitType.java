package hu.restumali.gamekeystore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AgeLimitType {

    All("Any"),
    ESRBE("Everyone"),
    ESRBEPLUS("Everyone 10+"),
    ESRBT("Teen"),
    ESRBM("Mature 17+"),
    ESRBA("Adults only 18+");

    @Getter
    private String name;
}
