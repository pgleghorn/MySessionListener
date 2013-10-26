package com.oracle.support;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Servlet listener that can log various info on session events. Supply
 * configuration via system properties, of the form
 * 
 * -DMySessionListener.<methodName>=<option,option,...>
 * 
 * where methodName is one of
 * 
 * attributeRemoved
 * attributeReplaced
 * attributeRemoved
 * valueUnbound
 * valueBound
 * sessionCreated
 * sessionDestroyed
 * sessionDidActivate
 * sessionWillPassivate
 * 
 * and <option> is one of the following
 * 
 * STACK - show stack trace of current thread
 * SOURCE - show class originating this event
 * SESSION - show session info like sessionid, maxexpiry, etc
 * THREAD - show thread info
 * ALL_ATTRIBUTES - show all session attributes
 * CHANGED_ATTRIBUTE - show only the changed session attribute (if applicable)
 * ALL - show all of the above
 * 
 * multiple options can be used, comma delimited. E.g.
 * 
 * -DMySessionListener.sessionCreated=SESSION,THREAD
 * -DMySessionListener.sessionDestroyed=ALL
 * -DMySessionListener.attributeReplaced=CHANGED_ATTRIBUTE
 * -DMySessionListener.attributeAdded=CHANGED_ATTRIBUTE
 * -DMySessionListener.attributeRemoved=CHANGED_ATTRIBUTE
 * 
 * @author Phil Gleghorn
 */
@WebListener
public class MySessionListener implements HttpSessionListener,
		HttpSessionActivationListener, HttpSessionBindingListener,
		HttpSessionAttributeListener {

	private HashMap<String, Set<String>> configurations = new HashMap<String, Set<String>>();

	private final String STACK = "STACK";
	private final String SESSION = "SESSION";
	private final String THREAD = "THREAD";
	private final String SOURCE = "SOURCE";
	private final String ALL_ATTRIBUTES = "ALL_ATTRIBUTES";
	private final String CHANGED_ATTRIBUTE = "CHANGED_ATTRIBUTE";
	private final String ALL = "ALL";

	private boolean isConfigured(String method, String configtype) {
		Set<String> configTypes = configurations.get(method);
		if (configTypes != null && configTypes.contains(configtype)) {
			return true;
		}
		return false;
	}

	private void showInfo(String method, String source, String clazz,
			HttpSession hs, Thread t, String name, Object value) {
		if (isConfigured(method, SOURCE) || isConfigured(method, ALL)) {
			log(method + "() source=" + source + ", class=" + clazz);
		}
		if (isConfigured(method, STACK) || isConfigured(method, ALL)) {
			StackTraceElement[] cause = Thread.currentThread().getStackTrace();
			for (int i = 0; i < cause.length; i++) {
				log(method + "() stack " + cause[i].getClassName() + "."
						+ cause[i].getMethodName() + "() ["
						+ cause[i].getFileName() + ":"
						+ cause[i].getLineNumber() + "]");
			}
		}
		if (isConfigured(method, SESSION) || isConfigured(method, ALL)) {
			log(method + "() session id=" + hs.getId());
			log(method + "() session creation time=" + hs.getCreationTime()
					+ " (" + new Date(hs.getCreationTime()) + ")");
			log(method + "() session last accessed time="
					+ hs.getLastAccessedTime() + " ("
					+ new Date(hs.getLastAccessedTime()) + ")");
			log(method + "() session max inactive interval="
					+ hs.getMaxInactiveInterval());
		}
		if (isConfigured(method, THREAD) || isConfigured(method, ALL)) {
			log(method + "() thread id=" + t.getId());
			log(method + "() thread name=" + t.getName());
			log(method + "() thread priority=" + t.getPriority());
			log(method + "() thread state=" + t.getState().toString());
		}
		if (isConfigured(method, ALL_ATTRIBUTES) || isConfigured(method, ALL)) {
			try {
				Enumeration<String> attrNames = hs.getAttributeNames();
				while (attrNames.hasMoreElements()) {
					String attrName = attrNames.nextElement();
					log(method + "() all_attributes " + attrName + "="
							+ hs.getAttribute(attrName));
				}
			} catch (IllegalStateException e) {
				// log(e.toString());
			}
		}
		if (isConfigured(method, CHANGED_ATTRIBUTE) || isConfigured(method, CHANGED_ATTRIBUTE)) {
			log(method + "() changed_attribute " + name + "=" + value);
		}
	}

	private void log(String msg) {
		long tid = Thread.currentThread().getId();
		System.out.println("t" + tid + ": " + msg);
	}

	/**
	 * Default constructor.
	 */
	public MySessionListener() {
		log("Hello from MySessionListener");
		Set<Object> ps = System.getProperties().keySet();
		for (Object o : ps) {
			String key = (String) o;
			if (key.startsWith("MySessionListener.")) {
				String[] propNameParts = key.split("\\.");
				String methodName = propNameParts[1];
				String[] propValueParts = System.getProperty(key, "")
						.split(",");
				for (int i = 0; i < propValueParts.length; i++) {
					String methodConfig = propValueParts[i];
					System.out.println("found configuration " + methodName
							+ " with " + methodConfig);
					Set<String> methodConfigs = new TreeSet<String>();
					if (configurations.containsKey(methodName)) {
						methodConfigs = configurations.get(methodName);
					}
					methodConfigs.add(methodConfig);
					configurations.put(methodName, methodConfigs);
				}
			}
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		showInfo("attributeRemoved", arg0.getSource().toString(), arg0
				.getClass().toString(), arg0.getSession(),
				Thread.currentThread(), arg0.getName(), arg0.getValue());
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		showInfo("attributeAdded", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(), arg0.getName(), arg0.getValue());
	}

	/**
	 * @see HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
	 */
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		log("valueUnbound() called for " + arg0.getName() + "=" + arg0.getValue());
		showInfo("valueUnbound", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(), arg0.getName(), arg0.getValue());
	}

	/**
	 * @see HttpSessionActivationListener#sessionDidActivate(HttpSessionEvent)
	 */
	public void sessionDidActivate(HttpSessionEvent arg0) {
		showInfo("sessionDidActivate", arg0.getSource().toString(), arg0
				.getClass().toString(), arg0.getSession(),
				Thread.currentThread(), "", "");
	}

	/**
	 * @see HttpSessionActivationListener#sessionWillPassivate(HttpSessionEvent)
	 */
	public void sessionWillPassivate(HttpSessionEvent arg0) {
		showInfo("valueBound", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(), "", "");
	}

	/**
	 * @see HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
	 */
	public void valueBound(HttpSessionBindingEvent arg0) {
		showInfo("valueBound", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(), arg0.getName(), arg0.getValue());
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		showInfo("attributeReplaced", arg0.getSource().toString(), arg0
				.getClass().toString(), arg0.getSession(),
				Thread.currentThread(), arg0.getName(), arg0.getValue());
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {
		showInfo("sessionCreated", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(), "", "");
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		showInfo("sessionDestroyed", arg0.getSource().toString(), arg0
				.getClass().toString(), arg0.getSession(),
				Thread.currentThread(), "", "");
	}

}
