import java.util.Observable;

public class Control {
    public int selfArr[][];
    public int attackArr[][];
    public int nships;
    int hitCount;
    int attacks;
    int shipsSunk;
    int selfShipsSunk;
    
    AttackCellController acc;
    SelfCellController scc;
    
    ship[] sArr = {null, null, null, null, null};
    
    TextPanel scorePanel = new TextPanel();
    TextPanel attackPanel = new TextPanel();
    TextPanel shipsSunkPanel = new TextPanel();
    TextPanel selfShipsSunkPanel = new TextPanel();
    TextPanel hitPanel = new TextPanel();
    
    boolean canAttack = false;
    
    GameStateManager gms;

    public Control(GameStateManager g)
    {
        hitCount = 0;
        attacks = 0;
        shipsSunk = 0;
        selfShipsSunk = 0;
        nships = 0;
        acc = new AttackCellController();
        scc = new SelfCellController();
        gms = g;
        selfArr = new int[13][13];

        attackArr = new int[13][13];
        shipsSunkPanel.add(selfShipsSunkPanel, "South");        
        hitPanel.add(shipsSunkPanel, "South");
        attackPanel.add(hitPanel, "South");
        scorePanel.add(attackPanel, "South");
        scorePanel.appendText("Status: ");
       	shipsSunkPanel.appendText("Enemy ships Sunk: ");
       	selfShipsSunkPanel.appendText("Own Ships sunk: ");
        hitPanel.appendText("Hits: ");
        attackPanel.appendText("Attacks:  ");
        
      
    }

    public int getSelfArrAtIndex(int x, int y)
    {
        return selfArr[x][y];
    }

    public void placeShip(int x, int y)
    {
        if(nships < 5 && x <= 7 && selfArr[x][y] == 0 && selfArr[x + 1][y] == 0  && selfArr[x + 2][y] == 0)
        {
            selfArr[x][y] = 1;
            selfArr[x + 1][y] = 1;
            selfArr[x + 2][y] = 1;
            
            
            sArr[nships] = new ship(x);
            scc.setSelfCells(sArr[nships]);
            nships++;
            
        }  
    }
    


    public void setAtackArrShips() {
    	for(int i = 0; i < nships; i++)
        	acc.setAttackArr(sArr[i]);
    }




}