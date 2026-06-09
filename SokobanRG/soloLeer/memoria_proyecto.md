# Plan de Trabajo, Skeletons y Guía de Integración: Proyecto Sokoban (LibGDX)

Este documento contiene la distribución del trabajo para dos personas, las variables comunes y los skeletons de código estructurados y limpios (sin comentarios internos) acordados para el desarrollo de la versión definitiva del juego.

---

## 1. Reparto del Trabajo por Persona

### Persona 1: UI y Recursos
Responsable de la interfaz gráfica en LibGDX (Screens, Stages, UI skin) y el cargado de assets visuales.
*   `Main.java` (Launcher Desktop)
*   `SokobanJuego.java` (Game class)
*   `pantalla/PantallaBase.java` (Abstracta)
*   `pantalla/PantallaLogin.java` & `PantallaRegistro.java`
*   `pantalla/PantallaMenu.java` & `PantallaSeleccionNivel.java`
*   `pantalla/PantallaJuego.java` (Gameplay visual)
*   `pantalla/PantallaEstadisticas.java` & `pantalla/PantallaRanking.java`
*   `service/RecursosManager.java`

### Persona 2: Lógica y Datos
Responsable de los modelos de datos, las reglas de Sokoban (matriz), hilos de ejecución y persistencia binaria.
*   `model/ElementoMapa.java` (Enum)
*   `model/Direccion.java` (Enum)
*   `model/Controlable.java` (Interface)
*   `model/Juego.java` (Abstracta)
*   `model/JuegoSokoban.java` (Lógica y recursividad)
*   `model/Usuario.java` & `Partida.java` (Serializables)
*   `service/AlmacenamientoBinario.java`
*   `service/TemporizadorHilo.java`

---

## 2. Variables Comunes y Compartidas

| Nombre de Variable | Tipo de Dato | Propósito / Propiedad |
| :--- | :--- | :--- |
| `nombreUsuario` | `String` | Nombre del perfil y nombre del archivo binario. |
| `contrasenia` | `String` | Clave para inicio de sesión seguro. |
| `nombreCompleto` | `String` | Nombre completo visible en el menú principal. |
| `nivelActual` | `int` | Nivel de juego activo (1 al 5). |
| `nivelMaximoDesbloqueado` | `int` | Nivel máximo desbloqueado por el jugador. |
| `movimientosRealizados` | `int` | Contador de movimientos válidos. |
| `segundosTranscurridos` | `int` | Tiempo transcurrido en el juego (usado por el hilo). |
| `mapaActual` | `ElementoMapa[][]` | Matriz que representa el tablero de juego. |
| `filaJugador` / `columnaJugador` | `int` / `int` | Coordenadas del jugador (@) en el mapa. |
| `historialPartidas` | `List<Partida>` | Lista de partidas finalizadas. |

---

## 3. Orden de Desarrollo y Archivos

1. `model/ElementoMapa.java` & `model/Direccion.java`
2. `model/Controlable.java`
3. `model/Juego.java`
4. `model/Partida.java` & `model/Usuario.java`
5. `model/JuegoSokoban.java`
6. `service/TemporizadorHilo.java`
7. `service/AlmacenamientoBinario.java`
8. `service/RecursosManager.java`
9. `SokobanJuego.java` & `Main.java`
10. `pantalla/PantallaBase.java`
11. `pantalla/PantallaLogin.java` & `pantalla/PantallaRegistro.java`
12. `pantalla/PantallaMenu.java` & `pantalla/PantallaSeleccionNivel.java`
13. `pantalla/PantallaJuego.java` & Pantallas de Estadísticas y Ranking

---

## 4. Caparazones y Estructuras - Persona 2 (Lógica)

### `model/ElementoMapa.java`
```java
package com.sokoban.model;

public enum ElementoMapa {
    PARED, CAJA, DESTINO, JUGADOR, VACIO, CAJA_EN_DESTINO, JUGADOR_EN_DESTINO
}
```

### `model/Direccion.java`
```java
package com.sokoban.model;

public enum Direccion {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA
}
```

### `model/Controlable.java`
```java
package com.sokoban.model;

public interface Controlable {
    boolean mover(Direccion direccion);
}
```

