/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.service;

/**
 *
 * @author Gabriel
 */
public class TemporizadorHilo extends Thread {
    private boolean activo;
    private int segundosTranscurridos;
    private Runnable accionAlActualizar;

    public TemporizadorHilo(Runnable accionAlActualizar) {
        this.accionAlActualizar = accionAlActualizar;
        this.segundosTranscurridos = 0;
        this.activo = true;
    }

    public int getSegundosTranscurridos() {
        return segundosTranscurridos;
    }

    public void detener() {
        this.activo = false;
    }

    public void reiniciar() {
        this.segundosTranscurridos = 0;
        this.activo = true;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(1000);
                segundosTranscurridos++;

                if (accionAlActualizar != null) {
                    accionAlActualizar.run();
                }

            } catch (InterruptedException e) {
                activo = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}
