package com.sokoban.service;
/**
 *
 * @author riche
 */

import com.sokoban.model.Desafio;
import com.sokoban.model.EstadoDesafio;
import com.sokoban.model.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoBinario {
    private final String DIRECTORIO_BASE = "datos_usuarios";
    private final String DIRECTORIO_DESAFIOS = "datos_usuarios/desafios";

    public AlmacenamientoBinario() {
        File carpeta = new File(DIRECTORIO_BASE);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public synchronized boolean existeUsuario(String nombreUsuario) {
        File carpetaUsuario = new File(DIRECTORIO_BASE, nombreUsuario);
        return carpetaUsuario.exists() && new File(carpetaUsuario, "usuario.dat").exists();
    }

    public synchronized Usuario cargarUsuario(String nombreUsuario) {
        File archivo = new File(DIRECTORIO_BASE + "/" + nombreUsuario, "usuario.dat");
        if (!archivo.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Usuario) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuario: " + e.getMessage());
        }
        return null;
    }

    public synchronized void guardarUsuario(Usuario usuario) {
        File carpetaUsuario = new File(DIRECTORIO_BASE, usuario.getNombreUsuario());
        if (!carpetaUsuario.exists()) {
            carpetaUsuario.mkdirs();
        }
        
        File archivo = new File(carpetaUsuario, "usuario.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(usuario);
        } catch (IOException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    public synchronized List<Usuario> cargarUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        File carpetaPrincipal = new File(DIRECTORIO_BASE);
        
        if (carpetaPrincipal.exists() && carpetaPrincipal.isDirectory()) {
            File[] carpetasUsuarios = carpetaPrincipal.listFiles(File::isDirectory);
            if (carpetasUsuarios != null) {
                for (File carpetaUsuario : carpetasUsuarios) {
                    Usuario u = cargarUsuario(carpetaUsuario.getName());
                    if (u != null) {
                        listaUsuarios.add(u);
                    }
                }
            }
        }
        return listaUsuarios;
    }

    public synchronized void guardarUsuarios(List<Usuario> usuarios) {
        for (Usuario u : usuarios) {
            guardarUsuario(u);
        }
    }

    public synchronized void guardarDesafio(Desafio desafio) {
        File carpeta = new File(DIRECTORIO_DESAFIOS);
        if (!carpeta.exists()) carpeta.mkdirs();
        File archivo = new File(carpeta, desafio.getId() + ".dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(desafio);
        } catch (IOException e) {
            System.err.println("Error al guardar desafio: " + e.getMessage());
        }
    }

    public synchronized Desafio cargarDesafio(String id) {
        File archivo = new File(DIRECTORIO_DESAFIOS, id + ".dat");
        if (!archivo.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Desafio) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized List<Desafio> cargarDesafiosDeUsuario(String nombreUsuario) {
        List<Desafio> lista = new ArrayList<>();
        File carpeta = new File(DIRECTORIO_DESAFIOS);
        if (!carpeta.exists()) return lista;
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".dat"));
        if (archivos == null) return lista;
        for (File f : archivos) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                Desafio d = (Desafio) ois.readObject();
                if (d.getNombreRemitente().equals(nombreUsuario) || d.getNombreDestinatario().equals(nombreUsuario)) {
                    lista.add(d);
                }
            } catch (Exception e) {
                System.err.println("Desafio corrupto ignorado: " + f.getName());
            }
        }
        return lista;
    }

    public synchronized List<Desafio> cargarDesafiosPendientesParaDestinatario(String nombreUsuario) {
        List<Desafio> lista = new ArrayList<>();
        for (Desafio d : cargarDesafiosDeUsuario(nombreUsuario)) {
            if (d.getNombreDestinatario().equals(nombreUsuario) && d.getEstado() == EstadoDesafio.PENDIENTE) {
                lista.add(d);
            }
        }
        return lista;
    }

    public synchronized List<Desafio> cargarDesafiosListosParaRemitente(String nombreUsuario) {
        List<Desafio> lista = new ArrayList<>();
        for (Desafio d : cargarDesafiosDeUsuario(nombreUsuario)) {
            if (d.getNombreRemitente().equals(nombreUsuario) && d.getEstado() == EstadoDesafio.B_JUGO) {
                lista.add(d);
            }
        }
        return lista;
    }
}

