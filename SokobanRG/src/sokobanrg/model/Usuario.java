package sokobanrg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombreUsuario;
    private String contrasenia;
    private String nombreCompleto;
    private Date fechaRegistro;
    private Date ultimaSesion;
    private int nivelMaximoDesbloqueado;
    private int nivelActual; // Nivel actual del usuario
    private int tiempoTotalJugado;
    private int partidasJugadas;
    private int puntuacionGeneral;
    private List<Partida> historialPartidas;
    private String rutaAvatar;

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
        this.puntuacionGeneral = 0;
        this.historialPartidas = new ArrayList<>();
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
        this.historialPartidas.add(partida);
        this.partidasJugadas++;
        this.tiempoTotalJugado += partida.getTiempoSegundos();
        if (partida.isCompletada()) {
            this.puntuacionGeneral += (1000 / Math.max(1, partida.getCantidadMovimientos()));
            if (partida.getNumeroNivel() >= this.nivelMaximoDesbloqueado && this.nivelMaximoDesbloqueado < 5) {
                this.nivelMaximoDesbloqueado = partida.getNumeroNivel() + 1;
            }
        }
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
}
