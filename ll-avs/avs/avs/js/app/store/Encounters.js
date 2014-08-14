Ext.define('LLVA.AVS.Wizard.store.Encounters', {
    extend: 'Ext.data.TreeStore',
    model: 'LLVA.AVS.Wizard.model.EncounterTreeNode',
    root: {
        expanded: false,
        leaf: false,
        text: 'Encounters',
        children: []        
    },    
    autoLoad: false,
    proxy: {
        type: 'ajax',
        url : undefined
    }    
});
