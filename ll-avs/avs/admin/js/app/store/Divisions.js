Ext.define('LLVA.AVS.Admin.store.Divisions', {
	extend: 'Ext.data.Store',

	model: 'LLVA.AVS.Admin.model.Division',

	autoLoad: false,
	autoSync: false,
	remoteSort: false,
	remoteFilter: false,

	proxy: {
		type: 'ajax',
		api: { 
			read: undefined   // configured at runtime
		},
		reader: {
			type: 'json',
			root: 'root',
			successProperty: 'success',
			totalProperty: 'totalCount',
			idProperty: 'insitutionId'
		},
		writer: {
			type: 'json',
			writeAllFields: false
		},
		simpleSortMode: true
	}
});
