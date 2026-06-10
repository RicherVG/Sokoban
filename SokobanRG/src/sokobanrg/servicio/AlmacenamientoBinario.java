package sokobanrg.servicio;

import sokobanrg.model.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoBinario {
    private final String DIRECTORIO_BASE = "datos_usuarios";

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
}
