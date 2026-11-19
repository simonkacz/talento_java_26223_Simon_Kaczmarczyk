package com.techlab.articulo.controller;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.service.ArticuloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*")
public class ArticuloController {

    private final ArticuloService articuloService;

    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    // GET - Listar todos los artículos
    @GetMapping
    public ResponseEntity<List<Articulo>> listarArticulos() {
        return ResponseEntity.ok(articuloService.listarTodos());
    }

    // GET - Obtener artículo por código (ID)
    @GetMapping("/{id}")
    public ResponseEntity<Articulo> obtenerArticulo(@PathVariable Long id) {
        return articuloService.obtenerPorCodigo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Crear nuevo artículo
    @PostMapping
    public ResponseEntity<Articulo> crearArticulo(@RequestBody Articulo articulo) {
        Articulo creado = articuloService.guardar(articulo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT - Actualizar artículo existente
    @PutMapping("/{id}")
    public ResponseEntity<Articulo> actualizarArticulo(@PathVariable Long id, @RequestBody Articulo articulo) {
        return articuloService.actualizar(id, articulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Eliminar artículo por código (ID)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable Long id) {
        if (articuloService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
