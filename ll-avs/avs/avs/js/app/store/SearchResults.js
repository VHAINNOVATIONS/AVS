Ext.define('LLVA.AVS.Wizard.store.SearchResults', {
    extend: 'Ext.data.Store',
    model: 'LLVA.AVS.Wizard.model.SearchResult',
    remoteSort: false,
    autoLoad: false,  
    proxy: {
        type: 'ajax',
        url : undefined,
        reader: {            
            root: 'root',
            totalProperty: 'totalCount'
        }        
    }  
});
