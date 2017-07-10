

import multiagent.remote.IStrategy;
import gameclient.AgentUtils;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import multiagent.remote.IAgent;

public class Strategy_Eimer2 extends UnicastRemoteObject implements IStrategy, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    
    public Strategy_Eimer2() throws RemoteException {
        super();
       
    }

    boolean init = false;
    boolean onWayHome = false;
    boolean onWayBack = true;
    
    int lastGoalX = 0;
    int lastGoalY = 0;
    
    int size;
    int plannedAgents = 0;
    int collecterX, collecterY;

    
    //Eimerkette
    @Override
    public void nextAction(IAgent agent) throws RemoteException {    	  	
        agent.take(); //Default Aktion
        
        IAgent[] ar = agent.getAgentArray(); 
        //Id des Roboters festlegen
        if (agent.getCustomData(0, 0) == 0){
        	agent.setCustomData(0, 0, agent.getAgentArray().length);
        }
        
        
        if (!init){
        	init = true;
            size = agent.getRememberFieldSize();
            
            plannedAgents = 3; // Aufrunden
            System.out.println("Geplante Agenten: " + plannedAgents);
                                    
            collecterX = agent.getHomeXY() / 3 * 2;
            collecterY = agent.getHomeXY() - 0;    	
        }
        
        
        if (ar.length == 1){
    		//collecterAction(agent); 
    		//finderAction(agent);
        	singelRobotAction(agent);
        	return;
        
     
        //Bewegen
    	} else if (ar.length >= 2){
        	if (agent.getCustomData(0, 0) == 1){
        		collecterAction(agent);  
        		return;
    		} else {
    			finderAction(agent);
    			return;
    		}
        }
    }
    
    
    



	public void singelRobotAction(IAgent agent){
        if (agent.check() > 0 && (agent.getLoad() < agent.getCapacity())) {
            agent.take();
            return;
        }    
        
        
        if (agent.buyPossible() && agent.getAgentArray().length < plannedAgents){
        	agent.buy();
        	return;
        }
        
        
        if (agent.getLoad() >= agent.getCapacity()){
        	int home = agent.getHomeXY();
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
        	} else {
        		moveRandom(agent);
        	}
        	return;
        }
        
        moveRandom(agent);
    }
    
    
    
    
    
    public void moveRandom(IAgent agent){
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
    		
    	default:
   	
            if (agent.requestField(AgentUtils.LEFT)) {
                agent.go(AgentUtils.LEFT);
            } else if (agent.requestField(AgentUtils.TOP)) {
                agent.go(AgentUtils.TOP);
            } else if (agent.requestField(AgentUtils.RIGHT)) {
                agent.go(AgentUtils.RIGHT);
            } else if (agent.requestField(AgentUtils.BOTTOM)) {
                agent.go(AgentUtils.BOTTOM);
            }
    	}    
        
    
    }
    
    
    public void collecterAction(IAgent agent){
    	//System.out.println("Position: " + agent.getPosx() + " / " + agent.getPosy());
    	
    	if (onWayBack) {
    		if (moveTo(agent, collecterX, collecterY)){
    			System.out.println("Am Collector Punkt");
    			onWayBack = false;
    			onWayHome = false;
    			return;
    		}
    	}
    	
    	if (onWayHome){
    		if (moveHome(agent)){
    			System.out.println("Zu Hause");
    			onWayHome = false;
    			onWayBack = true;
    		}    		
    	}
    	
    	if (!onWayHome && !onWayBack) {
    		System.out.println("Von Collector Punkt aufgenommen");
    		agent.take();
    		if (agent.check() == 0 || agent.getLoad() >= agent.getCapacity()){
    			onWayHome = true;
    		}

    		return;
    	}
    }
    
    
    public boolean moveHome(IAgent agent) {
    	int home = agent.getHomeXY();
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
    		return true;
    	}        	    
    	return false;
    }
    
    
    
    private void finderAction(IAgent agent) {   	
        if (agent.check() > 0 && (agent.getLoad() < agent.getCapacity()) && !(agent.getPosx() == collecterX && agent.getPosy() == collecterY)) {
            agent.take();
            return;
        }    
        
        
        if (agent.buyPossible() && agent.getAgentArray().length < plannedAgents){
        	agent.buy();
        	return;
        }
        
        
        if (agent.getLoad() >= agent.getCapacity()){       	
        	if ((agent.getPosx() > collecterX) && (agent.requestField(AgentUtils.LEFT))){
        		agent.go(AgentUtils.LEFT);
        	} else if ((agent.getPosx() < collecterX) && (agent.requestField(AgentUtils.RIGHT))){
        		agent.go(AgentUtils.RIGHT);
        	} else if ((agent.getPosy() > collecterY) && (agent.requestField(AgentUtils.TOP))){
        		agent.go(AgentUtils.TOP);
        	} else if ((agent.getPosy() < collecterY) && (agent.requestField(AgentUtils.BOTTOM))){
        		agent.go(AgentUtils.BOTTOM);
        	} else if (agent.getPosx() == collecterX && agent.getPosy() == collecterY){
        		agent.put();
        	} else {
        		moveRandom(agent);
        	}
        	return;
        }
        
        moveRandom(agent);
		
	}


    
    public boolean moveTo(IAgent agent, int x, int y) {    	
    		System.out.println("Auf dem Weg zu: " + x + "/" + y);
    		
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
        		moveRandom(agent);
        	}        	
    		return false;
    }
    

}
