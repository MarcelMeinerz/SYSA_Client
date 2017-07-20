/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent.remote;

import java.io.Serializable;

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
     * Diese Method geibt den Name des Spielers zurueck.
     * @return Name des Spielers
     */
    public String getName();

    /**
     * Diese Methode liefert die X-Koordinate zurueck, an der sich der Agent befindet.
     * @return X-Koordinate
     */
    public int getPosx();

    /**
     * Diese Methode liefert die Y-Koordinate zurueck, an der sich der Agent befindet.
     * @return Y-Koordinate
     */
    public int getPosy();

    /**
     * Diese Methode liefert die maximale Tragfaehigkeit des Agents.
     * @return maximale Tragfaehigkeit
     */
    public int getCapacity();

    /**
     * Diese Methode liefert die momentane aufgenommene Ladung des Agents.
     * @return momentane aufgenommene Ladung
     */
    public int getLoad();

    /**
     * Diese Methode setzt die Order des Agenten auf Bewegung in die angegebene Richtung direction.
     * @param direction
     * <br>Richtungen:
     * <br><BLOCKQUOTE>     - links : {@link gameclient.AgentUtils#LEFT } </BLOCKQUOTE>
     * <BLOCKQUOTE>     - rechts : {@link gameclient.AgentUtils#RIGHT } </BLOCKQUOTE>
     * <BLOCKQUOTE>     - hoch : {@link gameclient.AgentUtils#TOP } </BLOCKQUOTE>
     * <BLOCKQUOTE>     - runter : {@link gameclient.AgentUtils#BOTTOM } </BLOCKQUOTE>
     */
    public void go(String direction);

    /**
     * Diese Methode setzt die Order des Agenten, dass er den Rohstoff auf seinem momentanen Feld aufnehmen soll.
     */
    public void take();

    /**
     * Diese Methode setzt prueft ob sich Rohstoffe auf seinem momentanen Feld aufnehmen befinden.
     * @return Rohstoffe
     */
    public int check();

    /**
     * Diese Methode setzt alle Rohstoffe die der Agent traegt auf dem Feld ab, auf dem er sich befindet.
     * {@link #put(int) }
     */
    public void put();

    /**
     * Diese Methode setzt eine bestimmte Anzahl von Rohstoffe die der Agent traegt auf dem Feld ab, auf dem er sich befindet.
     * @param value Anzahl der Rohstoffe die abgelegt werden sollen.
     * {@link #put() }
     */
    public void put(int value);

    /**
     * Diese Methode gibt die aktuelle Order des Agents zurueck.
     * @return Order
     */
    public String getOrder();

    /**
     * Diese Methode prueft ob das Feld in der angebenen Richtung frei ist.
     * @param direction
     * <br>Richtungen:
     * <br><BLOCKQUOTE>     - links : {@link gameclient.AgentUtils#LEFT } </BLOCKQUOTE>
     * <BLOCKQUOTE>     - rechts : {@link gameclient.AgentUtils#RIGHT } </BLOCKQUOTE>
     * <BLOCKQUOTE>     - hoch : {@link gameclient.AgentUtils#TOP } </BLOCKQUOTE>
     * <BLOCKQUOTE>     - runter : {@link gameclient.AgentUtils#BOTTOM } </BLOCKQUOTE>
     * @return true wenn Feld in der angebenen Richtung frei, sonst false
     */
    public boolean requestField(String direction);

    /**
     * Liefert die X- bzw. Y-Koordinate des Zentrums des {@code PlayingFields}.
     * Hierbei sind die Werte der X- und Y-Koordinate gleich.
     * @return Koordinate des Zentrums
     */
    public int getHomeXY();

    /**
     * Prueft ob der Roboter sich auf einem Spawn-Feld befindet.
     * @return true wenn der Roboter sich auf einem Spawnfeld befindet, sonst false
     */
    public boolean checkIfOnSpawn();

    /**
     * Liefert den aktuellen Punktestand des Spielers zurueck.
     * @return aktuellen Punktestand des Spielers
     */
    public int getPoints();

    /**
     * Diese Methode setzt die Order des Agenten auf {@link gameclient.AgentUtils#BUY}
     * Mit Hilfe dieser Methode wird der Kaufvorgang initialisiert.
     */
    public void buy();

    /**
     * Liefert die zu erzielende Punktzahl, die benoetigt wird um das Spiel zu gewinnen.
     * @return zu erzielende Punktzahl
     */
    public int getTargetAmount();

    /**
     * Liefert den Wert eines Roboters (Agent), der entrichtet werden muss um einen solchen zu erwerben.
     * @return Wert eines Roboters (Agent)
     */
    public int getAgentsValue();

    /**
     * Liefert die maximale Anzahl an Robotern (Agent), die ein Spieler besitzen kann
     * @return maximale Anzahl an Robotern (Agent)
     */
    public int getMaxAgents();
    
    /**
     * Gibt einen booleschen Wert zurueck, der darueber Auskunft gibt, ob die finanziellen Mittel vorhanden sind, einen neuen Roboter zu erwerben.
     * @return true wenn Roboter (Agent) gekauft werden kann, sonst false
     */
    public boolean hasEnoughToBuy();
    
    /**
     * Gibt einen booleschen Wert zurueck, der angibt ob die maximale Roboteranzahl bereits erreicht wurde
     * @return true wenn maximale Roboteranzahl bereits erreicht, sonst false
     * {@link #getMaxAgents() }
     */
    public boolean hasMaxAgents();
    
    /**
     * Prueft ob sich kein Agent des Spielers auf einem Spawn-Feld befindet
     * @return true wenn sich kein Agent des Spielers auf einem Spawn-Feld befindet, sonst false
     */
    public boolean checkSpawnIsPossible();
    
    /**
     * Liefert aus dem multidimensionalen {@code Array} {@code CustomData} den Datensatz an der Stelle [i][j]
     * @param i maximale Anzahl an moeglichen Agents 
     * @param j moegliche Anzahl von zu speichernden Datensaetzen 
     * @return Wert der sich auf den angegebenen Koordinaten befindet. Default-Value = -1
     * {@link #getMaxAgents() }
     */
    public int getCustomData(int i, int j);

    /**
     * Schreibt den uebergebenen Wert data in das multidimensionale Array CustomData an Stelle [i][j]
     * @param i maximale Anzahl an moeglichen Agents
     * @param j moegliche Anzahl von zu speichernden Datensaetzen
     * @param data Wert der an das Array uebergeben wird
     * {@link #getMaxAgents() } {@link #getCustomData(int, int) }
     */
    public void setCustomData(int i, int j, int data);
    
    /**
     * Gibt einen booleschen Wert zurueck, der Auskunft gibt ob ein neuer Roboter gekauft werden kann 
     * <br>Bedingung: 
     * <br><BLOCKQUOTE>- finanzielle Mittel vorhanden, </BLOCKQUOTE>
     * <BLOCKQUOTE>- kein anderer Agent auf Spawn-Feld, </BLOCKQUOTE> 
     * <BLOCKQUOTE>- maximale Anzahl an Robotern wurde noch nicht erreicht  </BLOCKQUOTE>
     * @return true wenn Bedingungen erfuellt sind, sonst false
     * {@link #hasEnoughToBuy() } {@link #checkIfOnSpawn() } {@link #hasMaxAgents() }
     */
    public boolean buyPossible();

    /**
     * Liefert ein {@code Array} mit allen Agenten des Spielers zurueck
     * @return Array mit allen Agenten des Spielers
     */
    public IAgent[] getAgentArray();
	 
    /**
     * Liefert die fuer das Feld mit den uebergebenen X-/Y-Koordinaten gemerkte Anzahl an Ressourcen.
     * @param x Koordinate x im RememberField
     * @param y Koordinate y im RememberField
     * @return Wert der an der Stelle x,y liegt
     */
    public int getRememberResources(int x, int y);

    /**
     * Traegt in das {@code RememberField} an der eigenen Position die uebergebene Anzahl an Ressourcen ein.
     * @param resources Wert der in das eigene Feld eingetragen wird
     * {@link #setRememberResources(int, int, int) }
     */
    public void setRememberResources(int resources);
    
    /**
     * Traegt in das {@code RememberField} an der uebergebenen X-/Y-Position die uebergebene Anzahl an Ressourcen ein.
     * @param x Koordinate x im RememberField
     * @param y Koordinate y im RememberField
     * @param resources Wert der in das eigene Feld eingetragen wird
     * {@link #setRememberResources(int) }
     */
    public void setRememberResources(int x, int y, int resources);
    
    /**
     * Liefert die Groesse des Spielfeldes zurueck.
     * @return Groesse des Spielfeldes
     */
    public int getRememberFieldSize();
        
    /**
     * Ueberschreibt die {@code RememberField} der anderen Agenten des Spielers mit dem {@code RememberField} des aufrufenden Agenten.
     */
    public void mergeRememberField();

    /**
     * Setzt alle Felder des RememberFields auf -1.
     */
    public void clearRememberField();
    
}
