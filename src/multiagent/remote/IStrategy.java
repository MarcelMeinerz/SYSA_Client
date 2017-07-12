package multiagent.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public interface IStrategy extends Remote {

    /**
     * Diese Methode initialisiert die Spielerstrategy des Spielers. Die Methode wird 
     * nach jeder Runde für alle Agents des Spielers aufgerufen.
     * @param agent Agent des Spielers
     * @throws RemoteException
     */
    public void nextAction(IAgent agent) throws RemoteException;

}
