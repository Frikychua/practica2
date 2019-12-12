/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author friky
 */
public class practica2 {

    public static void imprimirPolitica(HashMap<Estado, String> map, int n) {
        // upper row
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print("--");
        }
        System.out.println("-");

        // maze content (by row)
        for (int j = 0; j < n; j++) {
            System.out.print("|");
            for (int i = 0; i < n; i++) {
                Estado s = new Estado(j, i);
                String movimiento = map.get(s);
                if (map.get(s) != null) {
                    if (movimiento.equals("UP")) {
                        System.out.print("^|");
                    } else if (movimiento.equals("DOWN")) {
                        System.out.print("v|");
                    } else if (movimiento.equals("RIGHT")) {
                        System.out.print(">|");
                    } else if (movimiento.equals("LEFT")) {
                        System.out.print("<|");
                    }
                } else {
                    System.out.print(" |");
                }
            }
            System.out.println("");
        }
        // lower row
        for (int i = 0; i < n; i++) {
            System.out.print("--");
        }
        System.out.println("-");
    }

    public static double calcularUtilidadMedia(Random r, BlackBoxEnvironment BB, HashMap<Estado, String> map) {
        double utilidad = 0.0;
        int nmovimientos = 0;
        int row = 0;
        int column = BB.getInitialCarColumn();
        Estado s = new Estado(row, column);
        int npasos = 0;
        for (int i = 0; i < 10000; i++) {
            npasos = 0;
            while (!BB.isGoal(row, column)) {
                ArrayList<Integer> accion = BB.applyAction(row, column, map.get(s), r);
                row = accion.get(0);
                column = accion.get(1);
                utilidad += BB.getReward(row, column);
                s = new Estado(row, column);
                nmovimientos++;
                npasos++;
            }
            row = 0;
            column = BB.getInitialCarColumn();
        }
        System.out.println(nmovimientos / 10000.0);
        return utilidad / 10000;
    }

    public static void main(String[] args) {
        int t = 10;
        double prob = 0.9;
        BlackBoxEnvironment bbe = new BlackBoxEnvironment(t, 2019, prob);
        QLearning ql = new QLearning(bbe, 2019);
        //HashMap<Estado, String> map = ql.algoritmo();

//        for (Estado key : map.keySet()) {
//            String value = map.get(key);
//            System.out.println("Value = " + value);
//        }
        bbe.printMaze();
        //imprimirPolitica(map, t);
        //System.out.println(calcularUtilidadMedia(new Random(2019), bbe, map));
        ValueIteration vi = new ValueIteration(t, 2019, 0.001,0.7);
        HashMap<Estado, Double> utilities = vi.algoritmo();
        utilities.keySet().stream().map((key) -> utilities.get(key)).forEachOrdered((value) -> {
            System.out.println("Utilidad = " + value);
        });
    }
}
