/* Options for jslint: */
/*jslint browser: true, unparam: true, sloppy: true, vars: true, white: true, regexp: true, maxerr: 50, indent: 4 */
/*global Ext, LLVA */

Ext.Loader.setConfig({enabled:true});

Ext.Loader.setPath({
    'Ext': '/ext/4.2/src',
    'LLVA.Session': '/loginutils/js',
    'LLVA.Utils': '/jsonutils/js'
});

Ext.ns('LLVA.PVS');
LLVA.PVS = {

    name: 'LLVA.PVS',
    appFolder: 'js/app',
    controllers: ['PVS'],

    requires: [
        'Ext.window.MessageBox',    
        'LLVA.Utils.MessageBox',
        'LLVA.Session.Manager',
        'LLVA.PVS.model.Demographics',
        'LLVA.PVS.model.FacilitySettings'
    ],
        
    stores: [],        
        
    params: {
        session: null,
        
        patientDfn: null,
        datetime: null,
        locationIen: null,

        facilitySettings: null,
        demographics: null,

        urls: {
            divisions:              '../w/login/divisions.action',
            userDivision:           '../w/login/defaultDivision.action',
            facilitySettings:       '../w/s/avs/fetch-settings.action',
            demographics:           '../w/s/avs/demographics.action',
            sheet:                  '../w/s/avs/pvs.action',
            encounters:             '../w/s/patient/patientEncounters.action'
        }
    },

    launch: function() {
        Ext.Ajax.timeout = 120000;
        APP_GLOBAL = this;
        this.loadHttpArgs();
        this.getUserDivision();        
    },

    loadHttpArgs: function() {
        var query = window.location.search;
        this.httpArgs = Ext.Object.fromQueryString(query.substring(1));
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
    
    getUserDivision: function() {
        if (this.httpArgsAreValid() === false) {
            alert('invalid args');
            return;
        }

        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Signon Setup..."});
        mask.show();    
    
        Ext.Ajax.request({
            url: this.getParam('urls').userDivision,
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
                this.setupSession();
            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
            }
        });                
    },
    
    setupSession: function() {
        var session = Ext.create('LLVA.Session.Manager', {
            scope: this,
            callback: this.loadFacilitySettings,
            hasConnectivity: this.getParam('hasConnectivity'),
            allowDuzLogin: true,
            stationNo: this.getParam('stationNo')
        });
        this.setParam('session', session);
    },

    loadFacilitySettings: function() {
        if (this.httpArgsAreValid() === false) {
            alert('invalid args');
            return;
        }

        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading settings..."});
        mask.show();    
    
        Ext.Ajax.request({
            url: this.getParam('urls').facilitySettings,
            params: {
                facilityNo: this.getParam('stationNo')
            },
            method: 'GET',
            scope: this,
            success: function(response) {
                mask.hide();
                var result = Ext.JSON.decode(response.responseText);
                if (result.success) {
                    var facilitySettings = Ext.create('LLVA.PVS.model.FacilitySettings', result.root[0]);
                    this.setParam('facilitySettings', facilitySettings); 
                }
                this.loadUserInterface();
            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
            }
        });                
    },
    
    loadUserInterface: function() {
    
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading patient info..."});
        mask.show();    
        
        Ext.Ajax.request({
            url: this.getParam('urls').demographics,
            params: {
                patientDfn: this.getParam('patientDfn')
            },
            method: 'GET',
            scope: this,
            success: function(response) {
                mask.hide();
                var result = Ext.JSON.decode(response.responseText);
                if (result.success) {
                    var demographics = Ext.create('LLVA.PVS.model.Demographics', result.root[0]);
                    this.setParam('demographics', demographics);
                    Ext.widget('viewport');
                }
            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
            }
        });
    },

    getParam: function(name) {
        return LLVA.PVS.params[name];
    },

    setParam: function(name, value) {
        LLVA.PVS.params[name] = value;
    }

};
