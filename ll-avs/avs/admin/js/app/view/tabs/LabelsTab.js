Ext.define('LLVA.AVS.Admin.view.tabs.LabelsTab', {
    extend: 'Ext.grid.Panel',
    alias : 'widget.labelstab',
    
    title: 'Labels',
    id: 'labels-grid',
    
    disabled: true,   
    frame: false,    
    
    store: 'Labels',
    disableSelection: true,
    invalidateScrollerOnRefresh: false,    
    autoScroll: true,
    
    viewConfig: {
        stripeRows: true
    },    
    columnLines: true,
    columns: [{
        header: 'Name',
        dataIndex: 'name',
        width: 165
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
            text: 'Labels' 
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
                        var recs = Ext.data.StoreManager.lookup('Labels').getUpdatedRecords();
                        var url = LLVA.AVS.Admin.getParam('urls').updateLabel;
                        var i;
                        for (i = 0; i < recs.length; i++) {             
                            recs[i].save({
                                url: url,
                                callback: function(recs, op, success) {
                                    Ext.data.StoreManager.lookup('Labels').load();
                                }
                            });   
                        }                    
                    }
                }, 
                canceledit: {
                    fn: function(editor, e) {
                        Ext.data.StoreManager.lookup('Labels').load();
                    }
                }
            }                                                    
        })                
    ] 
});
