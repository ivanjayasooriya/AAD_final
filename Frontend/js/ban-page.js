const baseUrl = "http://localhost:8080/api/v1/user/ban";
const token = localStorage.getItem("token");
const payload = token ? JSON.parse(atob(token.split('.')[1])) : { sub: "Guest" };
const userId = payload.id;

$(document).ready(function () {
    // loadBanDetails();
});

// 🔄 Load ban info (optional but recommended)
// function loadBanDetails() {
//     $.ajax({
//         url: `${baseUrl}/users/ban-status`,
//         method: "GET",
//         headers: {
//             "Authorization": "Bearer " + localStorage.getItem("token")
//         },
//         success: function (res) {
//             if (res.data.reason) {
//                 $("#banReasonText").text(res.data.reason);
//             }
//
//             if (res.data.expiry) {
//                 const date = new Date(res.data.expiry);
//                 $("#banExpiryText").text("Ban expires on: " + date.toLocaleString());
//             } else {
//                 $("#banExpiryText").text("This ban is permanent.");
//             }
//         }
//     });
// }

function sendAppeal() {
    const message = $("#appealMessage").val();

    if (!message.trim()) {
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Please enter a message",
            footer: "<a href=\"#\">Why do I have this issue?</a>"
        });
        // alert("Please enter a message");
        return;
    }

    $.ajax({
        url: `${baseUrl}/appeal`,
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        contentType: "application/json",
        data: JSON.stringify({
            userId: userId,
            message: message
        }),
        success: function () {
            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Appeal sent successfully!",
                showConfirmButton: false,
                timer: 1500
            });
            // alert("Appeal sent successfully");
            $("#appealMessage").val("");
        },
        error: function () {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Failed to send appeal",
                footer: "<a href=\"#\">Why do I have this issue?</a>"
            });
            // alert("Failed to send appeal");
        }
    });
}

function goToLogin() {
    localStorage.removeItem("token");
    window.location.href = "sign-in.html";
}