const baseUrl = "http://localhost:8080/api/v1/auth/sign-in";

function signIn() {
    const username = $('#username').val();
    const password = $('#password').val();

    if (!username || !password) {
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Move…",
            footer: "<a href=\"#\">Why do I have this issue?</a>"
        });
        // alert("Please enter both username and password");
        return;
    }

    $.ajax({
        url: baseUrl,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            "username": username,
            "password": password
        }),
        success: function (response) {
            // Standardizing check for common API response patterns
            if (response.status === 200 || response.status === "SUCCESS") {
                localStorage.setItem("token", response.data.accessToken);

                const token = localStorage.getItem("token");
                const payload = JSON.parse(atob(token.split('.')[1]));
                const role = payload.role;
                const userId = payload.id;

                if (role === "ADMIN") {
                    window.location.href = "admin-dashboard.html";
                } else if (role === "USER") {
                    checkBanStatus(userId);
                }
            }
        },
        error: (error) => {
            let msg = "Sign-In failed"
            if (error.responseJSON) {
                msg = error.responseJSON.message || error.responseJSON.data;
            }
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: msg,
                footer: "<a href=\"#\">Why do I have this issue?</a>"
            });
            // alert(msg);
        }
    });
}

function checkBanStatus(userId) {
    $.ajax({
        url: "http://localhost:8080/api/v1/user/ban/check/" + userId,
        method: 'GET',
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function (response) {
            if (response.data === "Banned") {
                window.location.href = "ban-page.html";

            } else {
                window.location.href = "user-home.html";
            }
        },
        error: (error) => {
            let msg = "Sign-In failed"
            if (error.responseJSON) {
                msg = error.responseJSON.message || error.responseJSON.data;
            }
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: msg,
                footer: "<a href=\"#\">Why do I have this issue?</a>"
            });
            // alert(msg);
        }
    });
}