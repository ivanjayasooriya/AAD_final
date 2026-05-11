const token = localStorage.getItem("token");
const payload = token ? JSON.parse(atob(token.split('.')[1])) : { id: null, sub: "Guest" };
const userId = payload.id;

const sidebar = new bootstrap.Offcanvas(document.getElementById('adminSidebar'));
$('#admin-sidebar-sensor').on('mouseenter', () => sidebar.show());

$(document).ready(fetchGenres);

if (!localStorage.getItem("token")) window.location.href = "sign-in.html";

function fetchGenres() {
    $.ajax({
        url: "http://localhost:8080/api/v1/genre/get-all",
        method: "GET",
        headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
        success: (res) => {
            const tbody = $("#genreTable tbody").empty();
            res.data.forEach(g => {
                tbody.append(`
          <tr>
            <td class="ps-3 text-muted">#${g.id}</td>
            <td>${g.name}</td>
            <td class="text-center">${g.trackCount || 0}</td>
            <td class="text-end pe-3">
              <button class="btn btn-sm btn-outline-primary" onclick="editGenre(${g.id}, '${g.name}')"><i class="bi bi-pencil"></i> Edit</button>
              <button class="btn btn-sm btn-outline-danger ms-2" onclick="deleteGenre(${g.id})"><i class="bi bi-trash"></i></button>
            </td>
          </tr>
        `);
            });
        }
    });
}

function saveGenre() {
    const id = $("#modalGenreId").val();
    const name = $("#modalGenreName").val().trim();
    if (!name) return Swal.fire({
        icon: "error",
        title: "Oops...",
        text: "Genre name cannot be empty!",
        footer: "<a href=\"#\">Why do I have this issue?</a>"
    });
    // alert("Genre name cannot be empty!");

    const url = id ? `http://localhost:8080/api/v1/genre/update/${id}` : `http://localhost:8080/api/v1/genre/add`;
    const type = id ? "PUT" : "POST";

    $.ajax({
        url,
        type,
        headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
        contentType: "application/json",
        data: JSON.stringify({ name }),
        success: () => {
            alert(id ? "Genre updated successfully!" : "Genre added successfully!");
            bootstrap.Modal.getInstance(document.getElementById("genreModal")).hide();
            fetchGenres();
            addAction(userId, id ? `Genre Updated - ${name}` : `Genre Added - ${name}`);
        },
        error: (error) => {
            let msg = "Genre submit failed"
            if (error.responseJSON) {
                msg = error.responseJSON.data || error.responseJSON.message;
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

function deleteGenre(id) {
    if (!confirm("Are you sure you want to delete this genre?")) return;
    $.ajax({
        url: `http://localhost:8080/api/v1/genre/delete/${id}`,
        type: "DELETE",
        headers: { "Authorization": "Bearer " + localStorage.getItem("token") },
        success: (res) => {
            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Genre deleted successfully!",
                showConfirmButton: false,
                timer: 1500
            });
            // alert("Genre deleted successfully!");
            fetchGenres();
            addAction(userId, `Genre Deleted - ${id}`);
        },
        error: (error) => {
            let msg = "Genre delete failed"
            if (error.responseJSON) {
                msg = error.responseJSON.data || error.responseJSON.message;
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

function showAddModal() {
    $("#genreModalTitle").text("Add Genre");
    $("#modalGenreId").val("");
    $("#modalGenreName").val("");
    new bootstrap.Modal(document.getElementById("genreModal")).show();
}

function editGenre(id, name) {
    $("#genreModalTitle").text("Edit Genre");
    $("#modalGenreId").val(id);
    $("#modalGenreName").val(name);
    new bootstrap.Modal(document.getElementById("genreModal")).show();
}

function logout() {
    localStorage.clear();
    window.location.href = "sign-in.html";
}