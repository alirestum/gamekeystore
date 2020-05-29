async function animateRating(element) {
    let rating = element.getAttribute('data-rating');
    let id = element.getAttribute('data-productId');
    let stars = document.getElementById(id).children;
    for (let i = 0; i < 5; i++){
        if (i<rating){
            stars[i].classList.remove('far');
            stars[i].classList.add('fas');
        } else {
            stars[i].classList.remove('fas');
            stars[i].classList.add('far' );
        }
    }
}

async function rateProduct(element) {
    let rating = element.getAttribute('data-rating');
    let id = element.getAttribute('data-productId');
    let url = 'rateproducts/' + id + '?rating=' + rating;
    fetch(url, {method: 'post'}).then((response) =>{
        return response.text();
    }).then((value) =>{
        let parser = new DOMParser();
        let resHtml = parser.parseFromString(value, 'text/html').getElementsByTagName('html')[0];
        document.getElementsByTagName('html')[0].innerHTML =
            resHtml.innerHTML;
        location.reload();

    });
}