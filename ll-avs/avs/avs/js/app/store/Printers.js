Ext.define('LLVA.AVS.Wizard.store.Printers', {
    extend: 'Ext.data.Store',
    
    model: 'LLVA.AVS.Wizard.model.Printer',
    
    autoLoad: false,   
    autoSync: true,
    pageSize: 100,    
    remoteSort: true,
    remoteFilter: true,    
    buffered: true,
    
    proxy: {
        type: 'ajax',
        url : undefined,
        reader: {            
            type: 'json',
			root: 'root',
			successProperty: 'success',
			totalProperty: 'totalCount',
			idProperty: 'ien'
        },        
		simpleSortMode: true   
    },
	sorters: [{
		property: 'name',
		direction: 'ASC'
	}]  
});
