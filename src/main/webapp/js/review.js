let currentRestaurantId;
let addReviewVisible = true;
let admin = false;


let loadReview = function () {
    let loadReviewsRequest = new XMLHttpRequest();
    let url = "detail/reviews?id=" + currentRestaurantId;
    loadReviewsRequest.open("GET", url, true);
    loadReviewsRequest.responseType = "json";

    loadReviewsRequest.onload = function () {
        let reviewsList = this.response;
        createReviewsList(reviewsList);
    };

    loadReviewsRequest.send();
}


let verifReview = function (review) {
    if (review.note === "" || review.note === null || review.note === 0) {
        return false;
    } else if (review.champ === "" || review.champ === null) {
        return false;
    } else if (review.titre === "" || review.titre === null) {
        return false;
    }
    return true;
}

let saveReview = function () {
    //GET RESULT FROM RADIO INPUT
    let element = document.getElementsByTagName('input');
    let noteReview;
    for (i = 0; i < element.length; i++) {
        if (element[i].type = "radio") {
            if (element[i].checked) {
                noteReview = element[i].value;
            }
        }
    }

    let review = {
        id: 0,
        id_user: 1,
        id_restaurant: currentRestaurantId,
        titre: document.getElementById("titreInput").value,
        champ: document.getElementById("comment").value,
        note: noteReview
    }


    let createReviewRequest = new XMLHttpRequest();

    if (verifReview(review)) {
        createReviewRequest.open("POST", "detail/reviews/add", true);
        createReviewRequest.responseType = "json";
        createReviewRequest.onload = function () {
            loadReview();
            document.getElementById("addReviewForm").reset();
            showAddReview();
        };

        createReviewRequest.setRequestHeader("content-type", "application/x-www-form-urlencoded");
        createReviewRequest.send("id=" + review.id_restaurant + "&titre=" + review.titre + "&champ=" + review.champ + "&note=" + review.note);
    } else {
        alert("Merci de remplir tous les champs avant d'ajouter un commentaire.");
    }
}

let deleteReview = function (reviewId) {
    if (confirm("Are you sure you want to delete this review ?")) {
        let deleteRequest = new XMLHttpRequest();
        let url = "detail/review/delete?id=" + reviewId;
        deleteRequest.open("POST", url, true);

        deleteRequest.onload = function () {
            loadReview();
        };

        deleteRequest.send();
    }
}

let connectedUser = function () {
    let addReviewsRequest = new XMLHttpRequest();
    let url = "detail/reviews/connexion";
    addReviewsRequest.open("GET", url, true);
    addReviewsRequest.responseType = "json";

    addReviewsRequest.onload = function () {
        let result = this.response;
        if (result === "admin") {
            admin = true;
            generateButton();
            generateEditButton(currentRestaurantId);
        } else if (result === "user") {
            admin = false;
            generateButton();
        } else if (result === "none") {
            admin = false;
            generateRedirectionButton();
        }
        console.log("admin : " + admin);
    };

    addReviewsRequest.send();
}

let showAddReview = function () {
    if (addReviewVisible) {
        document.getElementById("addReview").hidden = false;
    } else {
        document.getElementById("addReview").hidden = true;
    }
    addReviewVisible = !addReviewVisible;
}

window.onload = function () {
    const params = new URLSearchParams(window.location.search);
    currentRestaurantId = params.get("id");
    loadReview();
    connectedUser();

    document.getElementById("addReviewValidBtn").onclick = function () {
        saveReview();
    };
}


////////////////////////////////////////////////
//    Fonction pour génération du contenu     //
////////////////////////////////////////////////

let createReviewsList = function (reviewList) {
    let reviewDiv = document.getElementById("reviewContainer");
    let newReviewList = reviewDiv.cloneNode(false);
    for (const review of reviewList) {
        newReviewList.appendChild(createReviewWrapper(review));
    }
    reviewDiv.parentNode.replaceChild(newReviewList, reviewDiv);
}

let createReviewWrapper = function (review) {
    let reviewWrapper = document.createElement("div");
    reviewWrapper.classList.add("column");

    reviewWrapper.appendChild(createReviewElement(review));
    return reviewWrapper;
}

