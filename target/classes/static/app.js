const API_ARTICULOS = "/api/articulos";
const API_CATEGORIAS = "/api/categorias";

let articulosCache = [];
let modoEdicion = false;

document.addEventListener("DOMContentLoaded", () => {
    cargarCategorias();
    cargarArticulos();

    document.getElementById("tipo").addEventListener("change", actualizarCamposPorTipo);
    document.getElementById("articulo-form").addEventListener("submit", guardarArticulo);
    document.getElementById("btn-cancelar").addEventListener("click", resetearFormulario);
    document.getElementById("btn-refrescar").addEventListener("click", () => {
        cargarArticulos(true);
    });
    document.getElementById("buscador").addEventListener("input", renderizarTabla);
    document.getElementById("filtro-tipo").addEventListener("change", renderizarTabla);
});

function actualizarCamposPorTipo() {
    const tipo = document.getElementById("tipo").value;
    const grupoElec = document.getElementById("grupo-electronico");
    const grupoAlim = document.getElementById("grupo-alimenticio");

    grupoElec.classList.add("hidden");
    grupoAlim.classList.add("hidden");

    if (tipo === "ELECTRONICO") {
        grupoElec.classList.remove("hidden");
    } else if (tipo === "ALIMENTICIO") {
        grupoAlim.classList.remove("hidden");
    }
}

async function cargarCategorias() {
    try {
        const res = await fetch(API_CATEGORIAS);
        if (!res.ok) return;
        const categorias = await res.json();
        const select = document.getElementById("categoria");
        select.innerHTML = '<option value="">Sin categoría</option>';
        categorias.forEach(cat => {
            const opt = document.createElement("option");
            opt.value = cat.codigo;
            opt.textContent = cat.nombre;
            select.appendChild(opt);
        });
    } catch (err) {
        console.error("Error cargando categorías:", err);
    }
}

async function cargarArticulos(notificar = false) {
    try {
        const res = await fetch(API_ARTICULOS);
        if (!res.ok) throw new Error("Error HTTP " + res.status);
        articulosCache = await res.json();
        renderizarTabla();
        if (notificar) {
            mostrarToast(`GET /api/articulos -> ${res.status} OK (${articulosCache.length} registros)`);
        }
    } catch (err) {
        mostrarToast("No se pudo conectar con el backend Spring Boot.", true);
    }
}

