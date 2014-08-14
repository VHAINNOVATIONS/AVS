Ext.define('LLVA.AVS.Admin.store.Translations', {
	extend: 'Ext.data.Store',

	model: 'LLVA.AVS.Admin.model.Translation',

	autoLoad: false,
	autoSync: true,
	pageSize: 200,
	remoteSort: true,
	remoteFilter: true,
	buffered: true,

	proxy: {
		type: 'ajax',
		api: {
			read: null, // configured at runtime
			update: null // configured at runtime
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
		property: 'source',
		direction: 'ASC'
	}]
});
