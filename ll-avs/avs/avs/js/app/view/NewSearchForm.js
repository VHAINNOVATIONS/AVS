Ext.define('LLVA.AVS.Wizard.view.NewSearchForm', {
    extend: 'Ext.window.Window',
    alias : 'widget.newsearchform',
    title: 'Krames Search',
    closeAction: 'hide',
    closable: true,
    modal: true,
    width: 300,
    height: 310,
    bodyPadding: 10,            
    
    initComponent: function() {
        
        var languages = Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            data : [
                {"abbr":"en", "name":"English"},
                {"abbr":"es", "name":"Spanish"},
                {"abbr":"zh", "name":"Chinese"},
                {"abbr":"vi", "name":"Vietnamese"},
                {"abbr":"ru", "name":"Russian"},
                {"abbr":"hy", "name":"Armenian"},
                {"abbr":"fa", "name":"Farse (Persian)"},
                {"abbr":"km", "name":"Khmer"},
                {"abbr":"ko", "name":"Korean"},
                {"abbr":"hmn", "name":"Hmong"},
                {"abbr":"tgl", "name":"Tagalog"},
                {"abbr":"de", "name":"German"},
                {"abbr":"fr", "name":"French"},
                {"abbr":"it", "name":"Italian"},
                {"abbr":"so", "name":"Somali"},
                {"abbr":"aa", "name":"Arabic"},
                {"abbr":"pp", "name":"Portuguese"}
            ]
        });        
        
        Ext.apply(this, {    
            items: [{
                xtype: 'form',
                bodyStyle: 'padding: 5px;',
                items: [{
                    xtype: 'label',
                    anchor: '100%',
                    margins: '5 0 5 0',
                    text: 'Keywords:'
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    name: 'KeywordsTextField',
                    id: 'KeywordsTextField',
                    hideLabel: true
                },
                {
                    xtype: 'radiogroup',
                    id: 'LogicalOperatorsRadioGroup',
                    fieldLabel: 'Logical operator',
                    defaults: {
                        name: 'logicalOperator'
                    },                    
                    items: [{
                        xtype: 'radiofield',
                        id: 'logicalAnd',
                        boxLabel: 'AND',
                        inputValue: 'And'
                    },
                    {
                        xtype: 'radiofield',
                        id: 'logicalOr',
                        boxLabel: 'OR',
                        inputValue: 'Or'
                    }]
                },
                {
                    xtype: 'label',
                    anchor: '100%',
                    margins: '5 0 5 0',
                    text: 'MeSH Codes:'
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    id: 'MeshCodesTextField',
                    hideLabel: true
                },
                {
                    xtype: 'label',
                    anchor: '100%',
                    margins: '5 0 5 0',
                    text: 'ICD9 Codes:'
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    id: 'Icd9CodesTextField',
                    hideLabel: true
                },
                {
                    xtype: 'label',
                    anchor: '100%',
                    margins: '5 0 5 0',
                    text: 'CPT Codes:'
                },        
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    id: 'CptCodesTextField',
                    hideLabel: true
                },
                {
                    xtype: 'combobox',
                    anchor: '100%',
                    fieldLabel: 'Language',
                    store: languages,
                    id: 'LanguageCombo',
                    labelWidth: 75,
                    queryMode: 'local',
                    displayField: 'name',
                    valueField: 'abbr'
                }],
                buttons: [{
                    text: 'Cancel',
                    action: 'cancel'
                }, {
                    text: 'Search',
                    action: 'search'
                }]
            }],
            
            listeners: {
                afterlayout: function(form) {
                    Ext.getCmp('KeywordsTextField').focus(false, 200);
                }
            }
        });
        
        this.callParent(arguments);
    }

});