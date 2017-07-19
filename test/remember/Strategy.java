package remember;


import multiagent.remote.IStrategy;
import gameclient.AgentUtils;

import java.awt.Point;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import multiagent.remote.IAgent;
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
	
	boolean presented;
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    
    public Strategy() throws RemoteException {
        super();
        System.out.println("Strategie Remember gestartet");
        presented = false;
    }

    @Override
    public void nextAction(IAgent agent) throws RemoteException {
    	if (!presented) {
            System.out.println("Player: " + agent.getName());
            presented = true;
    	}
    	
        agent.take(); //Default Aktion
                
        //agent.setCustomData(0, 1, 2);
		
        if (agent.getLoad() >= agent.getCapacity()){
            //Agent voll beladen
        	//System.out.println(agent.getName() + " geht nach Hause.");
        	int home = agent.getHomeXY();
        	String direction = "";
        	
        	if ((agent.getPosx() > home+1) && (agent.requestField(AgentUtils.LEFT))){
        		direction = AgentUtils.LEFT;
        	} else if ((agent.getPosx() < home-1) && (agent.requestField(AgentUtils.RIGHT))){
        		direction = AgentUtils.RIGHT;
        	}
        	
        	if ((agent.getPosy() > home+1) && (agent.requestField(AgentUtils.TOP))){
        		direction = AgentUtils.TOP;
        	} else if ((agent.getPosy() < home-1) && (agent.requestField(AgentUtils.BOTTOM))){
        		direction = AgentUtils.BOTTOM;
        	} 
        	
        	if (agent.checkIfOnSpawn()){
        		agent.put();
        	} else {
        		if ("".equals(direction)) {
		    		//System.out.println(agent.getName() + " will nach Hause, kommt nicht durch und l�uft deshalb bl�d herum.");
		        	//zuf�llige Richtung festlegen
		        	direction = getRandomDirection(agent);
        		}
            	agent.go(direction);
        		setUndoPreviousDirection(agent, direction);
        	}
        	//aktuelle Position mit Anzahl an Ressourcen in Spielfeld eintragen
        	agent.setRememberResources(agent.check());
        } else if (agent.check() > 0) {
        	//einsammeln
            agent.take();
        	//aktuelle Position mit Anzahl an Ressourcen in Spielfeld eintragen (1 einzusammelnde Ressource bereits abziehen)
            agent.setRememberResources(agent.check() - 1);
        } else if (agent.getLoad() > 0 && agent.checkIfOnSpawn()) {
        	//Agent hat Ressourcen geladen (Kapazit�tsgrenze aber noch nicht erreicht)
        	//und befindet sich auf Spawn-Feld --> abladen
        	agent.put();
        } else if(agent.buyPossible() && agent.getAgentArray().length <= 2 && (double)(agent.getPoints() + agent.getLoad())/agent.getTargetAmount() < 0.5){
        	//Kauf eines neuen Agenten m�glich und erw�nscht
        	agent.buy();
        	return;
        } else {
        	//aktuelle Position mit Anzahl an Ressourcen in Spielfeld eintragen
        	agent.setRememberResources(agent.check());
        	//(hier immer: aktuelle Position = 0 Ressourcen)
        	
        	// n�chste Mine nach gemerktem Spielfeld bestimmen
            Point goal = getNearestGoal(agent, agent.getCapacity() - agent.getLoad());
            
            String direction = "";
            
            if (goal.x != -1 && goal.y != -1) {
            	
            	//Richtung anhand des Ziels festlegen
            	if (goal.x < agent.getPosx()) {
            		if (agent.requestField(AgentUtils.LEFT))
            			direction = AgentUtils.LEFT;
            	} else if (goal.x > agent.getPosx()) {
            		if (agent.requestField(AgentUtils.RIGHT))
            			direction = AgentUtils.RIGHT;
            	}
            	
            	if (goal.y < agent.getPosy()) {
            		if (agent.requestField(AgentUtils.TOP))
            			direction = AgentUtils.TOP;
            	} else if (goal.y > agent.getPosy()) {
            		if (agent.requestField(AgentUtils.BOTTOM))
            			direction = AgentUtils.BOTTOM;
            	} 
            	//System.out.println(agent.getName() + " geht zu: (" + goal.x + "|" + goal.y + ") �ber " + direction + ".");
            	
            	if ("".equals(direction) || !agent.requestField(direction)) {
                	System.out.println(agent.getName() + " kommt nirgendwo durch.");
                	//zuf�llige Richtung festlegen
                	direction = getRandomDirection(agent);
        			//direction = AgentUtils.BOTTOM;
            	}
            } else {
            	//System.out.println(agent.getName() + " l�uft bl�d herum.");
            	//zuf�llige Richtung festlegen
            	direction = getRandomDirection(agent);
            }
                    	
        	if (!"".equals(direction)) {
        		agent.go(direction);
        		setUndoPreviousDirection(agent, direction);
        	}
            
        }
        
        agent.mergeRememberField();
    }
    
    public Point getNearestGoal(IAgent agent, int minRessources) {   	
    	int xPos = agent.getPosx();
    	int yPos = agent.getPosy();
    	
    	int distance = 0;
    	
    	while (distance < agent.getRememberFieldSize() - 1) {
	    	for (int i = xPos - distance; i <= xPos + distance; i++) {
    			if (i >= 0 && i < agent.getRememberFieldSize()) {
		    		for (int j = yPos - distance; j <= yPos + distance; j++) {
		    			if (j >= 0 && j < agent.getRememberFieldSize()) {
		    				if (i == xPos - distance || i == xPos + distance || j == yPos - distance || j == yPos + distance) {
								if (agent.getRememberResources(i, j) >= minRessources) {
					            	//System.out.println("Ressources: (" + i + "|" + j + "): " + agent.getRememberResources(i, j) + " - min: " + minRessources);							
									return new Point(i, j);
								}
			    			}
		    			}
		    			else {
		    				continue;
		    			}
					}
    			}
    			else {
    				continue;
    			}
			}
	    	distance += 1;
    	}
        
    	return new Point(-1, -1);    	
    }

    public String getRandomDirection(IAgent agent) {
    	int direction = (int)(Math.random() * 4);
    	String undoDirection = getUndoPreviousDirection(agent);
    	
    	String goTo = "";
    	int counter = 0;
    	    	
    	while (counter < 4) {
    		switch (direction){
        	case 0:
        		if (agent.requestField(AgentUtils.LEFT) && !fieldIsSpawn(agent, AgentUtils.LEFT) && AgentUtils.LEFT != undoDirection) {
                    goTo = AgentUtils.LEFT;
                    return goTo;
                }
        		break;
        	case 1:
        		if (agent.requestField(AgentUtils.TOP) && !fieldIsSpawn(agent, AgentUtils.TOP) && AgentUtils.TOP != undoDirection) {
        			goTo = AgentUtils.TOP;
                    return goTo;
                }
        		break;
        	case 2:
        		if (agent.requestField(AgentUtils.RIGHT) && !fieldIsSpawn(agent, AgentUtils.RIGHT) && AgentUtils.RIGHT != undoDirection) {
        			goTo = AgentUtils.RIGHT;
                    return goTo;
                }
        		break;
        	case 3:
        		if (agent.requestField(AgentUtils.BOTTOM) && !fieldIsSpawn(agent, AgentUtils.BOTTOM) && AgentUtils.BOTTOM != undoDirection) {
        			goTo = AgentUtils.BOTTOM;
                    return goTo;
        		}
        		break;
        	}
    		direction = (direction + 1) % 4;
    		counter += 1;
    	}
    	
    	if (goTo == "") {
    		//Undo-Direction versuchen
    		//if (agent.requestField(undoDirection)) {
                goTo = undoDirection;
                return goTo;
            //}
    		//ansonsten ist Agent eingekesselt --> Richtung egal, Agent kann nichts tun
    	}
    	    	
    	return goTo;
    }
    
    public boolean fieldIsSpawn (IAgent agent, String direction) {
    	int x = agent.getPosx();
    	int y = agent.getPosy();
    	
    	switch (direction){
    	case AgentUtils.LEFT:
            x -= 1;
    		break;
    	case AgentUtils.TOP:
    		y -= 1;
    		break;
    	case AgentUtils.RIGHT:
    		x += 1;
    		break;
    	case AgentUtils.BOTTOM:
    		y += 1;
    		break;
    	}
    	
    	int home = agent.getRememberFieldSize() / 2;
    	
        return (x >= home-1 && x <= home+1) && (y >= home-1 && y <= home+1);
    }

    public void setUndoPreviousDirection(IAgent agent, String direction) {
    	int undoDirection = 0;
    	
    	switch (direction){
    	case AgentUtils.LEFT:
    		undoDirection = 3; //AgentUtils.RIGHT;
    		break;
    	case AgentUtils.TOP:
    		undoDirection = 4; //AgentUtils.BOTTOM;
    		break;
    	case AgentUtils.RIGHT:
    		undoDirection = 1; //AgentUtils.LEFT;
    		break;
    	case AgentUtils.BOTTOM:
    		undoDirection = 2; //AgentUtils.TOP;
    		break;
    	}
    	
    	agent.setCustomData(0, 0, undoDirection);
    }

    public String getUndoPreviousDirection(IAgent agent) {
    	int undo = agent.getCustomData(0, 0);
    	
    	String undoDirection = "";
    	
    	switch (undo){
    	case 1:
    		undoDirection = AgentUtils.LEFT;
    		break;
    	case 2:
    		undoDirection = AgentUtils.TOP;
    		break;
    	case 3:
    		undoDirection = AgentUtils.RIGHT;
    		break;
    	case 4:
    		undoDirection = AgentUtils.BOTTOM;
    		break;
    	}
    	
    	return undoDirection;
    }
    
}
