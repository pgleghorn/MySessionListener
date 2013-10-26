Servlet listener that can log various info on session events. Supply
configuration via system properties, of the form:

-DMySessionListener.<methodName>=<option,option,...>

where methodName is one of:

attributeRemoved
attributeReplaced
attributeRemoved
valueUnbound
valueBound
sessionCreated
sessionDestroyed
sessionDidActivate
sessionWillPassivate

and <option> is one of the following:

STACK - show stack trace of current thread
SOURCE - show class originating this event
SESSION - show session info like sessionid, maxexpiry, etc
THREAD - show thread info
ATTRIBUTES - show all session attributes
ALL - show all of the above

multiple options can be used, comma delimited. E.g.

-DMySessionListener.sessionCreated=SESSION,THREAD
-DMySessionListener.sessionDestroyed=ALL
-DMySessionListener.attributeReplaced=ATTRIBUTES
-DMySessionListener.attributeAdded=ATTRIBUTES
-DMySessionListener.attributeRemoved=ATTRIBUTES

