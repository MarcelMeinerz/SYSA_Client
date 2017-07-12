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

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    /**
     *
     * @throws RemoteException
     */
    public Strategy() throws RemoteException {
        super();
    }

    /**
     *
     * @param agent
     * @throws RemoteException
     */
    @Override
    public void nextAction(IAgent agent) throws RemoteException {
    	  	
        
    }
    


    

}
