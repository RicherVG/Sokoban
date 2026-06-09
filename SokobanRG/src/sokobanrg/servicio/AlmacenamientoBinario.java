package sokobanrg.servicio;

import sokobanrg.model.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoBinario {
    private final String RUTA_ARCHIVO = "datos_usuarios/usuarios.dat";

    public AlmacenamientoBinario() {
        File carpeta = new File("datos_usuarios");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public synchronized List<Usuario> cargarUsuarios() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Usuario>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public synchronized void guardarUsuarios(List<Usuario> usuarios) {
        File archivo = new File(RUTA_ARCHIVO);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
}
