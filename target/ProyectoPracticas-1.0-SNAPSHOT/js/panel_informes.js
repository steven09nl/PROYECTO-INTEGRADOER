document.addEventListener('DOMContentLoaded', cargarInformes);

function cargarInformes() {
    fetch('api/informes')
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector('#tablaInformes tbody');
        tbody.innerHTML = '';

        if (data.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center text-muted fw-semibold">
                        No hay informes registrados todavía.
                    </td>
                </tr>
            `;
            return;
        }

        data.forEach(info => {

            // Badge dependiendo del tipo
            let badgeClass = "bg-primary";

            if (info.tipoInforme.toLowerCase().includes("final")) {
                badgeClass = "bg-success";
            }
            if (info.tipoInforme.toLowerCase().includes("consolidado")) {
                badgeClass = "bg-info text-dark";
            }

            tbody.innerHTML += `
                <tr>
                    <td class="fw-semibold">${info.idInforme}</td>
                    <td>
                        <span class="badge ${badgeClass} px-3 py-2">
                            ${info.tipoInforme}
                        </span>
                    </td>
                    <td>${info.periodo}</td>
                    <td>${info.fechaGeneracion}</td>
                    <td>
                        <a href="${info.urlArchivo}" target="_blank" class="btn btn-outline-primary btn-sm fw-semibold">
                            Ver Documento
                        </a>
                    </td>
                </tr>
            `;
        });
    })
    .catch(error => console.error('Error al cargar informes:', error));
}

document.getElementById('formInforme').addEventListener('submit', function(e) {
    e.preventDefault();

    const idUsuarioGen = document.getElementById('idUsuarioLogueado').value;
    const tipoInforme = document.getElementById('tipoInforme').value;
    const periodo = document.getElementById('periodo').value;
    const urlArchivo = document.getElementById('urlArchivo').value;

    const params = new URLSearchParams();
    params.append('idUsuarioGen', idUsuarioGen);
    params.append('tipoInforme', tipoInforme);
    params.append('periodo', periodo);
    params.append('urlArchivo', urlArchivo);

    fetch('api/informes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(res => res.json())
    .then(data => {
        if(data.status === 'success') {
            alert(data.mensaje);

            // Limpiar formulario
            document.getElementById('formInforme').reset();

            // Recargar tabla
            cargarInformes();
        } else {
            alert('Error: ' + data.mensaje);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error en la comunicación con el servidor.');
    });
});