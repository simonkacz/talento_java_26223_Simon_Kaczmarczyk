package com.techlab.articulo.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALIMENTICIO")
public class ArticuloAlimenticio extends Articulo {

    @Column(name = "dias_para_vencimiento")
    private Integer diasParaVencimiento;

    public ArticuloAlimenticio() {
        super();
    }

    public ArticuloAlimenticio(String nombre, double precio, Categoria categoria, Integer diasParaVencimiento) {
        super(nombre, precio, categoria);
        this.diasParaVencimiento = diasParaVencimiento;
    }

    public ArticuloAlimenticio(Long codigo, String nombre, double precio, Categoria categoria, Integer diasParaVencimiento) {
        super(codigo, nombre, precio, categoria);
        this.diasParaVencimiento = diasParaVencimiento;
    }

    public Integer getDiasParaVencimiento() {
        return diasParaVencimiento;
    }

    public void setDiasParaVencimiento(Integer diasParaVencimiento) {
        this.diasParaVencimiento = diasParaVencimiento;
    }

    @Override
    public String getTipoArticulo() {
        return "Alimenticio";
    }

    @Override
    public String getDetalleEspecifico() {
        int dias = (diasParaVencimiento != null) ? diasParaVencimiento : 0;
        return "Vence en " + dias + " días";
    }
}
