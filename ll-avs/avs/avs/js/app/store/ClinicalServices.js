Ext.define('LLVA.AVS.Wizard.store.ClinicalServices', {
    extend: 'Ext.data.Store',
    
    model: 'LLVA.AVS.Wizard.model.ClinicalService',
    
    autoLoad: false,   
    autoSync: true,
    remoteSort: false,
    remoteFilter: false,    
    buffered: false,
    
    proxy: {
        type: 'ajax',
        url : undefined,
        reader: {            
            type: 'json',
			root: 'root',
			successProperty: 'success',
			totalProperty: 'totalCount',
			idProperty: 'id'
        },        
		simpleSortMode: true   
    },
	sorters: [{
		property: 'name',
		direction: 'ASC'
	}]  
});
