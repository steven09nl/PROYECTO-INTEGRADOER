document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const mensajeError = document.getElementById('mensajeError');

    const btn = document.querySelector("#loginForm button[type='submit']");

    // Limpiar mensajes previos
    mensajeError.style.display = 'none';
    mensajeError.className = "";

    // Deshabilitar botón mientras carga
    btn.disabled = true;
    btn.textContent = "Ingresando...";

    const params = new URLSearchParams();
    params.append('email', email);
    params.append('password', password);

    fetch('api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {

        if (data.status === 'success') {

            // Normalizar rol
            const rol = (data.rol || '').trim().toLowerCase();

            // Redirigir según rol
            if (rol === 'estudiante') {
                window.location.href = 'panel_estudiante.html';
            } else if (rol === 'asesor') {
                window.location.href = 'panel_asesor.html';
            } else if (rol === 'administrador') {
                window.location.href = 'panel_director.html';
            } else {
                mensajeError.textContent = "Error: Rol de usuario no reconocido (" + data.rol + ").";
                mensajeError.style.display = 'block';
                mensajeError.classList.add("alert", "alert-danger", "mt-3", "fw-semibold");
            }

        } else {
            mensajeError.textContent = data.mensaje;
            mensajeError.style.display = 'block';
            mensajeError.classList.add("alert", "alert-danger", "mt-3", "fw-semibold");
        }

    })
    .catch(error => {
        console.error('Error en la conexión:', error);
        mensajeError.textContent = "Error al conectar con el servidor.";
        mensajeError.style.display = 'block';
        mensajeError.classList.add("alert", "alert-danger", "mt-3", "fw-semibold");
    })
    .finally(() => {
        // Restaurar botón
        btn.disabled = false;
        btn.textContent = "Ingresar";
    });
});