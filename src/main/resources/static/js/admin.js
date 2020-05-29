let parser = new DOMParser();

async function updateItemCount(element) {
    let productId = element.name;
    let url = 'products/' + productId + '/updatequantity?quantity=' + element.value;
    await fetch(url, {method: 'post'}).then((response) => {
        return response.text();
    }).then((html) => {
        replaceProducts(html);
    });
}

async function removeItem(element) {
    let productId = element.getAttribute('data-productId');
    let url = 'products/' + productId + '/removeproduct';
    await fetch(url, {method: 'post'}).then((response) => {
        return response.text();
    }).then((html) => {
        replaceProducts(html);
    })
}

function replaceProducts(html) {
    let resHtml = parser.parseFromString(html, 'text/html');
    console.log(resHtml);
    document.getElementById('order-products').innerHTML =
        resHtml.getElementById('order-products').innerHTML;
}

function searchProducts(element) {
    if (element.value.length !== 0)
        document.getElementsByClassName('search-result')[0].innerHTML = '';

    if (element.value.length < 2)
        return false;

    let param = element.value;
    let url = '/admin/products/search?name=' + param;
    fetch(url, {method: 'get'}).then((response) => {
        return response.text()
    }).then((html) => {
        let resHtml = parser.parseFromString(html, 'text/html');
        oldTable = document.getElementsByClassName('search-result')[0];
        oldTable.classList.add('search-pre-animation');
        oldTable.innerHTML =
            resHtml.getElementsByClassName('search-result')[0].innerHTML;
        setTimeout(function () {
            oldTable.classList.remove('search-pre-animation')
        }, 1000);
        let names = document.getElementsByClassName('product-search-name');
        let pattern = new RegExp(param, 'gi');
        for (let name of names) {
            name.innerHTML = name.innerText.replace(pattern, param.bold().italics());
        }
    });
}

async function addOrderItem(element) {
    let productId = element.getAttribute('data-productId');
    let url = 'products/' + productId + '/add';
    fetch(url, {method: 'post'}).then((response) => {
        response.text().then((value) => {
            console.log(value);
            let alert = document.getElementsByClassName('alert-success')[0];
            alert.innerText = value;
            alert.classList.remove('d-none');
            setTimeout(() => {
                alert.classList.add('d-none');
            }, 1000)

        });
    })
}

//TODO logically delete products
//TODO pwd change