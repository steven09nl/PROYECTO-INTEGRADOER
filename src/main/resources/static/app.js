async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });
  const text = await response.text();
  try {
    return { status: response.status, body: JSON.parse(text) };
  } catch {
    return { status: response.status, body: text };
  }
}

document.getElementById('btnHealth').addEventListener('click', async () => {
  const result = await requestJson('/api/health');
  document.getElementById('healthResult').textContent = JSON.stringify(result.body, null, 2);
});

document.getElementById('btnPracticas').addEventListener('click', async () => {
  const result = await requestJson('/api/practicas');
  document.getElementById('practicasResult').textContent = JSON.stringify(result.body, null, 2);
});

document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const form = new FormData(e.target);
  const payload = Object.fromEntries(form.entries());
  const result = await requestJson('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify(payload)
  });
  document.getElementById('loginResult').textContent = JSON.stringify(result.body, null, 2);
});

document.getElementById('bitacoraForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const form = new FormData(e.target);
  const payload = {
    idEstudiante: Number(form.get('idEstudiante')),
    idPractica: Number(form.get('idPractica')),
    estado: form.get('estado'),
    modalidad: form.get('modalidad'),
    fechaEnvio: form.get('fechaEnvio'),
    calificacion: form.get('calificacion') ? Number(form.get('calificacion')) : null
  };
  const result = await requestJson('/api/bitacoras', {
    method: 'POST',
    body: JSON.stringify(payload)
  });
  document.getElementById('bitacoraResult').textContent = JSON.stringify(result.body, null, 2);
});