### `model/Juego.java`
```java
package com.sokoban.model;

public abstract class Juego {
    protected boolean activo;
    protected int nivelActual;
    protected int movimientosRealizados;

    public Juego() {}

    public boolean isActivo() { return false; }
    public void setActivo(boolean activo) {}
    public int getNivelActual() { return 0; }
    public void setNivelActual(int nivelActual) {}
    public int getMovimientosRealizados() { return 0; }
    public void setMovimientosRealizados(int movimientosRealizados) {}

    public abstract boolean cargarNivel(int numeroNivel);
    public abstract void reiniciarNivel();
    public abstract boolean verificarVictoria();
}
```

### `model/Partida.java`
```java
package com.sokoban.model;

import java.io.Serializable;
import java.util.Date;

public class Partida implements Serializable {
    private static final long serialVersionUID = 2L;
    private int numeroNivel;
    private boolean completada;
    private int cantidadMovimientos;
    private int tiempoSegundos;
    private Date fechaPartida;

    public Partida(int numeroNivel, boolean completada, int cantidadMovimientos, int tiempoSegundos) {}
}
```

### `model/Usuario.java`
```java
package com.sokoban.model;

import java.io.Serializable;
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
    private int tiempoTotalJugado;
    private int partidasJugadas;
    private int puntuacionGeneral;
    private List<Partida> historialPartidas;

    public Usuario(String nombreUsuario, String contrasenia, String nombreCompleto, String rutaAvatar) {}

    public String getNombreUsuario() { return null; }
    public String getContrasenia() { return null; }
    public void setContrasenia(String contrasenia) {}
    public String getNombreCompleto() { return null; }
    public int getNivelMaximoDesbloqueado() { return 1; }
    public void setNivelMaximoDesbloqueado(int nivel) {}
    public List<Partida> getHistorialPartidas() { return null; }
    public void agregarPartida(Partida partida) {}
    public int getPuntuacionGeneral() { return 0; }
}
```

### `model/JuegoSokoban.java`
```java
package com.sokoban.model;

public class JuegoSokoban extends Juego implements Controlable {
    private ElementoMapa[][] mapaActual;
    private ElementoMapa[][] mapaOriginal;
    private int filaJugador;
    private int columnaJugador;
    private int filasTotales;
    private int columnasTotales;

    public JuegoSokoban() {}

    public ElementoMapa[][] getMapaActual() { return null; }
    public int getFilasTotales() { return 0; }
    public int getColumnasTotales() { return 0; }
    public int getFilaJugador() { return 0; }
    public int getColumnaJugador() { return 0; }

    @Override public boolean cargarNivel(int numeroNivel) { return false; }
    @Override public void reiniciarNivel() {}
    @Override public boolean mover(Direccion direccion) { return false; }
    public boolean deshacerMovimiento() { return false; }
    public int contarCajasFueraDeDestinoRecursivo(int fila, int columna) { return 0; }
    @Override public boolean verificarVictoria() { return false; }
}
```

### `service/AlmacenamientoBinario.java`
```java
package com.sokoban.service;

import com.sokoban.model.Usuario;
import java.util.List;

public class AlmacenamientoBinario {
    private final String RUTA_ARCHIVO = "datos_usuarios/usuarios.dat";

    public AlmacenamientoBinario() {}

    public synchronized List<Usuario> cargarUsuarios() { return null; }
    public synchronized void guardarUsuarios(List<Usuario> usuarios) {}
}
```

### `service/TemporizadorHilo.java`
```java
package com.sokoban.service;

public class TemporizadorHilo extends Thread {
    private boolean activo;
    private int segundosTranscurridos;
    private Runnable accionAlActualizar;

    public TemporizadorHilo(Runnable accionAlActualizar) {}

    public int getSegundosTranscurridos() { return 0; }
    public void detener() {}
    public void reiniciar() {}
    @Override public void run() {}
}
```

---

## 5. Caparazones y Estructuras - Persona 1 (UI/LibGDX)

