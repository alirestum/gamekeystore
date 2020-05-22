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