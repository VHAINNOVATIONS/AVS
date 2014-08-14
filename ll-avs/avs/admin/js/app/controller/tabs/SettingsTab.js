Ext.define('LLVA.AVS.Admin.controller.tabs.SettingsTab', {
    extend: 'Ext.app.Controller',

    settingsLoaded: false,
    tiuNoteTextLoaded: false,

    views: ['tabs.SettingsTab'],

    stores: ['Settings'],

    models: ['Setting'],

    init: function() {

        this.control({
            'settingstab': {
                activate: function(tab) {
                    this.loadSettings(tab, false);
                    this.loadTiuNoteText(tab, false);
                },
                'loadsettings': function(tab, reset) {
                    this.loadSettings(tab, reset);
                },
                'loadtiunotetext': function(tab, reset) {
                    this.loadTiuNoteText(tab, reset);
                }
            },
            'settingstab button[action=save]': {
                click: this.saveTiuNoteText
            }
        });
    },

    loadSettings: function(tab, reset) {
        if (reset) {
            this.settingsLoaded = false;
        }       
        if (this.settingsLoaded) {
            return;
        }
        var params = {
            divisionNo: LLVA.AVS.Admin.getParam('stationNo')
        };        
        this.getStore('Settings').load({params: params});
        this.settingsLoaded = true;                
    },
    
    loadTiuNoteText: function(tab, reset) {
        if (reset) {
            this.tiuNoteTextLoaded = false;
        }       
        if (this.tiuNoteTextLoaded) {
            return;
        }
        Ext.Ajax.request({
            url: LLVA.AVS.Admin.getParam('urls').fetchTiuNoteText,
            method: 'GET',
            scope: this,
			params: {
                divisionNo: LLVA.AVS.Admin.getParam('stationNo')
			},            
            success: function(response) {        
                var tiuNoteTextInput = tab.down('#tiuNoteText');
                tiuNoteTextInput.setValue(response.responseText);                
            },
            failure: function(response, requestOptions) {
                LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
            }
        });
        this.tiuNoteTextLoaded = true;
    },
    
    saveTiuNoteText: function(button) {

        var mask = new Ext.LoadMask(button.up('settingstab'), {msg:"Saving..."});
        mask.show();

        var timerStart = new Date();

        Ext.Ajax.request({
            url: LLVA.AVS.Admin.getParam('urls').saveTiuNoteText,
            params: {
                divisionNo: LLVA.AVS.Admin.getParam('stationNo'),
                tiuNoteText: button.up('settingstab').down('#tiuNoteText').getValue()
            },
            method: 'POST',
            scope: this,
            success: function(response) {

                // Introduce a delay so the user can actually see the LoadMask
                // in case the Save function happens really fast.
                var task = new Ext.util.DelayedTask(function(){
                    mask.hide();
                    Ext.JSON.decode(response.responseText);
                });

                task.delay(LLVA.AVS.Admin.calculateDelay(timerStart));

            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.Utils.MessageBox.ajaxFailure(response, requestOptions);
            }
        });
        
    }
    
});

