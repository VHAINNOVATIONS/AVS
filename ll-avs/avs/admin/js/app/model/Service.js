Ext.define('LLVA.AVS.Admin.model.Service', {
	extend: 'Ext.data.Model',

	fields: [
		{name: 'id', type: 'int'},		
		'name',
		'location',
		'hours',
        'phone',
        'comment'
	]
});
