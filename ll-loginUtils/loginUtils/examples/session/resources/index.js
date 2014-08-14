/**
 * LLVA.Session example
 * by Curtis Farnham, Mar 2012
 */

/**
 * (Required) Callback function to execute upon successful login
 */
var callback = function() {

	var user = session.getUser();

	new Ext.panel.Panel({
		frame: true,
		title: 'Logged In',
		width: 200,
		renderTo: 'content',
		html: 'Login successful.<br />Welcome ' + user.get('userNameDisplay') + '!'
	});

	// Configure header
	var tpl = new Ext.Template('Logged in as {userName} ({facilityName}) | <a href="javascript:session.logout()">Logout</a>');
	var user = session.getUser();
	tpl.overwrite('login-info', {
		userName: user.get('userNameDisplay'),
		facilityName: user.get('divisionName')
	});
	Ext.get('login-info').show();
};

/**
 * (Optional) Overwrite the default AJAX endpdoint URLs with your own.
 */
var sessionOptions = {
	userInfo:    '/intellinote/w/s/user/userInfo.action',
	facilities:  '/intellinote/w/login/stations.action',
	login:       '/intellinote/w/login/LoginController',
	logout:      '/intellinote/w/login/logout.action'
};

/**
 * Fire away!
 * 
 * To log out, call session.logout() (See the link defined in the HTML above.)
 */
var session = new LLVA.Session(callback, sessionOptions);
