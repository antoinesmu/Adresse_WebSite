function updateStatus(userId) {
    let setPrivilegeRequest = new XMLHttpRequest();
    setPrivilegeRequest.open("POST", "users/privilege?id=" + userId, true);

    setPrivilegeRequest.onload = function () {
        let result = this.response;
        if (result == '"error"') {
            alert("You can not modify your own status");
            location.reload();
        }
    }
    setPrivilegeRequest.setRequestHeader(
        "Content-type",
        "application/x-www-form-urlencoded"
    );
    setPrivilegeRequest.send("privilege?id=" + userId);
}
