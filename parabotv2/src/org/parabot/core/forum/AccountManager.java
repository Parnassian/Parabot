package org.parabot.core.forum;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.core.parsers.scripts.SDNScripts;
import org.parabot.core.ui.LoginUI;
import org.parabot.environment.api.utils.WebUtil;
import org.parabot.environment.scripts.SDNScriptExecuter;

/**
 * Handles logging in to parabot forum, only certain classes may use this class.
 * 
 * @author Everel
 * 
 */
public final class AccountManager {
	private static boolean validated = false;
	private static AccountManager instance = null;

	private Account account = null;

	private AccountManager() {

	}

	public static final void validate() {
		if (validated) {
			return;
		}
		instance = new AccountManager();

		Core.verbose("Initializing account manager accessors...");
		final ArrayList<AccountManagerAccess> accessors = new ArrayList<AccountManagerAccess>();
		accessors.add(SDNScripts.MANAGER_FETCHER);
		accessors.add(LoginUI.MANAGER_FETCHER);
		accessors.add(SDNScriptExecuter.MANAGER_FETCHER);

		for (final AccountManagerAccess accessor : accessors) {
			accessor.setManager(instance);
		}
		Core.verbose("Account managers initialized.");
	}
	
	public final boolean isLoggedIn() {
		return account != null;
	}
	
	public final Account getAccount() {
		return account;
	}

	public final boolean login(final String user, final String pass) {
		if (account != null) {
			throw new IllegalStateException("Already logged in.");
		}
		String contents = null;
		try {
			contents = WebUtil.getContents(String.format(
					Configuration.LOGIN_SERVER,
					URLEncoder.encode(user, "UTF-8"),
					URLEncoder.encode(pass, "UTF-8")));
		} catch (Throwable t) {
			return false;
		}

		if (contents.equals("correct")) {
			account = new Account(user, pass);
			return true;
		} 
		return false;
	}

}
