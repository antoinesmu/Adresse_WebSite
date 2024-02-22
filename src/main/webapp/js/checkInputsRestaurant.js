let checkName = function () {
    const nameInput = document.getElementById("name");
    if (!nameInput.checkValidity()) {
        document.getElementById("div_name").classList.toggle("show");
    }
}

let checkSpecialty = function () {
    const specialtyInput = document.getElementById("specialty");
    if (!specialtyInput.checkValidity()) {
        document.getElementById("div_specialty").classList.toggle("show");
    }
}

let checkAddress = function () {
    const addressInput = document.getElementById("address");
    if (!addressInput.checkValidity()) {
        document.getElementById("div_address").classList.toggle("show");
    }
}

let checkPhone = function () {
    const phoneInput = document.getElementById("phone");
    if (!phoneInput.checkValidity()) {
        document.getElementById("div_phone").classList.toggle("show");
    }
}

let checkWebsite = function () {
    const websiteInput = document.getElementById("website");
    if (!websiteInput.checkValidity()) {
        document.getElementById("div_website").classList.toggle("show");
    }
}

let checkPrice = function () {
    const priceInput = document.getElementById("price");
    if (!priceInput.checkValidity()) {
        document.getElementById("div_price").classList.toggle("show");
    }
}


window.onload = function () {

    document.getElementById("submit").onclick = function () {
        /*
        let validation = checkName();
        checkSpecialty();
        checkAddress();
        checkWebsite();
        checkPrice();

         */
        if (validation) {
            console.log("-------------------");
            console.log("/ ça fonctionne ! /");
            console.log("-------------------");
            document.getElementById("submit").submit();
        }
    };
}
