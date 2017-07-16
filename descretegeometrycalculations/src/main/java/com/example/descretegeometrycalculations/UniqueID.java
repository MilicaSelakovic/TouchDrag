package com.example.descretegeometrycalculations;


public class UniqueID {
    private int current;


    public UniqueID() {
        current = 0;
    }


    public String getID() {
        current++;
        return Integer.toString(current);
    }

}
