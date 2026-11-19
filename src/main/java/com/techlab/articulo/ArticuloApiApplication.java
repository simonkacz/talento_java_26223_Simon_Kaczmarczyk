package com.techlab.articulo;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.ArticuloRepository;
import com.techlab.articulo.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArticuloApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticuloApiApplication.class, args);
    }

    // Precarga los datos iniciales de la preentrega si la base de datos está vacía
    @Bean
    CommandLineRunner precargarDatos(CategoriaRepository categoriaRepo, ArticuloRepository articuloRepo) {
        return args -> {
            if (categoriaRepo.count() == 0 && articuloRepo.count() == 0) {
                Categoria electronica = categoriaRepo.save(
                        new Categoria("Electrónica", "Dispositivos tecnológicos")
                );
                Categoria alimentos = categoriaRepo.save(
                        new Categoria("Alimentos", "Comestibles y bebidas")
                );
                Categoria libreria = categoriaRepo.save(
                        new Categoria("Librería y Oficina", "Insumos escolares y de escritorio")
                );

                articuloRepo.save(new ArticuloElectronico("Teclado Mecánico", 45000.0, electronica, 12));
                articuloRepo.save(new ArticuloAlimenticio("Leche Entera", 1200.0, alimentos, 5));
                articuloRepo.save(new Articulo("Cuaderno A4", 3500.0, libreria));
            }
        };
    }
}
