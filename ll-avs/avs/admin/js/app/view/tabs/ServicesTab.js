Ext.define('LLVA.AVS.Admin.view.tabs.ServicesTab', {
    extend: 'Ext.grid.Panel',
    alias : 'widget.servicestab',
    
    title: 'Services',
    id: 'services-grid',
    
    disabled: true,
    
    store: 'Services',
    invalidateScrollerOnRefresh: false,
    
    selServiceRec: undefined,
    selServiceIndex: undefined,    
    
    columnLines: true,
    
    initComponent: function() {
        var servicesTab = this;
                
        Ext.apply(this, {                   

            columns: [{
                header: 'Name',
                dataIndex: 'name',
                flex: 0.75,
                editor: {
                    allowBlank: true
                }
            }, {
                header: 'Location',
                dataIndex: 'location',
                flex: 0.75,
                editor: {
                    allowBlank: true
                }
            }, {
                header: 'Operating Hours',
                dataIndex: 'hours',
                width: 100,
                editor: {
                    allowBlank: true
                }
            }, {
                header: 'Phone',
                dataIndex: 'phone',
                width: 125,
                editor: {
                    allowBlank: true
                }
            }, {
                header: 'Comment',
                dataIndex: 'comment',
                width: 150,
                editor: {
                    allowBlank: true
                }
            }],
            selModel: {
                selType: 'rowmodel',
                allowDeselect: true,
                listeners: {
                    scope: this,
                    'deselect': function() {
                        this.down('#removeService').setDisabled(true);
                    }
                }
            },
            viewConfig: {
                id: 'servicesTabView',
                markDirty: false,
                disableSelection: false,
                singleSelect: true,
                stripeRows: true
            },            
            tbar: {
                margin: '0 0 3 0',
                ui: 'footer',
                items: [{
                    xtype: 'tbtext', 
                    style:'font-weight: bold;font-size: 12px;',
                    text: 'Clinical Services Information' 
                }, 
                '->', 
                { 
                    itemId: 'addService',
                    text: 'Add Service',
                    icon: 'images/add.png',
                    handler : function() {
                        servicesTab.addService();                        
                    }
                }, {
                    itemId: 'removeService',
                    text: 'Remove Service',
                    icon: 'images/delete.png',
                    handler: function() {
                        servicesTab.deleteService();
                    },
                    disabled: true
                }]
            },
            plugins: [
                Ext.create('Ext.grid.plugin.RowEditing', {
                    pluginId: 'rowEditing',
                    clicksToEdit: 1,
                    autoCancel: false,
                    listeners: {
                        scope: this,
                        edit: {
                            fn: this.afterServiceEdit   
                        }, 
                        canceledit: {
                            fn: this.cancelServiceEdit
                        }
                    }                                                    
                })
            ],      
            listeners: {
                scope: this,
                itemclick: {
                    fn: this.onServiceRowClicked  
                },                  
                'selectionchange': function(view, records) {
                    servicesTab.down('#removeService').setDisabled(!records.length);
                }
            }
        });

        servicesTab.callParent(arguments);    
    },
    
    loadServices: function() {
        Ext.data.StoreManager.lookup('Services').load();
    },
    
    addService: function() {
        Ext.getCmp('services-grid').getPlugin('rowEditing').cancelEdit();  
        var r = Ext.ModelManager.create({
            name: '',
            location: '',
            hours: '',
            phone: '',
            comment: ''           
        }, 'LLVA.AVS.Admin.model.Service');
        var cnt = Ext.data.StoreManager.lookup('Services').data.getCount();
        Ext.data.StoreManager.lookup('Services').insert(cnt, r);
        Ext.getCmp('services-grid').getPlugin('rowEditing').startEdit(cnt, 0);              
    },
    
    deleteService: function() {
        Ext.getCmp('services-grid').getPlugin('rowEditing').cancelEdit();  
        if (this.selServiceRec) {
            this.selServiceRec.destroy({
                url: LLVA.AVS.Admin.getParam('urls').destroyService,
                callback: function(recs, op, success) {
                    Ext.getCmp('services-grid').loadServices();
                }
            });            
        }               
    },
    
    onServiceRowClicked: function(view, rec, item, index, e) {
        this.selServiceRec = rec;
        this.selServiceIndex = index;
        this.down('#removeService').setDisabled(false);
    },
    
    afterServiceEdit: function(editor, e) {       
        // make sure service name is not blank
        if (e.record.get('name') === '') {
            Ext.MessageBox.show({
                title: 'Invalid Name',
                msg: 'Please enter a valid service name.',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function() {
                    var cnt = Ext.data.StoreManager.lookup('Services').data.getCount();
                    Ext.getCmp('services-grid').getPlugin('rowEditing').startEdit(cnt-1, 0);       
                }
            });
        } else {   
            var i = 0;
            var valid = true;
            // iterate through grid store to make sure name is unique          
            Ext.data.StoreManager.lookup('Services').each(function(rec) {
                if (e.record.get('id') !== rec.get('id')) {                        
                    if (e.record.get('name') === rec.get('name')) {
                        valid = false;                
                    }
                }
                i++;
            });                       
            if (valid) {  
                var recs = Ext.data.StoreManager.lookup('Services').getNewRecords();
                var isNew = recs.length > 0;
                if (!isNew) {
                    recs = Ext.data.StoreManager.lookup('Services').getUpdatedRecords();
                }                
                for (var i = 0; i < recs.length; i++) { 
                    var url;
                    if (isNew) {
                        url = LLVA.AVS.Admin.getParam('urls').createService;
                    } else {
                        url = LLVA.AVS.Admin.getParam('urls').updateService;
                    }
                    if (i < recs.length-1) {
                        recs[i].save({url: url});
                    } else {
                        recs[i].save({
                            url: url,
                            callback: function(recs, op, success) {
                                e.grid.loadServices(); 
                            }
                        });                    
                    }
                }        
                this.selServiceRec = e.record;                                                            
            } else {
                Ext.MessageBox.show({
                    title: 'Invalid Name',
                    msg: 'Please enter a unique service name.',
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING,
                    fn: function() {
                         e.record.set('name', '')
                         Ext.getCmp('services-grid').getPlugin('rowEditing').startEdit(e.rowIdx, e.colIdx);    
                    }
                });     
                            
            }
        }        
    }, 
    
    cancelServiceEdit: function(editor, e) {
        // delete record if name is blank
        if (e.record.get('name') === '') {
            Ext.data.StoreManager.lookup('Services').removeAt(e.rowIdx);
            e.grid.loadServices();
        }
    }
    
});
