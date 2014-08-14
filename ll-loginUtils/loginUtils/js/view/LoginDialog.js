Ext.define('LLVA.Session.view.LoginDialog', {
	extend: 'Ext.window.Window',
	alias : 'widget.loginDialog',

	autoShow: true,
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
		id: 'loginForm',
	    fieldDefaults: {
			labelWidth:80
		},
	    frame: true,
	    items: [{
			xtype: 'textarea',
	        id: 'vaMessage',
	        width: 522,
	        height: 275,
	        hideLabel: true,
	        readOnly: true,
	        style: {
	            marginBottom: '12px'
	        },
	        value: 'U.S. Government Computer System\n\nU. S. government systems are intended to be used by ' +
	            'authorized government network users for viewing and retrieving information only, except as ' +
	            'otherwise explicitly authorized for official business and limited personal use in accordance ' +
	            'with policy. Information from these systems resides on and transmits through computer systems ' +
	            'and networks funded by the government. All access or use constitutes understanding and acceptance ' +
	            'that there is no reasonable expectation of privacy in the use of Government networks or systems.\n\n' + 
	            'The data and documents on this system include Federal records that contain sensitive information ' + 
	            'protected by various Federal statutes, including the Privacy Act, 5 U.S.C. Section 552a, and veterans\' ' +
	            'records confidentiality statutes such as 38 U.S.C. Sections 5701 and 7332. Access to the data and records ' + 
	            'is on a need-to-know basis only.\n\nAll access or use of this system constitutes user understanding ' + 
	            'and acceptance of these terms and constitutes unconditional consent to review and action including ' + 
	            '(but not limited to) monitoring, recording, copying, auditing, inspecting, investigating, restricting ' + 
	            'access, blocking, tracking, disclosing to authorized personnel, or any other authorized actions by all ' + 
	            'authorized government and law enforcement personnel.\n\nUnauthorized user attempts or acts to ' + 
	            '(1) access, upload, change, or delete information on this system, (2) modify this system, (3) deny ' + 
	            'access to this system, (4) accrue resources for unauthorized use or (5) otherwise misuse this system ' + 
	            'are strictly prohibited. Such attempts or acts are subject to action that may result in criminal, civil, ' + 
	            'or administrative penalties.'
	    }, { 
	    	xtype: 'combobox',
	        id: 'facilityNo',
	        fieldLabel: 'Institution',
	        name: 'station',
	        width: 435,
	        store: 'LoginFacilities',
	        displayField: 'divisionName',
	        valueField: 'stationNumber',
	        autoSelect: true,
	        forceSelection: true,
	        selectOnFocus: true,
	        allowBlank: false,
	        blankText: 'Please select your VA institution.'
	    }, {
	    	xtype: 'textfield',
	        id: 'access',
	        name: 'access',
	        fieldLabel: 'Access Code', 
	        inputType: 'password',  
	        allowBlank: false,
	        blankText: 'Please enter your Vista Access Code.',
	        value: ''
	    }, {
	    	xtype: 'textfield',
	        id: 'verify',
	        name: 'verify',
	        fieldLabel: 'Verify Code', 
	        inputType: 'password', 
	        allowBlank: false,
	        blankText: 'Please enter your Vista Verify Code.',
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
