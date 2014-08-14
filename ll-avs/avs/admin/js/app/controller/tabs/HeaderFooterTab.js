Ext.define('LLVA.AVS.Admin.controller.tabs.HeaderFooterTab', {
	extend: 'Ext.app.Controller',

	activated: false,

	views: ['tabs.HeaderFooterTab'],

	init: function() {
		this.control({
			'headerfootertab': {
				activate: this.loadData,
                'loaddata': function(tab, reset) {
                    this.loadData(tab, reset);
                }
			},
			'headerfootertab #headerHtml': {
				change: this.refreshPreviewPane
			},
			'headerfootertab #footerHtml': {
				change: this.refreshPreviewPane
			},
			'headerfootertab button[action=save]': {
				click: this.saveData
			}
		});
	},
	
	loadData: function(tab, reset) {
        if (reset) {
            this.activated = false;
        }    
		if (this.activated) {
			return;
		}		
		var mask = new Ext.LoadMask(tab, {msg:"Loading Header and Footer..."});
		mask.show();
		Ext.Ajax.request({
			url: LLVA.AVS.Admin.getParam('urls').getHeaderFooter,
			method: 'GET',
			scope: this,
			params: {
                divisionNo: LLVA.AVS.Admin.getParam('stationNo'),
                language: LLVA.AVS.Admin.getParam('language')
			},            
			success: function(response) {

				mask.hide();
				
				var result = Ext.JSON.decode(response.responseText);

				var data = result.root;
				
				var headerInput = tab.down('#headerHtml');
				headerInput.setValue(data[0]);
				
				var footerInput = tab.down('#footerHtml');
				footerInput.setValue(data[1]);

				this.refreshPreviewPane(headerInput);
				
				this.dataLoaded = true;
			},
			failure: function(response, requestOptions) {
				mask.hide();
				LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
			}
		});
	},

	refreshPreviewPane: function(textarea) {

		var tab = textarea.up('headerfootertab');

		var content = '	<div id="sheet-wrapper">'
			+ '		<div id="sheet">'
			+ '			<div id="sheet-contents">'
			+ '				<div id="sheet-header">'
			+					this.doTextReplacements(tab.down('#headerHtml').getValue())
			+ '				</div>'
			+ '				<div id="groups">'
			+ '					<div style="font-size: 1.5em;font-weight: bold;margin:20px 0;text-align:center">[SHEET CONTENTS HERE]</div>'
			+ '				</div>'
			+ '				<div id="sheet-footer">'
			+					this.doTextReplacements(tab.down('#footerHtml').getValue())
			+ '				</div>'
			+ '			</div>'
			+ '		</div>'
			+ '	</div>';

		if (tab.down('#previewPane') !== null) {
			tab.remove('previewPane');
		}
		tab.insert(1, {
			xtype: 'panel',
			title: 'Preview',
			id: 'previewPane',
			frame: true,
			margin: '10 0',
			html: content
		});
	},
	
	doTextReplacements: function(text) {

		var date = new Date();
		
		return text
			.replace('%PATIENT_NAME%', 'Bonaparte,Napoleon')
			.replace('%ENCOUNTER_DATETIME%', '05/28/2011 07:00')
			.replace('%ENCOUNTER_DATE%', '05/28/2011')
			.replace('%CURRENT_DATETIME%', Ext.Date.format(date, 'm/d/Y H:i'))
			.replace('%CURRENT_DATE%', Ext.Date.format(date, 'm/d/Y'))
            .replace('%FACILITY_NAME%', 'Metropolis VA Medical Center');
	},
	
	saveData: function(button) {

		var mask = new Ext.LoadMask(button.up('headerfootertab'), {msg:"Saving..."});
		mask.show();

		var timerStart = new Date();

		Ext.Ajax.request({
			url: LLVA.AVS.Admin.getParam('urls').saveHeaderFooter,
			params: {
                divisionNo: LLVA.AVS.Admin.getParam('stationNo'),
                language: LLVA.AVS.Admin.getParam('language'),
				header: button.up('headerfootertab').down('#headerHtml').getValue(),
				footer: button.up('headerfootertab').down('#footerHtml').getValue()
			},
			method: 'POST',
			scope: this,
			success: function(response) {

				// Introduce a delay so the user can actually see the LoadMask
				// in case the Save function happens really fast.
				var task = new Ext.util.DelayedTask(function(){
					mask.hide();
					
					Ext.JSON.decode(response.responseText);
				});

				task.delay(LLVA.AVS.Admin.calculateDelay(timerStart));

			},
			failure: function(response, requestOptions) {
				mask.hide();
				LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
			}
		});
		
	}
});

