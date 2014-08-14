Ext.define('LLVA.AVS.Admin.store.Labels', {
	extend: 'Ext.data.Store',

	model: 'LLVA.AVS.Admin.model.Label',

	autoLoad: false,
	autoSync: false,
	remoteSort: true,
	remoteFilter: false,

	proxy: {
		type: 'ajax',
		api: { 
			read: undefined,   // configured at runtime
			update: undefined
		},
		reader: {
			type: 'json',
			root: 'root',
			successProperty: 'success',
			totalProperty: 'totalCount',
			idProperty: 'name'
		},
		writer: {
			type: 'json',
			writeAllFields: false
		},
		simpleSortMode: true
	}
});
