Ext.define('LLVA.AVS.Wizard.view.ContentEditor', {
	extend: 'Ext.window.Window',
	alias : 'widget.contentEditor',

	autoShow: true,
	title: 'AVS Editor',
	closeAction: 'destroy',
	closable: false,
    modal: true,
    width: 820,
    height: 610, 
	items: [{
		xtype: 'form',
		bodyStyle: 'padding: 5px;',
		items: [{
			xtype: 'tinymcefield',
			width: 795,
			height: 525,
			name: 'content',
            tinymceConfig: {
                content_css: '/avs/css/editor.css',
                theme_advanced_buttons1: 'fullscreen,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,|,fontselect,fontsizeselect',
                theme_advanced_buttons2: 'cut,copy,paste,|,undo,redo,|,forecolor,backcolor,anchor,image,hr,|,pagebreak,|,code',
                theme_advanced_buttons3: '',
                theme_advanced_buttons4: '',
                theme_advanced_path : false,
                statusbar : false
            }
		}],
		buttons: [{
			text: 'Cancel',
			action: 'cancel'
		}, {
			text: 'Update',
			action: 'save'
		}]
			
	}]
});
