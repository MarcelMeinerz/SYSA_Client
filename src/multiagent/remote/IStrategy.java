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

    public void nextAction(IAgent agent) throws RemoteException;

}
