/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.util.HashMap;

/**
 *
 * @author friky
 */
public class ValueIteration {

    HashMap<Estado, Double> utilities = new HashMap<Estado, Double>();
    BlackBoxEnvironment bbe;
    int n;
    int seed;
    double epsilon;

    public ValueIteration(int n, int seed, double epsilon) {
        this.n = n;
        this.seed = seed;
        this.epsilon = epsilon;
    }

    public HashMap<Estado, Double> algoritmo() {
        double delta;
        double utilityPrime;
        bbe = new BlackBoxEnvironment(n, seed, 1);

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (bbe.maze[j][i] != -1) {
                    utilities.put(new Estado(j, i), 0.0);
                }
            }
        }

        do {
            delta = 0;

            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    if (bbe.maze[j][i] != -1 && dentroLaberinto(i, j)) {
                        if (bbe.isGoal(j, i)) {
                            utilityPrime = bbe.getReward(j, i);
                            double abs = Math.abs(utilities.get(new Estado(j, i)) - utilityPrime);
                            if (abs > delta) {
                                delta = abs;
                            }
                            utilities.put(new Estado(j, i), utilityPrime);
                        } else {
                            utilityPrime = bbe.getReward(j, i) + getSum(j, i);
                            double abs = Math.abs(utilities.get(new Estado(j, i)) - utilityPrime);
                            if (abs > delta) {
                                delta = abs;
                            }
                            utilities.put(new Estado(j, i), utilityPrime);
                        }
                    }
                }
            }
        } while (!(delta < epsilon));

        return utilities;
    }

    public boolean dentroLaberinto(int j, int i) {
        if (i >= 0 && i < n) {
            if (j >= 0 && j < n) {
                return true;
            }
        }
        return false;
    }

    public double getSum(int j, int i) {
        double sum = 0;
        //abajo
        int nAcciones = 0;
        if (dentroLaberinto(j + 1, i) && bbe.maze[j + 1][i] != -1) {
            sum += utilities.get(new Estado(j + 1, i));
            nAcciones++;
        }
        //arriba
        if (dentroLaberinto(j - 1, i) && bbe.maze[j - 1][i] != -1) {
            sum += utilities.get(new Estado(j - 1, i));
            nAcciones++;
        }
        //derecha
        if (dentroLaberinto(j, i + 1) && bbe.maze[j][i + 1] != -1) {
            sum += utilities.get(new Estado(j, i + 1));
            nAcciones++;
        }
        //izquierda
        if (dentroLaberinto(j, i - 1) && bbe.maze[j][i - 1] != -1) {
            sum += utilities.get(new Estado(j, i - 1));
            nAcciones++;
        }
        return sum;
    }

}
