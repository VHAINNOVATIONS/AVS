Place int the server/default/lib directory 

ll-kaajee-jboss.jar 
vha-stddata-basic-18.0.jar
vha-stddata-client-18.0.jar
h2-xxx.jar


The vha-std database must be configured.  An example
*-ds.xml file is in this directory.

I used a local H2 database successfully.


the following blurb needs to be added to server/default/conf/login-config.xml

  <application-policy name="vistaRealm">
    <authentication>
      <login-module code="gov.va.med.lom.kaajee.jboss.security.auth.KaajeeLoginModule" flag="required">
        <module-option name="principalClass">gov.va.med.lom.kaajee.jboss.model.KaajeePrincipal</module-option>
      </login-module>
      
    </authentication>
  </application-policy>