Ext.define('LLVA.AVS.Wizard.model.ClinicalService', {
    extend: 'Ext.data.Model',

    fields: [        
        {name: 'id', type: 'int'},
        {name: 'name', type: 'string'},
        {name: 'location', type: 'string'},
        {name: 'hours', type: 'string'},
        {name: 'phone', type: 'string'},
        {name: 'comment', type: 'string'}
    ],
    idProperty: 'id'

});