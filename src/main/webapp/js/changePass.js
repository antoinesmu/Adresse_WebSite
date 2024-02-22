let changePasswordVisible = true;


let showChangePassword = function () {
    if (changePasswordVisible) {
        document.getElementById("changePassword").hidden = false;
    } else {
        document.getElementById("changePassword").hidden = true;
    }
    changePasswordVisible = !changePasswordVisible;
}

window.onload = function () {

    document.getElementById("buttonChangePassword").onclick = function () {
        showChangePassword();
    };
    document.getElementById("changePassValidBtn").onclick = function () {
        checkPass();
    };
}

function checkPass() {
    var champA = document.getElementById("newPassword").value;
    var champB = document.getElementById("verifyPassword").value;

    if (champA !== champB) {
        resetForm();
        alert("Les mots de passe ne correspondent pas");

    }

}

function resetForm() {
    document.getElementById("changePasswordForm").reset();
}
