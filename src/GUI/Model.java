/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Stocks.Market;
import Stocks.TraderAccount;
import Users.Accounts;

/**
 *
 * @author WilsonZD
 */






public class Model 
{
    
    private TraderAccount traderaccount;
    private Accounts accounts;
    Market market;
    
    

    /**
     * @return the u
     */
    public TraderAccount getTraderAccount() {
        return traderaccount;
    }

    /**
     * @param u the u to set
     */
    public void setTraderAccount(TraderAccount u) {
        this.traderaccount = u;
    }

    /**
     * @return the accounts
     */
    public Accounts getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }
    
    
    
    
   
    
    
}
