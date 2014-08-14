Ext.define('LLVA.AVS.Wizard.view.PrinterSelectionForm', {
    extend: 'Ext.window.Window',
    alias : 'widget.printerselectionform',
    title: 'Print',
    closeAction: 'hide',
    closable: true,
    modal: true,
    width: 350,
    height: 450,
    bodyPadding: 5,  
    selectedPrinter: undefined,
    
    initComponent: function() {
                
        var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
            groupHeaderTpl: '{name} ({rows.length} Device{[values.rows.length > 1 ? "s" : ""]})',
            hideGroupedHeader: true,
            startCollapsed: true
        });                
                
        Ext.apply(this, {    
            id: 'printerselectionform',   
            items: [{
                xtype: 'label',
                anchor: '100%',
                margins: '5 0 5 0',
                text: 'Select a printer from the list below.'
            },
            {
                xtype: 'gridpanel',
                height: 370,
                id: 'printersgridpanel',
                width: 327,
                loadMask: true,   
                store: 'Printers', 
                buttonAlign: 'left',
                verticalScroller: {
                    xtype: 'paginggridscroller'
                },                
                viewConfig: {
                    trackOver: false,
                    singleSelect: true,
                    stripeRows: true                                  
                },                
                columnLines: true,
                invalidateScrollerOnRefresh: false,
                columns: [
                    {
                        xtype: 'gridcolumn',
                        width: 135,
                        dataIndex: 'name',
                        text: 'Printer'
                    },
                    {
                        xtype: 'gridcolumn',
                        width: 170,
                        dataIndex: 'location',
                        text: 'Location'
                    }
                ],
                tbar: {
                    margin: '0 0 3 0',
                    ui: 'footer',
                    style: {
                        fontSize: '10pt'
                    },
                    items: [{
                        xtype: 'label',
                        text: 'Printer Name: '
                    },{
                        xtype: 'textfield',
                        width: 100,
                        id: 'filter-text',
                        scope: this,
                        listeners: {
                            scope: this,
                            specialkey: function(field, e) {
                                this.filterByEnterPress(field, e);
                            }
                        }                        
                    }, {
                        text: 'Search',
                        icon: 'images/find.png',
                        id: 'filterBtn',
                        scope: this,
                        handler: function(btn) {
                            this.filterByButtonClick(btn);
                        }
                    }, {
                        text: 'Reset',
                        scope: this,
                        id: 'resetBtn',
                        handler: function(btn) {
                            this.removeFiltersAndReload(btn);
                        }
                    }]
                },
                fbar: {
                    margin: '5 0 0 0',
                    ui: 'footer',  
                    id: 'PrintersGridFooter',                    
                    style: {
                        fontSize: '10pt'
                    },
                    items: [{
                        xtype: 'label',
                        id: 'selectedPrinterLabel',
                        text: 'Selected Printer: ',
                        disabled: true
                    },{
                        xtype: 'label',
                        id: 'selectedPrinterValue',
                        text: ''
                    }]                    
                },  
                bbar: {
                    margin: '0 0 5 3',
                    defaultType: 'checkbox',
                    border: 0,
                    items: [{
                        boxLabel: 'Set this device as the default printer',
                        inputValue: '1',
                        id: 'defaultDeviceCheckbox',
                        disabled: true,
                        listeners: {
                            scope: this,
                            change: function(fld, newVal, oldVal, e) {
                                Ext.getCmp('applyPrinterSelectionBtn').setDisabled(false);   
                            }
                        }
                    }]

                },                                
                selModel: Ext.create('Ext.selection.RowModel', {
                    listeners:{
                        scope: this,                    
                        select: function(row, rec, index, e) {                            
                            this.onPrinterSelected(Ext.getCmp('printersgridpanel').getSelectionModel().getLastSelected());
                        }
                    }      
                })
            }],
            buttons: [{
                text: 'Cancel',
                id: 'cancelPrinterSelectionBtn',
                action: 'cancel'                
            },{
                text: 'Apply',
                id: 'applyPrinterSelectionBtn',
                disabled: true,
                action: 'apply'                
            }, {
                text: 'Print',
                id: 'printerSelectionBtn',
                disabled: true,
                action: 'print'            
            }],
            listeners: {
                scope: this,
                itemclick: {
                    fn: this.onPrinterSelected  
                }, 
                afterlayout: function(form) {
                    Ext.getCmp('filter-text').focus(false, 200);
                }                
            }                
            
        });
        
        this.callParent(arguments);
    },  

    setSelectedPrinter: function(printer) {
        this.onPrinterSelected(printer);
    },    
    
    onPrinterSelected: function(rec) {
        this.selectedPrinter = rec; 
        if (rec != undefined) {     
            var name, isDefault;
            if (rec.name != undefined) {
                name = rec.name;
            } else {
                name = rec.get('name');
            }
            if (rec.isDefault != undefined) {
                isDefault = rec.isDefault;
            } else {
                isDefault = rec.get('isDefault');
            }
            Ext.getCmp('selectedPrinterValue').setText('<b>' + name + '</b>', false);
            Ext.getCmp('selectedPrinterLabel').setDisabled(false);
            Ext.getCmp('printerSelectionBtn').setDisabled(false);            
            Ext.getCmp('defaultDeviceCheckbox').setDisabled(false);
            Ext.getCmp('defaultDeviceCheckbox').setValue(isDefault);
        }            
    },
    
	filterByButtonClick: function(button) {
		var textfield = button.up('toolbar').down('#filter-text');
		this.filterByKeyword(textfield);
	},
	
	filterByEnterPress: function(textfield, event) {
		if (event.getKey() === event.ENTER) {
			this.filterByKeyword(textfield);
		}
	},

	filterByKeyword: function(textfield) {
		var store = Ext.getCmp('printersgridpanel').getStore();
		store.clearFilter(true);
		var keywords = textfield.getValue();
		if (!keywords) {
			var resetButton = textfield.up('toolbar').down('button[id=reset]Btn');
			return this.removeFiltersAndReload(resetButton);
		}
		store.removeAll();
		store.filter('name', keywords);
	},
	
	removeFiltersAndReload: function(button) {
		var textfield = button.up('toolbar').down('#filter-text');
		textfield.setValue('');
		Ext.getCmp('printersgridpanel').getStore().clearFilter();
	},
    
    loadPrinters: function() {
        Ext.getCmp('printersgridpanel').getStore().clearFilter();
    }
    
});