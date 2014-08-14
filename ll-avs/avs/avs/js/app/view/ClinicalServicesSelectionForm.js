Ext.define('LLVA.AVS.Wizard.view.ClinicalServicesSelectionForm', {
    extend: 'Ext.window.Window',
    alias : 'widget.clinicalservicesselectionform',
    title: 'Clinical Services',
    closeAction: 'destroy',
    closable: true,
    modal: true,
    width: 600,
    height: 450,
    bodyPadding: 5,
    selectedServiceDescriptions: undefined,    
    selectionModel: undefined,
    me: this,
    
    initComponent: function() {

        Ext.apply(this, {    
            id: 'clinicalservicesselectionform',   
            items: [{
                xtype: 'label',
                anchor: '100%',
                margins: '5 0 10 0',
                text: 'Select clinical services from the list below to include with AVS printout.'
            },
            {
                xtype: 'gridpanel',
                height: 365,
                id: 'servicesgridpanel',
                width: 577,
                loadMask: true,   
                store: 'ClinicalServices', 
                buttonAlign: 'left',
                selModel: Ext.create('Ext.selection.CheckboxModel', {                        
                    listeners: {
                        scope: this,
                        selectionchange: function(sm, selections) {
                            this.onSelectionChange(selections);
                        }
                    }     
                }),                    
                viewConfig: {
                    trackOver: false,
                    stripeRows: true                                  
                },                
                columnLines: true,
                invalidateScrollerOnRefresh: false,
                columns: [{
                    header: 'Name',
                    dataIndex: 'name',
                    flex: 0.75
                }, {
                    header: 'Location',
                    dataIndex: 'location',
                    flex: 0.75
                }, {
                    header: 'Operating Hours',
                    dataIndex: 'hours',
                    width: 100
                }],
                listeners: {
                    scope: this,
                    render : function(grid) {
                        grid.store.on('load', function(store, records, options){
                            if ((this.selectedServiceDescriptions != null) &&
                                (this.selectedServiceDescriptions != '')) {
                                var servicesArr = this.selectedServiceDescriptions.split(',');
                                var selModel = Ext.getCmp('servicesgridpanel').getSelectionModel();
                                var store = Ext.getCmp('servicesgridpanel').getStore();
                                var i;
                                for (i = 0; i < servicesArr.length; i++) {
                                    var index = store.getById(servicesArr[i]);
                                    selModel.select(index, true, true);
                                }    
                            }                                
                        });                         
                    }
                }       
            }],                  
            buttons: [{
                text: 'Cancel',
                id: 'cancelSelectServicesBtn',
                action: 'cancel'                
            }, {
                text: 'OK',
                id: 'selectServicesOkBtn',
                disabled: true,
                action: 'ok'            
            }]         
        });
        
        this.callParent(arguments);
    },  
    
    onSelectionChange: function(selections) {
        Ext.getCmp('selectServicesOkBtn').setDisabled(selections.length == 0);
        this.selectedServiceDescriptions = '';
        var i;
        for (i = 0; i < selections.length; i++) {
            this.selectedServiceDescriptions += selections[i].get('id');
            if (i < selections.length-1) {
                this.selectedServiceDescriptions += ',';
            }
        }
    },

    setSelectedServices: function(selections) {
        this.selectedServiceDescriptions = selections;
    },
    
    getSelectedServices: function() {
        return this.selectedServiceDescriptions;
    }
});