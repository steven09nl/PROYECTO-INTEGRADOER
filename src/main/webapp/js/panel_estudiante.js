document.getElementById('horasForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const idBitacora = document.getElementById('idBitacora').value;
    const fecha = document.getElementById('fecha').value;
    const horaEntrada = document.getElementById('horaEntrada').value;
    const horaSalida = document.getElementById('horaSalida').value;

    const params = new URLSearchParams();
    params.append('idBitacora', idBitacora);
    params.append('fecha', fecha);
    params.append('horaEntrada', horaEntrada);
    params.append('horaSalida', horaSalida);

    fetch('api/registro_horas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        const mensajeEl = document.getElementById('mensaje');

        mensajeEl.textContent = data.mensaje;
        mensajeEl.className = ""; // limpiar clases anteriores

        if (data.status === "success") {
            mensajeEl.classList.add("alert", "alert-success", "mt-3", "fw-semibold");
            document.getElementById("horasForm").reset();
        } else {
            mensajeEl.classList.add("alert", "alert-danger", "mt-3", "fw-semibold");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Ocurrió un error al enviar las horas.');
    });
});