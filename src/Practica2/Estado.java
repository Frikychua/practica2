/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.util.Objects;

/**
 *
 * @author friky
 */
public class Estado {

    int fila; //row
    int columna; //column

    public Estado(int fila,int columna) {
        this.fila = fila;
        this.columna = columna;
    }
    @Override
    public int hashCode(){
        return Objects.hash(fila,columna);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estado other = (Estado) obj;
        if (this.fila != other.fila) {
            return false;
        }
        if (this.columna != other.columna) {
            return false;
        }
        return true;
    }


}
