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
Ext.Date.formatFunctions.fmdate = function() {
	return (this.getFullYear() - 1700) * 10000
			+ (this.getMonth() + 1) * 100
			+ this.getDate()
			+ this.getHours() / 100
			+ this.getMinutes() / 10000
			+ this.getSeconds() / 1000000
			+ this.getMilliseconds() / 1000000000;
};

Ext.Date.parseFunctions.fmdate = function(input, strict) {
    var remainder = parseFloat(input);
    if (!remainder) {
    	return null;
    }
    var years = 1700 + Math.floor(remainder / 10000);
    remainder -= (years - 1700) * 10000;
    var months = Math.floor(remainder / 100) - 1;
    remainder -= (months + 1) * 100;
    var days = Math.floor(remainder);
    remainder -= days;
    var hours = Math.floor(remainder * 100);
    remainder -= hours / 100;
    var minutes = Math.floor(remainder * 10000);
    remainder -= minutes / 10000;
    var seconds = Math.floor(remainder * 1000000);
    remainder -= seconds / 1000000;
    var milliseconds = Math.floor(remainder * 1000000000);

    return new Date(years, months, days, hours, minutes, seconds, milliseconds);
};