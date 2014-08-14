Ext.define('LLVA.AVS.Wizard.store.RemoteMeds', {
    extend: 'Ext.data.JsonStore',
    
    fields: ['id', 'name', 'type', 'status', 'sig', 'refills', 'dateExpires', 
             'dateLastFilled', 'site', 'provider'],
    
    proxy: {
        type: 'memory',
        reader: {            
            type: 'json',
			root: 'remoteMeds',
            idProperty: 'id'
        }
    }
});
