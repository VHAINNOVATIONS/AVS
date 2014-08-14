/* Options for jslint: */
/*jslint browser: true, unparam: true, sloppy: true, vars: true, white: true, regexp: true, maxerr: 50, indent: 4 */
/*global Ext, LLVA */

Ext.Loader.setConfig({enabled:true});

Ext.Loader.setPath({
    'Ext': '/ext/4.1.1a/src',
    'LLVA.Session': '/loginutils/js',
    'LLVA.Utils': '/jsonutils/js'
});

Ext.ns('LLVA.AVS');
LLVA.AVS.Admin = {

    name: 'LLVA.AVS.Admin',
    appFolder: 'js/app',
    controllers: ['Panel', 'tabs.TranslationsTab', 'tabs.ServicesTab', 'tabs.DisclaimersTab', 
                  'tabs.HeaderFooterTab', 'tabs.SettingsTab', 'tabs.LabelsTab'],

    requires: [
        'LLVA.Utils.MessageBox',
        'LLVA.Session.Manager',
        'Ext.grid.plugin.CellEditing',
        'Ext.tab.Panel',
        'Ext.grid.PagingScroller',
        'Ext.selection.CellModel',
        'Ext.toolbar.Spacer',
        'Ext.form.Label',
        'Ext.form.field.Hidden',
        'Ext.layout.container.Column',
        'Ext.window.MessageBox',
        'Ext.grid.Panel',
        'Ext.panel.Table',
        'Ext.grid.View',
        'Ext.view.Table',
        'Ext.selection.RowModel',
        'Ext.grid.Lockable',
        'Ext.view.TableChunker',
        'Ext.grid.LockingView'
    ],

    params: {
        session: null,        
        loadMaskMinimumDuration: 800, // millisecs

        urls: {
            isUserAdmin:       '../w/s/user/isUserAdmin.action',
            userDivision:      '../w/login/defaultDivision.action',            
            divisions:         '../w/login/divisions.action',     
            getHeaderFooter:   '../w/s/avs/get-header-footer.action',
            saveHeaderFooter:  '../w/s/avs/save-header-footer.action',
            fetchTranslations: '../w/s/avs/fetch-translations.action',
            updateTranslation: '../w/s/avs/update-translation.action',
            fetchServices:     '../w/s/avs/fetch-services.action',
            updateService:     '../w/s/avs/update-service.action',
            createService:     '../w/s/avs/add-service.action',
            destroyService:    '../w/s/avs/delete-service.action',
            fetchDisclaimers:  '../w/s/avs/fetch-disclaimers.action',
            searchClinics:     '../w/s/avs/search-clinics.action',
            saveDisclaimers:   '../w/s/avs/save-disclaimers.action',
            fetchSettings:     '../w/s/avs/fetch-settings-grid.action',
            saveSettings:      '../w/s/avs/save-settings.action',
            fetchTiuNoteText:  '../w/s/avs/fetch-tiuNoteText.action',
            saveTiuNoteText:   '../w/s/avs/save-tiuNoteText.action',
            fetchLabels:       '../w/s/avs/fetch-labels-grid.action',
            updateLabel:       '../w/s/avs/update-label.action',
            languages:         '../w/s/avs/languages.action',
            help:              '../help/index.html'
        }
    },

    launch: function() {
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
            'userDuz'
        ];

        var index = 0;
        var property;
        for (index in requiredArgs) {
            if (requiredArgs.hasOwnProperty(index)) {
                property = requiredArgs[index];
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
    
    getUserAdminStatus: function() {
        if (this.httpArgsAreValid() === false) {
            alert('invalid args');
            return;
        }
        
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Signon Setup..."});
        mask.show();    
    
        Ext.Ajax.request({
            url: this.getParam('urls').isUserAdmin,
            params: {
                stationNo: this.getParam('stationNo'),
                userDuz: this.getParam('userDuz')
            },
            method: 'GET',
            scope: this,
            success: function(response) {
                mask.hide();
                this.setParam('isUserAdmin', response.responseText === 'true');
                this.loadUserInterface();
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
            allowDuzLogin: true,
            stationNo: this.getParam('stationNo'),            
            callback: this.getUserAdminStatus
        });
        this.setParam('session', session);                
    },    
    
    getUserDivision: function() {
        if (this.httpArgsAreValid() === false) {
            alert('invalid args');
            return;
        }
        this.setupProxies();

        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Configuring Interface..."});
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
                this.setParam('divisionName', result.name);
                this.setupSession();
            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
            }
        });                
    },
    

    loadUserInterface: function() {
        this.setupProxies(); 
        Ext.widget('adminPanel');        
        var tabPanel = Ext.getCmp('admintabpanel');
        tabPanel.items.each(function(item, index, len) {  
            if (item.getId() === 'translations-grid' ||
                LLVA.AVS.Admin.getParam('isUserAdmin')) {
                item.setDisabled(false);
            }
        }); 
        this.configureHeader();
    },

    isCompatibleBrowser: function() {
        var isCompatible = true;
        if (Ext.isIE6 || Ext.isIE7) {
            LLVA.Utils.MessageBox.error(
                'Your web browser is obsolete and is not supported by this application. ' +
                'Please contact your system administrator about upgrading Internet Explorer ' +
                'to version 8 or above.',
                'Incompatible web browser'
            );
            isCompatible = false;
        }
        return isCompatible;
    },
    
    configureHeader: function() {
        var admin = Ext.getCmp('adminPanel');        
        admin.setTitle('After Visit Summary Settings - Logged in as ' + 
            this.getParam('session').user.get('userNameDisplay') + 
            ' (' + this.getParam('divisionName') + ')');                
    },

    setupProxies: function() {
        var proxy = null;
        var mgr = Ext.data.StoreManager;
        var urls = this.getParam('urls');
        
        proxy = mgr.getByKey('Translations').getProxy();
        proxy.api.read = urls.fetchTranslations;
        proxy.api.update = urls.updateTranslation;
        
        proxy = mgr.getByKey('Services').getProxy();
        proxy.api.create = urls.createService;
        proxy.api.read = urls.fetchServices;        
        proxy.api.update = urls.updateService;    
        proxy.api.destroy = urls.destroyService;    
        
        proxy = mgr.getByKey('Clinics').getProxy();
        proxy.api.read = urls.searchClinics;       
        
        proxy = mgr.getByKey('Divisions').getProxy();
        proxy.api.read = urls.divisions;       

        proxy = mgr.getByKey('Languages').getProxy();
        proxy.api.read = urls.languages;         
                
        proxy = mgr.getByKey('Settings').getProxy();
        proxy.api.read = urls.fetchSettings;        
        proxy.api.update = urls.saveSettings;            
                
        proxy = mgr.getByKey('Labels').getProxy();
        proxy.api.read = urls.fetchLabels;        
        proxy.api.update = urls.updateLabel;         
    },
    
    resetViews: function() {
        var tabPanel = Ext.getCmp('admintabpanel');
        var currentTab = LLVA.AVS.Admin.getParam('currentTab');
        tabPanel.items.each(function(item, index, len) {  
            if (currentTab == item.getId() && item.getId() == 'headerfooter') {
                item.fireEvent('loaddata', item, true);
            } if (currentTab == item.getId() && item.getId() == 'settings-grid') {
                item.fireEvent('loadsettings', item, true);
                item.fireEvent('loadtiunotetext', item, true);
            }
        }); 
    },

    /**
     * Used to delay removal of the LoadMask in cases when everything happens so fast
     * that the user may not have sufficient visual feedback to know that the button
     * they pressed actually did something.
     */
    calculateDelay: function(timerStart) {
        var timerEnd = new Date();
        var diff = timerEnd.getTime() - timerStart.getTime();
        var delay = LLVA.AVS.Admin.getParam('loadMaskMinimumDuration') - diff;
        if (delay < 0) {
            delay = 0;
        }
        return delay;
    },

    logout: function() {
        this.getParam('session').logout();
    },
    
    getParam: function(name) {
        return LLVA.AVS.Admin.params[name];
    },

    setParam: function(name, value) {
        LLVA.AVS.Admin.params[name] = value;
    }

};