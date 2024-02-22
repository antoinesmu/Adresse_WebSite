let currentUserId;


let deleteUser = function (userId) {
    if (confirm("are you sure you want to delete this user?")) {

        let deleteUserRequest = new XMLHttpRequest();
        let url = "users/delete?id=" + userId;

        deleteUserRequest.open("POST", url, true);

        deleteUserRequest.onload = function () {
            let result = this.response;
            if (result == '"error"') {
                alert("vous ne pouvez pas vous supprimez vous même");
            } else {
                location.reload();
            }
        }
        deleteUserRequest.setRequestHeader("content-type", "application/x-www-form-urlencoded")
        deleteUserRequest.send("delete?id=" + userId);
    }
}