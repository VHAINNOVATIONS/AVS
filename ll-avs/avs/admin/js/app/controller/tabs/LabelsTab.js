Ext.define('LLVA.AVS.Admin.controller.tabs.LabelsTab', {
    extend: 'Ext.app.Controller',

    dataLoaded: false,

    views: ['tabs.LabelsTab'],

    stores: ['Labels'],

    models: ['Label'],

    init: function() {

        this.control({
            'labelstab': {
                activate: this.loadLabels
            }
        });
    },
    
    loadLabels: function() {
        if (this.dataLoaded) {
            return;
        }
        var params = {
            language: Ext.getCmp('languagesCombo').getValue()
        };
        this.getStore('Labels').load({params: params});
        this.dataLoaded = true;                
    }    
});

