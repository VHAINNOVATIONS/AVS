Ext.define('LLVA.AVS.Admin.view.tabs.SettingsTab', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.settingstab',

    disabled: true,   
    
    title: 'Settings',
    id: 'settings-grid',
    bodyPadding: 5,
    items: [{
        xtype: 'panel',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,        
        items: [{
            xtype: 'grid',
            store: 'Settings',
            disableSelection: true,
            invalidateScrollerOnRefresh: false,
            width: '100%',
            flex: 0.5,
            frame: true,
            viewConfig: {
                stripeRows: true
            },    
            columnLines: true,
            columns: [{
                header: 'Setting',
                dataIndex: 'name',
                width: 300
            }, {
                header: 'Value',
                dataIndex: 'value',
                flex: 1,
                editor: {
                    allowBlank: true
                }
            }],
            selType: 'cellmodel',        
            tbar: {
                margin: '0 0 3 0',
                ui: 'footer',
                items: [{
                    xtype: 'tbtext', 
                    style:'font-weight: bold;font-size: 12px;',
                    text: 'Facility Settings' 
                }]
            },
            plugins: [
                Ext.create('Ext.grid.plugin.RowEditing', {
                    pluginId: 'rowEditing',
                    clicksToEdit: 1,
                    autoCancel: false,
                    scope: this,
                    listeners: {
                        scope: this,
                        edit: {
                            fn: function(editor, e) {
                                var recs = Ext.data.StoreManager.lookup('Settings').getUpdatedRecords();
                                var url = LLVA.AVS.Admin.getParam('urls').saveSettings;
                                var i;
                                for (i = 0; i < recs.length; i++) {             
                                    recs[i].save({
                                        url: url,
                                        params: {
                                            divisionNo: LLVA.AVS.Admin.getParam('stationNo')
                                        },
                                        callback: function(recs, op, success) {
                                            var params = {
                                                divisionNo: LLVA.AVS.Admin.getParam('stationNo')
                                            }; 
                                            Ext.data.StoreManager.lookup('Settings').load({params: params});
                                        }
                                    });   
                                }                    
                            }
                        }, 
                        canceledit: {
                            fn: function(editor, e) {
                                Ext.data.StoreManager.lookup('Settings').load();
                            }
                        }
                    }                                                    
                })                
            ] 
        },{
            title: 'TIU Note Text',
            xtype: 'form',
            id: 'tiuNoteForm',
            frame: true,
            flex: 0.5,
            margin: '10 0 0 0',
            items: [{
                xtype: 'label',
                id: 'tiuNoteTextLabel',
                html: 'Note: individual lines should not exceed the width of the edit box, or 80 characters.<br>' + 
                      'Please wrap each line with a carriage return.'
            },{
                xtype: 'hiddenfield',
                id: 'stationNo'
            },{
                xtype: 'textarea',
                id: 'tiuNoteText',
                labelAlign: 'top',
                hideLabel: true,
                width: 460,
                height: 190
            }],    
            buttons: {
                items: [{
                    action: 'save',
                    icon: 'images/database_save.png',
                    text: 'Save'
                }]
            }
        }]
    }]        
});
