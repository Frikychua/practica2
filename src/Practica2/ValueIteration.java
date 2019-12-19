/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author friky
 */
public class ValueIteration {

    HashMap<Estado, Double> utilities = new HashMap<Estado, Double>();
    HashMap<Estado, Double> utilitiesAux = new HashMap<Estado, Double>();
    HashMap<Estado, String> policy = new HashMap<Estado, String>();
    BlackBoxEnvironment bbe;
    int n;
    int seed;
    double epsilon;
    double prob;
    Random r;
    //Inicializamos los valores necesarios
    public ValueIteration(int n, int seed, double epsilon, double prob) {
        this.n = n;
        this.seed = seed;
        this.epsilon = epsilon;
        this.prob = prob;
        r = new Random(2019);
    }

    public HashMap<Estado, Double> algoritmo() {
        long startTime = System.nanoTime();
        double delta;
        double utilityPrime;
        bbe = new BlackBoxEnvironment(n, seed, 1);
        //Se inicializan todas las utilidades a 0
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (bbe.maze[j][i] != -1) {
                    if (j == (n - 1)) {
                        utilities.put(new Estado(j, i), 0.0);
                    } else {
                        utilities.put(new Estado(j, i), 0.0);
                    }
                }
            }
        }
        do {
            //Se inicializa Delta a 0
            delta = 0;

            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    if (bbe.maze[j][i] != -1 && dentroLaberinto(j, i)) {
                        if (bbe.isGoal(j, i)) {
                            utilityPrime = bbe.getReward(j, i);
                            double abs = Math.abs(utilities.get(new Estado(j, i)) - utilityPrime);
                        } else {
                            utilityPrime = bbe.getReward(j, i) + getSum(j, i);
                            double abs = Math.abs(utilities.get(new Estado(j, i)) - utilityPrime);
                            if (abs > delta) {
                                delta = abs;
                            }
                            utilitiesAux.put(new Estado(j, i), utilityPrime);
                        }
                    }
                }
            }
            utilities.putAll(utilitiesAux);
            System.out.println("Delta: " + delta);
        } while (!(delta < epsilon));
        long endTime = System.nanoTime();
        System.out.println("Duración: " + (endTime - startTime) / 1e6 + " ms");
        return utilities;
    }

    public boolean dentroLaberinto(int j, int i) {
        //Comprobamos que esa posición se encuentra dentro del laberinto
        if (i >= 0 && i < n) {
            if (j >= 0 && j < n) {
                return true;
            }
        }
        return false;
    }

    public double getSum(int j, int i) {
        String accion = "";
        int aleatorio;
        double max = Double.MIN_VALUE;
        double utilidadAbajo, utilidadArriba, utilidadDerecha, utilidadIzquierda, utilidad;
        //Calculamos la utilidad para cada uno de los movimientos posibles
        
        //abajo
        if (dentroLaberinto(j + 1, i) && bbe.maze[j + 1][i] != -1) {
            utilidadAbajo = utilities.get(new Estado(j + 1, i));
        } else {
            utilidadAbajo = utilities.get(new Estado(j, i));
        }
        //arriba
        if (dentroLaberinto(j - 1, i) && bbe.maze[j - 1][i] != -1) {
            utilidadArriba = utilities.get(new Estado(j - 1, i));
        } else {
            utilidadArriba = utilities.get(new Estado(j, i));
        }
        //derecha
        if (dentroLaberinto(j, i + 1) && bbe.maze[j][i + 1] != -1) {
            utilidadDerecha = utilities.get(new Estado(j, i + 1));
        } else {
            utilidadDerecha = utilities.get(new Estado(j, i));
        }
        //izquierda
        if (dentroLaberinto(j, i - 1) && bbe.maze[j][i - 1] != -1) {
            utilidadIzquierda = utilities.get(new Estado(j, i - 1));
        } else {
            utilidadIzquierda = utilities.get(new Estado(j, i));
        }
        
        //Seleccionamos el movimiento con mayor utilidad y aplicamos el modelo de transición
        
        //Asignamos como primera utilidad a comprobar ir hacia abajo y alguna tiene utilidad mayor escogeremos esa
        max = utilidadAbajo * prob + utilidadArriba* (1.0 - prob) / 3 + utilidadDerecha * (1.0 - prob) / 3 + utilidadIzquierda * (1.0 - prob) / 3;
        accion = "DOWN";
        
        utilidad = utilidadArriba * prob + utilidadAbajo * (1.0 - prob) / 3 + utilidadDerecha * (1.0 - prob) / 3 + utilidadIzquierda * (1.0 - prob) / 3;
        if (utilidad > max) {
            max = utilidad;
            accion = "UP";
        }
        utilidad = utilidadIzquierda * prob + utilidadArriba * (1.0 - prob) / 3 + utilidadDerecha * (1.0 - prob) / 3 + utilidadAbajo * (1.0 - prob) / 3;
        if (utilidad > max) {
            max = utilidad;
            accion = "LEFT";
        }
        utilidad = utilidadDerecha * prob + utilidadArriba * (1.0 - prob) / 3 + utilidadIzquierda * (1.0 - prob) / 3 + utilidadAbajo * (1.0 - prob) / 3;
        if (utilidad > max) {
            max = utilidad;
            accion = "RIGHT";
        }
        
        //Creamos la política escogiendo el movimiento con mayor utilidad y la colocamos en el HashMap
        policy.put(new Estado(j, i), accion);
        return max;
    }

}
