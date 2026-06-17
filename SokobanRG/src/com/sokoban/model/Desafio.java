package com.sokoban.model;

/**
 * @author riche
 */

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Desafio implements Serializable {
    private static final long serialVersionUID = 2L;  

    private String id;
    private String nombreRemitente;
    private String nombreDestinatario;
    private int nivel;
    private EstadoDesafio estado;

    private int puntuacionRemitente;
    private int tiempoRemitente;
    private int movimientosRemitente;

    private int puntuacionDestinatario;
    private int tiempoDestinatario;
    private int movimientosDestinatario;

    private Date fechaCreacion;
    private Date fechaResolucion;

    private boolean sinVidas;

    public Desafio(String nombreRemitente, String nombreDestinatario, int nivel) {
        this(nombreRemitente, nombreDestinatario, nivel, false);
    }

    public Desafio(String nombreRemitente, String nombreDestinatario, int nivel, boolean sinVidas) {
        this.id = UUID.randomUUID().toString();
        this.nombreRemitente = nombreRemitente;
        this.nombreDestinatario = nombreDestinatario;
        this.nivel = nivel;
        this.sinVidas = sinVidas;
        this.estado = EstadoDesafio.PENDIENTE;
        this.puntuacionRemitente = -1;
        this.puntuacionDestinatario = -1;
        this.tiempoRemitente = 0;
        this.tiempoDestinatario = 0;
        this.movimientosRemitente = 0;
        this.movimientosDestinatario = 0;
        this.fechaCreacion = new Date();
    }

    public static int calcularPuntuacion(int nivel, int tiempoSegundos, int movimientos, boolean sinVidas) {
        if (sinVidas) return 0;
        int lvl = nivel == 0 ? 5 : nivel; 
        int puntajeBase = lvl * 1000;
        int bonoMaximo = lvl * 2000;
        int penalizacion = (Math.max(0, movimientos) * 20) + (Math.max(0, tiempoSegundos) * 5);
        int bonoResultante = Math.max(0, bonoMaximo - penalizacion);
        return puntajeBase + bonoResultante;
    }

    public String getId() {
        return id;
    }

    public String getNombreRemitente() {
        return nombreRemitente;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public int getNivel() {
        return nivel;
    }

    public EstadoDesafio getEstado() {
        return estado;
    }

    public void setEstado(EstadoDesafio estado) {
        this.estado = estado;
    }

    public int getPuntuacionRemitente() {
        return puntuacionRemitente;
    }

    public void setPuntuacionRemitente(int puntuacionRemitente) {
        this.puntuacionRemitente = puntuacionRemitente;
    }

    public int getTiempoRemitente() {
        return tiempoRemitente;
    }

    public void setTiempoRemitente(int tiempoRemitente) {
        this.tiempoRemitente = tiempoRemitente;
    }

    public int getMovimientosRemitente() {
        return movimientosRemitente;
    }

    public void setMovimientosRemitente(int movimientosRemitente) {
        this.movimientosRemitente = movimientosRemitente;
    }

    public int getPuntuacionDestinatario() {
        return puntuacionDestinatario;
    }

    public void setPuntuacionDestinatario(int puntuacionDestinatario) {
        this.puntuacionDestinatario = puntuacionDestinatario;
    }

    public int getTiempoDestinatario() {
        return tiempoDestinatario;
    }

    public void setTiempoDestinatario(int tiempoDestinatario) {
        this.tiempoDestinatario = tiempoDestinatario;
    }

    public int getMovimientosDestinatario() {
        return movimientosDestinatario;
    }

    public void setMovimientosDestinatario(int movimientosDestinatario) {
        this.movimientosDestinatario = movimientosDestinatario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public boolean isSinVidas() {
        return sinVidas;
    }

    public void setSinVidas(boolean sinVidas) {
        this.sinVidas = sinVidas;
    }

    public String getDescripcionNivel() {
        String base = nivel == 0 ? "Campeonato" : "Nivel " + nivel;
        return base + (sinVidas ? " (Sin Vidas)" : "");
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();

    }
}
