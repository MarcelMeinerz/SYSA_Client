/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent.remote;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public interface IAgent extends Serializable {

    /**
     *
     * @return
     */
    public String getName();

    /**
     *
     * @return
     */
    public int getPosx();

    /**
     *
     * @return
     */
    public int getPosy();

    /**
     *
     * @return
     */
    public int getCapacity();

    /**
     *
     * @return
     */
    public int getLoad();

    /**
     *
     * @param direction
     */
    public void go(String direction);

    /**
     *
     */
    public void take();

    /**
     *
     * @return
     */
    public int check();

    /**
     *
     */
    public void put();

    /**
     *
     * @param value
     */
    public void put(int value);

    /**
     *
     * @return
     */
    public String getOrder();

    /**
     *
     * @param direction
     * @return
     */
    public boolean requestField(String direction);

    /**
     *
     * @return
     */
    public int getHomeXY();

    /**
     *
     * @return
     */
    public boolean checkIfOnSpawn();

    /**
     *
     * @return
     */
    public int getPlanedPut();

    /**
     *
     * @return
     */
    public int getPoints();

    /**
     *
     */
    public void buy();

    /**
     *
     * @return
     */
    public int getTargetAmount();

    /**
     *
     * @return
     */
    public int getAgentsValue();

    /**
     *
     * @return
     */
    public int getMaxAgents();
    
    /**
     *
     * @return
     */
    public boolean hasEnoughToBuy();
    
    /**
     *
     * @return
     */
    public boolean hasMaxAgents();
    
    /**
     *
     * @return
     */
    public boolean checkSpawnIsPossible();
    
    /**
     *
     * @param i
     * @param j
     * @return
     */
    public int getCustomData(int i, int j);

    /**
     *
     * @param i
     * @param j
     * @param data
     */
    public void setCustomData(int i, int j, int data);
    
    /**
     *
     * @return
     */
    public boolean buyPossible();

    /**
     *
     * @return
     */
    public IAgent[] getAgentArray();
	 
    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int getRememberResources(int x, int y);

    /**
     *
     * @param resources
     */
    public void setRememberResources(int resources);
    
    /**
     *
     * @param x
     * @param y
     * @param resources
     */
    public void setRememberResources(int x, int y, int resources);
    
    /**
     *
     * @return
     */
    public int getRememberFieldSize();
        
    /**
     *
     */
    public void initializeRememberField();

    /**
     *
     */
    public void mergeRememberField();

}
