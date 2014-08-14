Ext.ns('LLVA');

/**
 * Remote VistA Session
 * 
 * This class takes care of remote VistA logins, using AJAX for communication 
 * with a backend running ll-loginUtils.  Displays login window if needed.
 * Initiate a session like this:
 * 
 *     var callback = function(user) {
 *         // Whatever should be done after successful login
 *         ...
 *     };
 * 
 *     // This parameter is optional:
 *     var options = {
 *         userInfo: ...,   // URL for logged-in user's session data
 *         facilities: ..., // URL for facility info
 *         login: ...,      // URL to submit login credentials to
 *         logout: ...      // URL to terminate the user's session
 *     };
 *
 *     var mySession = new LLVA.Session(callback, options);
 * 
 * To log out, do this:
 * 
 *     mySession.logout();
 * 
 * @param {Object} callback Callback function executed after successful login
 * @param {Object} options (Optional) Configuration options
 */
LLVA.Session = function(callback, options) {

	/**
	 * Runtime ExtJS dependencies
	 * 
	 * @var {Array}
	 */
	var dependencies = ['Ext.LoadMask', 'Ext.window.MessageBox', 'Ext.util.Cookies'],
	
	/**
	 * Function to be executed as a callback upon successful login
	 * 
	 * @var {Function}
	 */
	loginCallback = null,
		
	/**
	 * Mask displayed while loading remote session data
	 * 
	 * @var {Ext.LoadMask}
	 */
	mask = null,

	/**
	 * URL's needed for session lifecycle
	 * 
	 * @var {Object}
	 */
	urls = {
			
		/**
		 * URL for logged-in user's session data
		 * 
		 * @var {String}
		 */
		userInfo:   '../w/s/user/userInfo.action',
		
		/**
		 * URL for facility info that populates the Facility selectbox 
		 * 
		 * @var {String}
		 */
		facilities: '../w/login/stations.action',
		
		/**
		 * URL to submit login credentials to
		 * 
		 * @var {String}
		 */
		login:      '../w/login/LoginController',
		
		/**
		 * URL to terminate the user's login session
		 * 
		 * @var {String}
		 */
		logout:     '../w/login/logout.action'
	},

	/**
	 * Logged-in user data
	 * 
	 * @var {LLVA.Session.model.User}
	 */
	user = null;

	
	construct(callback, options);

	/**
	 * Constructor - Attempt to retrieve remote session data.  If user
	 * isn't logged in, show login window.
	 * 
	 * @param {Function} callback Callback function to be performed upon successful login
	 * @param {Object} options Optional parameters to overwrite the defaults, such as
	 *                         URL's.  @see setOptions().
	 */
	function construct(callback, options) {

		Ext.syncRequire(dependencies);

		if (Ext.isFunction(callback)) {
			loginCallback = callback;
		} else {
			return showAlertDialog('No login callback function is set.');
		}

		mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Session..."});
		mask.show();

		setOptions(options);

		loadSessionFromServer();
	}
	
	/**
	 * Load user session from remote server
	 */
	function loadSessionFromServer() {

		Ext.Ajax.request({
			url: urls.userInfo,
			method: 'GET',
			success: function(response) {

				var result = Ext.JSON.decode(response.responseText);

				mask.hide();

				if (result.success && result.totalCount == 1) {                    
					user = createUser(result.root[0]);
					loginCallback(user);
				} else {
					showAlertDialog('Malformed Session User data returned from server');
				}
			},
			failure: function(response, requestOptions) {

				mask.hide();

				if (response.status == 403) {
                    var params = getComObjectSessionParams();
                    if ((params.institution == null) || (params.userDuz == null)) {
					    showLoginWindow();
                    } else {
                        submitCOMLoginRequest(params.institution, params.userDuz);
                    }					
				} else {
					showServerFailureDialog(response, requestOptions);
				}
			}
		});
	}

	/**
	 * Display alert box
	 * 
	 * @param {String} message Message to display
	 * @param {String} title (Optional) Title of the alert box
	 */
	function showAlertDialog(message, title) {

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
	}

	/**
	 * Display server failure dialog
	 * 
	 * @param {Object} response XMLHttpRequest object containing the response data
	 * @param {Object} requestOptions Parameters that were passed to the request  
	 */
	function showServerFailureDialog(response, requestOptions) {

		return showAlertDialog('The server returned an unexpected error.  Unable to proceed.<br /><br />'
				 + 'URI: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ' + requestOptions.url.replace(/\?.*/, '') + '<br />'
				 + 'Response: &nbsp; ' + response.status + ' ' + response.statusText);
	}

	/**
	 * Apply config options
	 * 
	 * @param {Object} options Configuration options, optionally containing
	 *                         these properties:
	 *                         urls - Object whose properties specify whichever
	 *                                default URL's to overwrite.  See above for details.
	 */
	function setOptions(options) {
		if (Ext.isObject(options)) {
			for (key in options) {
				if (urls.hasOwnProperty(key)) {
					urls[key] = options[key];
				}
			}
		}
	}

	/**
	 * Retrieve session-related parameters that the COM Object may put 
	 * into the URL, so we can avoid showing the login window unnecessarily.
	 * 
	 * @TODO As of this writing, the COM Object is still undergoing
	 * development.  This method should be reviewed and tested once the 
	 * COM Object has stabilized.
	 * 
	 * @return {Object}
	 */
	function getComObjectSessionParams() {
		var params = {};

		var httpArgs = Ext.urlDecode(location.search.substring(1));
		if (httpArgs.stationNo) {
			params.institution = httpArgs.stationNo;
		}
		if (httpArgs.userDuz) {
			params.userDuz = httpArgs.userDuz;
		}

		return params;
	}
	
	/**
	 * Create and populate an Ext.data.Model representing the logged-in user
	 * 
	 * @param {Object} properties Properties defining the logged-in user
	 * @return {LLVA.Session.model.User}
	 */
	function createUser(properties) {

		Ext.syncRequire('Ext.data.Model');

		Ext.define('LLVA.Session.model.User', {
			extend: 'Ext.data.Model',
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

			/**
			 * Explode the userRoles parameter.  This method is automatically called
			 * by the constructor method.
			 */
			init: function() {
			    var roles = this.get('userRoles');
			    if (typeof roles == 'undefined') {
				    this.set('userRoles', []);

			    } else if (typeof roles == 'string') {
				    this.set('userRoles', roles.split("^"));
			    }
			},

			/**
			 * Determine if the user has a given role
			 */
		    hasRole: function(role) {
				var roles = this.get('userRoles');
				for (var i = 0; i < roles.length; i++) {
					if (roles[i] == role) {
						return true;
					}
				}
				return false;
			}
		});

		return Ext.create('LLVA.Session.model.User', properties);
	}

	/**
	 * Create and display the login window
	 */
	function showLoginWindow() {

		var loginWindow = Ext.create('Ext.window.Window', {

			requires: ['Ext.util.Cookies'],

		    layout: 'fit',
		    width: 550,
		    height: 440,
		    title: 'Restricted Access',
		    plain: true,
		    hidden: false,
		    closable: false,
		    resizable: false,
		    border: false,
		    modal: true,
		    draggable: false,
		    items: [{
			    xtype: 'form',
				id: "loginForm",
			    fieldDefaults: {
					labelWidth:80
				},
			    frame: true,
			    items: [{
					xtype: 'textarea',
			        id: "vaMessage",
			        width: 522,
			        height: 275,
			        hideLabel: true,
			        readOnly: true,
			        style: {
			            marginBottom: "12px"
			        },
			        value: "U.S. Government Computer System\n\nU. S. government systems are intended to be used by " +
			            "authorized government network users for viewing and retrieving information only, except as " +
			            "otherwise explicitly authorized for official business and limited personal use in accordance " +
			            "with policy. Information from these systems resides on and transmits through computer systems " +
			            "and networks funded by the government. All access or use constitutes understanding and acceptance " +
			            "that there is no reasonable expectation of privacy in the use of Government networks or systems.\n\n" + 
			            "The data and documents on this system include Federal records that contain sensitive information " + 
			            "protected by various Federal statutes, including the Privacy Act, 5 U.S.C. Section 552a, and veterans' " +
			            "records confidentiality statutes such as 38 U.S.C. Sections 5701 and 7332. Access to the data and records " + 
			            "is on a need-to-know basis only.\n\nAll access or use of this system constitutes user understanding " + 
			            "and acceptance of these terms and constitutes unconditional consent to review and action including " + 
			            "(but not limited to) monitoring, recording, copying, auditing, inspecting, investigating, restricting " + 
			            "access, blocking, tracking, disclosing to authorized personnel, or any other authorized actions by all " + 
			            "authorized government and law enforcement personnel.\n\nUnauthorized user attempts or acts to " + 
			            "(1) access, upload, change, or delete information on this system, (2) modify this system, (3) deny " + 
			            "access to this system, (4) accrue resources for unauthorized use or (5) otherwise misuse this system " + 
			            "are strictly prohibited. Such attempts or acts are subject to action that may result in criminal, civil, " + 
			            "or administrative penalties."
			    }, { 
			    	xtype: 'combobox',
			        id: "facilityNo",
			        fieldLabel: "Institution",
			        //name: "institution",
			        name: "station",
			        width: 435,
			        store: getFacilitiesStore(), //'LLVA.Session.store.Facilities',
			        displayField: "divisionName",
			        valueField: "stationNumber",
			        autoSelect: true,
			        forceSelection: true,
			        selectOnFocus: true,
			        allowBlank: false,
			        blankText: "Please select your VA institution."
			    }, {
			    	xtype: 'textfield',
			        id: "access",
			        name: "access",
			        fieldLabel: "Access Code", 
			        inputType: "password",  
			        allowBlank: false,
			        blankText: "Please enter your Vista Access Code.",
			        value: ''
			    }, {
			    	xtype: 'textfield',
			        id: "verify",
			        name: "verify",
			        fieldLabel: "Verify Code", 
			        inputType: "password", 
			        allowBlank: false,
			        blankText: "Please enter your Vista Verify Code." ,
			        value: ''
			    }],
			    buttons: [{
				    text: 'Login',
				    action: 'login',
			        formBind: true,
			        width: 150
				}]
		    }]
		});

		configureLoginWindowEventHandlers(loginWindow);
		
		loginWindow.show();
	}

	/**
	 * Add event handlers for the Login Window
	 * 
	 * @param {Ext.window.Window} loginWindow Login window to configure
	 */
	function configureLoginWindowEventHandlers(loginWindow) {

		var submitFormByPressingEnter = function(field, event) {
		    if (event.getKey() == event.ENTER) {
		    	submitLoginRequest(field.up('form'));
		    }
		};

		loginWindow.down('field[id=access]').on('specialkey', submitFormByPressingEnter);
		loginWindow.down('field[id=verify]').on('specialkey', submitFormByPressingEnter);

		loginWindow.down('button[action=login]').on('click', function(button) {
			submitLoginRequest(button.up('form'));
		});
	}
	
	/**
	 * Create the store for Facilities data
	 * 
	 * @return {Ext.data.Store}
	 */
	function getFacilitiesStore() {

		Ext.syncRequire('Ext.util.Cookies');

		var store = Ext.create('Ext.data.Store', {

			fields: [
	            {name: 'divisionName', type: 'string'},
	            {name: 'stationNumber', type: 'string'}
	        ],

		    //storeId: 'LLVA.Session.store.Facilities',
		    autoLoad: {
				scope: this,
				callback: function() {
					var facilityField = Ext.getCmp('facilityNo');
					var accessCodeField = Ext.getCmp('access');
					var facilityStore = facilityField.store;

					var facilityNo = Ext.util.Cookies.get("gov.va.med.lom.defaultFacilityNo");

					if (facilityNo && facilityStore.findExact("stationNumber", facilityNo) >= 0) {
		    			// Cookie matches a value in the store. Set it as default and move on.
		    			facilityField.setValue(facilityNo);
		                accessCodeField.focus(false, false);

		    		} else if (facilityStore.getTotalCount() > 0) {
		    			// Cookie's value isn't in the store.  Pick the first entry.
		    			var defaultStationNumber = facilityStore.first().data.stationNumber;
		    			facilityField.setValue(defaultStationNumber);
		    			accessCodeField.focus(false, false);

					} else {
						// Store is empty.  Grab focus and hope for the best.
						facilityField.focus(false, false);
					}

				}
			},

		    proxy: {
			    type: 'ajax',
			    api: {
					read: urls.facilities
				},
				listeners: {
					exception: function(proxy, response, operation, listenerOptions) {
						showServerFailureDialog(response, response.request.options);
						return false;
					}
				},
			    reader: {
			        type: 'json',
			        root: 'root',
			        successProperty: 'success'
			    }
			}
		});

		return store;
	}
	
	/**
	 * Submit login credentials
	 *
	 * @todo Fix the failure action.  We're not getting any feedback from the 
	 *       server when it doesn't successfully process a request.
	 * @param {Ext.form.Panel} form Form to submit
	 */
	function submitLoginRequest(form) {
		
    	if (!form.getForm().isValid()) {
    		return showAlertDialog('Please complete the form and try again', 'Login Failed');
    	}

    	Ext.syncRequire('Ext.util.Cookies');

    	var nextYear = new Date();
    	nextYear.setDate(nextYear.getDate() + 365);
        Ext.util.Cookies.set(
    		'gov.va.med.lom.defaultFacilityNo', form.down('field[id=facilityNo]').value, nextYear
		);

        form.down('field[id=vaMessage]').disable();

        form.submit({
        	clientValidation: true,
            url: urls.login,
            method: 'POST',
            waitTitle: 'In Progress', 
            waitMsg: 'Communicating with server...',

            failure: function(formEl, action) {
            	form.down('field[id=vaMessage]').enable();
                switch (action.failureType) {
                    case Ext.form.Action.CLIENT_INVALID:
                        showAlertDialog('Access and/or Verify code not valid at the selected institution', 'Login Failed');
                        break;
                    case Ext.form.Action.CONNECT_FAILURE:
                        showAlertDialog('Remote server failed to respond', 'Login Failed');
                        break;
                    case Ext.form.Action.SERVER_INVALID:
                        showAlertDialog(action.result.errors, 'Login Failed');
                        break;
                }
            },
            success: function() {
        		form.up('window').close();
        		loadSessionFromServer();
        	}
        });
	}

	/**
	 * Submit DUZ login for COM object apps
	 *
	 * @param {String} stationNo The division number
	 * @param {String} userDuz The user DUZ     
	 */
	function submitCOMLoginRequest(stationNo, userDuz) {    
		mask = new Ext.LoadMask(Ext.getBody(), {msg: "Logging on..."});
		mask.show();
        
		Ext.Ajax.request({
			url: urls.login,
            params: {
                institution: stationNo,
                userDuz: userDuz
            },
			method: 'POST',
			failure: function(response, requestOptions) {
				mask.hide();
                showServerFailureDialog(response, requestOptions);
            },
			success: function(response) {
				mask.hide();
                if (response.responseText == '') {
                    showLoginWindow();  
                } else {
                    loadSessionFromServer();          
                }
			}
		});
    }    
    
	/**
	 * Get the logged-in user
	 * 
	 * @return {LLVA.Session.model.User}
	 */
	this.getUser = function() {
		return user;
	};

	/**
	 * Sign out.  If successful, refresh the page.
	 */
	this.logout = function() {
		mask = new Ext.LoadMask(Ext.getBody(), {msg: "Logging out..."});
		mask.show();
	
		Ext.Ajax.request({
			url: urls.logout,
			method: 'POST',
			failure: function(response, requestOptions) {
				mask.hide();
				showServerFailureDialog(response, requestOptions);
			},
			success: function(response) {
				// reload the page
				window.location.href = window.location.href;
			}
		});
	};

};

