Ext.define('LLVA.AVS.Admin.view.tabs.HeaderFooterTab', {
    extend: 'Ext.panel.Panel',
    alias : 'widget.headerfootertab',
    disabled: true,    
    autoScroll: true,
    bodyPadding: 10,
    title: 'Header/Footer',
    id: 'headerfooter',
    items: [{
        xtype: 'panel',
        layout: 'column',
        border: false,
        items: [{
            xtype: 'panel',
            border: false,
            columnWidth: 0.6,
            items: [{
                xtype: 'form',
                width: 485,
                frame: true,
                items: [{
                    xtype: 'textarea',
                    id: 'headerHtml',
                    labelAlign: 'top',
                    fieldLabel: 'Header HTML',
                    width: 475,
                    height: 125
                },{
                    xtype: 'textarea',
                    id: 'footerHtml',
                    labelAlign: 'top',
                    fieldLabel: 'Footer HTML',
                    width: 475,
                    height: 125
                }],    
                buttons: {
                    items: [{
                        action: 'save',
                        icon: 'images/database_save.png',
                        text: 'Save'
                    }]
                }
            }]
        },{
            xtype: 'panel',
            columnWidth: 0.4,
            border: false,
            bodyPadding: '0 20',
            items: [{
                xtype: 'grid',
                id: 'replacements-grid',
                width: 310,
                title: 'Optional String Replacements',
                frame: true,
                store: {
                    fields:['string', 'description'],
                    data: [
                       {string: '%PATIENT_NAME%', description: 'Patient name'},
                       {string: '%ENCOUNTER_DATE%', description: 'Date of the encounter'},
                       {string: '%ENCOUNTER_DATETIME%', description: 'Date and time of the encounter'},
                       {string: '%CURRENT_DATE%', description: 'Current date'},
                       {string: '%CURRENT_DATETIME%', description: 'Current date and time'},
                       {string: '%FACILITY_NAME%', description: 'Name of the facility'}
                   ]
                },
                columnLines: true,
                columns: [{
                    dataIndex: 'string',
                    flex: 1.1
                },{
                    dataIndex: 'description',
                    flex: 0.9
                }],
                hideHeaders: true
            }]
        }] 
    }
    // , {previewPane created here at runtime}
    ]
    
});
