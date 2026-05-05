let practicaSeleccionada = 0;

document.addEventListener('DOMContentLoaded', cargarPracticas);

function cargarPracticas() {
    fetch('api/practicas')
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector('#tablaPracticas tbody');
        tbody.innerHTML = '';

        if (data.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="4" class="text-center text-muted fw-semibold">
                        No hay prácticas registradas.
                    </td>
                </tr>
            `;
            return;
        }

        data.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td class="fw-semibold">${p.idPractica}</td>
                    <td>${p.nombre}</td>
                    <td>
                        <span class="badge bg-info text-dark px-3 py-2">
                            ${p.tipoPractica}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-primary btn-sm fw-semibold"
                            onclick="abrirPreguntas(${p.idPractica})">
                            Gestionar Preguntas
                        </button>
                    </td>
                </tr>
            `;
        });
    });
}

document.getElementById('formPractica').addEventListener('submit', function(e) {
    e.preventDefault();

    const params = new URLSearchParams();
    params.append('nombre', document.getElementById('nombre').value);
    params.append('tipo', document.getElementById('tipo').value);
    params.append('horas', document.getElementById('horas').value);
    params.append('inicio', document.getElementById('inicio').value);
    params.append('fin', document.getElementById('fin').value);
    params.append('semestre', document.getElementById('semestre').value);

    fetch('api/practicas', { method: 'POST', body: params })
    .then(() => {
        alert('Práctica creada');

        document.getElementById('formPractica').reset();
        cargarPracticas();
    });
});

function abrirPreguntas(id) {
    practicaSeleccionada = id;
    document.getElementById('spanId').textContent = id;
    document.getElementById('modalPreguntas').style.display = 'block';

    // Si tienes overlay en el HTML, también lo mostramos
    const overlay = document.getElementById('overlay');
    if (overlay) overlay.style.display = 'block';
}

function guardarPregunta() {
    const texto = document.getElementById('nuevaPregunta').value;

    if (!texto.trim()) {
        alert("Debes escribir una pregunta.");
        return;
    }

    const params = new URLSearchParams();
    params.append('idPractica', practicaSeleccionada);
    params.append('pregunta', texto);

    fetch('api/preguntas', { method: 'POST', body: params })
    .then(() => {
        alert('Pregunta agregada');
        document.getElementById('nuevaPregunta').value = '';
    });
}