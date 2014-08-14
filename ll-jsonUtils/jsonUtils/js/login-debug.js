Ext.ns('LLVA.Login');

/* Login Window */
LLVA.Login.LoginWindow = Ext.extend(Ext.Window,
  
    (function() {

        // private members
        var me;
        var loginForm;
        var warningMessage = "U.S. Government Computer System\r\n\r\nU. S. government systems are intended to be used by " +
                             "authorized government network users for viewing and retrieving information only, except as " +
                             "otherwise explicitly authorized for official business and limited personal use in accordance " +
                             "with policy. Information from these systems resides on and transmits through computer systems " +
                             "and networks funded by the government. All access or use constitutes understanding and acceptance " +
                             "that there is no reasonable expectation of privacy in the use of Government networks or systems.\r\n\r\n" + 
                             "The data and documents on this system include Federal records that contain sensitive information " + 
                             "protected by various Federal statutes, including the Privacy Act, 5 U.S.C. Section 552a, and veterans' " +
                             "records confidentiality statutes such as 38 U.S.C. Sections 5701 and 7332. Access to the data and records " + 
                             "is on a need-to-know basis only.\r\n\r\nAll access or use of this system constitutes user understanding " + 
                             "and acceptance of these terms and constitutes unconditional consent to review and action including " + 
                             "(but not limited to) monitoring, recording, copying, auditing, inspecting, investigating, restricting " + 
                             "access, blocking, tracking, disclosing to authorized personnel, or any other authorized actions by all " + 
                             "authorized government and law enforcement personnel.\r\n\r\nUnauthorized user attempts or acts to " + 
                             "(1) access, upload, change, or delete information on this system, (2) modify this system, (3) deny " + 
                             "access to this system, (4) accrue resources for unauthorized use or (5) otherwise misuse this system " + 
                             "are strictly prohibited. Such attempts or acts are subject to action that may result in criminal, civil, " + 
                             "or administrative penalties.";
                             
        function doLogin() {
            loginForm.getForm().submit({ 
                method: "POST", 
                waitTitle: "Just a second...", 
                waitMsg: "Sending data...",
                success: function() { 
                    me.close();
                    if (typeof me.loginCallback == 'function') {
                        me.loginCallback();
                    }
                },
                failure: function(form, action) { 
                    switch (action.failureType) {
                        case Ext.form.Action.CLIENT_INVALID:
                            Ext.Msg.alert("Login Failed!", "Form fields may not be submitted with invalid values");
                            break;
                        case Ext.form.Action.CONNECT_FAILURE:
                            Ext.Msg.alert("Login Failed!", "Ajax communication failed");
                            break;
                        case Ext.form.Action.SERVER_INVALID:
                            Ext.Msg.alert("Login Failed!", action.result.errors);
                            break;
                    }
                    loginForm.getForm().reset(); 
                } 
            });               
        }                                         
      
        return {

            // public members
            title: undefined,
            loginUrl: undefined,
            logoutUrl: undefined,
            stationsUrl: undefined,
            loginCallback: undefined,
            logoutCallback: undefined,
                        
            initComponent: function() {
                me = this;
                              
                Ext.apply(this, {  
                    layout:'fit',
                    width:550,
                    height: 470,
                    title: undefined,
                    plain: true,
                    hidden: true,
                    closable: false,
                    resizable: false,
                    border: false,
                    modal: true,
                    draggable: true,    
                    items: [
                        loginForm = new Ext.form.FormPanel({
                            labelWidth:80,
                            frame:true, 
                            title: me.title, 
                            url: this.loginUrl,
                            monitorValid: true,                    
                            items: [                    
                                new Ext.form.TextArea({
                                    id: "vaMessage",
                                    width: 522,
                                    height: 275,
                                    hideLabel: true,
                                    readOnly: true,
                                    style: {
                                        marginBottom: "12px"
                                    },
                                    value: warningMessage            
                                }),                      
                                new Ext.form.Hidden({
                                    id: "institution",
                                    name: "institution",
                                    value: Ext.util.Cookies.get("gov.lom.med.defaultFacilityNo")            
                                }),
                                new Ext.form.ComboBox({ 
                                    id: "institutionField",
                                    fieldLabel: "Institution",
                                    name: "stationCode",
                                    width: 435,
                                    store: new LLVA.Login.StationsStore({
                                        dataUrl: me.stationsUrl                                      
                                    }),
                                    displayField: "divisionName",
                                    valueField: "stationNumber",
                                    value: Ext.util.Cookies.get("gov.lom.med.defaultFacilityStr"),
                                    typeAhead: false,
                                    forceSelection: true,
                                    triggerAction: "all",
                                    emptyText: "",
                                    selectOnFocus: true,
                                    allowBlank: false,
                                    blankText: "Please select your VA institution.",
                                    listeners:{
                                        "select": function() {
                                            Ext.getCmp("institution").setValue(Ext.getCmp('institutionField').value);
                                        }            
                                    }           
                                }),                                 
                                new Ext.form.TextField({
                                    id: "access",
                                    name: "access",
                                    fieldLabel: "Access Code", 
                                    inputType: "password",  
                                    allowBlank: false,
                                    blankText: "Please enter your Vista Access Code.",
                                    value: ''
                                }),    
                                new Ext.form.TextField({
                                    id: "verify",
                                    name: "verify",
                                    fieldLabel: "Verify Code", 
                                    inputType: "password", 
                                    allowBlank: false,
                                    blankText: "Please enter your Vista Verify Code." ,
                                    value: '',
                                    listeners: {
                                        specialkey: function(f,e){
                                            if (e.getKey() == e.ENTER) {
                                                doLogin();
                                            }
                                        }
                                    }
                                })
                            ],
                            buttons: [
                                new Ext.Button({
                                    text: "Login",
                                    formBind: true,
                                    width: 150,
                                    handler: function() { 
                                        Ext.util.Cookies.set("gov.lom.med.defaultFacilityNo", Ext.getCmp('institution').value, nextYear());
                                        Ext.util.Cookies.set("gov.lom.med.defaultFacilityStr",Ext.getCmp('institutionField').getRawValue(), nextYear());
                                        doLogin();
                                    }                                   
                                }) 
                            ]
                        })                    
                    ]                            
                });                
                
                LLVA.Login.LoginWindow.superclass.initComponent.apply(this, arguments);
            }, 
            listeners: {
                show: function() { 
                   if (Ext.getCmp('institutionField').getValue() === "") {
                       Ext.getCmp('institutionField').focus(false, 100);
                   } else {
                       Ext.getCmp('access').focus(false, false);
                   }
                }
            },
            login: function(params) {                              
                if (params == null) {
                    this.show();
                } else {
                    var conn = new Ext.data.Connection({
                        url: me.loginUrl,
                        extraParams: params 
                    });
                    conn.request({
                        success: function() {
                            if (typeof me.loginCallback == 'function') {
                                me.loginCallback();
                            }
                        },
                        failure: function() {
                            me.show();
                        }
                    });                                
                  
                }
            },
            
            logout: function() {
                var conn = new Ext.data.Connection({
                    url: me.logoutUrl 
                });
                conn.request({
                    success: function() {
                        LLVA.Login.LoginWindow._instance = null;
                        if (typeof me.logoutCallback == 'function') {
                            me.logoutCallback();
                        }
                    },
                    failure: function() {
                        if (typeof me.logoutCallback == 'function') {
                            me.logoutCallback();
                        }
                    }
                });              
            }
            
        };
    }())
);


