Ext.define('LLVA.AVS.Admin.controller.tabs.DisclaimersTab', {
	extend: 'Ext.app.Controller',

	dataLoaded: {
		facility: {},
		clinic: {},
		provider: {}
	},
	
	views: ['tabs.DisclaimersTab'],

	stores: ['Clinics'],

	models: ['Clinic'],

	init: function() {
		this.control({
			'disclaimerstab form#facilityDisclaimers': {
				activate: this.loadFacilityData
			},
			'disclaimerstab form#providerDisclaimers': {
				activate: this.loadProviderData
			},
			'disclaimerstab form#clinicDisclaimers combobox': {
				select: this.loadClinicData
			},
			'disclaimerstab form button[action=save]': {
				click: this.saveDisclaimerData
			}
		});
	},

	loadClinicData: function(combobox, records, eOpts) {

		var clinic = records[0];
		var labelText = clinic.get('name') + ' clinic-wide disclaimers:';
		
		var form = combobox.up('form');
		form.down('#clinicDisclaimersName').setText(labelText);
		form.down('#clinicDisclaimersText').show();
		form.down('#clinicDisclaimersName').show();
		form.down('button').show();
		
		this.populateDisclaimersField(form, 'clinic', clinic.get('ien'));
	},
	
	loadFacilityData: function(form) {

		var stationNo = LLVA.AVS.Admin.getParam('stationNo');

		if (!Ext.isEmpty(this.dataLoaded.facility[stationNo])) {
			return;
		}

		var labelText = LLVA.AVS.Admin.getParam('divisionName') + ' facility-wide disclaimers:';
		form.down('#facilityDisclaimersName').setText(labelText);

		this.populateDisclaimersField(form, 'facility', stationNo);
	},

	loadProviderData: function(form) {

        var userDuz = LLVA.AVS.Admin.getParam('userDuz');

		if (!Ext.isEmpty(this.dataLoaded.provider[userDuz])) {
			return;
		}

        var user = LLVA.AVS.Admin.getParam('session').user;
		var labelText = 'Disclaimers for patients of ' + user.get('userName01') + ':';
		    form.down('#providerDisclaimersName').setText(labelText);

		this.populateDisclaimersField(form, 'provider', userDuz);
	},

	populateDisclaimersField: function(form, disclaimersType, itemIen) {

  	    var stationNo = LLVA.AVS.Admin.getParam('stationNo');
    
		var mask = new Ext.LoadMask(form.up('disclaimerstab'), {msg:"Loading..."});
		mask.show();

		Ext.Ajax.request({
			url: LLVA.AVS.Admin.getParam('urls').fetchDisclaimers + '?stationNo=' + stationNo + '&type=' + disclaimersType + '&ien=' + itemIen,
			method: 'GET',
			scope: this,
			success: function(response) {

				mask.hide();

				var result = Ext.JSON.decode(response.responseText);

				var data = result.root;
				
				form.down('#' + disclaimersType + 'Ien').setValue(itemIen);
				form.down('#' + disclaimersType + 'DisclaimersText').setValue(data[0]);
				
				this.dataLoaded[disclaimersType][itemIen] = true;
			},
			failure: function(response, requestOptions) {
				mask.hide();
				LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
			}
		});
		
	},

	saveDisclaimerData: function(button) {

		var form = button.up('form');
		var id = form.getId();
		var disclaimersType = id.replace(/Disclaimers$/, '');
		
		var mask = new Ext.LoadMask(form.up('disclaimerstab'), {msg:"Saving..."});
		mask.show();

		var timerStart = new Date();

		Ext.Ajax.request({
			url: LLVA.AVS.Admin.getParam('urls').saveDisclaimers,
			params: {
                stationNo: LLVA.AVS.Admin.getParam('stationNo'),
				type: disclaimersType,                
				ien: form.down('#' + disclaimersType + 'Ien').getValue(),
				text: form.down('#' + disclaimersType + 'DisclaimersText').getValue()
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

