/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextUI;

/**
 *
 * @author treybriggs
 */
public class SellSharesInputValidator implements InputValidator {

    @Override
    public boolean validate(Object input) {
        try {
        Integer.parseInt((String)input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;    }
    
}
