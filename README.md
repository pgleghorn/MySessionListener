    Servlet listener that can log various info on session events. Supply
    configuration via system properties, of the form
    
    -DMySessionListener.<methodName>=<option,option,...>
    
    where methodName is one of
    
    attributeRemoved
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
    CHANGED_ATTRIBUTE - show only the changed session attribute (if applicable)
    ALL - show all of the above
    
    multiple options can be used, comma delimited. E.g.
    
    -DMySessionListener.sessionCreated=SESSION,THREAD
    -DMySessionListener.sessionDestroyed=ALL
    -DMySessionListener.attributeReplaced=CHANGED_ATTRIBUTE
    -DMySessionListener.attributeAdded=CHANGED_ATTRIBUTE
    -DMySessionListener.attributeRemoved=CHANGED_ATTRIBUTE

    To install, copy the jar into <cs_deployed_webapp>/WEB-INF/lib, and edit web.xml to add the listener:
    
    <listener>
    <listener-class>com.oracle.support.MySessionListener</listener-class>
    </listener>
    
    Output is written to application server stdout (e.g catalina.out in case of tomcat). Example output (Using
        -DMySessionListener.sessionCreated=SESSION,THREAD
        -DMySessionListener.sessionDestroyed=ALL
    ) is as follows:
    
    t103: sessionCreated() session id=D7EB3D94BC2C68EB471A935888A7282A
    t103: sessionCreated() session creation time=1382961605616 (Mon Oct 28 08:00:05 EDT 2013)
    t103: sessionCreated() session last accessed time=1382961605616 (Mon Oct 28 08:00:05 EDT 2013)
    t103: sessionCreated() session max inactive interval=1800
    t103: sessionCreated() thread id=103
    t103: sessionCreated() thread name=http-bio-8080-exec-7
    t103: sessionCreated() thread priority=5
    t103: sessionCreated() thread state=RUNNABLE
    
    t89: sessionDestroyed() source=org.apache.catalina.session.StandardSessionFacade@3d56c72b, class=class javax.servlet.http.HttpSessionEvent
    t89: sessionDestroyed() stack java.lang.Thread.getStackTrace() [Thread.java:1436]
    t89: sessionDestroyed() stack com.oracle.support.MySessionListener.showInfo() [MySessionListener.java:85]
    t89: sessionDestroyed() stack com.oracle.support.MySessionListener.sessionDestroyed() [MySessionListener.java:231]
    t89: sessionDestroyed() stack org.apache.catalina.session.StandardSession.expire() [StandardSession.java:806]
    t89: sessionDestroyed() stack org.apache.catalina.session.StandardSession.isValid() [StandardSession.java:658]
    t89: sessionDestroyed() stack org.apache.catalina.session.ManagerBase.processExpires() [ManagerBase.java:534]
    t89: sessionDestroyed() stack org.apache.catalina.session.ManagerBase.backgroundProcess() [ManagerBase.java:519]
    t89: sessionDestroyed() stack org.apache.catalina.core.ContainerBase.backgroundProcess() [ContainerBase.java:1352]
    t89: sessionDestroyed() stack org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.processChildren() [ContainerBase.java:1530]
    t89: sessionDestroyed() stack org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.processChildren() [ContainerBase.java:1540]
    t89: sessionDestroyed() stack org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.processChildren() [ContainerBase.java:1540]
    t89: sessionDestroyed() stack org.apache.catalina.core.ContainerBase$ContainerBackgroundProcessor.run() [ContainerBase.java:1519]
    t89: sessionDestroyed() stack java.lang.Thread.run() [Thread.java:619]
    t89: sessionDestroyed() session id=42C0EFEAAD46AB71AD51C7D7857D4C3F
    t89: sessionDestroyed() session creation time=1382871676065 (Sun Oct 27 07:01:16 EDT 2013)
    t89: sessionDestroyed() session last accessed time=1382871722033 (Sun Oct 27 07:02:02 EDT 2013)
    t89: sessionDestroyed() session max inactive interval=90000
    t89: sessionDestroyed() thread id=89
    t89: sessionDestroyed() thread name=ContainerBackgroundProcessor[StandardEngine[Catalina]]
    t89: sessionDestroyed() thread priority=5
    t89: sessionDestroyed() thread state=RUNNABLE
    t89: sessionDestroyed() all_attributes _authkey_=AD5500E05C75403AE99EA31352A1B9C499139B35498C668B6F4E749237B351C1A877134F3C5D06F18B5187823D841A59
    t89: sessionDestroyed() all_attributes username=phil
    t89: sessionDestroyed() all_attributes currentUser=1378452090066
    t89: sessionDestroyed() all_attributes currentUserPassword={AES}5841F60B59B976AEA4EDC1E18ECF52DE
    t89: sessionDestroyed() all_attributes currentACL=Browser,ElementEditor,ElementReader,PageEditor,PageReader,RemoteClient,SiteGod,TableEditor,UserEditor,UserReader,Visitor,VisitorAdmin,WSAdmin,WSEditor,WSUser,xceladmin,xceleditor,xcelpublish
    t89: sessionDestroyed() all_attributes csrfuuid=08afea07-1b33-4636-ab70-7eb2f883fc99
