/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextUI;

/**
 *
 * @author treybriggs
 */
public class MoneyInputValidator implements InputValidator {

    @Override
    public boolean validate(Object input) {
        try {
        Double.parseDouble((String)input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;    }
    
}
