Ext.define('LLVA.AVS.Admin.controller.tabs.TranslationsTab', {
	extend: 'Ext.app.Controller',

	activated: false,

	views: ['tabs.TranslationsTab'],

	stores: ['Translations'],

	models: ['Translation'],

	init: function() {

		this.control({
			'translationstab': {
				activate: this.loadData,
                'loadData': function(reset) {
                    alert('load data event');
                    this.loadData(reset);  
                }                
			},
			'#search-text': {
				specialkey: this.performSearchByPressingEnter
			},
			'button[action=search]': {
				click: this.performSearchByClickingButton
			},
			'button[action=reset]': {
				click: this.removeFiltersAndReload
			}
		});
	},

	loadData: function(reset) {
        if (reset) {
            this.activated = false;
        }
		if (this.activated) {
			return;
		}
        Ext.apply(this.getStore('Translations').getProxy().extraParams, {
            divisionNo: LLVA.AVS.Admin.getParam('stationNo'),
            language: LLVA.AVS.Admin.getParam('language')
        });
		this.getStore('Translations').guaranteeRange(0, 199);
		this.activated = true;
	},

	performSearchByClickingButton: function(button) {
		var textfield = button.up('toolbar').down('#search-text');
		this.searchByKeyword(textfield);
	},
	
	performSearchByPressingEnter: function(textfield, event) {
		if (event.getKey() === event.ENTER) {
			this.searchByKeyword(textfield);
		}
	},

	searchByKeyword: function(textfield) {

		var store = this.getStore('Translations');
		store.filters.clear();

		var keywords = textfield.getValue();
		if (!keywords) {
			var resetButton = textfield.up('toolbar').down('button[action=reset]');
			return this.removeFiltersAndReload(resetButton);
		}
			
		store.removeAll();
		store.filter([
			{property: "source", value: keywords}, 
			{property: "translation", value: keywords} 
		]);
	},
	
	removeFiltersAndReload: function(button) {
		var textfield = button.up('toolbar').down('#search-text');
		textfield.setValue('');

		this.getStore('Translations').clearFilter();
	}
});

