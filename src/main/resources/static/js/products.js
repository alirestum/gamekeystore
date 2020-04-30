async function addToCart(button) {
    let productId = button.getAttribute('data-productId');
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/addtocart?productId=' + productId, true);
    xhr.send();
    updateCartCounter();
}

function updateCartCounter() {
    let cart = document.getElementById('cart-icon');
    let current = cart.getAttribute('cart-counter');
    cart.setAttribute('cart-counter', current++);
}

function filter(form) {
    let categoryCheckBoxes = form.getElementsByClassName('form-check-input');
    let categories = [];
    let parser = new DOMParser();
    for (let item of categoryCheckBoxes){
        if (item.checked){
            categories.push(item.value);
        }
    }
    let maxPrice = document.getElementById('priceFilter').value;
    let platform = document.getElementById('platformFilter').value;
    let requestParam = '?platform=' + platform + '&maxprice=' + maxPrice + '&categories=' + categories;
    fetch('/api/products/filter' + requestParam).then((response) =>{
       return response.text();
    }).then((html) => {
        let resHtml = parser.parseFromString(html, 'text/html');
        document.getElementsByClassName('products')[0].innerHTML =
            resHtml.getElementsByClassName('products')[0].innerHTML;
    });
    console.log(requestParam);
}