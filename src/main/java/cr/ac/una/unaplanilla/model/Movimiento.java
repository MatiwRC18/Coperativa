package cr.ac.una.unaplanilla.model;

import java.time.LocalDateTime;

public class Movimiento {

    private LocalDateTime fechaHora;
    private String folio;
    private String tipoCuenta;
    private String tipoMovimiento;
    private int monto;

    public Movimiento(String folio, String tipoCuenta, String tipoMovimiento, int monto) {
        this.folio = folio;
        this.tipoCuenta = tipoCuenta;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
    }

    public Movimiento(LocalDateTime fechaHora, String folio, String tipoCuenta, String tipoMovimiento, int monto) {
        this.fechaHora = fechaHora;
        this.folio = folio;
        this.tipoCuenta = tipoCuenta;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
    }

    public Movimiento() {
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }
}
