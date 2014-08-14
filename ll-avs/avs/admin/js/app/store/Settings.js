Ext.define('LLVA.AVS.Admin.store.Settings', {
	extend: 'Ext.data.Store',

	model: 'LLVA.AVS.Admin.model.Setting',

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
			idProperty: 'setting'
		},
		writer: {
			type: 'json',
			writeAllFields: false
		},
		simpleSortMode: true
	}
});
