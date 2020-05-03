function applyCoupon() {
    let couponName = document.getElementById('coupon-code').value;
    let url = '/cart/applycoupon?couponName=' + couponName;
    fetch(url, {
        method: 'post'
    }).then((response) => {
        return response.text();
    }).then((value) => document.getElementById('cart-total').innerText = 'Cart total: ' + value)
}