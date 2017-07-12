/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public interface IMultiAgentServer extends Remote{

    /**
     *
     * @param name
     * @param aThis
     * @return
     * @throws RemoteException
     */
    public boolean addPlayer(IPlayer name,IStrategy aThis) throws RemoteException; 

    /**
     *
     * @return
     * @throws RemoteException
     */
    public String print() throws RemoteException;

}
