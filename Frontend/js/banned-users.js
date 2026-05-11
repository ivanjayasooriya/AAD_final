const baseUrl = "http://localhost:8080/api/v1/user";
const token = localStorage.getItem("token");
const payload = token ? JSON.parse(atob(token.split('.')[1])) : { sub: "Guest" };
const adminId = payload.id;

$(document).ready(function () {
    fetchBannedUsers();
});

// 🚀 Fetch banned users
function fetchBannedUsers() {
    $.ajax({
        url: `${baseUrl}/ban/all`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function (res) {
            const tbody = $("#bannedTable tbody").empty();

            res.data.forEach(user => {
                const row = `
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td class="text-end">
                            <button class="btn btn-sm btn-outline-primary"
                                onclick="openCheck('${user.id}', '${user.username}', '${user.email}', '${user.banReason || ""}')">
                                Check
                            </button>

                            <button class="btn btn-sm btn-outline-warning ms-2"
                                onclick="openExtend('${user.id}')">
                                Extend
                            </button>
                        </td>
                    </tr>
                `;
                tbody.append(row);
            });
        }
    });
}

function addAction(userId, activity) {
    $.ajax({
        url: "http://localhost:8080/api/v1/activity/add",
        method: "POST",
        headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
        contentType: "application/json",
        data: JSON.stringify(
            { userId: userId, activity: activity, activityDate: new Date().toISOString() }),
        success: () => {
            console.log("Log added successfully");
        },
        error: (error) => {
            console.error("Error adding log:", error);
        }
    });
}

function openCheck(id, username, email, reason) {
    $("#checkUserId").val(id);
    $("#checkUsername").text(username);
    $("#checkEmail").text(email);
    $("#banReason").text(reason || "No reason provided");
    getUserAppeals(id);

    $("#checkModal").modal("show");
}

function getUserAppeals(id) {
    $.ajax({
        url: baseUrl +"/ban/get-appeal/" + id,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function (res) {
            $("#checkMessage").text(res.data || "No appeal message");
        },
        error: function (err) {
            console.error(err);
        }
    });
}

function unbanUser() {
    const userId = $("#checkUserId").val();

    $.ajax({
        url: `${baseUrl}/ban/unban/${userId}`,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function () {
            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "User unbanned successfully!",
                showConfirmButton: false,
                timer: 1500
            });
            // alert("User unbanned");
            addAction(adminId, `User Unbanned - ${userId}`);
            $("#checkModal").modal("hide");
            fetchBannedUsers();
        }
    });
}

function openExtend(userId) {
    $("#extendUserId").val(userId);
    $("#extendModal").modal("show");
}

function extendBan() {
    const userId = $("#extendUserId").val();
    const duration = $("#extendDuration").val();
    const reason = $("#extendReason").val();

    $.ajax({
        url: `${baseUrl}/ban/add`,
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        contentType: "application/json",
        data: JSON.stringify({
            userId: userId,
            banDuration: duration,
            banReason: reason
        }),
        success: function () {
            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Ban extended successfully!",
                showConfirmButton: false,
                timer: 1500
            });
            addAction(adminId, `User Ban Extended - ${userId}`)
            // alert("Ban extended");
            $("#extendModal").modal("hide");
            fetchBannedUsers();
        }
    });
}