Ext.define('LLVA.AVS.Wizard.model.FacilitySettings', {
    extend: 'Ext.data.Model',
    fields: [
        'facilityNo', 
        'timeZone', 
        {name: 'tiuNoteEnabled', type: 'boolean'}, 
        {name: 'kramesEnabled', type: 'boolean'}, 
        {name: 'servicesEnabled', type: 'boolean'}, 
        {name: 'refreshFrequency', type: 'int'}
    ]
});
