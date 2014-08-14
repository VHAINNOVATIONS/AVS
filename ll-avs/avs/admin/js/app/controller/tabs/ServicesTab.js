Ext.define('LLVA.AVS.Admin.controller.tabs.ServicesTab', {
	extend: 'Ext.app.Controller',

	dataLoaded: false,

	views: ['tabs.ServicesTab'],

	stores: ['Services'],

	models: ['Service'],

	init: function() {

		this.control({
			'servicestab': {
				activate: this.loadData
			}
		});
	},

	loadData: function() {
		if (this.dataLoaded) {
			return;
		}
		this.getStore('Services').load();
		this.dataLoaded = true;                
	}
});

