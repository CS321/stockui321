/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextUI;

/**
 *
 * @author treybriggs
 */
public class UsernameInputValidator implements InputValidator {

    @Override
    public boolean validate(Object input) {
        if (((String)input).length() > 2 && ((String)input).length() <= 16) {
            return true;
        }
        return false;
    }
    
}
