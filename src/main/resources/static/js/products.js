async function addToCart(button) {
    let productId = button.getAttribute('data-productId');
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/addtocart?productId=' + productId, true);
    xhr.send();
    updateCartCounter();
    document.getElementsByClassName('cart-success')[0].classList.remove('cart-success-hidden');
    setTimeout(() =>{
        document.getElementsByClassName('cart-success')[0].classList.add('cart-success-hidden');
    }, 5000);
}

function updateCartCounter() {
    let cart = document.getElementById('cart-icon');
    let current = cart.getAttribute('cart-counter');
    cart.setAttribute('cart-counter', current++);
}

async function filter(size, page) {
    let filterForm = document.getElementById('filter');
    let categoryCheckBoxes = filterForm.getElementsByClassName('form-check-input');
    let categories = [];
    let parser = new DOMParser();
    for (let item of categoryCheckBoxes){
        if (item.checked){
            categories.push(item.value);
        }
    }
    let maxPrice = document.getElementById('priceFilter').value;
    let platform = document.getElementById('platformFilter').value;
    let ageLimit = document.getElementById('ageLimitFilter').value;
    let itemCnt = document.getElementById('itemcnt').value;
    let requestParam = '?platform=' + platform + '&maxprice=' + maxPrice + '&categories=' + categories
        + '&agelimit=' + ageLimit + '&page=' + page + '&size=' + itemCnt;
    fetch('/api/products/filter' + requestParam).then((response) =>{
       return response.text();
    }).then((html) => {
        let resHtml = parser.parseFromString(html, 'text/html');
        document.getElementsByClassName('products-container')[0].innerHTML =
            resHtml.getElementsByClassName('row')[0].outerHTML;

        filterForm = document.getElementById('filter');
        categoryCheckBoxes = filterForm.getElementsByClassName('form-check-input');
        for (let item of categoryCheckBoxes){
            if (categories.includes(item.value)){
                item.checked = true;
            }
        }
        document.getElementById('priceFilter').value = maxPrice;
        document.getElementById('platformFilter').value = platform;
        document.getElementById('ageLimitFilter').value = ageLimit;
        document.getElementById('itemcnt').value = itemCnt;
        window.scrollTo({
            behavior: "smooth",
            left: 0,
            top: html
        });
    });
}

async function paginate(page) {
    let pageNumber = page.id;
    let pageSize = document.getElementById('itemcnt').value;
    await filter(pageSize, pageNumber);
}

async function itemsPerPage(select) {
    let itemCnt = select.value;
    await filter(itemCnt, 0);
    document.getElementById('itemcnt').value = itemCnt;
}

async function searchProducts(element) {
    if (element.value.length !== 0 )
        document.getElementsByClassName('search')[0].innerHTML = '';

    if (element.value.length < 2)
        return false;

    let parser = new DOMParser();
    let param = element.value;
    let url = '/api/products/search?name=' + param;
    fetch(url, {method: 'post'}).then((response) => {
       return response.text()
    }).then((html) =>{
        let resHtml = parser.parseFromString(html,'text/html');
        document.getElementsByClassName('search')[0].innerHTML =
            resHtml.getElementsByClassName('table')[0].outerHTML;

        let names = document.getElementsByClassName('product-search-name');
        let pattern = new RegExp(param, 'gi');
        for (let name of names){
            name.innerHTML = name.innerText.replace(pattern, param.bold().italics());
            name.innerText[0] = name.innerText[0].toUpperCase();
        }
    });
}