LLVA.Login.LoginWindow._instance = null;

LLVA.Login.LoginWindow.getInstance = function(config){
  if (this._instance === null){
    this._instance = new LLVA.Login.LoginWindow(config);
  }
  return this._instance;
};            


/* Logged-In User */
LLVA.Login.User = Ext.extend(Ext.util.Observable,
  
    (function() {
  
        // private members  
        var me;
        var userStore;
      
        return {
      
            // public members
            userStoreUrl: undefined,
            userLoadedCallback: undefined,
            userDuz: '',            
            userName01: '',
            userNameDisplay: '',
            userparentAdministrativeFacilityStationNumber: '',
            userParentComputerSystemStationNumber: '',
            userLastName: '',
            userFirstName: '',
            userMiddleName: '',
            userPrefix: '',
            userSuffix: '',
            userDegree: '',
            signonLogIen: '',
            userLoginStationNumber: '',
            divisionName: '',
            userRoles: undefined,
            
            constructor : function(config) {
              
                me = this;
                
                Ext.apply(this, config);            
                
                userStore = new Ext.data.JsonStore({
                    proxy: new Ext.data.HttpProxy({
                        url: this.userStoreUrl,
                        method: 'POST'
                    }),
                    root:'root',
                    storeId: 'userStore',
                    fields: [
                        {name: 'userDuz'},
                        {name: 'userName01'},
                        {name: 'userNameDisplay'},
                        {name: 'userparentAdministrativeFacilityStationNumber'},
                        {name: 'userParentComputerSystemStationNumber'},
                        {name: 'userLastName'},
                        {name: 'userFirstName'},
                        {name: 'userMiddleName'},
                        {name: 'userPrefix'},
                        {name: 'userSuffix'},
                        {name: 'userDegree'},
                        {name: 'signonLogIen'},
                        {name: 'userLoginStationNumber'},
                        {name: 'divisionName'},
                        {name: 'userRoles'}
                    ],
                    listeners:{
                        load: function(store, records, options) {
                            var userRecord = this.getAt(0);
                            me.userDuz = userRecord.get("userDuz");
                            me.userName01 = userRecord.get("userName01");
                            me.userNameDisplay = userRecord.get("userNameDisplay");
                            me.userparentAdministrativeFacilityStationNumber = userRecord.get("userparentAdministrativeFacilityStationNumber");
                            me.userParentComputerSystemStationNumber = userRecord.get("userParentComputerSystemStationNumber");
                            me.userLastName = userRecord.get("userLastName");
                            me.userFirstName = userRecord.get("userFirstName");
                            me.userMiddleName = userRecord.get("userMiddleName");
                            me.userPrefix = userRecord.get("userPrefix");
                            me.userSuffix = userRecord.get("userSuffix");
                            me.userDegree = userRecord.get("userDegree");
                            me.signonLogIen = userRecord.get("signonLogIen");
                            me.userLoginStationNumber = userRecord.get("userLoginStationNumber");
                            me.divisionName = userRecord.get("divisionName");
                            var rolesStr = userRecord.get("userRoles");
                            var ary = rolesStr.split("^");
                            me.userRoles = [];
                            for(i = 0; i < ary.length; i++) {
                                me.userRoles[ary[i]] = ary[i];
                            }
                            if (typeof me.userLoadedCallback == 'function') {
                                me.userLoadedCallback(me);
                            }
                        }     
                    }
                });
            },
            
            loadUser: function() { 
                userStore.load();
            },
            
            isUserInRole:  function(role) {
                if (this.userRoles === null) { 
                    return false; 
                }
                return (this.userRoles[role] !== null) ? true : false;  
            }
             
        };
    }())
);  


LLVA.Login.User._instance = null;

LLVA.Login.User.getInstance = function(config){
  if (this._instance === null){
    this._instance = new LLVA.Login.User(config);
  }
  return this._instance;
};


/* Stations Store */
LLVA.Login.StationsStore = Ext.extend(Ext.data.Store,
    
    (function() {
      
        return {
          
            // public members    
            dataUrl: undefined,
          
            constructor: function(config) {
              
                Ext.apply(this, config);
              
                Ext.apply(this, {

                    autoLoad: true,
                    proxy: new Ext.data.HttpProxy({
                        api: {
                            read: this.dataUrl
                        },
                        method: 'POST'
                    }),
                    reader: new Ext.data.JsonReader({
                        idProperty: 'stationNumber',
                        root: 'root',
                        fields: [
                            {name: 'divisionName'},
                            {name: 'stationNumber'}
                        ]
                    })       
                });
                  
                LLVA.Login.StationsStore.superclass.constructor.apply(this, arguments);
            }        
        };
        
    }())
    
);  