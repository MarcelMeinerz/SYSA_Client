package schnecke_ohne_kaufen;

import gameclient.AgentUtils;
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

    /**
     * {@link schnecke.Strategy}
     * Um ein Blockieren zu vermeiden, fährt der Roboter, wenn der geplante Weg blockiert ist, drei Runden lang in eine zufällige Richtung. 
     * Ist der Weg dann noch immer blockiert, wiederholt er dies bis er seinen Weg fortsetzen kann.
     * Dies passiert auch wenn der äußerste Punkt der Spirale erreicht ist. 
     * Allerdings endet auch das Spiel damit, da er über jedes Feld gefahren ist und alle Rohstoffe dort gesammelt hat. 
     * Eine Ausnahme ist, wenn ein anderer Roboter auf einem bereits abgefahrenen Feld Rohstoffe platziert hat.
     * Die Strategie sieht keinen Kauf von weiteren Robotern vor, da diese nur hintereinanderfahren würden und hauptsächlich nur profitieren, wenn auf einem Feld mehr Rohstoffe liegen als der vordere Roboter laden kann.
     * Ein Nachteil dieser Strategie ist, dass an evtl. vollen Feldern vorbeigefahren wird, da erst die aktuelle Runde vervollständigt wird.

     */
    private static final long serialVersionUID = 1L;
    
    
    public Strategy() throws RemoteException {
        super();
       
    }

    boolean init = false;
    boolean onWayHome = false;
    boolean onWayBack = false;
    
    int lastX = 0;
    int lastY = 0;
    
    int lastGoalX = 0;
    int lastGoalY = 0;
    
    int size;
    int radius = 4;
    int round = 0;
    int randomStreak = 3;
    
    int goalX = 0;
    int goalY = 0;
    
    //Schnecke
    @Override
    public void nextAction(IAgent agent) throws RemoteException {
    	  	
        agent.take(); //Default Aktion
        if (agent.check() > 0 && (agent.getLoad() < agent.getCapacity()) && !onWayBack) {
            agent.take();
            return;
        }    
        
        
        if (!init){
        	init = true;
            size = agent.getRememberFieldSize();
                                    
            goalX = agent.getHomeXY() - 2;
            goalY = agent.getHomeXY() - 2;
            
        	System.out.println("Init Ziel: " + goalX + "/" + goalY);
        	
        }
 
        if (agent.getLoad() >= agent.getCapacity()){
        	int home = agent.getHomeXY();
        	
        	if (!onWayHome && !onWayBack){
	        	onWayHome = true;
	        	lastX = agent.getPosx();
	        	lastY = agent.getPosy();
        		System.out.println("Voll geladen, will von " + lastX + "/" + lastY + " nach Hause");
	        	
	        	lastGoalX = goalX;
	        	lastGoalY = goalY;
        	}
        	System.out.println("Auf nach Hause");

        	
        	if ((agent.getPosx() > home+1) && (agent.requestField(AgentUtils.LEFT))){
        		agent.go(AgentUtils.LEFT);
        	} else if ((agent.getPosx() < home-1) && (agent.requestField(AgentUtils.RIGHT))){
        		agent.go(AgentUtils.RIGHT);
        	} else if ((agent.getPosy() > home+1) && (agent.requestField(AgentUtils.TOP))){
        		agent.go(AgentUtils.TOP);
        	} else if ((agent.getPosy() < home-1) && (agent.requestField(AgentUtils.BOTTOM))){
        		agent.go(AgentUtils.BOTTOM);
        	} else if (agent.checkIfOnSpawn()){
        		agent.put();
        		onWayHome = false;
        		onWayBack = true;
        		goalX = lastX;
        		goalY = lastY;
        		System.out.println("Auf dem Weg nach " + goalX + "/" + goalY + " zur�ck.");
        	} else {
        		System.out.println("Kein Weg nach Hause gefunden");
        		
        	}
        	return;
        }

        

        
        if (moveTo(agent, goalX, goalY)){
        	System.out.println("Ziel " + goalX + "/" + goalY + " erreicht.");

        	if (!onWayHome && !onWayBack){
        		round = round + 1;
	        	if (round > 4){
	        		System.out.println("Radius erh�ht");
	        		round = 1;
	            	radius = radius + 2;
	            	goalX = goalX - 1;
	            	goalY = goalY - 1;
	        	}
	        	
	        	switch (round){
	        		case 1:
	        			goalX = goalX + radius;
	        			break;
	        		case 2:
	        			goalY = goalY + radius;
	        			break;
	        		case 3:
	        			goalX = goalX - radius;
	        			break;
	        		case 4:	
	        			goalY = goalY - radius;
	        			break;        	
	        	}
	        	System.out.println("Neues Ziel: " + goalX + "/" + goalY);
        	} else if (onWayBack){
        		onWayBack = false;
	        	goalX = lastGoalX;
	        	goalY = lastGoalY;
	        	System.out.println("Neues altes Ziel: " + goalX + "/" + goalY);
        	}
        } else {
        	return;
        }

        
    }
    
    
    public boolean moveTo(IAgent agent, int x, int y) {    	
    		System.out.println("Auf dem Weg zu: " + x + "/" + y);
    	
    		if (randomStreak > 0){
    			moveRandom(agent);
    		} 
    		
    		if ((agent.getPosy() > y) && (agent.requestField(AgentUtils.TOP))){
        		agent.go(AgentUtils.TOP);
        	} else if ((agent.getPosx() < x) && (agent.requestField(AgentUtils.RIGHT))){
        		agent.go(AgentUtils.RIGHT);
        	} else if ((agent.getPosx() > x) && (agent.requestField(AgentUtils.LEFT))){
        		agent.go(AgentUtils.LEFT);
        	} else if ((agent.getPosy() < y) && (agent.requestField(AgentUtils.BOTTOM))){
        		agent.go(AgentUtils.BOTTOM);
        	} else if (agent.getPosx() == x && agent.getPosy() == y){       		
        		return true;
        	} else {
        		System.out.println("Blockiert, move random");
        		randomStreak = 3;
        		moveRandom(agent);
        	}
        	
    		return false;
    }
    
    
    public void moveRandom(IAgent agent){
    	randomStreak = randomStreak -1;
    	int direction = 1 + (int)(Math.random() * ((4 - 1) + 1));
    	
    	
    	switch (direction){
    	case 0:
    		if (agent.requestField(AgentUtils.LEFT)) {
                agent.go(AgentUtils.LEFT);
            }
    		break;
    	case 1:
    		if (agent.requestField(AgentUtils.TOP)) {
                agent.go(AgentUtils.TOP);
            }
    		break;
    	case 2:
    		if (agent.requestField(AgentUtils.RIGHT)) {
                agent.go(AgentUtils.RIGHT);
            }
    		break;
    	case 3:
    		if (agent.requestField(AgentUtils.BOTTOM)) {
                agent.go(AgentUtils.BOTTOM);
    		}
    		break;
    	}
    }

    

}
