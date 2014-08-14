Ext.define('LLVA.AVS.Wizard.view.RemoteMedsSelectionForm', {
    extend: 'Ext.window.Window',
    alias : 'widget.remotemedsselectionform',
    title: 'Remote Medications',
    closeAction: 'destroy',
    closable: false,
    modal: true,
    width: 960,
    height: 435,
    bodyPadding: 5,
    remoteMedsDict: undefined,
    remoteMedsJson: undefined,
    selRemoteMedsJson: undefined, 
    unselRemoteMedsJson: undefined,
    selectionModel: undefined,
    me: this,
    
    initComponent: function() {
    
        Ext.apply(this, {   
            id: 'remotemedsselectionform',   
            items: [{
                xtype: 'label',
                anchor: '100%',
                margins: '5 0 5 0',
                html: '<p style="font: 11px Tahoma,Verdana,Helvetica,sans-serif;font-weight:bold;padding-bottom:5px;">' +  
                  'This patient has medications from other VA facilities.  Please select the ' +
                  'medications from the list below that the patient is currently taking.</p>'
            },
            {
                xtype: 'gridpanel',
                height: 323,
                id: 'remotemedsgridpanel',
                width: 938,
                loadMask: true,   
                store: 'RemoteMeds', 
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
                    width: 130
                }, {
                    header: 'Sig',
                    dataIndex: 'sig',
                    flex: 0.15
                }, {
                    header: 'Type',
                    dataIndex: 'type',
                    width: 50
                }, {
                    header: 'Status',
                    dataIndex: 'status',
                    width: 55
                }, {
                    header: 'Refills',
                    dataIndex: 'refills',
                    width: 40
                }, {
                    header: 'Expiration Date',
                    dataIndex: 'dateExpires',
                    width: 100
                }, {
                    header: 'Last Filled',
                    dataIndex: 'dateLastFilled',
                    width: 100
                }, {
                    header: 'Facility',
                    dataIndex: 'site',
                    width: 115
                }, {
                    header: 'Provider/Documentor',
                    dataIndex: 'provider',
                    width: 115
                }]    
            }],                           
            buttons: [{
                text: 'Continue',
                id: 'selectRemoteMedsOkBtn',
                disabled: false,
                action: 'continue'            
            }]         
        });
        
        this.callParent(arguments);
    },  
    
    onSelectionChange: function(selections) {
        this.selRemoteMedsJson = [];
        this.unselRemoteMedsJson = [];
        var selDict = {};
        var i;
        for (i = 0; i < selections.length; i++) {
            selDict[selections[i].get('id')] = selections[i];
        }
        for (i = 0; i < this.remoteMedsJson.length; i++) {
            if (selDict[this.remoteMedsJson[i].id]) {
                this.selRemoteMedsJson.push(this.remoteMedsJson[i]);
            } else {
                this.unselRemoteMedsJson.push(this.remoteMedsJson[i]);
            }            
        }
    },

    setRemoteMedsJson: function(remoteMedsJson) {
        this.remoteMedsJson = remoteMedsJson;
        this.selRemoteMedsJson = [];
        this.unselRemoteMedsJson = [];        
        for (i = 0; i < this.remoteMedsJson.length; i++) {
            this.unselRemoteMedsJson.push(this.remoteMedsJson[i]);
        }       
        Ext.getCmp('remotemedsgridpanel').getStore().loadData(remoteMedsJson);
    },
    
    getSelectedRemoteMeds: function() {
        return this.selRemoteMedsJson;
    },
    
    getUnselectedRemoteMeds: function() {
        return this.unselRemoteMedsJson;
    }
});