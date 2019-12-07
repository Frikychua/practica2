package Practica2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author friky
 */
public class QLearning {

    HashMap<Estado, String> policy = new HashMap<Estado, String>();
    HashMap<Estado, Double> utilities = new HashMap<Estado, Double>();
    HashMap<Estado, double[]> QTable = new HashMap<Estado, double[]>();
    BlackBoxEnvironment BB;
    Random r;
    int nEpisodios = 1000;
    final double alfa = 0.1;
    final double vInicio = -1.0;

    public QLearning(BlackBoxEnvironment b,int seed) {
        this.BB = b;
        r = new Random(seed);
        
    }

    public HashMap<Estado, String> algoritmo() {
        for (int i = 0; i < nEpisodios; i++) {
        int row = 0;
        int column = BB.getInitialCarColumn();
        int accionActual;
        
            while (!BB.isGoal(row, column)) {
                Estado s = new Estado(row, column);
                //se coge el movimiento con mayor valor en la QTabla
                if (QTable.get(s) != null) {
                    accionActual = getMax(QTable.get(new Estado(row, column)));
                } else {
                    //si ese estado es null porque no ha sido visitado se inicializa a
                    QTable.put(s, new double[]{vInicio,vInicio,vInicio,vInicio});
                    accionActual = r.nextInt(4);
                }
                //se aplica la accion al estado actual
                ArrayList accion = BB.applyAction(row, column, BB.getActions()[accionActual], r);
                //Almacenamos el proximo estado
                Estado s2 = new Estado((int) accion.get(0), (int) accion.get(1)); //Siguiente estado
                //
                double[] sPrime = new double[4];
                //Guardamos el array con los valores para los movimientos del estado actual
                System.arraycopy(QTable.get(s), 0, sPrime, 0, 4);
                //System.out.println("-");
                //Si el proximo estado es el final aplicamos las formula sumando la recompensa
                if (BB.isGoal((int) accion.get(0), (int) accion.get(1))) {
                    sPrime[accionActual] = ((1.0 - alfa) * sPrime[accionActual]) + (alfa * ((double) accion.get(2)));
                    QTable.put(s, sPrime);
                    //System.out.println("Termina");
                } else {
                    //Si el estado es nulo, es decir no ha sido visitado lo inicializamos
                    if (QTable.get(s2) == null) {
                        QTable.put(s2, new double[]{vInicio,vInicio,vInicio,vInicio});
                    }
                    //Aplicamos la formula
                    sPrime[accionActual] = (1.0 - alfa) * sPrime[accionActual] + (alfa * ((double) (accion.get(2)) + QTable.get(s2)[getMax(QTable.get(s))]));
                    QTable.put(s, sPrime);
                }
                //Actualizamos fila y columna
                row = (int) accion.get(0);
                column = (int) accion.get(1);
            }
            //Generamos la poltica con la QTabla en el hashmap policy
            QTable.keySet().forEach((key) -> {
                policy.put(key, BB.getActions()[getMax(QTable.get(key))]);
            });
        }
        return policy;
    }

    public int getMax(double[] recompensas) {
        int maxIndex = r.nextInt(recompensas.length);

        double max = recompensas[maxIndex];

        for (int i = 0; i < recompensas.length; i++) {
            if (recompensas[i] > max) {
                max = recompensas[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

}
