Ext.define('LLVA.Utils.MessageBox', {
	extend: 'Ext.util.Observable',
	
	requires: ['Ext.window.MessageBox'],

	statics: {

		warn: function(message) {
	
			if (Ext.isEmpty(message)) {
				Ext.Error.raise('No warning message supplied');
			}
	
			Ext.Msg.show({
				title: 'Alert',
				msg: message,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.WARNING
			});
		},
	
		error: function(message) {

			if (Ext.isEmpty(message)) {
				Ext.Error.raise('No error message supplied');
			}
	
			Ext.Msg.show({
				title: 'Error',
				msg: message,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
		},

		ajaxFailure: function(response, requestOptions) {

			var uri = requestOptions.url.replace(/\?(.*)/, '');
			var responseDescription = response.status + ' ' + response.statusText;

			this.error('The server returned an unexpected error.  Unable to proceed.<br /><br />'
					 + 'URI: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ' + uri + '<br />'
					 + 'Response: &nbsp; ' + responseDescription);
		}

	}
	
});
