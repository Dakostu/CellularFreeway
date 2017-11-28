/*
 * @(#)Street.java
 * @(#)Main.java
 *
 * This program simulates the cellular automaton model for freeway traffic
 * as laid out by Kai Nagel and Michael Schreckenberg
 * (Journal de Physique I, EDP Sciences, 1992, 2 (12), pp.2221-2229).
 *
 * This program uses the license found in the LICENSE.md file.
 */


/**
 * The public class Street provides a constructor
 * and every method necessary to fulfill the steps described
 * in Nagel's and Schreckenberg's abstract.
 *
 * @version 1.00 27 Nov 2017
 * @author Daniel Kostuj
 */

public class Street {

    private Boolean isOccupied;
    private String slot;
    private Street[] freeway;

    // constructor for one street slot
    public Street () {
        isOccupied = false;
        slot = ".";
    }

    // constructor for a street, consisting of street slots
    public Street (int length) {

        freeway = new Street[length];
        initializeStreet(freeway);
    }


    // initialize empty street array
    private void initializeStreet (Street nullStreet[]) {
        for (int i = 0; i < nullStreet.length; ++i)
            nullStreet[i] = new Street();

    }

    // put a "car" on a random street slot
    private void putCar () {
        int random;
        do {
            random = (int) (Math.random() * freeway.length);
        } while (freeway[random].isOccupied);
        freeway[random].isOccupied = true;
        // 'realistic case': velocity is a number between [0,5] (p.2222)
        freeway[random].slot = "" + (int) (Math.random() * (5 + 1));
    }

    // insert an amount of cars
    public void insertCars(int amount) {
        for (int i = 0; i < amount; ++i)
            putCar();
    }


    // refresh car position according to the steps laid down in the abstract
    private void refreshFreeway (double randLimit) {

        for (int i = 0; i < freeway.length; ++i) {
            if (freeway[i].isOccupied) {
                boolean isWayFree = true;
                int notFreeNewSpeed = 1;

                for (int j = 1; j <= (Integer.valueOf(freeway[i].slot) + 1) && isWayFree; ++j)
                    if (freeway[(i+j) % freeway.length].isOccupied) {
                        isWayFree = false;
                        notFreeNewSpeed = j;
                    }

                // The steps are explained on page 2222
                // Step 1 - acceleration
                if (isWayFree && (Integer.valueOf(freeway[i].slot) < 5))
                    freeway[i].slot = "" + ((Integer.valueOf(freeway[i].slot) + 1));
                    // Step 2 - slowing down (due to other cars)
                else if (!isWayFree && Integer.valueOf(freeway[i].slot) >= 1)
                    freeway[i].slot = "" + (notFreeNewSpeed - 1);

                // Step 3 - randomization
                if ((Math.random() <= randLimit) && (Integer.valueOf(freeway[i].slot) > 0))
                    freeway[i].slot = "" + (Integer.valueOf(freeway[i].slot)-1);
            }
        }

        // Step 4 - car motion
        Street newFreeway[] = new Street[freeway.length];
        initializeStreet(newFreeway);
        for (int j = 0; j < this.freeway.length; ++j) {
            if (freeway[j].isOccupied) {
                newFreeway[((j + (Integer.valueOf(freeway[j].slot))) % newFreeway.length)].slot
                        = "" + (Integer.valueOf(freeway[j].slot));
                newFreeway[((j + (Integer.valueOf(freeway[j].slot))) % newFreeway.length)].isOccupied = true;
            }
        }

        freeway = newFreeway;
    }


    // simulate freeway traffic and output to console - continuous mode
    public void simulateTraffic (double randomLimit, long interval) {

        System.out.println (this.toString());
        while (true) { increaseSimulationStep(randomLimit, interval); }
    }
    
    // simulate freeway traffic and output to console - finite steps mode
    public void simulateTraffic(double randomLimit, long interval, int steps) {

        System.out.println (this.toString());
        for (int i = 0; i < steps; ++i) { increaseSimulationStep(randomLimit, interval); }
    
    }
    
    // launch simulation for one step
    private void increaseSimulationStep(double randomLimit, long interval) {
        refreshFreeway(randomLimit);
        System.out.println(this.toString());
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            System.out.println("--- PROGRAM WAS INTERRUPTED ---");
          e.printStackTrace();
        }
    
    
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < freeway.length; ++i)
            out += freeway[i].slot;
        return out;
    }
}
