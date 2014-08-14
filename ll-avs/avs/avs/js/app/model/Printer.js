Ext.define('LLVA.AVS.Wizard.model.Printer', {
    extend: 'Ext.data.Model',

    fields: [        
        {name: 'ien', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'displayName', type: 'string'},
        {name: 'ipAddress', type: 'string'},
        {name: 'location', type: 'string'},
        {name: 'rightMargin', type: 'int'},        
        {name: 'pageLength', type: 'int'},
        {name: 'isDefault', type: 'boolean'}
    ],
    idProperty: 'ien'

});
