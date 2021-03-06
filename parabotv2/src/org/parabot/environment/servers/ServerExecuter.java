package org.parabot.environment.servers;

import org.parabot.core.Context;
import org.parabot.core.ui.components.BotToolbar;

/**
 * 
 * @author Everel
 *
 */
public abstract class ServerExecuter {

	public abstract void run(final ThreadGroup tg);

	public void finalize(final ThreadGroup tg, final ServerProvider provider, final String serverName) {
		// loads the server and its it to the gui
		new Thread(tg, new Runnable() {
			@Override
			public void run() {
				try {
					final Context context = new Context(provider);
					BotToolbar.getInstance().addTab(context, serverName);
					context.load();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}

}
