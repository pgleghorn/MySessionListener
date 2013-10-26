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
