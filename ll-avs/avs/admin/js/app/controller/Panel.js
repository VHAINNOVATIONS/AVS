Ext.define('LLVA.AVS.Admin.controller.Panel', {
    extend: 'Ext.app.Controller',

    views: ['Panel'],
    
    stores: ['Divisions', 'Languages'],
    
    refs: [{
        ref: 'translationsTab',
        selector: 'TranslationsTab'
    }],    
    
    init: function() {
        LLVA.AVS.Admin.setParam('translationstab');
        this.control({
            'adminPanel': {
                afterrender: function(panel) {
                    this.adjustHeight(panel);
                    this.loadDivisions();
                    this.loadLanguages();
                },
                'divisionSelect' : function(division) {
                    this.divisionSelect(division);
                },
                'languageSelect' : function(language) {
                    this.languageSelect(language);
                }
            },
			'button[action=help]': {
				click: this.showHelp
			}
        });
    },    

	showHelp: function() {
		window.open(LLVA.AVS.Admin.getParam('urls').help);		
	},    
    
    adjustHeight: function(panel) {
        var desiredHeight = Ext.getBody().getSize().height - panel.getPosition()[1] - 15;
        if (desiredHeight > 650) {
            desiredHeight = 650;
        }
        panel.setHeight(desiredHeight);
    },
    
    loadDivisions: function() {
        var params = {
            stationNo: LLVA.AVS.Admin.getParam('stationNo')
        };
        var store = this.getStore('Divisions');
        store.load({params: params, callback: function(records, operation, success) {
            Ext.each(records, function(rec, index) {
                if (rec.get('isDefault')) {
                    Ext.getCmp('divisionsCombo').setValue(rec.get('facilityNo'));
                    LLVA.AVS.Admin.setParam('stationNo', rec.get('facilityNo'));
                }
            });
        }});
    },

    loadLanguages: function() {   
        var store = this.getStore('Languages');
        store.load({callback: function(records, operation, success) {
            Ext.each(records, function(rec, index) {
                if (rec.get('abbreviation') === 'en') {
                    Ext.getCmp('languagesCombo').setValue(rec.get('abbreviation'));
                    LLVA.AVS.Admin.setParam('language', rec.get('abbreviation'));
                }
            });
        }});         
    },

    divisionSelect: function(stationNo) {
        LLVA.AVS.Admin.setParam('stationNo', stationNo);
        LLVA.AVS.Admin.resetViews();
    },

    languageSelect: function(language) {
        LLVA.AVS.Admin.setParam('language', language);
        LLVA.AVS.Admin.resetViews();
    }        

});

