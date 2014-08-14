Ext.define('LLVA.AVS.Admin.store.Services', {
	extend: 'Ext.data.Store',

	model: 'LLVA.AVS.Admin.model.Service',

	autoLoad: false,
	autoSync: false,
	remoteSort: false,
	remoteFilter: false,

	proxy: {
		type: 'ajax',
		api: { 
            create: undefined, // configured at runtime
			read: undefined, 
			update: undefined,
            destroy: undefined
		},
		reader: {
			type: 'json',
			root: 'root',
			successProperty: 'success',
			totalProperty: 'totalCount',
			idProperty: 'id'
		},
		writer: {
			type: 'json',
			writeAllFields: false
		},
		simpleSortMode: true
	},
	sorters: [{
		property: 'name',
		direction: 'ASC'
	}]
});
