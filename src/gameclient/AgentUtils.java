/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameclient;

import java.awt.Color;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public class AgentUtils {
    
    public static final String GO = "go";
    public static final String PUT = "put";
    public static final String TAKE = "take";
    public static final String CHECK = "check";
    public static final String LEFT = "l";
    public static final String RIGHT = "r";
    public static final String TOP = "t";
    public static final String BOTTOM = "b";
    public static final String BUY = "buy";
    
    public static final String ROT = "#f77462";
    public static final String BLAU = "#659bf2";
    public static final String GELB = "#f7f35b";
    public static final String CYAN = "#60e3f2";
    public static final String LILA = "#b091e0";
    public static final String ORANGE = "#ffa500";
    public static final String ROSA = "#f2a7c2";
    
    
    public static  final Color [] COLORS = new Color[]{
    	Color.decode(ROT), //rot 
    	Color.decode(BLAU), //blau
    	Color.decode(GELB), //gelb 
    	Color.decode(CYAN), //cyan
    	Color.decode(LILA), //lila 
    	Color.decode(ORANGE), //orange 
    	Color.decode(ROSA), //rosa 
    	Color.WHITE
    	};
    
}

