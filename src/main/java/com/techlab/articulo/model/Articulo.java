package com.techlab.articulo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "articulos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_articulo", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GENERAL")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo",
        defaultImpl = Articulo.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Articulo.class, name = "GENERAL"),
        @JsonSubTypes.Type(value = ArticuloElectronico.class, name = "ELECTRONICO"),
        @JsonSubTypes.Type(value = ArticuloAlimenticio.class, name = "ALIMENTICIO")
})
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_codigo")
    private Categoria categoria;

    public Articulo() {
    }

    public Articulo(String nombre, double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Articulo(Long codigo, String nombre, double precio, Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @JsonProperty("tipoArticulo")
    public String getTipoArticulo() {
        return "General";
    }

    @JsonProperty("detalleEspecifico")
    public String getDetalleEspecifico() {
        return "Sin detalles específicos";
    }

    @Override
    public String toString() {
        return "Articulo [Tipo=" + getTipoArticulo() +
                ", codigo=" + codigo +
                ", nombre=" + nombre +
                ", precio=$" + precio +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "Sin categoria") +
                ", " + getDetalleEspecifico() + "]";
    }
}
