document.addEventListener('DOMContentLoaded', cargarPreguntas);

const idBitacora = document.getElementById('idBitacora').value;
const idPractica = document.getElementById('idPractica').value;

// 1. Cargar las preguntas configuradas por el Director
function cargarPreguntas() {
    fetch(`api/preguntas?idPractica=${idPractica}`)
    .then(res => res.json())
    .then(data => {
        const contenedor = document.getElementById('contenedorPreguntas');
        contenedor.innerHTML = '';

        if (data.length === 0) {
            contenedor.innerHTML = `
                <div class="alert alert-warning">
                    No hay preguntas registradas para esta práctica.
                </div>
            `;
            return;
        }

        data.forEach(p => {
            contenedor.innerHTML += `
                <div class="card mb-4 shadow-sm" style="border-radius: 16px;">
                    <div class="card-body">
                        <h6 class="fw-bold text-primary mb-2">Pregunta</h6>
                        <p class="mb-3">${p.pregunta}</p>

                        <label class="form-label fw-semibold">Tu respuesta</label>
                        <textarea 
                            id="respuesta_${p.idPregunta}" 
                            class="form-control mb-3"
                            rows="3" 
                            placeholder="Escribe tu respuesta aquí..."
                        ></textarea>

                        <button class="btn btn-primary w-100" onclick="enviarRespuesta(${p.idPregunta})">
                            Guardar Respuesta
                        </button>
                    </div>
                </div>
            `;
        });
    });
}

// 2. Enviar la respuesta a una pregunta específica
function enviarRespuesta(idPregunta) {
    const textoRespuesta = document.getElementById(`respuesta_${idPregunta}`).value;
    
    if(!textoRespuesta) {
        alert('Debes escribir una respuesta');
        return;
    }

    const params = new URLSearchParams();
    params.append('idPregunta', idPregunta);
    params.append('idBitacora', idBitacora);
    params.append('textoRespuesta', textoRespuesta);

    fetch('api/respuestas', { method: 'POST', body: params })
    .then(res => res.json())
    .then(data => {
        if(data.status === 'success') {
            alert('Respuesta guardada con éxito');
        } else {
            alert('Error: ' + data.mensaje);
        }
    });
}

// 3. Enviar Evidencia (URL y Descripción)
document.getElementById('formEvidencia').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const params = new URLSearchParams();
    params.append('idBitacora', idBitacora);
    params.append('urlArchivo', document.getElementById('urlArchivo').value);
    params.append('descripcion', document.getElementById('descripcion').value);

    fetch('api/evidencias', { method: 'POST', body: params })
    .then(res => res.json())
    .then(data => {
        if(data.status === 'success') {
            alert('Evidencia subida con éxito');
            document.getElementById('urlArchivo').value = '';
            document.getElementById('descripcion').value = '';
        } else {
            alert('Error al subir evidencia: ' + data.mensaje);
        }
    });
});