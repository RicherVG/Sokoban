/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sokobanrg.model;

import java.util.Stack;
/**
 *
 * @author Gabriel
 */
public class JuegoSokoban extends Juego implements Controlable {
    private ElementoMapa[][] mapaActual;
    private ElementoMapa[][] mapaOriginal;
    private int filaJugador;
    private int columnaJugador;
    private int filasTotales;
    private int columnasTotales;

    public JuegoSokoban() {
    }

    public ElementoMapa[][] getMapaActual() {
        return mapaActual;
    }

    public int getFilasTotales() {
        return filasTotales;
    }

    public int getColumnasTotales() {
        return columnasTotales;
    }

    public int getFilaJugador() {
        return filaJugador;
    }

    public int getColumnaJugador() {
        return columnaJugador;
    }

    @Override
    public boolean cargarNivel(int numeroNivel) {
        String[] nivel;

        switch (numeroNivel) {
            case 1:
                nivel = new String[]{
                    "#######",
                    "#     #",
                    "#  @  #",
                    "#  $  #",
                    "#  .  #",
                    "#     #",
                    "#######"
                };
                break;

            case 2:
                nivel = new String[]{
                    "########",
                    "#      #",
                    "#  @   #",
                    "#  $$  #",
                    "#  ..  #",
                    "#      #",
                    "########"
                };
                break;

            case 3:
                nivel = new String[]{
                    "#########",
                    "#       #",
                    "#   @   #",
                    "#  $$   #",
                    "#  #.   #",
                    "#   .   #",
                    "#       #",
                    "#########"
                };
                break;

            case 4:
                nivel = new String[]{
                    "##########",
                    "#        #",
                    "#   @    #",
                    "#  $$$   #",
                    "#  ...   #",
                    "#   #    #",
                    "#        #",
                    "##########"
                };
                break;

            case 5:
                nivel = new String[]{
                    "###########",
                    "#         #",
                    "#   @     #",
                    "#  $$$    #",
                    "#  # #    #",
                    "#  ...    #",
                    "#         #",
                    "###########"
                };
                break;

            default:
                return false;
        }

        nivelActual = numeroNivel;
        movimientosRealizados = 0;
        activo = true;

        filasTotales = nivel.length;
        columnasTotales = nivel[0].length();

        mapaActual = new ElementoMapa[filasTotales][columnasTotales];
        mapaOriginal = new ElementoMapa[filasTotales][columnasTotales];

        for (int fila = 0; fila < filasTotales; fila++) {

            for (int columna = 0; columna < columnasTotales; columna++) {

                char simbolo = nivel[fila].charAt(columna);

                switch (simbolo) {

                    case '#':
                        mapaActual[fila][columna] = ElementoMapa.PARED;
                        mapaOriginal[fila][columna] = ElementoMapa.PARED;
                        break;

                    case '$':
                        mapaActual[fila][columna] = ElementoMapa.CAJA;
                        mapaOriginal[fila][columna] = ElementoMapa.CAJA;
                        break;

                    case '.':
                        mapaActual[fila][columna] = ElementoMapa.DESTINO;
                        mapaOriginal[fila][columna] = ElementoMapa.DESTINO;
                        break;

                    case '@':
                        mapaActual[fila][columna] = ElementoMapa.JUGADOR;
                        mapaOriginal[fila][columna] = ElementoMapa.JUGADOR;

                        filaJugador = fila;
                        columnaJugador = columna;
                        break;

                    default:
                        mapaActual[fila][columna] = ElementoMapa.VACIO;
                        mapaOriginal[fila][columna] = ElementoMapa.VACIO;
                }
            }
        }

        return true;
    }

    @Override
    public void reiniciarNivel() {
        if (mapaOriginal == null) {
            return;
        }

        mapaActual = new ElementoMapa[filasTotales][columnasTotales];

        for (int fila = 0; fila < filasTotales; fila++) {
            for (int columna = 0; columna < columnasTotales; columna++) {
                mapaActual[fila][columna] = mapaOriginal[fila][columna];

                if (mapaActual[fila][columna] == ElementoMapa.JUGADOR ||
                    mapaActual[fila][columna] == ElementoMapa.JUGADOR_EN_DESTINO) {
                    filaJugador = fila;
                    columnaJugador = columna;
                }
            }
        }

        movimientosRealizados = 0;
        activo = true;
    }

