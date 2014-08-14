Ext.define('LLVA.Session.Manager', {
	extend: 'Ext.util.Observable',

	config: {
		urls: {
			userInfo:   '../w/s/user/userInfo.action',
			facilities: '../w/login/stations.action',
			login:      '../w/login/LoginController',
			logout:     '../w/login/logout.action'
		},
		offlineUrls: {
			userInfo:   '/loginutils/offline-data/user-info.json',
			facilities: '/loginutils/offline-data/facilities.json',
			login:      '/loginutils/offline-data/login.json',
			logout:     '/loginutils/offline-data/logout.json'
		},
		hasConnectivity: true,
		cookieName: 'gov.va.med.lom.defaultFacilityNo',
        stationNo: null,
		user: null,
		scope: null,
		callback: null,
		autoInit: true,
        allowDuzLogin: true,
        flag403: false,
		listeners: {}
	},

	requires: [
   		'Ext.util.Cookies',
		'Ext.form.Panel',
		'Ext.form.field.ComboBox',
		'LLVA.Session.store.Facilities',
		'LLVA.Session.view.LoginDialog',
		'LLVA.Session.model.User',
		'LLVA.Utils.MessageBox'
	],

	loadMask: null,

	constructor: function(config) {

		this.callParent(arguments);

		this.initConfig(config);

		this.addEvents({
			beforelogin: true,
			beforelogindialogshow: true,
			beforelogout: true,
			login: true,
			logindialogshow: true,
			logout: true,
			sessionload: true
		});
		
		if (this.hasConnectivity == false) {
			this.urls = this.offlineUrls;
		}

		if (Ext.isFunction(this.callback) == false) {
			Ext.Error.raise('Callback function not given');
		}
        
        this.flag403 = false;
        
		if (this.autoInit == true) {
			this.init();
		}
	},

	init: function() {
		Ext.onReady(this.loadUserInfo, this);
	},

	loadUserInfo: function() {

		this.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Session..."});
		this.loadMask.show();

		Ext.Ajax.request({
			url: this.urls.userInfo,
			method: 'GET',
			extraParams: this.getHttpArgs(),
			scope: this,
			callback: this.processUserInfoResult
		});
	},

	getHttpArgs: function() {
		var params = {};

		var httpArgs = Ext.urlDecode(location.search.substring(1));
		if (this.stationNo) {
            params.institution = this.stationNo;
        } else if (httpArgs.stationNo) {
			params.institution = httpArgs.stationNo;
		}
		if (httpArgs.userDuz) {
			params.userDuz = httpArgs.userDuz;
		}
        /*
		if (httpArgs.vistaToken) {
			params.vistaToken = httpArgs.vistaToken;
		}
        */
		return params;
	},

	processUserInfoResult: function(options, success, response) {

		this.loadMask.hide();

		var json = Ext.JSON.decode(response.responseText);
		if (success == true && json != null && json.success) {
			this.user = Ext.create('LLVA.Session.model.User', json.root[0]);

			if (this.fireEvent('sessionload') === false) {
				return;
			}

			this.callback.apply(this.scope || this, [this.user]);
			return;
        }

		if (response.status == 403) {
            
            var params = this.getHttpArgs();
            if (!this.flag403 && ((params.institution != null) && 
                (((params.userDuz != null) && this.allowDuzLogin) || 
                 (params.vistaToken != null)))) {                
                this.flag403 = true;
                this.submitComLoginRequest();
            } else {
			    this.showLoginDialog();
            }            
            
		} else {
			LLVA.Utils.MessageBox.error('Failed loading session info from server');
		}

	},       
		
	showLoginDialog: function() {

		if (this.fireEvent('beforelogindialogshow') === false) {
			return;
		}

		Ext.create('LLVA.Session.store.Facilities');
		var dialog = Ext.create('LLVA.Session.view.LoginDialog');

		this.configureLoginDialogListeners(dialog);
		this.configureFacilityField(dialog);

		this.fireEvent('logindialogshow');
	},

	configureLoginDialogListeners: function(dialog) {

		var submitFormByPressingEnter = function(field, event) {
		    if (event.getKey() == event.ENTER) {
		    	this.submitLoginRequest(dialog);
		    }
		};
		dialog.down('textfield#access').on('specialkey', submitFormByPressingEnter, this);
		dialog.down('textfield#verify').on('specialkey', submitFormByPressingEnter, this);

		dialog.down('button[action=login]').on('click', function(button) {
			this.submitLoginRequest(dialog);
		}, this);
	},
	
	configureFacilityField: function(dialog) {

		var store = dialog.down('combobox#facilityNo').store;
		store.getProxy().api.read = this.urls.facilities;

		store.load({
			scope: this,
			callback: function() {
				var facilityField = dialog.down('combobox#facilityNo');
				var accessCodeField = dialog.down('textfield#access');

				var facilityNo = Ext.util.Cookies.get(this.getCookieName);

				if (facilityNo && store.findExact('stationNumber', facilityNo) >= 0) {
	    			// Cookie matches a value in the store. Set it as default and move on.
	    			facilityField.setValue(facilityNo);
	                accessCodeField.focus(false, false);

	    		} else if (store.getTotalCount() > 0) {
	    			// Cookie's value isn't in the store.  Pick the first entry.
	    			var defaultStationNumber = store.first().data.stationNumber;
	    			facilityField.setValue(defaultStationNumber);
	    			accessCodeField.focus(false, false);

				} else {
					// Store is empty.  Grab focus and hope for the best.
					facilityField.focus(false, false);
				}

			}
		});
	},
	
	submitComLoginRequest: function() {    
		this.loadMask = new Ext.LoadMask(Ext.getBody(), {msg: "Logging on..."});
		this.loadMask.show();
        
		Ext.Ajax.request({
            scope: this,
			url: this.urls.login,
            params: this.getHttpArgs(),
			method: 'POST',
			failure: function(response, requestOptions) {
				this.loadMask.hide();
                showServerFailureDialog(response, requestOptions);
            },
			success: function(response) {
				this.loadMask.hide();
                if (response.responseText == '') {
                    this.showLoginDialog();  
                } else {
                    this.loadSessionFromServer();          
                }
			}
		});
    },        
    
	submitLoginRequest: function(dialog) {

		if (this.fireEvent('beforelogin') === false) {
			return;
		}

		var form = dialog.down('form');

    	if (!form.getForm().isValid()) {
    		return showAlertDialog('Please complete the form and try again', 'Login Failed');
    	}

    	var nextYear = new Date();
    	nextYear.setDate(nextYear.getDate() + 365);
        Ext.util.Cookies.set(
    		this.getCookieName(), form.down('field[id=facilityNo]').value, nextYear
		);

        form.down('field[id=vaMessage]').disable();

        form.submit({
        	clientValidation: true,
            url: this.urls.login,
            method: 'POST',
            scope: this,
            waitTitle: 'In Progress', 
            waitMsg: 'Communicating with server...',

            failure: function(domForm, action) {
            	this.handleLoginFailure(form, action);
            },
            success: function() {
        		form.up('window').close();

        		this.fireEvent('login');

        		this.loadUserInfo();
        	}
        });
	},
	
	loadSessionFromServer: function() {

		Ext.Ajax.request({
			url: this.urls.userInfo,
			method: 'GET',
            scope: this,
			success: function(response) {

				var result = Ext.JSON.decode(response.responseText);

				if (result.success && result.totalCount == 1) {                    
					this.processUserInfoResult(null, result.success, response);
				} else {
					this.showAlertDialog('Malformed Session User data returned from server');
				}
			},
			failure: function(response, requestOptions) {

				if (response.status == 403) {
                    var params = this.getHttpArgs();
                    if (this.flag403 || ((params.institution == null) || 
                        ((params.userDuz == null) && (params.vistaToken == null)))) {                        
					    this.showLoginDialog();
                    } else {
                        this.flag403 = true;
                        this.submitComLoginRequest();
                    }					
				} else {
					this.showServerFailureDialog(response, requestOptions);
				}
			}
		});
	},
    
	handleLoginFailure: function(form, action) {

    	form.down('field[id=vaMessage]').enable();

    	var msg = '';
        switch (action.failureType) {
            case Ext.form.Action.CLIENT_INVALID:
            	msg = 'Access and/or Verify code not valid at the selected institution';
                break;

            case Ext.form.Action.CONNECT_FAILURE:
            	msg = 'Remote server failed to respond';
                break;
                
            case Ext.form.Action.SERVER_INVALID:
            default:
                msg = action.result.errors;
                break;
        }
        
        LLVA.Utils.MessageBox.error(msg);
    },
    
	showServerFailureDialog: function(response, requestOptions) {

		this.showAlertDialog('The server returned an unexpected error.  Unable to proceed.<br /><br />'
		         + 'URI: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ' + requestOptions.url.replace(/\?.*/, '') + '<br />'
				 + 'Response: &nbsp; ' + response.status + ' ' + response.statusText);
	},    
    
	showAlertDialog: function(message, title) {

		if (Ext.isEmpty(title)) {
			title = 'Error';
		}

		Ext.Msg.show({
			title: title,
			//closable: false,
			buttons: Ext.Msg.OK,
			msg: message,
			icon: Ext.MessageBox.ERROR
		});
	},    

	logout: function() {
		
		if (this.fireEvent('beforelogout') === false) {
			return;
		}

		var loadMask = new Ext.LoadMask(Ext.getBody(), {msg: "Logging out..."});
		loadMask.show();
	
		Ext.Ajax.request({
			url: this.urls.logout,
			method: 'POST',
			scope: this,
			success: function(response) {
				loadMask.hide();
				this.fireEvent('logout');
				window.location.href = window.location.href;
			},
			failure: function(response, requestOptions) {
				loadMask.hide();
				LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
			}
		});
	}

});
