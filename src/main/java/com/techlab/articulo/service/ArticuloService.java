package com.techlab.articulo.service;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.ArticuloRepository;
import com.techlab.articulo.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final CategoriaRepository categoriaRepository;

    public ArticuloService(ArticuloRepository articuloRepository, CategoriaRepository categoriaRepository) {
        this.articuloRepository = articuloRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Articulo> listarTodos() {
        return articuloRepository.findAll();
    }

    public Optional<Articulo> obtenerPorCodigo(Long codigo) {
        return articuloRepository.findById(codigo);
    }

    public Articulo guardar(Articulo articulo) {
        resolverCategoria(articulo);
        return articuloRepository.save(articulo);
    }

    public Optional<Articulo> actualizar(Long codigo, Articulo datosActualizados) {
        return articuloRepository.findById(codigo).map(articuloExistente -> {
            articuloExistente.setNombre(datosActualizados.getNombre());
            articuloExistente.setPrecio(datosActualizados.getPrecio());

            if (datosActualizados.getCategoria() != null && datosActualizados.getCategoria().getCodigo() != null) {
                categoriaRepository.findById(datosActualizados.getCategoria().getCodigo())
                        .ifPresent(articuloExistente::setCategoria);
            } else if (datosActualizados.getCategoria() == null) {
                // Mantiene la categoría previa si no se envió en el JSON (útil para PUT simple en Postman)
            }

            if (articuloExistente instanceof ArticuloElectronico electronicoExistente
                    && datosActualizados instanceof ArticuloElectronico electronicoNuevo) {
                electronicoExistente.setGarantiaMeses(electronicoNuevo.getGarantiaMeses());
            } else if (articuloExistente instanceof ArticuloAlimenticio alimenticioExistente
                    && datosActualizados instanceof ArticuloAlimenticio alimenticioNuevo) {
                alimenticioExistente.setDiasParaVencimiento(alimenticioNuevo.getDiasParaVencimiento());
            }

            return articuloRepository.save(articuloExistente);
        });
    }

    public boolean eliminar(Long codigo) {
        if (articuloRepository.existsById(codigo)) {
            articuloRepository.deleteById(codigo);
            return true;
        }
        return false;
    }

    private void resolverCategoria(Articulo articulo) {
        if (articulo.getCategoria() != null && articulo.getCategoria().getCodigo() != null) {
            Categoria categoriaGestionada = categoriaRepository
                    .findById(articulo.getCategoria().getCodigo())
                    .orElse(null);
            articulo.setCategoria(categoriaGestionada);
        } else {
            articulo.setCategoria(null);
        }
    }
}
