package multiagent.strategy;







import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import multiagent.remote.IAgent;
import multiagent.remote.IStrategy;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 *
 * @version 1.0
 *
 *
 */
public class Strategy extends UnicastRemoteObject implements IStrategy, Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Die Klasse {@code Strategy} ist die Strategie des Spielers, die fuer alle Agenten gilt.
     * @throws RemoteException
     */
    public Strategy() throws RemoteException {
        super();
    }

    @Override
    public void nextAction(IAgent agent) throws RemoteException {
    }
    


    

}
