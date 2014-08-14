Ext.define('LLVA.Session.store.Facilities', {
    extend: 'Ext.data.Store',

    autoLoad: false,

	fields: ['divisionName', 'stationNumber'],

	storeId: 'LoginFacilities',

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

