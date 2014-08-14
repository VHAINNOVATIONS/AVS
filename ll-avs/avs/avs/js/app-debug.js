/* Options for jslint: */
/*jslint browser: true, unparam: true, sloppy: true, vars: true, white: true, regexp: true, maxerr: 50, indent: 4 */
/*global Ext, LLVA */

Ext.Loader.setConfig({enabled:true});

Ext.Loader.setPath({
    'Ext': '/ext/4.2/src',
    'Ext.ux': '/ext/4.2/ux',
    'Ext.ux.form.field': '/ext/4.2/ux/form/field',
    'LLVA.Session': '/loginutils/js',
    'LLVA.Utils': '/jsonutils/js'
});

Ext.ns('LLVA.AVS');
LLVA.AVS.Wizard = {

    name: 'LLVA.AVS.Wizard',
    appFolder: 'js/app',
    controllers: ['Wizard'],
    
    serverIndex: 0,

    requires: [
        'LLVA.Utils.MessageBox',
        'LLVA.Session.Manager',
        'Ext.form.field.Display',
        'Ext.window.MessageBox',
        'Ext.selection.CheckboxModel',
        'Ext.ux.form.field.TinyMCE',
        'LLVA.AVS.Wizard.model.Demographics',
        'LLVA.AVS.Wizard.model.SearchResult',
        'LLVA.AVS.Wizard.model.Printer',
        'LLVA.AVS.Wizard.model.ClinicalService'
    ],
    
    stores: ['Printers'],
    
    params: {
        session: null,
        hasConnectivity: true,
        
        patientDfn: null,
        datetime: null,
        locationIen: null,

        facilitySettings: null,
        demographics: null,
        trendingOffsetDays: null,                

        urls: {
            servers:                'servers.json',
            userInfo:               '/w/s/user/userInfo.action',
            facilities:             '/w/login/stations.action',
            login:                  '/w/login/LoginController',
            logout:                 '/w/login/logout.action',            
            divisions:              '/w/login/divisions.action',
            userDivision:           '/w/login/defaultDivision.action',
            clientStrings:          '/w/s/avs/getClientStrings.action',
            facilitySettings:       '/w/s/avs/fetch-settings.action',
            demographics:           '/w/s/avs/demographics.action',
            sheet:                  '/w/s/avs/avs.action',
            kramesSearch:           '/w/s/infosheets/kramesSearchContent.action',
            kramesContent:          '/w/s/infosheets/kramesGetContent.action',
            encounters:             '/w/s/patient/patientEncounters.action',
            getRemoteMeds:          '/w/s/avs/getRemoteMeds.action',
            setRemoteVAMedsHtml:    '/w/s/avs/setRemoteVaMedsHtml.action',
            setRemoteNonVAMedsHtml: '/w/s/avs/setRemoteNonVAMedsHtml.action',
            pdf:                    '/w/s/avs/pdf.action',
            tiu:                    '/w/s/avs/tiu.action',
            setCustomContent:       '/w/s/avs/setCustomContent.action',
            setLocked:              '/w/s/avs/setLocked.action',
            setDefaultPrinter:      '/w/s/avs/setDefaultPrinter.action',
            getDefaultPrinter:      '/w/s/avs/getDefaultPrinter.action',
            printers:               '/w/s/avs/find-printers.action',
            saveComments:           '/w/s/avs/saveComments.action',
            fetchServices:          '/w/s/avs/fetch-services.action',
            languages:              '/w/s/avs/languages.action',
            printPdf:               '/w/s/avs/printPdf.action',
            admin:                  '/admin/index-cprs.html',
            help:                   '/help/index.html',
            tips:                   '/help/tips.html'
        }
    },

    launch: function() {
        Ext.Ajax.timeout = 120000;
        Ext.Ajax.cors = false;
        Ext.Ajax.useDefaultXhrHeader = true;
        
        APP_GLOBAL = this;
        this.loadHttpArgs();
        if (this.httpArgsAreValid() === false) {
            alert('invalid args');
            return;
        }      
        this.getServers();        
    },

    loadHttpArgs: function() {
        var query = window.location.search;
        this.setParam('protocol', window.location.protocol);
        this.setParam('hostname', window.location.hostname);
        this.setParam('port', window.location.port != '' ? window.location.port : 80);
        var path = window.location.pathname.split('/');
        this.setParam('root', '/' + path[1]);
        this.setParam('path', window.location.pathname);
        var qry = window.location.search.substring(1);
        this.setParam('qry', qry);
        this.httpArgs = Ext.Object.fromQueryString(qry);        
        this.setParam('hasConnectivity', this.httpArgs.offline ? false : true);
    },

    httpArgsAreValid: function() {
        var requiredArgs = [
            'stationNo',
            'userDuz',
            'patientDfn'
        ];

        var index = 0;
        for (index in requiredArgs) {
            if (requiredArgs.hasOwnProperty(index)) {
                var property = requiredArgs[index];
                if (this.httpArgs[property]) {                    
                    this.setParam(property, this.httpArgs[property]);
                } else {
                    LLVA.Utils.MessageBox.error('Required HTTP argument is missing: "' + property + '"');
                    return false;                
                }
            }
        }
        return true;
    },

    updateQueryStringParameter: function(uri, key, value) {
        var re = new RegExp("([?|&])" + key + "=.*?(&|$)", "i");
        separator = uri.indexOf('?') !== -1 ? "&" : "?";
        if (uri.match(re)) {
            return uri.replace(re, '$1' + key + "=" + value + '$2');
        } else {
            return uri + separator + key + "=" + value;
        }
    },    
    
    getServers: function() {
        Ext.Ajax.request({
            url: this.getParam('urls').servers,
            method: 'GET',
            scope: this,
            success: function(response) {
                var result = Ext.JSON.decode(response.responseText);
                this.setParam('servers', result.servers);
                this.getUserDivision();
            },
            failure: function(response, requestOptions) {
                var obj = {"servers": [this.getParam('hostname')]};                
                this.setParam('servers', obj.servers);
                this.getUserDivision();
            }
        });                
    },    
    
    handleAjaxError: function(error, action, fn) {
        this.serverIndex++;
        if (this.serverIndex < this.getParam('servers').length) {                    
            var server = this.getParam('servers')[this.serverIndex];                        
            var url = this.getParam('protocol') + '//' + server + ':' + this.getParam('port') + 
                      this.getParam('path') + '?' + this.getParam('qry');
            window.location = url;
        } else {        
            Ext.MessageBox.alert('AVS Server Error',
                'A server error is preventing the application from functioning.<br>' +
                'Please restart the application, or contact technical support.<br><br>' +
                'Error: \"' + error + '\", Action: \"' + action + '\"');   
        }      
    },
    
    getUrl: function(name) {
        var server = this.getParam('servers')[this.serverIndex];
        var url = this.getParam('protocol') + '//' + server + ':' + this.getParam('port') + 
               this.getParam('root') + this.getParam('urls')[name];
        return url;
    },    
    
    getUserDivision: function() {
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Signon Setup..."});
        mask.show();    
        Ext.Ajax.request({
            url: this.getUrl('userDivision'),
            params: {
                stationNo: this.getParam('stationNo'),
                userDuz: this.getParam('userDuz')
            },
            method: 'GET',
            scope: this,
            success: function(response) {
                mask.hide();                
                var result = Ext.JSON.decode(response.responseText);
                this.setParam('stationNo', result.facilityNo);
                this.setupSession(this.loadFacilitySettings);
            },
            failure: function(response, requestOptions) {            
                this.handleAjaxError(response.status, 'getUserDivision', this.getUserDivision);
            }
        });                
    },
    
    setupSession: function(callback) {
        var session = Ext.create('LLVA.Session.Manager', {
            scope: this,
            urls: {
                userInfo:   this.getUrl('userInfo'),
                facilities: this.getUrl('facilities'),
                login:      this.getUrl('login'),
                logout:     this.getUrl('logout')
            },            
            callback: callback,
            hasConnectivity: this.getParam('hasConnectivity'),
            allowDuzLogin: true,
            stationNo: this.getParam('stationNo')
        });
        this.setParam('session', session);
        this.setupProxies();
    },

    loadFacilitySettings: function() {
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading settings..."});
        mask.show();    
    
        Ext.Ajax.request({
            url: this.getUrl('facilitySettings'),
            params: {
                facilityNo: this.getParam('stationNo')
            },
            method: 'GET',
            scope: this,
            success: function(response) {
                mask.hide();
                var result = Ext.JSON.decode(response.responseText);
                if (result.success) {
                    var facilitySettings = Ext.create('LLVA.AVS.Wizard.model.FacilitySettings', result.root[0]);
                    this.setParam('facilitySettings', facilitySettings); 
                }
                this.loadUserInterface();
            },
            failure: function(response, requestOptions) {
                mask.hide();
                this.handleAjaxError(response.status, 'loadFacilitySettings', this.loadFacilitySettings);
            }
        });                
    },
    
    setupProxies: function() {
        var proxy = null;
        var mgr = Ext.data.StoreManager;
                      
        proxy = mgr.getByKey('ClinicalServices').getProxy();
        proxy.api.read = this.getUrl('fetchServices');
        
        proxy = mgr.getByKey('Printers').getProxy();
        proxy.api.read = this.getUrl('printers');        
        
        proxy = mgr.getByKey('SearchResults').getProxy();
        proxy.api.read = this.getUrl('kramesSearch');      

        //proxy = mgr.getByKey('Encounters').getProxy();
        //proxy.api.read = this.getUrl('encounters');                
    },    
    
    loadUserInterface: function() {
    
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading patient info..."});
        mask.show();    
        
        Ext.Ajax.request({
            url: this.getUrl('demographics'),
            params: {
                patientDfn: this.getParam('patientDfn')
            },
            method: 'GET',
            scope: this,
            success: function(response) {
                mask.hide();
                var result = Ext.JSON.decode(response.responseText);
                if (result.success) {
                    var demographics = Ext.create('LLVA.AVS.Wizard.model.Demographics', result.root[0]);
                    this.setParam('demographics', demographics);
                    Ext.widget('wizardviewport');
                }
            },
            failure: function(response, requestOptions) {
                mask.hide();
                this.handleAjaxError(response.status, 'loadUserInterface', this.loadUserInterface);
            }
        });
    },

    getParam: function(name) {
        return LLVA.AVS.Wizard.params[name];
    },

    setParam: function(name, value) {
        LLVA.AVS.Wizard.params[name] = value;
    }

};