let createReviewElement = function (review) {
    let reviewCard = document.createElement("section");
    reviewCard.classList.add("cards");
    reviewCard.id = "review-" + review.id;

    reviewCard.appendChild(generateReviewTitle(review));
    reviewCard.appendChild(generateReviewLegende(review));
    reviewCard.appendChild(generateComment(review));

    if (admin) {
        reviewCard.appendChild(generateDeleteButton(review));
    }

    return reviewCard;
}

let generateReviewTitle = function (review) {
    let titre = document.createElement("h4");
    titre.classList.add("title");
    titre.innerHTML = review.titre;
    return titre;
}

let generateReviewLegende = function (review) {
    let legende = document.createElement("div");
    legende.classList.add("main");

    legende.appendChild(generateUserName(review));
    legende.appendChild(generateStars(review));

    return legende;
}

let generateDivColumn = function () {
    let division = document.createElement("div");
    division.classList.add("column");
    return division;
}

let generateUserName = function (review) {
    let divName = generateDivColumn();

    let userName = document.createElement("p");
    userName.classList.add("column");
    userName.classList.add("legende");
    userName.innerHTML = review.userLogin;

    divName.appendChild(userName);
    return divName;
}

let generateStars = function (review) {
    let divStars = generateDivColumn();
    divStars.classList.add("displayLeft");

    let starsContainer = document.createElement("span");
    starsContainer.classList.add("stars");
    let emptyStars = 5 - review.note;

    if (emptyStars > 0) {
        for (let i = 0; i < emptyStars; i++) {
            starsContainer.appendChild(generateStar(false));
        }
    }
    if (review.note > 0) {
        for (let i = 0; i < review.note; i++) {
            starsContainer.appendChild(generateStar(true));
        }
    }

    divStars.appendChild(starsContainer);
    return divStars;
}

let generateComment = function (review) {
    let comment = document.createElement("p");
    comment.classList.add("belowStars");
    comment.innerHTML = review.champ;
    return comment;
}


let generateStar = function (filled) {
    let star = document.createElement("img");

    if (filled) {
        star.setAttribute("src", "./images/starFill.svg");
    } else {
        star.setAttribute("src", "./images/starEmpty.svg");
    }

    return star;
}

let generateDeleteButton = function (review) {
    let deleteButton = document.createElement("button");
    deleteButton.type = "button";
    deleteButton.id = "button-delete-" + review.id;
    deleteButton.classList.add("deleteButton", "button");

    let deleteLink = document.createElement("a");
    deleteLink.href = "#";
    deleteLink.title = "supprimer";
    deleteLink.innerText = "x";
    deleteLink.onclick = function () {
        deleteReview(review.id);
    }

    deleteButton.appendChild(deleteLink);
    return deleteButton;
}


let generateButton = function () {
    console.log("LE BOUTON AFFICHE LE FORM");

    let addButton = document.createElement("button");
    addButton.type = "button";
    addButton.id = "buttonAddReview";
    addButton.classList.add("button");
    addButton.innerText = "Ajouter un commentaire";

    addButton.onclick = function () {
        showAddReview();
    };

    document.getElementById("addButtonContainer").appendChild(addButton);
}

let generateRedirectionButton = function () {
    console.log("LE BOUTON REDIRIGE LE LOGIN");

    let addButton = document.createElement("button");
    addButton.type = "button";
    addButton.id = "buttonAddReview";
    addButton.classList.add("button");

    let loginLink = document.createElement("a");
    loginLink.href = "login";
    loginLink.title = "Se connecter";
    loginLink.innerText = "Ajouter un commentaire";

    addButton.appendChild(loginLink);
    document.getElementById("addButtonContainer").appendChild(addButton);
}

let generateEditButton = function (restaurantId) {
    console.log("LE BOUTON REDIRIGE VERS EDIT");

    let addButton = document.createElement("button");
    addButton.type = "button";
    addButton.id = "buttonEditReview";
    addButton.classList.add("button");

    let loginLink = document.createElement("a");
    loginLink.href = "./admin/edit?id=".concat(restaurantId);
    loginLink.title = "Modifier les données";
    loginLink.innerText = "Modifier les informations du restaurant";

    addButton.appendChild(loginLink);
    document.getElementById("editButtonContainer").appendChild(addButton);

}