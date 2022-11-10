package br.com.unifertil.glaycon.events;

import java.math.BigDecimal;

public class PerfilMetal {

    private BigDecimal sequencia;
    private BigDecimal sequenciaQuimica;
    private BigDecimal codElemento;
    private String metodo;
    private String garantia;
    private String descNutri;
    private BigDecimal vlrmax;
    private BigDecimal vlrmin;

    public BigDecimal getSequencia() {
        return sequencia;
    }

    public void setSequencia(BigDecimal sequencia) {
        this.sequencia = sequencia;
    }

    public BigDecimal getSequenciaQuimica() {
        return sequenciaQuimica;
    }

    public void setSequenciaQuimica(BigDecimal sequenciaQuimica) {
        this.sequenciaQuimica = sequenciaQuimica;
    }

    public BigDecimal getCodElemento() {
        return codElemento;
    }

    public void setCodElemento(BigDecimal codElemento) {
        this.codElemento = codElemento;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getDescNutri() {
        return descNutri;
    }

    public void setDescNutri(String descNutri) {
        this.descNutri = descNutri;
    }

    public BigDecimal getVlrmax() {
        return vlrmax;
    }

    public void setVlrmax(BigDecimal vlrmax) {
        this.vlrmax = vlrmax;
    }

    public BigDecimal getVlrmin() {
        return vlrmin;
    }

    public void setVlrmin(BigDecimal vlrmin) {
        this.vlrmin = vlrmin;
    }


}