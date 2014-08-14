Ext.define('LLVA.AVS.Admin.model.Translation', {
	extend: 'Ext.data.Model',

	fields: [
		{name: 'id', type: 'int'},
		{
			name: 'type', 
			type: 'int',
			convert: function(value, record) {
				var	label;
				switch(value) {
					case 1:
						label = 'Location Name';
						break;
					case 2:
						label = 'Order Text';
						break;
					default:
						label = 'Unknown';
				}
				return	label;
			}
		},
		'source',
		'translation'
	]
});
