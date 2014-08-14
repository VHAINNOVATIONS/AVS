/**
 * The two functions in this file enable Ext JS to understand the FileMan
 * date format.  Using this format is as simple as passing 'fmdate' to 
 * Ext JS wherever it would normally expect a date format string.
 * 
 * For example, in a Model class's list of fields, you can use:
 *     fields: {
 *         {name: 'onsetDate', type: 'date', format: 'fmdate'}
 *     }
 */
Ext.Date.formatFunctions.fuzzy = function() {
	var now = new Date();
	var difference = Math.round((now.getTime() - this.getTime()) / 1000);
	var epoch;
	
	var times = [
	    ['second',       1],
	    ['minute',      60],
	    ['hour',      3600],
	    ['day',      86400],
	    ['week',    604800],
	    ['month',  2592000],
	    ['year',  31536000]
    ];

	if (!difference) {
		epoch = {
			unit: times[0][0],
			duration: times[0][1],
			count: 0
		};
		
	} else {
		
		var index = 0;
		var absoluteDifference = Math.abs(difference);
		for (index = 0; index < 7; index++) {
			if (absoluteDifference < times[index][1] * 0.75) {
				break;
			}
		}
		
		var epochCount = Math.round(absoluteDifference / times[index - 1][1]);

		epoch = {
			unit: times[index - 1][0],
			duration: times[index - 1][1],
			count: epochCount
		};
	}

	var pluralize = function(singular, plural, number) {
		return Ext.String.format((Math.abs(number) == 1 ? singular : plural), number);
	};

	var output;

	// is date in future?
	if (difference < 0) {
		switch(epoch.unit) {
			case 'year':
				output = pluralize('in {0} year', 'in {0} years', epoch.count);
				break;
			case 'month':
				output = pluralize('in {0} month', 'in {0} months', epoch.count);
				break;
			case 'week':
				output = pluralize('in {0} week', 'in {0} weeks', epoch.count);
				break;
			case 'day':
				output = pluralize('in {0} day', 'in {0} days', epoch.count);
				break;
			case 'hour':
				output = pluralize('in {0} hour', 'in {0} hours', epoch.count);
				break;
			case 'minute':
				output = pluralize('in {0} minute', 'in {0} minutes', epoch.count);
				break;
			case 'second':
				output = pluralize('in {0} second', 'in {0} seconds', epoch.count);
				break;
		}
	} else {
		switch(epoch.unit) {
			case 'year':
				output = pluralize('{0} year ago', '{0} years ago', epoch.count);
				break;
			case 'month':
				output = pluralize('{0} month ago', '{0} months ago', epoch.count);
				break;
			case 'week':
				output = pluralize('{0} week ago', '{0} weeks ago', epoch.count);
				break;
			case 'day':
				output = pluralize('{0} day ago', '{0} days ago', epoch.count);
				break;
			case 'hour':
				output = pluralize('{0} hour ago', '{0} hours ago', epoch.count);
				break;
			case 'minute':
				output = pluralize('{0} minute ago', '{0} minutes ago', epoch.count);
				break;
			case 'second':
				output = pluralize('{0} second ago', '{0} seconds ago', epoch.count);
				break;
		}
	}
	return output;
};
