/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.interfaces;

import app.domain.BallisticInbound;

/**
 *
 * @author Nate
 */
public interface Interceptor {
    public void launch(BallisticInbound bi) throws Exception;
    public void destroy();
    public BallisticInbound getTarget();
}
