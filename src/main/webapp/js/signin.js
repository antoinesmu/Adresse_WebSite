function checkPass() {
    var champA = document.getElementById("password").value;
    var champB = document.getElementById("passwordVerif").value;

    if (champA !== champB) {
        resetForm();
        alert("Les mots de passe ne correspondent pas");
    }

}

function resetForm() {
    document.getElementById("signinForm").reset();
}


function getError() {
    let getErrorRequest = new XMLHttpRequest();
    getErrorRequest.open("GET", "signin/verifyLogin", true);

    getErrorRequest.onload = function () {
        let result = this.response;
        if (result == "true") {
            alert("Login already exist");
        } else {
            postForm();
        }
    }
    getErrorRequest.send();
}


window.onload = function () {
    document.getElementById("valider").onclick = function () {
        checkPass();
        getError();
    }
}

let postForm = function () {
    document.getElementById("signinForm").submit();
}