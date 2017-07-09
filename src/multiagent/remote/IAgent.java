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

    public String getName();

    public int getPosx();

    public int getPosy();

    public int getCapacity();

    public int getLoad();

    public void go(String direction);

    public void take();

    public int check();

    public void put();

    public void put(int value);

    public String getOrder();

    public boolean requestField(String direction);

    public int getHomeXY();

    public boolean checkIfOnSpawn();

    public int getPlanedPut();

    public int getPoints();

    public void buy();

    public int getTargetAmount();

    public int getAgentsValue();

    public int getMaxAgents();
    
    public boolean hasEnoughToBuy();
    
    public boolean hasMaxAgents();
    
    public boolean checkSpawnIsPossible();
    
    public int getCustomData(int i, int j);

    public void setCustomData(int i, int j, int data);
    
    public boolean buyPossible();

	public IAgent[] getAgentArray();
	 
    public int getRememberResources(int x, int y);

    public void setRememberResources(int resources);
    
    public void setRememberResources(int x, int y, int resources);
    
    public int getRememberFieldSize();
        
    public void initializeRememberField();

	public void mergeRememberField();

}
