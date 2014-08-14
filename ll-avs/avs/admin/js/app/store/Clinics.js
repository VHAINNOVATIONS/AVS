Ext.define('LLVA.AVS.Admin.store.Clinics', {
	extend: 'Ext.data.Store',

	model: 'LLVA.AVS.Admin.model.Clinic',

	autoLoad: false,
	pageSize: 25,
	buffered: true,

	proxy: {
		type: 'ajax',
		api: {
			read: null // configured at runtime
		},
		reader: {
			type: 'json',
			root: 'root',
			successProperty: 'success'
		}
	}

});
