/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameclient;

import java.awt.event.WindowEvent;
import multiagent.remote.IPlayer;
import multiagent.remote.IMultiAgentServer;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import multiagent.remote.IStrategy;
import multiagent.strategy.Strategy;

/**
 * 
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public class ClientImpl extends UnicastRemoteObject implements Serializable, IPlayer {

    private static final long serialVersionUID = 1L;
    private IMultiAgentServer server;
    private String name;
    private IStrategy strategy;
    private int points;
    private ClientFrame frame;

    /**
     *
     * @param frame
     * @throws RemoteException
     */
    public ClientImpl(ClientFrame frame) throws RemoteException {
        this.frame = frame;
    }

    /**
     *
     * @param name
     * @param strategy
     * @param serverName
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean connect(String name,IStrategy strategy, String serverName ) throws RemoteException {
        try {
            this.strategy = strategy;
            this.name = name;
            if(serverName.isEmpty()){
                serverName="localhost";
            }
            server = (IMultiAgentServer) Naming.lookup("//"+serverName+"/server");
            server.addPlayer(this,strategy);
            System.out.println(server.print());
            return true;
        } catch (NotBoundException | MalformedURLException | AccessException   ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    @Override
    public IStrategy getStrategy() {
        return strategy;
    }

    /**
     *
     * @param points
     * @throws RemoteException
     */
    @Override
    public void setPoints(int points) throws RemoteException {
        this.points = points;
    }

    /**
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public int getPoints() throws RemoteException {
        return points;
    }

    /**
     *
     * @throws RemoteException
     */
    @Override
    public void dispose() throws RemoteException {
        frame.dispose();
    }

    /**
     *
     * @param name
     * @throws RemoteException
     */
    @Override
    public void setName(String name) throws RemoteException {
        this.name=name;
    }
    
    /**
     *
     */
    @Override
    public void resetStrategy(){
        try {
            strategy= new Strategy();
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
