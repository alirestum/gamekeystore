async function applyCoupon() {
    let couponName = document.getElementById('coupon-code').value;
    let url = '/cart/applycoupon?couponName=' + couponName;
    let alert = document.getElementsByClassName('alert-danger')[0];

    fetch(url, {method: 'post'}).then((response) =>{
       response.text().then((value => {
           if (response.status === 400) {
               alert.innerHTML = value;
               alert.classList.remove('d-none');
               setTimeout(() => {
                   alert.classList.add('d-none');
               }, 5000);
           } else {
               document.getElementById('cart-total').innerText = 'Cart total: ' + value;
               document.getElementsByClassName('alert-success')[0].classList.remove('d-none');
               setTimeout(() => {
                   document.getElementsByClassName('alert-success')[0].classList.add('d-none');
               }, 5000);
           }
       })) ;
    });
}

async function updateCount(input){
    let id = input.name;
    let url = '/cart/updatecount?id=' + id + "&quantity=" + input.value;
    await fetch(url, {method: 'post'}).then((response) =>{
        location.reload();
    });
}