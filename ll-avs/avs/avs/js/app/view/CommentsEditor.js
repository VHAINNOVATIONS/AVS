Ext.define('LLVA.AVS.Wizard.view.CommentsEditor', {
	extend: 'Ext.window.Window',
	alias : 'widget.commentsEditor',

	autoShow: true,
	title: 'Patient Instructions',
	closeAction: 'destroy',
	closable: true,
	width: 550,
	height: 315,
	items: [{
		xtype: 'form',
		bodyStyle: 'padding: 5px;',
		items: [{
			xtype: 'htmleditor',
			width: 525,
			height: 230,
			name: 'comments',
			labelAlign: 'top',
			fieldLabel: 'Instructions for the patient'
		}],
		buttons: [{
			text: 'Cancel',
			action: 'cancel'
		}, {
			text: 'Save',
			action: 'save'
		}]
			
	}]
});
