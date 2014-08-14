Ext.define('LLVA.AVS.Admin.view.tabs.DisclaimersTab', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.disclaimerstab',

    disabled: true,
    
    autoScroll: true,
    bodyPadding: '5 5',
    title: 'Disclaimers',
    items: [{
        xtype: 'tabpanel',
        //bodyPadding: '10 0',
        plain: true,
        border: false,
        items: [{
            title: 'Facility',
            xtype: 'form',
            id: 'facilityDisclaimers',
            width: 570,
            frame: true,
            items: [{
                xtype: 'label',
                id: 'facilityDisclaimersName',
                text: 'Facility Disclaimers'
            },{
                xtype: 'hiddenfield',
                id: 'facilityIen'
            },{
                xtype: 'textarea',
                id: 'facilityDisclaimersText',
                labelAlign: 'top',
                hideLabel: true,
                width: 550,
                height: 150,
                padding: '5 0 0 0'
            }],    
            buttons: {
                items: [{
                    action: 'save',
                    icon: 'images/database_save.png',
                    text: 'Save'
                }]
            }
        },{
            title: 'Clinic',
            xtype: 'form',
            id: 'clinicDisclaimers',
            width: 570,
            frame: true,
            items: [{
                xtype: 'combo',
                store: 'Clinics',
                id: 'clinicIen',
                displayField: 'name',
                valueField: 'ien',
                typeAhead: false,
                fieldLabel: 'Clinic',
                forceSelection: true,
                minChars: 1,
                labelWidth: 40,
                //hideLabel: true,
                hideTrigger: true,
                anchor: '65%',
                queryMode: 'remote',
                queryParam: 'startFrom',

                listConfig: {
                    loadingText: 'Searching...',
                    emptyText: 'No matching clinics found.'
                }
            },{
                xtype: 'label',
                id: 'clinicDisclaimersName',
                text: 'CLINIC NAME',
                hidden: true
            },{
                xtype: 'textarea',
                hidden: true,
                id: 'clinicDisclaimersText',
                labelAlign: 'top',
                hideLabel: true,
                width: 550,
                height: 150,
                padding: '5 0 0 0'
            }],    
            buttons: {
                items: [{
                    action: 'save',
                    icon: 'images/database_save.png',
                    text: 'Save',
                    hidden: true
                }]
            }
        },{
            title: 'Provider',
            xtype: 'form',
            id: 'providerDisclaimers',
            width: 570,
            frame: true,
            items: [{
                xtype: 'label',
                id: 'providerDisclaimersName',
                text: 'Provider Disclaimers'
            },{
                xtype: 'hiddenfield',
                id: 'providerIen'
            },{
                xtype: 'textarea',
                id: 'providerDisclaimersText',
                labelAlign: 'top',
                hideLabel: true,
                width: 550,
                height: 150,
                padding: '5 0 0 0'
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
