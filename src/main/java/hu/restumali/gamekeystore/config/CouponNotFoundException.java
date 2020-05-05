package hu.restumali.gamekeystore.config;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
