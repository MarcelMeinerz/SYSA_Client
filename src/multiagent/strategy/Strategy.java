package multiagent.strategy;







import multiagent.remote.IStrategy;
import gameclient.AgentUtils;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import multiagent.remote.IAgent;

/**
 *
 * @author Donni
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
