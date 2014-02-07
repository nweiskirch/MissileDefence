/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import app.domain.interfaces.Interceptor;
import app.managers.LauncherManager;
import utils.LoggingManager;

/**
 *
 * @author Nate
 */
public class PredictorInterceptor implements Interceptor {

    private BallisticInbound target;

    public void launch(BallisticInbound bi) throws Exception {
        target = bi;
        LauncherManager.getInstance().addToTarget(bi, null);
    }

    public void destroy() {
        LoggingManager.logInfo("Interceptor Detroyed");
    }

    public BallisticInbound getTarget() {
        return target;
    }
}
