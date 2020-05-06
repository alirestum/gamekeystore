package hu.restumali.gamekeystore.config;

import lombok.NoArgsConstructor;

public class NoValidBillingAddressException extends RuntimeException {

    public NoValidBillingAddressException(String errorMessage){
        super(errorMessage);
    }
}
