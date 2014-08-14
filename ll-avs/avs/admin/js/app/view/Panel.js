Ext.define('LLVA.AVS.Admin.view.Panel', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.adminPanel',
    id: 'adminPanel',
    renderTo: 'content',
    autoShow: true,
    width: 875,
    border: false,
    frame: true,
    layout: 'fit',
    currentTab: 'translationstab',
    dockedItems: [{
        xtype: 'toolbar',
        ui: 'header',
        dock: 'top',
        name: 'adminToolbar',
        layout: {
            xtype: 'hbox',
            pack: 'start',
            align: 'middle'
        },
        padding: '0 0 5 0',
        items: [{
            xtype: 'combo',
            id: 'divisionsCombo',
            fieldLabel: 'Site/Division',
            labelWidth: '75px',
            labelAlign: 'left',
            matchFieldWidth: true,
            width: 350,   
            allowBlank: false,
            editable: false,
            required:true,
            mode:'local',
            triggerAction:'all',
            forceSelection:true,
            selectOnFocus: true,
            autoSelect: true,
            autoSelecting: false,
            store: 'Divisions',       
            tpl: new Ext.XTemplate(
                '<tpl for="."><div class="x-boundlist-item">',
                '{name}&nbsp;({facilityNo})',
                '</div></tpl>'
            ),
            displayField: 'name',
            valueField: 'facilityNo',
            margin: '0 20 0 0',
            queryMode: 'local',
            action: 'division',
            disabled: true,                            
            listeners: {
                'select': {    
                    fn: function(combo, records) { 
                        var store = combo.getStore();      
                        var facilityNo = records[0].get('facilityNo');  
                        Ext.getCmp('adminPanel').fireEvent('divisionSelect', facilityNo);
                    }
                }
            }                   
        },{
            xtype: 'combo',
            id: 'languagesCombo',
            fieldLabel: 'Language',
            labelWidth: '65px',
            width: '250px',
            labelAlign: 'left',
            allowBlank: false,
            required:true,
            mode:'local',
            triggerAction:'all',
            editable: false,
            forceSelection:true,
            selectOnFocus: true,
            autoSelect: true,
            autoSelecting: false,
            store: 'Languages',       
            displayField: 'name',
            valueField: 'abbreviation',
            value: 'en',
            queryMode: 'local',
            action: 'language',
            disabled: true,
            listeners: {
                'select': {    
                    fn: function(combo, records) { 
                        var store = combo.getStore();      
                        var language = records[0].get('abbreviation');    
                        Ext.getCmp('adminPanel').fireEvent('languageSelect', language);
                    }
                }
            }            
        },
        '->', 
        {
            xtype: 'button',
            text: 'Help',
            icon: 'images/information.png',
            action: 'help'                
        }]
    }],
    items: [{
        xtype: 'tabpanel', 
        id: 'admintabpanel',
        border: true,
        layout: 'fit',
        resizeTabs: true,
        minTabWidth: 110,
        items: [{
            xtype: 'translationstab'
        },{
            xtype: 'servicestab'
        },{
            xtype: 'disclaimerstab'
        },{
            xtype: 'headerfootertab'
        },{
            xtype: 'settingstab'
        },{
            xtype: 'labelstab'
        }],
        listeners: {            
            tabchange: function(tabPanel, newTab, oldTab, eOpts)  {
                LLVA.AVS.Admin.setParam('currentTab', newTab.id);
                if (newTab.id == 'headerfooter' || newTab.id == 'settings-grid') {
                    Ext.getCmp('divisionsCombo').setDisabled(false);
                } else {
                    Ext.getCmp('divisionsCombo').setDisabled(true);
                    Ext.getCmp('languagesCombo').setDisabled(true);
                }
            }        
        }
    }]
});
