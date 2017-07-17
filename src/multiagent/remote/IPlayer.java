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
public interface IPlayer extends Remote{

    /**
     * Diese Methode verbindet dan Spieler mit dem Server
     * @param name Spielername
     * @param strat Strategy des Spielers
     * @param serverName IP-Adresse des Servers mit dem die Verbindung aufgebaut wird
     * @return true, wenn der Spieler mit dem Server verbunden ist, sonst false
     * @throws RemoteException
     */
    public boolean connect(String name, IStrategy strat,String serverName) throws RemoteException;
    
    /**
     * Diese Methode git den Spielername zurueck
     * @return Spielername
     * @throws RemoteException
     */
    public String getName()throws RemoteException;

    /**
     * Diese Methode git die Strategy des Spielers zurueck
     * @return Strategy des Spielers
     * @throws RemoteException
     */
    public IStrategy getStrategy() throws RemoteException;
    
    /**
     * Diese Methode setzt die Punkte des Spielers
     * @param points Punkte des Spielers
     * @throws RemoteException
     */
    public void setPoints(int points) throws RemoteException;
    
    /**
     * Diese Methode gibt die Punkte des Spielers zurueck
     * @return Punkte des Spielers
     * @throws RemoteException
     */
    public int getPoints() throws RemoteException;
    
    /**
     * Diese Methode schliesst das Programm des Spielers
     * @throws RemoteException
     */
    public void dispose() throws RemoteException;
    
    /**
     * Diese Methode setzt den Namen des Spielers
     * @param name Name des Spielers
     * @throws RemoteException
     */
    public void setName(String name) throws RemoteException;
    
    /**
     * Diese Methode resetet die Strategy des Spielers bei Spielwiederholung
     * @throws RemoteException
     */
    public void resetStrategy() throws RemoteException;
    
}