### `SokobanJuego.java`
```java
package com.sokoban;

import com.badlogic.gdx.Game;
import com.sokoban.model.Usuario;
import com.sokoban.service.AlmacenamientoBinario;
import com.sokoban.service.RecursosManager;

public class SokobanJuego extends Game {
    private AlmacenamientoBinario almacenamiento;
    private RecursosManager recursosManager;
    private Usuario usuarioActual;

    @Override public void create() {}
    public AlmacenamientoBinario getAlmacenamiento() { return null; }
    public RecursosManager getRecursosManager() { return null; }
    public Usuario getUsuarioActual() { return null; }
    public void setUsuarioActual(Usuario usuario) {}
    public void mostrarPantallaLogin() {}
    public void mostrarPantallaRegistro() {}
    public void mostrarPantallaMenu() {}
    public void mostrarPantallaSeleccionNivel() {}
    public void mostrarPantallaJuego(int nivelSeleccionado) {}
    public void mostrarPantallaEstadisticas() {}
    public void mostrarPantallaRanking() {}
    @Override public void dispose() {}
}
```

### `service/RecursosManager.java`
```java
package com.sokoban.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RecursosManager {
    private AssetManager assets;
    private Skin skinUI;

    public RecursosManager() {}

    public void cargarRecursos() {}
    public Skin getSkinUI() { return null; }
    public TextureRegion getTextureElemento(String nombre) { return null; }
    public void liberarRecursos() {}
}
```

### `pantalla/PantallaBase.java`
```java
package com.sokoban.pantalla;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sokoban.SokobanJuego;

public abstract class PantallaBase implements Screen {
    protected final SokobanJuego juegoSokoban;
    protected Stage escenario;
    protected OrthographicCamera camaraUI;
    protected FitViewport vista;

    public PantallaBase(SokobanJuego juegoSokoban) {}

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void resize(int ancho, int alto) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}

    protected TextButton crearBoton(String texto) { return null; }
    protected Label crearEtiqueta(String texto, boolean esTitulo) { return null; }
    protected TextField crearCampoTexto(String placeholder) { return null; }
}
```

### `pantalla/PantallaLogin.java`
```java
package com.sokoban.pantalla;

import com.sokoban.SokobanJuego;

public class PantallaLogin extends PantallaBase {
    public PantallaLogin(SokobanJuego juego) { super(juego); }
    @Override public void show() { super.show(); }
    private void autenticarUsuario(String usuario, String contrasenia) {}
}
```

### `pantalla/PantallaRegistro.java`
```java
package com.sokoban.pantalla;

import com.sokoban.SokobanJuego;

public class PantallaRegistro extends PantallaBase {
    public PantallaRegistro(SokobanJuego juego) { super(juego); }
    @Override public void show() { super.show(); }
    private void registrarUsuario(String usuario, String pass, String nombre) {}
}
```

### `pantalla/PantallaMenu.java`
```java
package com.sokoban.pantalla;

import com.sokoban.SokobanJuego;

public class PantallaMenu extends PantallaBase {
    public PantallaMenu(SokobanJuego juego) { super(juego); }
    @Override public void show() { super.show(); }
    public void actualizarDatosUsuario() {}
}
```

### `pantalla/PantallaSeleccionNivel.java`
```java
package com.sokoban.pantalla;

import com.sokoban.SokobanJuego;

public class PantallaSeleccionNivel extends PantallaBase {
    public PantallaSeleccionNivel(SokobanJuego juego) { super(juego); }
    @Override public void show() { super.show(); }
}
```

### `pantalla/PantallaJuego.java`
```java
package com.sokoban.pantalla;

import com.sokoban.SokobanJuego;
import com.sokoban.model.JuegoSokoban;
import com.sokoban.service.TemporizadorHilo;

public class PantallaJuego extends PantallaBase {
    private JuegoSokoban juegoSokoban;
    private TemporizadorHilo temporizador;

    public PantallaJuego(SokobanJuego juego, int nivel) { super(juego); }
    @Override public void show() { super.show(); }
    @Override public void render(float delta) { super.render(delta); }
    public void inicializarJuego() {}
    private void procesarEntradaTeclado() {}
    private void dibujarMapa() {}
}
```
