Ext.define('LLVA.AVS.Wizard.model.SearchResult', {
    extend: 'Ext.data.Model',

    fields: [        
        {name: 'contentId', type: 'int'},
        {name: 'contentTypeId', type: 'int'},
        {name: 'contentObjectType', type: 'string'},
        {name: 'language', type: 'string'},
        {name: 'title', type: 'string'},
        {name: 'gender', type: 'string'},        
        {name: 'blurb', type: 'string'}
    ],
    idProperty: 'contentId'

});
