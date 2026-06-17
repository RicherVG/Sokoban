package com.sokoban.model;

/**
 *
 * @author riche
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombreUsuario;
    private String contrasenia;
    private String nombreCompleto;
    private Date fechaRegistro;
    private Date ultimaSesion;
    private int nivelMaximoDesbloqueado;
    private int nivelActual;
    private int tiempoTotalJugado;
    private int partidasJugadas;
    private int nivelesCompletados;
    private int puntuacionGeneral;
    private List<Partida> historialPartidas;
    private String rutaAvatar;
    private List<String> amigos;
    private int volumen;
    private String idioma;
    private boolean modoSinVidas;
    private Map<Integer, Integer> mejorPuntuacionPorNivel;
    private String controles;
    private Boolean cuentaActiva;
    private List<String> solicitudesAmistad;

    public Usuario(String nombreUsuario, String contrasenia, String nombreCompleto, String rutaAvatar) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.nombreCompleto = nombreCompleto;
        this.rutaAvatar = rutaAvatar;
        this.fechaRegistro = new Date();
        this.ultimaSesion = new Date();
        this.nivelMaximoDesbloqueado = 1;
        this.nivelActual = 1;
        this.tiempoTotalJugado = 0;
        this.partidasJugadas = 0;
        this.nivelesCompletados = 0;
        this.puntuacionGeneral = 0;
        this.historialPartidas = new ArrayList<>();
        this.amigos = new ArrayList<>();
        this.volumen = 100;
        this.idioma = "es";
        this.controles = "teclado";
        this.modoSinVidas = false;
        this.mejorPuntuacionPorNivel = new HashMap<>();
        this.cuentaActiva = true;
        this.solicitudesAmistad = new ArrayList<>();
    }

    public String getRutaAvatar() {
        return rutaAvatar;
    }

    public void setRutaAvatar(String rutaAvatar) {
        this.rutaAvatar = rutaAvatar;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getUltimaSesion() {
        return ultimaSesion;
    }

    public void setUltimaSesion(Date ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public int getNivelMaximoDesbloqueado() {
        return nivelMaximoDesbloqueado;
    }

    public void setNivelMaximoDesbloqueado(int nivelMaximoDesbloqueado) {
        this.nivelMaximoDesbloqueado = nivelMaximoDesbloqueado;
    }

    public int getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }

    public void setTiempoTotalJugado(int tiempoTotalJugado) {
        this.tiempoTotalJugado = tiempoTotalJugado;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public int getPuntuacionGeneral() {
        return puntuacionGeneral;
    }

    public void setPuntuacionGeneral(int puntuacionGeneral) {
        this.puntuacionGeneral = puntuacionGeneral;
    }

    public List<Partida> getHistorialPartidas() {
        return historialPartidas;
    }

    public void agregarPartida(Partida partida) {
        agregarPartida(partida, false);
    }

    public void agregarPartida(Partida partida, boolean sinVidas) {
        this.historialPartidas.add(partida);
        this.partidasJugadas++;
        this.tiempoTotalJugado += partida.getTiempoSegundos();
        if (partida.isCompletada()) {
            this.nivelesCompletados++;
            if (nivelesCompletados < 0) nivelesCompletados = 0; // guard

            int nivel = partida.getNumeroNivel();
            if (nivel >= this.nivelMaximoDesbloqueado && this.nivelMaximoDesbloqueado < 5) {
                this.nivelMaximoDesbloqueado = nivel + 1;
            }

            // Si el modo sin vidas esta activo, no se cuentan puntos
            if (!sinVidas) {
                int movs = Math.max(0, partida.getCantidadMovimientos());
                int segundos = Math.max(0, partida.getTiempoSegundos());
                
                int puntajeBase = nivel * 1000;
                int bonoMaximo = nivel * 2000;
                
                int penalizacion = (movs * 20) + (segundos * 5);
                int bonoResultante = Math.max(0, bonoMaximo - penalizacion);
                int puntajeEsteNivel = puntajeBase + bonoResultante;

                if (mejorPuntuacionPorNivel == null) mejorPuntuacionPorNivel = new HashMap<>();
                int mejorAnterior = mejorPuntuacionPorNivel.getOrDefault(nivel, 0);
                if (puntajeEsteNivel > mejorAnterior) {
                    int diferencia = puntajeEsteNivel - mejorAnterior;
                    this.puntuacionGeneral += diferencia;
                    mejorPuntuacionPorNivel.put(nivel, puntajeEsteNivel);
                }
            }
        }
    }

    public int getNivelesCompletados() {
        if (nivelesCompletados < 0) return 0;
        return nivelesCompletados;
    }

    public int getTiempoPromedioPorNivel() {
        if (nivelesCompletados <= 0) return 0;
        return tiempoTotalJugado / nivelesCompletados;
    }

    public boolean isModoSinVidas() {
        return modoSinVidas;
    }

    public void setModoSinVidas(boolean modoSinVidas) {
        this.modoSinVidas = modoSinVidas;
    }

    public Map<Integer, Integer> getMejorPuntuacionPorNivel() {
        if (mejorPuntuacionPorNivel == null) mejorPuntuacionPorNivel = new HashMap<>();
        return mejorPuntuacionPorNivel;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rutaAvatar='" + rutaAvatar + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", nivelMaximoDesbloqueado=" + nivelMaximoDesbloqueado +
                ", partidasJugadas=" + partidasJugadas +
                '}';
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public void agregarAmigo(String amigo) {
        if (!this.amigos.contains(amigo)) {
            this.amigos.add(amigo);
        }
    }

    public int getVolumen() {
        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getControles() {
        return controles;
    }

    public void setControles(String controles) {
        this.controles = controles;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();

        if (nivelesCompletados < 0) nivelesCompletados = 0;
        if (mejorPuntuacionPorNivel == null) mejorPuntuacionPorNivel = new HashMap<>();
        if (historialPartidas == null) historialPartidas = new ArrayList<>();
        if (amigos == null) amigos = new ArrayList<>();
        if (cuentaActiva == null) cuentaActiva = true;
        if (solicitudesAmistad == null) solicitudesAmistad = new ArrayList<>();
    }

    public void resetStats() {
        this.nivelMaximoDesbloqueado = 1;
        this.nivelActual = 1;
        this.tiempoTotalJugado = 0;
        this.partidasJugadas = 0;
        this.nivelesCompletados = 0;
        this.puntuacionGeneral = 0;
        if (this.historialPartidas != null) this.historialPartidas.clear();
        if (this.mejorPuntuacionPorNivel != null) this.mejorPuntuacionPorNivel.clear();
    }
    
    public boolean isCuentaActiva() {
        return cuentaActiva != null ? cuentaActiva : true;
    }

    public void setCuentaActiva(boolean cuentaActiva) {
        this.cuentaActiva = cuentaActiva;
    }

    public List<String> getSolicitudesAmistad() {
        if (solicitudesAmistad == null) solicitudesAmistad = new ArrayList<>();
        return solicitudesAmistad;
    }

    public void agregarSolicitudAmistad(String usuario) {
        if (!getSolicitudesAmistad().contains(usuario) && !getAmigos().contains(usuario)) {
            getSolicitudesAmistad().add(usuario);
        }
    }

    public void removerSolicitudAmistad(String usuario) {
        getSolicitudesAmistad().remove(usuario);
    }
}
