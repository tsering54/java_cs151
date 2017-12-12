
public class GameStateManager {

    PlayerScreen playerOne;
    PlayerScreen playerTwo;

    GameState currentPlayer;
    GameState nextPlayer;
    GameState hold;
    public Control con1;
    public Control con2;
    GameState gState;
    int stageCounter;
    

    public GameStateManager()
    {
    	stageCounter = 0;
    	
        con1 = new Control(this);
        con2 = new Control(this);
        playerOne = new PlayerScreen("Player1", true, this, con1);
        playerTwo = new PlayerScreen("Player2", false,this, con2);
        
        currentPlayer = playerOne;
        nextPlayer = playerTwo;

    }

    protected void toggleScreen()
    {
    	if(((PlayerScreen)currentPlayer).con.nships == 5 )
    	{	
    		((PlayerScreen)currentPlayer).con.canAttack = true;
    		stageCounter++;

    		playerOne.toggleVisability();
    		playerTwo.toggleVisability();
       
    		hold = currentPlayer;
    		currentPlayer = nextPlayer;
    		nextPlayer = hold;        
        
    		if(stageCounter == 2)
    		{
    			this.updateAttackGrids();
    		}
        
    		if(stageCounter > 2)
    			updateSelfGrids();
    	}

    }
    


    public void updateAttackGrids()
    {
    	ship[] hold;
    	hold = con1.sArr;
    	con1.sArr = con2.sArr;
    	con2.sArr = hold;
    	con1.setAtackArrShips();
    	con2.setAtackArrShips();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
            {
            	
                con1.attackArr[i][j] = con2.selfArr[i][j];
                con2.attackArr[i][j] = con1.selfArr[i][j];
            }
    }
    
    public void updateSelfGrids()
    {           
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
            {
            	if(((PlayerScreen)nextPlayer).con.attackArr[i][j] == 2)
            	{
            		((PlayerScreen)currentPlayer).con.selfArr[i][j] = 2;
            		((PlayerScreen)nextPlayer).con.attackArr[i][j] = 4;
            		i = 11;
            		j = 11;
            		((PlayerScreen)currentPlayer).con.scc.reflectAttacks();
            	}
            	else if(((PlayerScreen)nextPlayer).con.attackArr[i][j] == 3)
            	{
            		((PlayerScreen)currentPlayer).con.selfArr[i][j] = 3;
            		((PlayerScreen)nextPlayer).con.attackArr[i][j] = 4;
            		i = 11;
            		j = 11;
            		((PlayerScreen)currentPlayer).con.scc.reflectAttacks();
            	}
            }                 
    }
    
    public void setOpponetsShipsSunk()
    {
    	((PlayerScreen)nextPlayer).con.selfShipsSunk = ((PlayerScreen)currentPlayer).con.shipsSunk;
    	((PlayerScreen)nextPlayer).con.selfShipsSunkPanel.setDigit("" + ((PlayerScreen)nextPlayer).con.selfShipsSunk);
    }

}
