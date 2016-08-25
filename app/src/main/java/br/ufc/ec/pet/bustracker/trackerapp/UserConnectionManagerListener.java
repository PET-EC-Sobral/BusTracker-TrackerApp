package br.ufc.ec.pet.bustracker.trackerapp;

/**
 * Created by santana on 24/07/16.
 */
public interface UserConnectionManagerListener {
    void onLogin(UserConnectionManager connection, int status);
}
