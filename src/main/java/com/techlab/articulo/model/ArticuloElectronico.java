package com.techlab.articulo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ELECTRONICO")
public class ArticuloElectronico extends Articulo {

    @Column(name = "garantia_meses")
    private Integer garantiaMeses;

    public ArticuloElectronico() {
        super();
    }

    public ArticuloElectronico(String nombre, double precio, Categoria categoria, Integer garantiaMeses) {
        super(nombre, precio, categoria);
        this.garantiaMeses = garantiaMeses;
    }

    public ArticuloElectronico(Long codigo, String nombre, double precio, Categoria categoria, Integer garantiaMeses) {
        super(codigo, nombre, precio, categoria);
        this.garantiaMeses = garantiaMeses;
    }

    public Integer getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public String getTipoArticulo() {
        return "Electrónico";
    }

    @Override
    public String getDetalleEspecifico() {
        int meses = (garantiaMeses != null) ? garantiaMeses : 0;
        return "Garantía: " + meses + " meses";
    }
}