    @Override
    public boolean mover(Direccion direccion) {
        int cambioFila = 0;
        int cambioColumna = 0;

        switch (direccion) {
            case ARRIBA:
                cambioFila = -1;
                break;
            case ABAJO:
                cambioFila = 1;
                break;
            case IZQUIERDA:
                cambioColumna = -1;
                break;
            case DERECHA:
                cambioColumna = 1;
                break;
        }

        int nuevaFilaJugador = filaJugador + cambioFila;
        int nuevaColumnaJugador = columnaJugador + cambioColumna;

        if (nuevaFilaJugador < 0 || nuevaFilaJugador >= filasTotales ||
            nuevaColumnaJugador < 0 || nuevaColumnaJugador >= columnasTotales) {
            return false;
        }

        ElementoMapa casillaDestino = mapaActual[nuevaFilaJugador][nuevaColumnaJugador];

        if (casillaDestino == ElementoMapa.PARED) {
            return false;
        }

        if (casillaDestino == ElementoMapa.CAJA || casillaDestino == ElementoMapa.CAJA_EN_DESTINO) {
            int nuevaFilaCaja = nuevaFilaJugador + cambioFila;
            int nuevaColumnaCaja = nuevaColumnaJugador + cambioColumna;

            if (nuevaFilaCaja < 0 || nuevaFilaCaja >= filasTotales ||
                nuevaColumnaCaja < 0 || nuevaColumnaCaja >= columnasTotales) {
                return false;
            }

            ElementoMapa casillaDespuesCaja = mapaActual[nuevaFilaCaja][nuevaColumnaCaja];

            if (casillaDespuesCaja == ElementoMapa.PARED ||
                casillaDespuesCaja == ElementoMapa.CAJA ||
                casillaDespuesCaja == ElementoMapa.CAJA_EN_DESTINO) {
                return false;
            }

            if (casillaDespuesCaja == ElementoMapa.DESTINO) {
                mapaActual[nuevaFilaCaja][nuevaColumnaCaja] = ElementoMapa.CAJA_EN_DESTINO;
            } else {
                mapaActual[nuevaFilaCaja][nuevaColumnaCaja] = ElementoMapa.CAJA;
            }
        }

        if (mapaOriginal[filaJugador][columnaJugador] == ElementoMapa.DESTINO ||
            mapaOriginal[filaJugador][columnaJugador] == ElementoMapa.JUGADOR_EN_DESTINO) {
            mapaActual[filaJugador][columnaJugador] = ElementoMapa.DESTINO;
        } else {
            mapaActual[filaJugador][columnaJugador] = ElementoMapa.VACIO;
        }

        if (casillaDestino == ElementoMapa.DESTINO ||
            casillaDestino == ElementoMapa.CAJA_EN_DESTINO) {
            mapaActual[nuevaFilaJugador][nuevaColumnaJugador] = ElementoMapa.JUGADOR_EN_DESTINO;
        } else {
            mapaActual[nuevaFilaJugador][nuevaColumnaJugador] = ElementoMapa.JUGADOR;
        }

        filaJugador = nuevaFilaJugador;
        columnaJugador = nuevaColumnaJugador;
        movimientosRealizados++;

        if (verificarVictoria()) {
            activo = false;
        }

        return true;
    }

    public boolean deshacerMovimiento() {
        return false;
    }

    public int contarCajasFueraDeDestinoRecursivo(int fila, int columna) {
        if (mapaActual == null) {
            return 0;
        }

        if (fila >= filasTotales) {
            return 0;
        }

        if (columna >= columnasTotales) {
            return contarCajasFueraDeDestinoRecursivo(fila + 1, 0);
        }

        int contador = 0;

        if (mapaActual[fila][columna] == ElementoMapa.CAJA) {
            contador = 1;
        }

        return contador + contarCajasFueraDeDestinoRecursivo(fila, columna + 1);
    }

    @Override
    public boolean verificarVictoria() {
        for (int fila = 0; fila < filasTotales; fila++) {

            for (int columna = 0; columna < columnasTotales; columna++) {

                if (mapaActual[fila][columna] == ElementoMapa.CAJA) {
                    return false;
                }

            }
        }

        return true;
    }
}
