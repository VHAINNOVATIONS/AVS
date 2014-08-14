Ext.define('LLVA.AVS.Wizard.model.Demographics', {
    extend: 'Ext.data.Model',
    fields: [
        'facilityNo', 
        'dfn', 
        {name: 'dob', type: 'date', dateFormat: 'fmdate'}, 
        'name', 
        'sex', 
        'ssn',
        'activityLevel', 
        'diabetesOnsetYear'
    ],

    getAge: function(now) {

		var age = 0;
		var then = this.get('dob');
		if (then !== null) {
			// start with the difference between the years
			age = now.getFullYear() - then.getFullYear();
			// adjust downward if the dayOfYear is prior to their birthday
			if (now.getMonth() < then.getMonth() 
				|| (now.getMonth() === then.getMonth() && now.getDate() < then.getDate())
			) {
				age = age - 1;
			}
		}
		return age;
	}

});
