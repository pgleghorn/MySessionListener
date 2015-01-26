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

/*
 * See Readme.md for instructions
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
	private final String ATTRIBUTE = "ATTRIBUTE";
	private final String VALUE = "VALUE";


	private boolean isConfigured(String method, String configtype) {
		Set<String> configTypes = configurations.get(method);
		if (configTypes != null && configTypes.contains(configtype)) {
			return true;
		}
		return false;
	}

	private void showInfo(String method, String source, String clazz,
			HttpSession hs, Thread t, String name, Object value) {

		log("----------begin--------->");
		try {
			if (isConfigured(method, SOURCE)) {
				log(method + "() source=" + source + ", class=" + clazz);
			}
		} catch (Exception e) {
			log(method + "() failed to log " + STACK + " due to: " + e);
		}

		try {
			if (isConfigured(method, STACK)) {
				StackTraceElement[] cause = Thread.currentThread()
						.getStackTrace();
				for (int i = 0; i < cause.length; i++) {
					log(method + "() stack " + cause[i].getClassName() + "."
							+ cause[i].getMethodName() + "() ["
							+ cause[i].getFileName() + ":"
							+ cause[i].getLineNumber() + "]");
				}
			}
		} catch (Exception e) {
			log(method + "() failed to log " + STACK + " due to: " + e);
		}

		try {
			if (isConfigured(method, SESSION)) {
				log(method + "() session id=" + hs.getId());
				log(method + "() session creation time=" + hs.getCreationTime()
						+ " (" + new Date(hs.getCreationTime()) + ")");
				log(method + "() session last accessed time="
						+ hs.getLastAccessedTime() + " ("
						+ new Date(hs.getLastAccessedTime()) + ")");
				log(method + "() session max inactive interval="
						+ hs.getMaxInactiveInterval());
			}
		} catch (Exception e) {
			log(method + "() failed to log " + SESSION + " due to: " + e);
		}

		try {
			if (isConfigured(method, THREAD)) {
				log(method + "() thread id=" + t.getId());
				log(method + "() thread name=" + t.getName());
				log(method + "() thread priority=" + t.getPriority());
				log(method + "() thread state=" + t.getState().toString());
			}
		} catch (Exception e) {
			log(method + "() failed to log " + THREAD + " due to: " + e);
		}

		try {
			if (isConfigured(method, ALL_ATTRIBUTES)) {
				Enumeration<String> attrNames = hs.getAttributeNames();
				while (attrNames.hasMoreElements()) {
					String attrName = attrNames.nextElement();
					log(method + "() all_attributes " + attrName + "="
							+ hs.getAttribute(attrName));
				}
			}
		} catch (Exception e) {
			log(method + "() failed to log " + ALL_ATTRIBUTES + " due to: " + e);
		}

		try {
			if (isConfigured(method, ATTRIBUTE)) {
				log(method + "() attribute " + name + "=" + value);
			}
		} catch (Exception e) {
			log(method + "() failed to log " + ATTRIBUTE + " due to: " + e);
		}

		try {
			if (isConfigured(method, VALUE)) {
				log(method + "() value " + name + "=" + value);
			}
		} catch (Exception e) {
			log(method + "() failed to log " + VALUE + " due to: " + e);
		}

		log("<----------end----------");
	}

	private void log(String msg) {
		long tid = Thread.currentThread().getId();
		Date now = new Date();
		System.out.println(now + " tid=" + tid + ": " + msg);
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
				.toString(), arg0.getSession(), Thread.currentThread(),
				arg0.getName(), arg0.getValue());
	}

	/**
	 * @see HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
	 */
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		showInfo("valueUnbound", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(),
				arg0.getName(), arg0.getValue());
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
		showInfo("sessionWillPassivate", arg0.getSource().toString(), arg0
				.getClass().toString(), arg0.getSession(),
				Thread.currentThread(), "", "");
	}

	/**
	 * @see HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
	 */
	public void valueBound(HttpSessionBindingEvent arg0) {
		showInfo("valueBound", arg0.getSource().toString(), arg0.getClass()
				.toString(), arg0.getSession(), Thread.currentThread(),
				arg0.getName(), arg0.getValue());
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
