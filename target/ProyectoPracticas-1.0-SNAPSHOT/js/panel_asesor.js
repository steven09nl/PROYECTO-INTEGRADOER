document.addEventListener('DOMContentLoaded', cargarBitacoras);

function cargarBitacoras() {
    fetch('api/calificaciones')
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector('#tablaBitacoras tbody');
        tbody.innerHTML = '';

        if (data.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-muted fw-semibold">
                        No hay bitácoras registradas.
                    </td>
                </tr>
            `;
            return;
        }

        data.forEach(b => {

            // Estado con estilo
            let estadoTexto = b.estado || "Enviada";
            let estadoBadge = "bg-secondary";

            if (estadoTexto.toLowerCase().includes("aprob")) estadoBadge = "bg-success";
            if (estadoTexto.toLowerCase().includes("reprob")) estadoBadge = "bg-danger";
            if (estadoTexto.toLowerCase().includes("correcc")) estadoBadge = "bg-warning text-dark";
            if (estadoTexto.toLowerCase().includes("enviad")) estadoBadge = "bg-primary";

            // Calificación con estilo
            let calificacionTexto = (b.calificacion > 0) ? b.calificacion : "Sin calificar";

            tbody.innerHTML += `
                <tr>
                    <td class="fw-semibold">${b.idBitacora}</td>
                    <td>${b.idEstudiante}</td>
                    <td>
                        <span class="badge ${estadoBadge} px-3 py-2">
                            ${estadoTexto}
                        </span>
                    </td>
                    <td>${b.fechaEnvio}</td>
                    <td>
                        <span class="fw-semibold">
                            ${calificacionTexto}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-primary btn-sm fw-semibold"
                            onclick="abrirModal(${b.idBitacora})">
                            Calificar
                        </button>
                    </td>
                </tr>
            `;
        });
    });
}

function abrirModal(idBitacora) {
    document.getElementById('spanIdBitacora').textContent = idBitacora;
    document.getElementById('idBitacoraEvaluar').value = idBitacora;

    // Limpiar campos previos
    document.getElementById('calificacion').value = '';
    document.getElementById('observacion').value = '';

    document.getElementById('modalCalificar').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
}

function cerrarModal() {
    document.getElementById('modalCalificar').style.display = 'none';
    document.getElementById('overlay').style.display = 'none';
}

document.getElementById('formCalificar').addEventListener('submit', function(e) {
    e.preventDefault();

    const idAsesor = document.getElementById('idAsesorLogueado').value;
    const idBitacora = document.getElementById('idBitacoraEvaluar').value;
    const calificacion = document.getElementById('calificacion').value;
    const estado = document.getElementById('estadoBitacora').value;
    const observacion = document.getElementById('observacion').value;

    const params = new URLSearchParams();
    params.append('idAsesor', idAsesor);
    params.append('idBitacora', idBitacora);
    params.append('calificacion', calificacion);
    params.append('estado', estado);
    params.append('observacion', observacion);

    fetch('api/calificaciones', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(res => res.json())
    .then(data => {
        if(data.status === 'success') {
            alert(data.mensaje);
            cerrarModal();
            cargarBitacoras();
        } else {
            alert('Error: ' + data.mensaje);
        }
    })
    .catch(err => console.error('Error:', err));
});