    Servlet listener that can log various info on session events. Supply
    configuration via system properties, of the form
    
    -DMySessionListener.<methodName>=<option,option,...>
    
    where methodName is one of
    
    attributeAdded
    attributeReplaced
    attributeRemoved
    valueUnbound
    valueBound
    sessionCreated
    sessionDestroyed
    sessionDidActivate
    sessionWillPassivate
    
    and <option> is one of the following
    
    STACK - show stack trace of current thread
    SOURCE - show class originating this event
    SESSION - show session info like sessionid, maxexpiry, etc
    THREAD - show thread info
    ALL_ATTRIBUTES - show all session attributes
    ATTRIBUTE - show the changed/added/removed session attribute (if applicable)
    VALUE - show the bound/unbound value (if applicable)
    
    multiple options can be used, comma delimited. In example of configured provided via tomcat's setenv.sh:
    
    JAVA_OPTS=" ${JAVA_OPTS} \
    -DMySessionListener.sessionCreated=SESSION,THREAD,ALL_ATTRIBUTES,STACK \
    -DMySessionListener.sessionDestroyed=SESSION,THREAD,ALL_ATTRIBUTES,STACK \
    -DMySessionListener.attributeReplaced=SESSION,ATTRIBUTE \
    -DMySessionListener.attributeAdded=SESSION,ATTRIBUTE \
    -DMySessionListener.attributeRemoved=SESSION,ATTRIBUTE \
    -DMySessionListener.valueBound=SESSION,VALUE \
    -DMySessionListener.valueUnbound=SESSION,VALUE "

    To install, copy the jar into <your_deployed_webapp>/WEB-INF/lib, and edit web.xml to add the listener as the last entry in the <listeners> block:
    
    <listeners>
    ...
    <listener>
    <listener-class>com.oracle.support.MySessionListener</listener-class>
    </listener>
    <listeners>
    
    Output is written to application server stdout, first which confirms the configuration in use:
    
    Mon Jan 26 09:23:16 EST 2015 tid=12: Hello from MySessionListener
    found configuration attributeReplaced with SESSION
    found configuration attributeReplaced with ATTRIBUTE
    found configuration attributeAdded with SESSION
    found configuration attributeAdded with ATTRIBUTE
    found configuration sessionCreated with SESSION
    found configuration sessionCreated with THREAD
    found configuration sessionCreated with ALL_ATTRIBUTES
    found configuration sessionCreated with STACK
    found configuration valueBound with SESSION
    found configuration valueBound with VALUE
    found configuration valueUnbound with SESSION
    found configuration valueUnbound with VALUE
    found configuration attributeRemoved with SESSION
    found configuration attributeRemoved with ATTRIBUTE
    found configuration sessionDestroyed with SESSION
    found configuration sessionDestroyed with THREAD
    found configuration sessionDestroyed with ALL_ATTRIBUTES
    found configuration sessionDestroyed with STACK
    
    and then subsequent session event messages like:
    
    Mon Jan 26 09:26:09 EST 2015 tid=96: ----------begin--------->
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack java.lang.Thread.getStackTrace() [Thread.java:1436]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack com.oracle.support.MySessionListener.showInfo() [MySessionListener.java:97]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack com.oracle.support.MySessionListener.sessionCreated() [MySessionListener.java:271]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.session.StandardSession.tellNew() [StandardSession.java:423]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.session.StandardSession.setId() [StandardSession.java:395]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.session.StandardSession.setId() [StandardSession.java:376]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.session.ManagerBase.createSession() [ManagerBase.java:655]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.connector.Request.doGetSession() [Request.java:2898]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.connector.Request.getSession() [Request.java:2316]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack org.apache.catalina.connector.RequestFacade.getSession() [RequestFacade.java:898]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Servlet.ServletRequest.getSession() [ServletRequest.java:1266]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Servlet.FRequestObj.getSession() [FRequestObj.java:457]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Access.AccessEngine.newSession() [AccessEngine.java:754]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Access.AccessEngine.doLogin() [AccessEngine.java:470]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Access.AccessEngine.doLogin() [AccessEngine.java:403]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Common.ftAppLogic.checkLogin() [ftAppLogic.java:3262]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Common.ContentServer._doCheckLogin() [ContentServer.java:590]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Common.ContentServer.execute() [ContentServer.java:458]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Servlet.FTServlet.execute() [FTServlet.java:129]
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() stack COM.FutureTense.Servlet.FTServlet.doPost() [FTServlet.java:61]
    ...
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() session id=CC176AE6928989DFE52683E664E92EE6
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() session creation time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() session last accessed time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() session max inactive interval=1800
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() thread id=96
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() thread name=http-bio-8443-exec-1
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() thread priority=5
    Mon Jan 26 09:26:09 EST 2015 tid=96: sessionCreated() thread state=RUNNABLE
    Mon Jan 26 09:26:09 EST 2015 tid=96: <----------end----------
    Mon Jan 26 09:26:09 EST 2015 tid=96: ----------begin--------->
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session id=CC176AE6928989DFE52683E664E92EE6
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session creation time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session last accessed time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session max inactive interval=90000
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() attribute username=DefaultReader
    Mon Jan 26 09:26:09 EST 2015 tid=96: <----------end----------
    Mon Jan 26 09:26:09 EST 2015 tid=96: ----------begin--------->
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session id=CC176AE6928989DFE52683E664E92EE6
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session creation time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session last accessed time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session max inactive interval=90000
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() attribute currentUser=2
    Mon Jan 26 09:26:09 EST 2015 tid=96: <----------end----------
    Mon Jan 26 09:26:09 EST 2015 tid=96: ----------begin--------->
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session id=CC176AE6928989DFE52683E664E92EE6
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session creation time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session last accessed time=1422282369005 (Mon Jan 26 09:26:09 EST 2015)
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() session max inactive interval=90000
    Mon Jan 26 09:26:09 EST 2015 tid=96: attributeAdded() attribute currentACL=Browser,Visitor
    Mon Jan 26 09:26:09 EST 2015 tid=96: <----------end----------
    
