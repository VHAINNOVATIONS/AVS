Ext.define('LLVA.Session.model.User', {
	extend: 'Ext.data.Model',

	fields: [
		{name: 'userDuz'},
		{name: 'userName01'},
		{name: 'userNameDisplay'},
		{name: 'userparentAdministrativeFacilityStationNumber'},
		{name: 'userParentComputerSystemStationNumber'},
		{name: 'userLastName'},
		{name: 'userFirstName'},
		{name: 'userMiddleName'},
		{name: 'userPrefix'},
		{name: 'userSuffix'},
		{name: 'userDegree'},
		{name: 'signonLogIen'},
		{name: 'userLoginStationNumber'},
		{name: 'divisionName'},
		{name: 'userRoles'}
	],

	init: function() {
		this.unpackRoles();
	},
	
	unpackRoles: function() {
	    var roles = this.get('userRoles');

	    if (typeof roles == 'undefined' || Ext.isEmpty(roles)) {
		    this.set('userRoles', []);

	    } else if (typeof roles == 'string') {
		    this.set('userRoles', roles.split("^"));
	    }
	},

    hasRole: function(role) {
		var roles = this.get('userRoles');
		for (var i = 0; i < roles.length; i++) {
			if (roles[i] == role) {
				return true;
			}
		}
		return false;
	}
});
