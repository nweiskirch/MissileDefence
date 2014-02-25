/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.managers;

import app.domain.missiles.Interceptor;
import java.util.ArrayList;

/**
 * Manages all inbound ballistic missiles
 *
 * @author Nate
 */
public class InterceptorManager {

    private static InterceptorManager instance;
    private ArrayList<Interceptor> entries;

    /**
     * @return the current static instance of the Interceptor Manager
     */
    public static InterceptorManager getInstance() {
        if (instance == null) {
            instance = new InterceptorManager();
        }
        return instance;
    }

    private InterceptorManager() {
        entries = new ArrayList<>();
    }

    /**
     *
     * @return The number of ballistic missiles being managed
     */
    public int getNumEntries() {
        return entries.size();
    }

    /**
     * Updates all of the current missiles
     *
     * @param millis the time that has passed since the last call to update()
     */
    public void update(int millis) {
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).update(millis);
        }
    }

    /**
     * Adds a ballistic missile to the manager
     *
     * @param i the missile to be added
     */
    public void addEntry(Interceptor i) {
        entries.add(i);
    }

    /**
     * Removes a ballistic missile from the manager
     *
     * @param i the missile to be removed
     */
    public void removeEntry(Interceptor i) {
        entries.remove(i);
    }

    public boolean contains(Interceptor i) {
        return entries.contains(i);
    }
}
