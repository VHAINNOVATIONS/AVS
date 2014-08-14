Ext.define('LLVA.PVS.controller.PVS', {
    extend: 'Ext.app.Controller',
    
    stores: [],    
    models: ['Demographics'],    
    views: ['Viewport'],
    
    user: undefined,
    curFontClass: 'normalFont',

    init: function() {
        
        this.control({
            'viewport': {
                beforerender: this.loadData
            },
            'viewport button[action=refresh]': {
                click: this.refresh
            }
        });
        
    },
    
    initFields: function() {
        this.curFontClass = 'normalFont';
    },
    
    loadData: function() {

        var frame = Ext.getCmp('frame');        
        var fmDatetime, locationIen;
    
        this.user = LLVA.PVS.getParam('session').getUser();
        LLVA.PVS.setParam('stationNo', this.user.get('userLoginStationNumber'));
        LLVA.PVS.setParam('divisionName', this.user.get('divisionName'));
        LLVA.PVS.setParam('userDuz', this.user.get('userDuz'));
        frame.setTitle('Pre-Visit Summary - ' + this.user.get('userNameDisplay') + ' (' + this.user.get('divisionName') + ')');

        var topToolbar = new Ext.Toolbar({
            ui: 'footer',
            dock: 'top',
            name: 'topToolbar',
            items: [{
                xtype: 'displayfield',
                fieldCls: 'x-infofield',
                name: 'name'
            }, '-', {
                xtype: 'displayfield',
                fieldCls: 'x-infofield',
                name: 'ssn'
            }, '-', {
                xtype: 'displayfield',
                fieldCls: 'x-infofield',
                name: 'dob'
            }]        
        });
        
        var bottomToolbar = new Ext.Toolbar({
            ui: 'footer',
            name: 'bottomToolbar',
            dock: 'top',
            height: 35,
            scope: this,
            pvs: this,
            items: [{
                fieldLabel: 'Encounter',
                labelWidth: 60,
                itemId: 'encounterCombo',
                name: 'encounter',
                xtype: 'combo',
                autoSelecting: false,
                scope: this,
                width: 320,   
                store: Ext.create('Ext.data.ArrayStore', {       
                    storeId: 'encountersStore',
                    idIndex: 0,
                    fields: ['id', 'fmDatetime', 'datetime', 'locationIen', 'locationName', 'status', 'selected']
                }),
                allowBlank: false,
                tpl: new Ext.XTemplate(
                    '<tpl for="."><div class="x-boundlist-item">',
                    '<h3>{locationName}</h3>',
                    '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{datetime} - {status}',
                    '</div></tpl>'
                ),
                displayField: 'locationName',
                queryMode: 'local',
                valueField: 'id',
                forceSelection:true,
                selectOnFocus: true,
                autoSelect: true,
                triggerAction: 'all',
                editable: false,                
                listeners: {
                    'select': {    
                        fn: function(combo, records) { 
                            var store = combo.getStore();      
                            var recordNum = store.findExact('id', records[0].get('id'));                            
                            LLVA.PVS.setParam('patientDfn', LLVA.PVS.getParam('demographics').get('dfn'));
                            LLVA.PVS.setParam('datetime', store.getAt(recordNum).get('fmDatetime'));
                            LLVA.PVS.setParam('locationIen', store.getAt(recordNum).get('locationIen'));
                            if (!autoSelecting) {      
                                combo.up('toolbar').pvs.initFields();                             
                                combo.up('toolbar').pvs.loadPvs();   
                            }                            
                        }
                    },
                    'afterrender': {
                        fn: function(combo) {
                            var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Encounters..."});
                            mask.show();
                            Ext.Ajax.request({
                                url:  LLVA.PVS.getParam('urls').encounters,                             
                                scope: this,
                                params: {
                                    facilityNo: combo.up('toolbar').pvs.user.get('userLoginStationNumber'),
                                    userDuz: combo.up('toolbar').pvs.user.get('userDuz'),
                                    patientDfn: LLVA.PVS.getParam('demographics').get('dfn')
                                },        
                                success: function (result, request) { 
                                    mask.hide();
                                    var encounters = Ext.JSON.decode(result.responseText);  
                                    if (encounters.length == 0) {
                                        Ext.getCmp('refreshBtn').setDisabled(true)
                                        Ext.getCmp('commentsBtn').setDisabled(true);
                                        Ext.getCmp('printBtn').setDisabled(true);
                                        Ext.getCmp('pdfBtn').setDisabled(true);
                                        return false;
                                    }                                    
                                    var data = [];  
                                    var i;
                                    for (i=0; i < encounters.length; i++) {    
                                        data.push([encounters[i].id, encounters[i].fmDatetime, encounters[i].datetime,
                                                  encounters[i].locationIen, encounters[i].locationName, encounters[i].status,
                                                  encounters[i].selected]);
                                    }        
                                    combo.getStore().loadData(data);    
                                    var id = '';
                                    combo.getStore().each(function(item, index, len) {
                                        var isSelected = item.get('selected');
                                        if (isSelected) {
                                            autoSelecting = true;
                                            combo.up('toolbar').pvs.initFields();   
                                            combo.setValue(item.get('id')); 
                                            LLVA.PVS.setParam('patientDfn', LLVA.PVS.getParam('demographics').get('dfn'));
                                            LLVA.PVS.setParam('datetime', item.get('fmDatetime'));
                                            LLVA.PVS.setParam('locationIen', item.get('locationIen'));
                                            combo.up('toolbar').pvs.loadPvs();  
                                            autoSelecting = false;
                                            return false;
                                        }
                                    });                                    
                                }
                            });       
                        }
                    }
                }
            },{
                id: 'refreshBtn',
                action: 'refresh',
                icon: 'images/page_refresh.png',
                text: 'Refresh',
                disabled: false
            }]
        });
        
        frame.addDocked(topToolbar, 0);
        frame.addDocked(bottomToolbar, 1);
        
        var title = '';
        var demographics = LLVA.PVS.getParam('demographics');
        topToolbar.down('displayfield[name=name]').setValue(demographics.get('name')); 
        topToolbar.down('displayfield[name=ssn]').setValue(demographics.get('ssn'));

        var dobString = '';
        if (demographics.get('dob') != null) {
            dobString = Ext.Date.format(demographics.get('dob'), 'M j, Y'); 
        }         
        var now = new Date();       
        var age = demographics.getAge(now);
        if (age > 0) {
            topToolbar.down('displayfield[name=dob]').setValue(dobString + ' (' + age + ')');
        }                
    },
    
    loadPvs: function() {
            
        var preview = Ext.getCmp('preview');
        
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Preview..."});
        mask.show();
        Ext.Ajax.request({
            url: LLVA.PVS.getParam('urls').sheet,
            params: {
                patientDfn: LLVA.PVS.getParam('patientDfn'),
                datetime: LLVA.PVS.getParam('datetime'),
                locationIen: LLVA.PVS.getParam('locationIen')
            },
            scope: this,
            success: function(response) {
                mask.hide();
                if (response) {
                    var preview = Ext.getCmp('preview');
                    preview.update(response.responseText, true);                    
                }   
            },
            failure: function(response, requestOptions) {
                mask.hide();
            }
        });
    },
    
    refresh: function(button) {
        this.loadPvs()    
    }
    
});