function renderizarTabla() {
    const tbody = document.getElementById("tabla-articulos");
    const texto = document.getElementById("buscador").value.toLowerCase().trim();
    const filtroTipo = document.getElementById("filtro-tipo").value;

    const filtrados = articulosCache.filter(art => {
        const coincideTexto =
            art.nombre.toLowerCase().includes(texto) ||
            String(art.codigo).includes(texto);
        const coincideTipo =
            filtroTipo === "TODOS" || art.tipoArticulo === filtroTipo;
        return coincideTexto && coincideTipo;
    });

    document.getElementById("contador-articulos").textContent =
        `${filtrados.length} artículo(s)`;

    if (filtrados.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="empty-msg">No hay artículos para mostrar.</td></tr>`;
        return;
    }

    tbody.innerHTML = filtrados.map(art => {
        const claseTag =
            art.tipoArticulo === "Electrónico" ? "tag-electronico" :
            art.tipoArticulo === "Alimenticio" ? "tag-alimenticio" : "tag-general";

        const nombreCategoria = art.categoria ? art.categoria.nombre : "Sin categoría";
        const precioFormateado = Number(art.precio).toLocaleString("es-AR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });

        return `
            <tr>
                <td><strong>#${art.codigo}</strong></td>
                <td><span class="tag ${claseTag}">${art.tipoArticulo}</span></td>
                <td>${escapeHtml(art.nombre)}</td>
                <td>${escapeHtml(nombreCategoria)}</td>
                <td>${escapeHtml(art.detalleEspecifico || "-")}</td>
                <td>$${precioFormateado}</td>
                <td>
                    <div class="actions-cell">
                        <button class="btn btn-sm btn-edit" onclick="prepararEdicion(${art.codigo})">Editar</button>
                        <button class="btn btn-sm btn-delete" onclick="eliminarArticulo(${art.codigo})">Eliminar</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

async function guardarArticulo(event) {
    event.preventDefault();

    const codigo = document.getElementById("articulo-codigo").value;
    const tipo = document.getElementById("tipo").value;
    const nombre = document.getElementById("nombre").value.trim();
    const precio = parseFloat(document.getElementById("precio").value);
    const categoriaCodigo = document.getElementById("categoria").value;

    const payload = {
        tipo: tipo,
        nombre: nombre,
        precio: precio,
        categoria: categoriaCodigo ? { codigo: Number(categoriaCodigo) } : null
    };

    if (tipo === "ELECTRONICO") {
        const garantia = document.getElementById("garantiaMeses").value;
        payload.garantiaMeses = garantia !== "" ? Number(garantia) : 0;
    } else if (tipo === "ALIMENTICIO") {
        const dias = document.getElementById("diasParaVencimiento").value;
        payload.diasParaVencimiento = dias !== "" ? Number(dias) : 0;
    }

    try {
        const url = modoEdicion ? `${API_ARTICULOS}/${codigo}` : API_ARTICULOS;
        const method = modoEdicion ? "PUT" : "POST";

        const res = await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error("Error HTTP " + res.status);

        mostrarToast(`${method} ${url} -> ${res.status} OK`);
        resetearFormulario();
        await cargarArticulos();
    } catch (err) {
        mostrarToast("Error al guardar el artículo: " + err.message, true);
    }
}

function prepararEdicion(codigo) {
    const art = articulosCache.find(a => a.codigo === codigo);
    if (!art) return;

    modoEdicion = true;
    document.getElementById("form-title").textContent = `Editando Artículo #${art.codigo}`;
    document.getElementById("btn-guardar").textContent = "Actualizar Artículo (PUT)";
    document.getElementById("btn-cancelar").classList.remove("hidden");

    document.getElementById("articulo-codigo").value = art.codigo;
    document.getElementById("nombre").value = art.nombre;
    document.getElementById("precio").value = art.precio;
    document.getElementById("categoria").value = art.categoria ? art.categoria.codigo : "";

    const selectTipo = document.getElementById("tipo");
    if (art.tipoArticulo === "Electrónico") {
        selectTipo.value = "ELECTRONICO";
        document.getElementById("garantiaMeses").value = art.garantiaMeses ?? 0;
    } else if (art.tipoArticulo === "Alimenticio") {
        selectTipo.value = "ALIMENTICIO";
        document.getElementById("diasParaVencimiento").value = art.diasParaVencimiento ?? 0;
    } else {
        selectTipo.value = "GENERAL";
    }
    selectTipo.disabled = true;
    actualizarCamposPorTipo();
}

async function eliminarArticulo(codigo) {
    if (!confirm(`¿Seguro que querés eliminar el artículo #${codigo}?`)) return;

    try {
        const url = `${API_ARTICULOS}/${codigo}`;
        const res = await fetch(url, { method: "DELETE" });
        if (!res.ok) throw new Error("Error HTTP " + res.status);

        mostrarToast(`DELETE ${url} -> ${res.status} No Content`);
        await cargarArticulos();
    } catch (err) {
        mostrarToast("Error al eliminar: " + err.message, true);
    }
}

function resetearFormulario() {
    modoEdicion = false;
    document.getElementById("articulo-form").reset();
    document.getElementById("articulo-codigo").value = "";
    document.getElementById("tipo").disabled = false;
    document.getElementById("form-title").textContent = "Nuevo Artículo";
    document.getElementById("btn-guardar").textContent = "Guardar Artículo (POST)";
    document.getElementById("btn-cancelar").classList.add("hidden");
    actualizarCamposPorTipo();
}

function mostrarToast(mensaje, esError = false) {
    const toast = document.getElementById("toast");
    toast.textContent = mensaje;
    toast.classList.toggle("error", esError);
    toast.classList.remove("hidden");
    clearTimeout(window._toastTimer);
    window._toastTimer = setTimeout(() => {
        toast.classList.add("hidden");
    }, 3500);
}

function escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
}
