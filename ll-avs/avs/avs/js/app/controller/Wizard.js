Ext.define('LLVA.AVS.Wizard.controller.Wizard', {
    extend: 'Ext.app.Controller',
    
    stores: ['SearchResults', 'Printers', 'ClinicalServices', 'RemoteMeds', 'Encounters'],    
    models: ['Demographics', 'Printer', 'SearchResult', 'EncounterTreeNode'],    
    views: ['Viewport', 'CommentsEditor', 'InfoSheets', 'NewSearchForm', 
            'PrinterSelectionForm', 'ContentEditor', 'ClinicalServicesSelectionForm',
            'RemoteMedsSelectionForm', 'EncountersTree'],
    refs: [
        {ref: 'encountersTree',  selector: 'encounterstree'}           
    ],                         
    
    serverIndex: 0,
    user: undefined,
    timer: undefined,
    infosheets: undefined,
    newSearchForm: undefined,
    printerSelectionForm: undefined,
    defaultPrinter: undefined,
    encounters: undefined,
    curFontClass: 'normalFont',
    curLanguage: 'en',
    printersLoaded: false,
    languages: undefined,
    curLabDateRange: -1, // no display
    charts: '',
    chartContent: '',
    chartArray: undefined,
    clinicalServicesSelectionForm: undefined,
    remoteMedsSelectionForm: undefined,
    printAllServiceDescriptions: false,
    selectedServiceDescriptions: '',
    remoteVaMedsJson: undefined,    
    remoteNonVaMedsJson: undefined,
    remoteVaMedicationsHtml: '',
    remoteNonVaMedicationsHtml: '',
    locked: false,
    fusionChart: undefined,
    initialRequest: true,
    contentEdited: false,
    editContentAlert: false, 
    diagnosisCodes: '',
    appTitle: '',
    kramesInstructions: '',
    medsTakingTitle: '',
    medsNotTakingTitle: '',
    remoteMedsNotTakingDisclaimer: '',
    refillsTitle: '',
    lastReleasedTitle: '',
    expiresTitle: '',
    facilityTitle: '',
    providerTitle: '',
    descriptionTitle: '',
    nonVaMedsNotTakingDisclaimer: '',
    startDateTitle: '',
    stopDateTitle: '',
    documentingFacilityTitle: '',    
    encounterChanged: true,

    init: function() {
        
        this.control({
            'wizardviewport': {
                beforerender: this.loadStringResources,
                afterrender: function() {                    
                    Ext.EventManager.addListener(Ext.getBody(), 'keydown', function(e){
                        if ((e.getTarget().type == undefined)  || (e.getTarget().type != 'text') && 
                            (e.getKey() == e.BACKSPACE )) {
                            e.preventDefault();
                        }
                    });                     
                }              
            },
            'wizardviewport button[action=refresh]': {
                click: this.refresh
            },            
            'wizardviewport button[action=printAll]': {
                click: function(button) { this.print('printAll'); }
            },
            'wizardviewport button[action=vistaPrinterSelection]': {
                click: function(button) { this.vistaPrinterSelection(); }
            },    
            'wizardviewport button[action=printToWindowsPrinter]': {
                click: function(button) { this.print('printToWindowsPrinter'); }
            },             
            'wizardviewport button[action=comments]': {
                click: this.editComments
            },
            'wizardviewport button[action=editContent]': {
                click: this.editContent
            },            
            'wizardviewport button[action=note]': {
                click: this.createNote
            },            
            'wizardviewport button[action=infosheets]': {
                click: this.showInfoSheets
            },
            'wizardviewport button[action=pdf]': {
                click: this.printToPdf
            },            
            'wizardviewport button[action=exit]': {
                click: function(button) { button.up('wizardviewport').close(); }
            },
            'commentsEditor button[action=save]': {
                click: this.saveComments
            },
            'commentsEditor button[action=cancel]': {
                click: this.cancelComments
            },            
            'contentEditor button[action=save]': {
                click: this.saveContentEdits
            },
            'contentEditor button[action=cancel]': {
                click: this.cancelContentEdits
            },                        
            'infosheets': {
                'insertSel': this.insertComments,
                'newSearch': this.newKramesSearch,
                'printDocuments': function(url) { 
                    this.print('printToVistaPrinter', url); 
                },
                'insertKramesTitles': function(titles) {
                    this.insertKramesTitles(titles);
                }
            },                                   
            'newsearchform button[action=search]': {
                click: this.searchKrames
            },
            'newsearchform button[action=cancel]': {
                click: this.cancelSearchKrames
            },
            'printerselectionform': {
                activate: this.loadVistaPrinters
            },
            'printerselectionform button[action=print]': {
                click: this.printToVistaPrinter
            },
            'printerselectionform button[action=apply]': {
                click: this.applyVistaPrinterSelection
            },            
            'printerselectionform button[action=cancel]': {
                click: this.cancelVistaPrint
            },
            'clinicalservicesselectionform': {
                activate: this.loadClinicalServices
            },
            'clinicalservicesselectionform button[action=ok]': {
                click: this.setSelectedServiceDescriptions
            },            
            'clinicalservicesselectionform button[action=cancel]': {
                click: this.cancelSelectServiceDescriptions
            },
            'remotemedsselectionform button[action=continue]': {
                click: this.setSelectedRemoteMeds
            },
            'encounterstree': {
                'selectedEncounters': function(selEncounters) {
                    this.setSelectedEncounters(selEncounters);
                }
            }
        });
        
    },
    
    setSelectedEncounters: function(selEncounters) {
        var encArr = [];        
        var locationIens = '';
        var locationNames = '';
        var datetimes = '';
        Ext.each(selEncounters, function(encounter, index) {            
            if (encArr.length > 0) {
                locationNames += ', ';
                locationIens += ',';
                datetimes += ',';
            }                   
            locationIens += encounter.locationIen;
            locationNames += encounter.locationName;
            datetimes += encounter.fmDatetime;
            encArr.push(encounter);
        });
        LLVA.AVS.Wizard.setParam('datetimes', datetimes);
        LLVA.AVS.Wizard.setParam('locationIens', locationIens); 
        LLVA.AVS.Wizard.setParam('locationNames', locationNames);        
        this.encounters = encArr;
        Ext.getCmp('encounterPicker').setValue(locationNames);  
        this.encounterChanged = true;        
    },
       
    initFields: function() {
        this.curFontClass = 'normalFont';
        this.curLanguage = 'en';
        this.curLabDateRange = -1;
        this.charts = '';
        this.initialRequest = true;
        this.contentEdited = false;
    },
    
    stopTimer: function() {
        if (this.timer != undefined) {
            clearInterval(this.timer);
        }
    },
    
    startTimer: function() {
        var wizard = this;
        var interval = LLVA.AVS.Wizard.getParam('facilitySettings').get('refreshFrequency');
        this.stopTimer();
        if ((interval != null) && (interval > 0)) {
            this.timer = setInterval(function(){wizard.loadAVS();}, interval);
        }
    },
        
    loadStringResources: function() {
		Ext.Ajax.request({
			url: LLVA.AVS.Wizard.getUrl('clientStrings'),
			params: {
                facilityNo: LLVA.AVS.Wizard.getParam('stationNo'),
				language: this.curLanguage
			},
			method: 'POST',
			scope: this,
			success: function(response) {
                var clientStrings = Ext.JSON.decode(response.responseText);
                this.appTitle = clientStrings.appTitle;
                this.kramesInstructions = clientStrings.kramesInstructions;
                this.medsTakingTitle = clientStrings.medsTakingTitle;
                this.medsNotTakingTitle = clientStrings.medsNotTakingTitle;
                this.remoteMedsNotTakingDisclaimer = clientStrings.remoteMedsNotTakingDisclaimer;
                this.refillsTitle = clientStrings.refillsTitle;
                this.lastReleasedTitle = clientStrings.lastReleasedTitle;
                this.expiresTitle = clientStrings.expiresTitle;
                this.facilityTitle = clientStrings.facilityTitle;
                this.providerTitle = clientStrings.providerTitle;
                this.descriptionTitle = clientStrings.descriptionTitle;
                this.nonVaMedsNotTakingDisclaimer = clientStrings.nonVaMedsNotTakingDisclaimer;
                this.startDateTitle = clientStrings.startDateTitle;
                this.stopDateTitle = clientStrings.stopDateTitle;
                this.documentingFacilityTitle = clientStrings.documentingFacilityTitle;   
                this.loadData();
			},
            failure: function(response) {
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'clientStrings', this.loadStringResources);
            }
		});    
    
    },
    
    loadEncounters: function(picker) { 
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Encounters..."});
        mask.show();
        
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('encounters'),                             
            scope: this,
            params: {
                facilityNo: this.user.get('userLoginStationNumber'),
                userDuz: this.user.get('userDuz'),
                patientDfn: LLVA.AVS.Wizard.getParam('demographics').get('dfn')
            },        
            success: function (result, request) { 
                mask.hide();
                var allEncounters = Ext.JSON.decode(result.responseText);  
                if (allEncounters.length == 0) {
                    Ext.getCmp('refreshBtn').setDisabled(true)
                    Ext.getCmp('commentsBtn').setDisabled(true);
                    Ext.getCmp('optionsBtn').setDisabled(true);
                    Ext.getCmp('editContentBtn').setDisabled(true);
                    Ext.getCmp('infoSheetsBtn').setDisabled(true);                    
                    Ext.getCmp('printBtn').setDisabled(true);                    
                    Ext.MessageBox.show({title: 'No Recent Encounters', 
                                 msg: 'This patient has no recent outpatient encounters.',
                                 buttons: Ext.MessageBox.OK,
                                 modal: false                                 
                    });
                    return false;
                }      
                var encArr = [];                
                var id = '';
                var patientDfn = '';
                var datetimes = '';
                var locationIens = '';
                var locationNames = '';
                Ext.each(allEncounters, function(parent, index) {
                    Ext.each(parent.children, function(encounter, index) {                        
                        var isSelected = encounter.selected;
                        if (isSelected) {                        
                            autoSelecting = true;                          
                            if (encArr.length > 0) {
                                datetimes += ',';
                                locationIens += ',';
                                locationNames += ', ';
                            }                              
                            datetimes += encounter.fmDatetime;
                            locationIens += encounter.locationIen;  
                            locationNames += encounter.locationName;
                            autoSelecting = false;
                            encArr.push(encounter);
                        }                        
                    });
                });    
                this.encounters = encArr;
                LLVA.AVS.Wizard.setParam('patientDfn',  LLVA.AVS.Wizard.getParam('demographics').get('dfn'));
                LLVA.AVS.Wizard.setParam('datetimes', datetimes);
                LLVA.AVS.Wizard.setParam('locationIens', locationIens); 
                LLVA.AVS.Wizard.setParam('locationNames', locationNames);                
                picker.getPicker().loadData(allEncounters); 
                picker.up('toolbar').wizard.initFields(); 
                picker.setValue(locationNames);  
                picker.up('toolbar').wizard.loadAVS();  
            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'encounters', function() {this.loadEncounters(picker)});
            }                                
        });                   
    },

    loadData: function() {

        var frame = Ext.getCmp('frame');        
        var fmDatetime, locationIen;
    
        this.user = LLVA.AVS.Wizard.getParam('session').getUser();
        LLVA.AVS.Wizard.setParam('stationNo', this.user.get('userLoginStationNumber'));
        LLVA.AVS.Wizard.setParam('divisionName', this.user.get('divisionName'));
        LLVA.AVS.Wizard.setParam('userDuz', this.user.get('userDuz'));
        frame.setTitle(this.appTitle + ' - ' + this.user.get('userNameDisplay') + ' (' + this.user.get('divisionName') + ')');

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
            }, '->', 
            {
                xtype: 'displayfield',
                fieldCls: 'x-statusfield',
                id: 'lastRefreshed'
            }]        
        });
        
        var bottomToolbar = new Ext.Toolbar({
            ui: 'footer',
            name: 'bottomToolbar',
            dock: 'top',
            height: 35,
            scope: this,
            wizard: this,
            items: [{
                fieldLabel: 'Encounters',
                labelWidth: 60,
                id: 'encounterPicker',
                name: 'encounter',
                xtype: 'pickerfield',
                scope: this,
                width: 375,   
                allowBlank: false,
                selectOnFocus: true,
                editable: false,
                createPicker: function() {
                    return Ext.createWidget('encounterstree');              
                },
                listeners: {
                    'afterrender': {
                        fn: function(picker) {
                            picker.up('toolbar').wizard.loadEncounters(picker);
                        }
                    },
                    'expand': {
                        fn: function(picker)  {
                            picker.getPicker().expandFirstNode();
                        }
                    }
                }
            },
            '->',            
            {
                id: 'refreshBtn',
                action: 'refresh',
                icon: 'images/page_refresh.png',
                text: 'Refresh',
                disabled: false
            },            
            '-',
            {
                id: 'commentsBtn',
                action: 'comments',
                icon: 'images/comment.png',
                text: 'Edit Instructions',
                disabled: false
            }, {
                id: 'editContentBtn',
                action: 'editContent',
                icon: 'images/pencil.png',
                text: 'Edit AVS',
                disabled: false
            }, {
                id: 'optionsBtn',
                xtype:'splitbutton',
                icon: 'images/options.png',
                text: 'Options',
                handler: function(btn) {btn.showMenu();},
                disabled: false,
                scope: this,
                menu: [{
                    id: 'fontBtn',
                    icon: 'images/font.png',
                    text: 'Font Size',
                    scope: this,
                    menu: [{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'normalFont',
                        text: 'Normal',
                        checked: true,
                        handler: function(item) {
                            this.fontSize(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'largeFont',
                        text: 'Large',
                        checked: false,
                        handler: function(item) {
                            this.fontSize(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'veryLargeFont',
                        text: 'Very Large',
                        checked: false,
                        handler: function(item) {
                            this.fontSize(item.getId());
                        }
                    }]
                },{
                    id: 'languageBtn',
                    icon: 'images/language.png',
                    text: 'Language',
                    scope: this,
                    menu: [{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'English',
                        text: 'English',
                        checked: true,
                        handler: function(item) {
                            this.setLanguage(item.getId());
                        }
                    }]
                },{
                    id: 'labDateRangeBtn',
                    icon: 'images/lab.png',
                    text: 'Lab Results',                    
                    scope: this,
                    menu: [{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'labNoDisplay',
                        text: 'Do Not Display',
                        checked: true,
                        handler: function(item) {
                            this.setLabDateRange(item.getId(), true);
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'lab1Week',
                        text: 'Last 1 Week',
                        checked: false,
                        handler: function(item) {
                            this.setLabDateRange(item.getId(), true);
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'lab1Month',
                        text: 'Last 1 Month',
                        checked: false,
                        handler: function(item) {
                            this.setLabDateRange(item.getId(), true);
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'lab3Months',
                        text: 'Last 3 Months',
                        checked: false,
                        handler: function(item) {
                            this.setLabDateRange(item.getId(), true);
                        }
                    }],
                    disabled: false
                },{
                    id: 'clinicalChartsBtn',
                    icon: 'images/chart_line.png',
                    text: 'Clinical Charts',                    
                    scope: this,
                    menu: [{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'chartsNoDisplay',
                        text: 'Do Not Display',
                        checked: true,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'chartsDisplayAll',
                        text: 'Display All',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },
                    '-',
                    {
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'BMI',
                        text: 'BMI',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'BP',
                        text: 'BP',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'CHOL',
                        text: 'Cholesterol',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'CRT',
                        text: 'Creatinine',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'EGFR',
                        text: 'eGFR',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'HBA1C',
                        text: 'HbA1C',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'HDL',
                        text: 'HDL',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'HGB',
                        text: 'HGB',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'LDL',
                        text: 'LDL',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'PLT',
                        text: 'Platelets',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'PSA',
                        text: 'PSA',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{                    
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'PULSE',
                        text: 'Pulse',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'TRG',
                        text: 'Triglycerides',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'WGT',
                        text: 'Weight',
                        checked: false,
                        handler: function(item) {
                            this.setChartsDisplayed(item.getId());
                        }
                    }],
                    disabled: false
                },{
                    id: 'sectionsBtn',
                    icon: 'images/layout_edit.png',
                    text: 'Sections Displayed',
                    scope: this,
                    menu: [{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'patientInfo',
                        text: 'Patient Info',
                        checked: false
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'clinicsVisited',
                        text: 'Clinics Visited',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'providers',
                        text: 'Providers',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'diagnoses',
                        text: 'Diagnoses',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'vitals',
                        text: 'Vitals',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'immunizations',
                        text: 'Immunizations',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'procedures',
                        text: 'Procedures',
                        checked: false
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'orders',
                        text: 'Orders',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'appointments',
                        text: 'Appointments',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'comments',
                        text: 'Instructions',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'pcp',
                        text: 'Primary Care Provider',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'primaryCareTeam',
                        text: 'Primary Care Team',
                        checked: true
                    },{                    
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'allergies',
                        text: 'Allergies',
                        checked: true
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'medications',
                        text: 'Medications',
                        checked: true                        
                    },{                    
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'clinicalCharts',
                        text: 'Clinical Charts',
                        checked: false,
                        handler: function(item) {
                            this.onClinicalChartsMenuChecked(item.checked);
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'labResults',
                        text: 'Lab Results',
                        checked: false,
                        handler: function(item) {
                            this.onLabResultsMenuChecked(item.checked);
                        }
                    }]
                }, {
                    id: 'printServiceDescriptionsBtn',
                    icon: 'images/physician.png',
                    text: 'Clinical Service Descriptions',                    
                    scope: this,
                    menu: [{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'printAllServiceDescriptions',
                        text: 'Include all Clinical Services information in printouts',
                        checked: false,
                        handler: function(item) {
                            Ext.getCmp('printSelectedServiceDescriptions').setChecked(false);                            
                            this.printAllServiceDescriptions = item.checked;
                            if (item.checked) {
                                this.selectedServiceDescriptions = '';
                            }
                        }
                    },{
                        xtype: 'menucheckitem',
                        scope: this,
                        id: 'printSelectedServiceDescriptions',
                        text: 'Select Clinical Services for inclusion in printouts',
                        checked: false,
                        handler: function(item) {
                            this.printAllServiceDescriptions = false;
                            Ext.getCmp('printAllServiceDescriptions').setChecked(false);
                            if (item.checked) {
                                this.selectServiceDescriptions(this.selectedServiceDescriptions);
                            } else {
                                this.selectedServiceDescriptions = '';
                            }
                        }
                    }],
                    disabled: !LLVA.AVS.Wizard.getParam('facilitySettings').get('servicesEnabled')                   
                },{
                    scope: this,
                    text: 'Edit Patient-Friendly Translations',    
                    icon: 'images/user_comment.png',                    
                    handler: function() {
                        var url = Ext.String.format('{0}?stationNo={1}&userDuz={2}',
                        LLVA.AVS.Wizard.getUrl('admin'),
                        LLVA.AVS.Wizard.getParam('stationNo'),
                        LLVA.AVS.Wizard.getParam('userDuz'));                              
                        window.open(url, '', 'width=880,height=650');
                    }
                },                
                '-',                
                {
                    xtype: 'menucheckitem',
                    scope: this,
                    id: 'lockSheet',
                    text: 'Prevent other users from editing this sheet',
                    checked: false,
                    handler: function(item) {
                        this.locked = item.checked;
                        this.saveLockedStatus(this.locked);
                    }
                }],   
                disabled: false                
            }, {
                id: 'infoSheetsBtn',
                action: 'infosheets',
                icon: 'images/information.png',
                text: 'Krames',
                disabled: !LLVA.AVS.Wizard.getParam('facilitySettings').get('kramesEnabled')                
            }, 
            '-',
            {
                id: 'printBtn',
                xtype:'splitbutton',
                handler: function(btn) {btn.showMenu();},
                icon: 'images/printer.png',
                text: 'Print',
                disabled: false,
                scope: this,
                menu: [{
                    scope: this,
                    text: 'Default Printer',
                    id: 'DefaultPrinterMenuItem',
                    icon: 'images/printer-share.png',     
                    handler: function() {
                        if (this.defaultPrinter) {
                            this.printToVistaPrinter();                            
                        } else {
                            this.print('printAll');
                        }
                    }
                },{
                    scope: this,
                    text: 'VistA Printer...',
                    action: 'vistaPrinterSelection',
                    icon: 'images/printer-network.png',     
                    handler: function() {
                        this.vistaPrinterSelection();
                    }
                },{
                    scope: this,
                    text: 'Windows Printer...',
                    action: 'printToWindowsPrinter',
                    icon: 'images/printer-plus.png',     
                    handler: function() {
                        this.print('printToWindowsPrinter');
                    }
                }],
                disabled: false
            }, {
                id: 'pdfBtn',
                icon: 'images/pdf.png',
                text: 'PDF',
                action: 'pdf'
            }]
        });
        
        frame.addDocked(topToolbar, 0);
        frame.addDocked(bottomToolbar, 1);
        
        var title = '';
        var demographics = LLVA.AVS.Wizard.getParam('demographics');
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

        //this.loadLanguages();
    },
    
    loadAVS: function() {
            
        if (LLVA.AVS.Wizard.getParam('locationIens') == '') {
            Ext.MessageBox.show({title: 'No Encounters Selected', 
                         msg: 'Please select one or more patient encounters.',
                         buttons: Ext.MessageBox.OK,
                         modal: false                                 
            });
            return;
        }        
            
        if ((this.infosheets !== undefined) &&
            (this.infosheets !== null)) {                                    
            this.infosheets.destroy();
            this.infosheets = null;
        }
        if ((this.newSearchForm !== null) && 
            (this.newSearchForm !== undefined)) {
            this.newSearchForm.destroy();
            this.newSearchForm = null;
        }
    
        APP_GLOBAL.getController('Wizard').stopTimer();
        var preview = Ext.getCmp('preview');
        
        if (this.encounterChanged) {
            var el = Ext.DomQuery.selectNode('#comments-div #comments-area');
            if (el) {
                el.innerHTML = '';
            }
            this.encounterChanged = false;
            this.initFields();
        }        
        var comments = this.getFormattedComments();
        var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Preview..."});
        mask.show();
        var initReq = this.initialRequest;

        Ext.Ajax.request({
            url: LLVA.AVS.Wizard.getUrl('sheet'),
            params: {
                patientDfn: LLVA.AVS.Wizard.getParam('patientDfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                comments: comments,
                fontClass: this.curFontClass,
                language: this.curLanguage,
                labDateRange: this.curLabDateRange,
                sections: this.getSectionsToDisplay(),
                printAllServiceDescriptions: this.printAllServiceDescriptions,
                selectedServiceDescriptions: this.selectedServiceDescriptions,
                locked: this.locked,
                charts: this.charts,
                remoteVaMedicationsHtml: this.remoteVaMedicationsHtml,
                remoteNonVaMedicationsHtml: this.remoteNonVaMedicationsHtml,
                initialRequest: this.initialRequest
            },
            scope: this,
            success: function(response) {
                mask.hide();
                if (response) {
                    var avsJson = Ext.JSON.decode(response.responseText);    
                    preview.update(avsJson.content, true);
                    this.curFontClass = avsJson.fontClass;
                    this.curLanguage = avsJson.language;
                    this.curLabDateRange = avsJson.labDateRange;
                    this.printAllServiceDescriptions = avsJson.printAllServiceDescriptions;
                    this.selectedServiceDescriptions = avsJson.selectedServiceDescriptions;
                    this.locked = avsJson.locked;
                    this.comments = avsJson.instructions;
                    this.charts = avsJson.charts; 
                    this.initialRequest = false;          
                    this.contentEdited = avsJson.contentEdited;
                    this.diagnosisCodes = avsJson.diagnosisCodes;

                    APP_GLOBAL.getController('Wizard').setLastRefreshed(avsJson.lastRefreshed);
                    APP_GLOBAL.getController('Wizard').fontSize(this.curFontClass);
                    APP_GLOBAL.getController('Wizard').setLanguage(this.curLanguage);
                    
                    var labDateRangeId;
                    if (this.curLabDateRange == 7) {
                        labDateRangeId = 'lab1Week';
                    } else if (this.curLabDateRange == 30) {
                        labDateRangeId = 'lab1Month';
                    } else if (this.curLabDateRange == 90) {
                        labDateRangeId = 'lab3Months';
                    } else {
                        labDateRangeId = 'labNoDisplay';
                    } 
                    APP_GLOBAL.getController('Wizard').setLabDateRange(labDateRangeId, false); 
                    APP_GLOBAL.getController('Wizard').setPrintAllServiceDescriptions(this.printAllServiceDescriptions);
                    APP_GLOBAL.getController('Wizard').setLocked(this.locked);
                    Ext.getCmp('printSelectedServiceDescriptions').setChecked((this.selectedServiceDescriptions != null) &&
                      (this.selectedServiceDescriptions != ''));    
                                    
                    if ((this.sections != null) && (this.sections != '')) {
                        var sectionsBtn = Ext.getCmp('sectionsBtn');
                        sectionsBtn.menu.items.each(function(item) {
                            item.setChecked(false);
                        });                       
                        var sectionsArr = this.sections.split(',');
                        var i;
                        for (i = 0; i < sectionsArr.length; i++) {
                            var cb = Ext.getCmp(sectionsArr[i]);
                            cb.setChecked(true);
                        }
                    }
                                    
                    var clinicalChartsBtn = Ext.getCmp('clinicalChartsBtn');
                    clinicalChartsBtn.menu.items.each(function(item) {
                        if (item.getId().lastIndexOf('menuseparator', 0) < 0) {  
                            item.setChecked(false);
                        }
                    });                         
                    if ((this.charts != null) && (this.charts != '')) {
                        var chartsArr = this.charts.split(',');
                        var i;
                        for (i = 0; i < chartsArr.length; i++) {
                            if (chartsArr[i] == 'ALL') {
                                Ext.getCmp('chartsDisplayAll').setChecked(true);
                                Ext.getCmp('clinicalCharts').setChecked(true);
                            } else if (chartsArr[i] == 'NONE') {
                                Ext.getCmp('chartsNoDisplay').setChecked(true);
                                Ext.getCmp('clinicalCharts').setChecked(false);
                            } else {
                                var cb = Ext.getCmp(chartsArr[i]);
                                cb.setChecked(true);
                            }
                        }
                    } else {
                        Ext.getCmp('chartsNoDisplay').setChecked(true);
                        Ext.getCmp('clinicalCharts').setChecked(false);
                    }                                         
                   
                    Ext.getCmp('commentsBtn').setDisabled(this.locked && !avsJson.userIsProvider);
                    Ext.getCmp('editContentBtn').setDisabled(this.locked && !avsJson.userIsProvider);                    
                    Ext.getCmp('optionsBtn').setDisabled(this.locked && !avsJson.userIsProvider);                   
                    Ext.getCmp('lockSheet').setDisabled(this.locked && !avsJson.userIsProvider); 
                    Ext.getCmp('printAllServiceDescriptions').setDisabled((this.locked && !avsJson.userIsProvider) ||
                        !LLVA.AVS.Wizard.getParam('facilitySettings').get('servicesEnabled'));                         
                    Ext.getCmp('printSelectedServiceDescriptions').setDisabled((this.locked && !avsJson.userIsProvider) ||
                        !LLVA.AVS.Wizard.getParam('facilitySettings').get('servicesEnabled'));                                                 
                    Ext.getCmp('refreshBtn').setDisabled(false)                    
                    Ext.getCmp('printBtn').setDisabled(false);
                    Ext.getCmp('pdfBtn').setDisabled(false);  
                                        
                }   
                this.getDefaultPrinter();
                
                if (initReq) {
                     this.remoteVaMedsJson = avsJson.remoteVaMeds;
                     this.remoteNonVaMedsJson = avsJson.remoteNonVaMeds;
                     if ((this.remoteVaMedsJson != null && this.remoteVaMedsJson.length > 0) ||
                         (this.remoteNonVaMedsJson != null && this.remoteNonVaMedsJson.length > 0)) {
                         this.selectRemoteMeds();
                     } else {
                        if (this.remoteVaMedsJson == null || this.remoteVaMedsJson.length == 0) {
                            var node = Ext.DomQuery.selectNode('div[id=remote-va-medications-div]');
                            if (node != null) {
                                node.innerHTML = '';       
                            }
                            node = Ext.fly('section-remote-va_medications');
                            if (node != null) {
                                node.removeCls('section'); 
                                node.addCls('section-hidden'); 
                            }
                        }
                        APP_GLOBAL.getController('Wizard').startTimer();  
                     }
                } else {
                    APP_GLOBAL.getController('Wizard').startTimer();                
                }                                                                
            },
            failure: function(response, requestOptions) {
                mask.hide();
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'sheet', this.loadAVS);
            }
        });
    },
    
    refresh: function(button) {
        //this.initialRequest = true;
        this.loadAVS()    
    },

    editComments: function(button) {    
        APP_GLOBAL.getController('Wizard').stopTimer();        
        var comments = this.getFormattedComments();
        var editor = Ext.widget('commentsEditor');
        editor.down('htmleditor[name=comments]').setValue(comments);        
    },

    saveComments: function(button) {
        var editor = button.up('commentsEditor');
        var comments = editor.down('htmleditor[name=comments]').getValue();
        comments = Ext.String.trim(comments);
        // remove zero-width space that htmleditor injects                         
        comments = comments.replace(/[\u200B-\u200D\uFEFF]/g, '');
        if (comments === '') {
            comments = 'None';
        }                       
        Ext.DomQuery.selectNode('#comments-div').innerHTML = 
            '<pre id="comments-area">' + comments + '</pre>';                               

        editor.destroy();
        
        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('saveComments'),                             
            scope: this,
            params: {
                facilityNo: user.get('userLoginStationNumber'),
                userDuz: user.get('userDuz'),
                patientDfn: LLVA.AVS.Wizard.getParam('demographics').get('dfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                comments: comments 
            },
            failure: function(response, requestOptions) {
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'saveComments', this.saveComments);
            }
        });
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }
    },
    
    cancelComments: function(button) {
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }        
        var editor = button.up('commentsEditor');
        editor.destroy();
    },    
    
    insertComments: function(content) {
        content = Ext.String.trim(content);
        
        var comments = this.getFormattedComments();        
        comments += '<br/>' + content + '<br/>';
        Ext.DomQuery.selectNode('#comments-div').innerHTML = 
            '<pre id="comments-area">' + comments + '</pre>';   

        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('saveComments'),                             
            scope: this,
            params: {
                facilityNo: user.get('userLoginStationNumber'),
                userDuz: user.get('userDuz'),
                patientDfn: LLVA.AVS.Wizard.getParam('demographics').get('dfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                comments: comments
            },
            failure: function(response, requestOptions) {
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'insertComments', this.insertComments);
            }
        });              
    },    
    
    insertKramesTitles: function(titles) {
        var str = titles.split('^');
        var content = this.kramesInstructions;
        var i;
        for (i = 0; i < str.length; i++) {
            content += "\"" + str[i] + "\"";
            if (i < str.length-1) {
                content += ', ';
            }
        }
    
        var comments = this.getFormattedComments();        
        comments += '<br/>' + content + '<br/>';
        Ext.DomQuery.selectNode('#comments-div').innerHTML = 
            '<pre id="comments-area">' + comments + '</pre>';    

        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('saveComments'),                             
            scope: this,
            params: {
                facilityNo: user.get('userLoginStationNumber'),
                userDuz: user.get('userDuz'),
                patientDfn: LLVA.AVS.Wizard.getParam('demographics').get('dfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                comments: comments
            },
            failure: function(response, requestOptions) {
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'insertKramesTitles', this.insertKramesTitles);
            }
        });            
    },
        
    editContent: function(button) {    
        APP_GLOBAL.getController('Wizard').stopTimer();        
        var content = Ext.DomQuery.selectNode('#groups').innerHTML;
        content = '<div id="editor-contents" class="normalFont">' + content + '</div>';
        var wizard = this;
        if (!this.editContentAlert) {
            this.editContentAlert = true;
            Ext.MessageBox.show({title: 'Custom AVS Edits', 
                                 msg: 'Please note that if the AVS is manually refreshed this will overwrite any\n' +
                                      'custom edits that are made.  Auto-refresh will also be disabled for the\n' +
                                      'current patient encounter if edits are made.',
                                 buttons: Ext.MessageBox.OKCANCEL,
                                 modal: false,
                                 fn: function(btn, text) {
                                    if (btn === 'ok') {                    
                                        var editor = Ext.widget('contentEditor');
                                        editor.down('tinymcefield[name=content]').setValue(content);                
                                    }
                                }
            });
        } else {
            var editor = Ext.widget('contentEditor');
            editor.down('tinymcefield[name=content]').setValue(content);  
        }        
    },

    saveContentEdits: function(button) {
        var editor = button.up('contentEditor');
        var content = editor.down('tinymcefield[name=content]').getValue();
        // extract the contents in the 'editor-contents' div
        var el = document.createElement('div');
        el.innerHTML = content;
        content = el.childNodes[0].innerHTML;
        Ext.DomQuery.selectNode('#groups').innerHTML = content;  
        editor.destroy();
        this.contentEdited = true;
        Ext.Ajax.request({
            url: LLVA.AVS.Wizard.getUrl('setCustomContent'),
            params: {
                patientDfn: LLVA.AVS.Wizard.getParam('patientDfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                customContent: content                
            },
            scope: this,
            failure: function(response, requestOptions) {
                LLVA.AVS.Wizard.handleAjaxError(response.status, 'saveContentEdits', this.saveContentEdits);
            }
        });                               
    },
    
    cancelContentEdits: function(button) {
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }        
        var editor = button.up('contentEditor');
        editor.destroy();
    },
                
    saveLockedStatus: function(isLocked) {
        Ext.Ajax.request({
            url: LLVA.AVS.Wizard.getUrl('setLocked'),
            params: {
                patientDfn: LLVA.AVS.Wizard.getParam('patientDfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                locked: isLocked
            },
            scope: this,
            success: function(response) {
            
            }
        });     
    },

    showInfoSheets: function() {
        APP_GLOBAL.getController('Wizard').stopTimer();
        if (this.infosheets == undefined ||
            this.infosheets == null) {
            this.infosheets = this.getView('InfoSheets').create();
        }
        this.infosheets.setDiagnosisCodes(this.diagnosisCodes);
        this.infosheets.show('infoSheetsBtn');
    },
    
    newKramesSearch: function() {
        APP_GLOBAL.getController('Wizard').stopTimer();
        if ((this.newSearchForm == null) || (this.newSearchForm == undefined)) {
            this.newSearchForm = this.getView('NewSearchForm').create();        
        }
        this.newSearchForm.show();
    },
    
    searchKrames: function(button) {
        var keywords;
        var logicalOperator;
        var meshCodes;
        var icdCodes;
        var cptCodes;
        var language;
        
        var searchForm = button.up('newsearchform');
        keywords = searchForm.down('textfield[name=KeywordsTextField]').getValue();
        var logicalOperator = searchForm.down('radiogroup[id=LogicalOperatorsRadioGroup]').items.first().getGroupValue();
        meshCodes = searchForm.down('textfield[id=MeshCodesTextField]').getValue();
        icdCodes = searchForm.down('textfield[id=Icd9CodesTextField]').getValue();
        cptCodes = searchForm.down('textfield[id=CptCodesTextField]').getValue();
        language = searchForm.down('combobox[id=LanguageCombo]').getValue();
        if ((keywords == '') && (meshCodes == '') && (icdCodes == '') && (cptCodes == '')) {
            alert('Please provide one or more search values.');
        } else {
            this.newSearchForm.hide();       
            this.infosheets.doSearch(keywords, logicalOperator, meshCodes, icdCodes, cptCodes, language);
        }
    },
    
    cancelSearchKrames: function(button) {
        this.newSearchForm.hide();
            if (!this.contentEdited) {
                APP_GLOBAL.getController('Wizard').startTimer();
            }
        },        
    
    loadVistaPrinters: function() {
        if (this.printersLoaded) {
            return;
        }
        this.printerSelectionForm.loadPrinters();
        this.printersLoaded = true;
    },     
    
    vistaPrinterSelection: function(button) {
        APP_GLOBAL.getController('Wizard').stopTimer();
        if ((this.printerSelectionForm == null) || (this.printerSelectionForm == undefined)) {
            this.printerSelectionForm = this.getView('PrinterSelectionForm').create();        
        }
        this.printerSelectionForm.setSelectedPrinter(this.defaultPrinter);
        this.printerSelectionForm.show();
    },
    
    getDefaultPrinter: function() {
        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('getDefaultPrinter'),                             
            scope: this,
            params: {
                facilityNo: user.get('userLoginStationNumber'),
                userDuz: user.get('userDuz')
            },        
            success: function (result, request) { 
                this.defaultPrinter = Ext.JSON.decode(result.responseText);
                if (this.defaultPrinter.name == null || this.defaultPrinter.name == "") {
                    this.defaultPrinter = null;
                    Ext.getCmp('DefaultPrinterMenuItem').setText('Default Printer');
                } else {
                    Ext.getCmp('DefaultPrinterMenuItem').setText('Default Printer (' + this.defaultPrinter.name + ')');
                }
            }
        });           
    },
    
    setDefaultPrinter: function() {
        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        var ien, ip, name;
        if (this.defaultPrinter != null) {
            ien = this.defaultPrinter.ien;
            ip = this.defaultPrinter.ipAddress;
            name = this.defaultPrinter.name;        
        } else {
            ien = "";
            ip = "";
            name = "";
        }
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('setDefaultPrinter'),                             
            scope: this,
            params: {
                facilityNo: user.get('userLoginStationNumber'),
                userDuz: user.get('userDuz'),
                printerIen: ien,
                printerIp: ip,
                printerName: name
            },        
            success: function (result, request) { 
            }
        });           
    },    
    
    printToVistaPrinter: function(button) {        
    
        var wizard = this;
        var numCharts = 0;
        var count = 0;
        var chartFilenames = '';
        var vistaPrinter = wizard.applyVistaPrinterSelection(button);
        if (this.printerSelectionForm) {
            this.printerSelectionForm.hide(); 
        }
        
        var callback = function(chartFilenames) {
        
            wizard.chartFilenames = chartFilenames;
            
            wizard.fadingWindow('Print Status','The document has been sent to<br><b>' + 
            Ext.String.htmlEncode(vistaPrinter.name) + '</b><br> for printing.');         
            
            var user = LLVA.AVS.Wizard.getParam('session').getUser();
            
            Ext.Ajax.request({
                url:  LLVA.AVS.Wizard.getUrl('printPdf'),                             
                scope: wizard,
                params: {
                    facilityNo: user.get('userLoginStationNumber'),
                    userDuz: user.get('userDuz'),
                    patientDfn: LLVA.AVS.Wizard.getParam('demographics').get('dfn'),
                    datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                    locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                    locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                    fontClass: wizard.curFontClass,
                    language: wizard.curLanguage,
                    labDateRange: wizard.curLabDateRange,     
                    sections: wizard.getSectionsToDisplay(),
                    printAllServiceDescriptions: wizard.printAllServiceDescriptions,
                    selectedServiceDescriptions: wizard.selectedServiceDescriptions,
                    charts: wizard.charts,
                    locked: wizard.locked,
                    printerIen: vistaPrinter.ien,
                    printerIp: vistaPrinter.ipAddress,
                    printerName: vistaPrinter.name,
                    chartFilenames: chartFilenames
                },        
                success: function (result, request) {
                }, 
                failure: function(response, request) {
                    if (response.status == '503') {
                         LLVA.AVS.Wizard.handleAjaxError(response.status, 'printPdf', function() {callback(chartFilenames);});
                    } else {
                        Ext.MessageBox.alert('Print Failure', 'Attempt to print to ' + 
                        Ext.String.htmlEncode(vistaPrinter.displayName) + 
                        ' was unsucessful.<br><br>Please try again, or contact technical support.'); 
                    }
                }
            });
        };                         
                
        var name;
        if (vistaPrinter.displayName != undefined) {
            name = vistaPrinter.displayName;
        } else {
            name = vistaPrinter.name;
        }
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }      
        
        if ((this.charts != 'NONE') && (document.getElementById('chartsFrame') != null)) {
            this.exportCharts(callback);                        
        } else {
            callback('');
        }                 
    }, 

    applyVistaPrinterSelection: function(button) {             
        var vistaPrinter = null;
        if (button != null) {
            this.printerSelectionForm.hide();   
            var defaultDeviceCheckbox = Ext.getCmp('defaultDeviceCheckbox');
            if (defaultDeviceCheckbox.getValue() == '1') {
                var selectedPrinter = this.printerSelectionForm.selectedPrinter; 
                if (selectedPrinter == null || !selectedPrinter.get) {
                    vistaPrinter = Ext.create('LLVA.AVS.Wizard.model.Printer');
                    vistaPrinter.ien = this.defaultPrinter.ien;
                    vistaPrinter.name = this.defaultPrinter.name;
                    vistaPrinter.ipAddress = this.defaultPrinter.ipAddress;
                    vistaPrinter.displayName = this.defaultPrinter.displayName;
                    vistaPrinter.isDefault = this.defaultPrinter.isDefault;
                    return vistaPrinter; 
                } else {
                    selectedPrinter.isDefault = true;                
                    this.defaultPrinter = Ext.create('LLVA.AVS.Wizard.model.Printer');
                    this.defaultPrinter.ien = selectedPrinter.get('ien');
                    this.defaultPrinter.name = selectedPrinter.get('name');
                    this.defaultPrinter.ipAddress = selectedPrinter.get('ipAddress');
                    this.defaultPrinter.displayName = selectedPrinter.get('displayName');
                    this.defaultPrinter.isDefault = true;
                    Ext.getCmp('DefaultPrinterMenuItem').setText('Default Printer (' + this.defaultPrinter.name + ')');
                }
            } else {
                this.defaultPrinter = null;
                Ext.getCmp('DefaultPrinterMenuItem').setText('Default Printer');                
            }
            vistaPrinter = Ext.create('LLVA.AVS.Wizard.model.Printer');
            vistaPrinter.ien = this.printerSelectionForm.selectedPrinter.get('ien');
            vistaPrinter.name = this.printerSelectionForm.selectedPrinter.get('name');
            vistaPrinter.ipAddress = this.printerSelectionForm.selectedPrinter.get('ipAddress');
            vistaPrinter.displayName = this.printerSelectionForm.selectedPrinter.get('displayName');
            vistaPrinter.isDefault = defaultDeviceCheckbox.getValue() == '1';
            this.setDefaultPrinter();            
        } else {
            vistaPrinter = this.defaultPrinter;
        }
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }
        return vistaPrinter;         
    },      
    
    cancelVistaPrint: function(button) {     
        this.printerSelectionForm.hide();        
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }
    },  
    
    loadClinicalServices: function(button) {
        this.getStore('ClinicalServices').load();    
    },
    
    setPrintAllServiceDescriptions: function(val) {
        var menuitem = Ext.getCmp('printAllServiceDescriptions');
        menuitem.setChecked(val);
    },
    
    selectServiceDescriptions: function(selectedIds) {
        APP_GLOBAL.getController('Wizard').stopTimer();
        this.clinicalServicesSelectionForm = this.getView('ClinicalServicesSelectionForm').create();        
        this.clinicalServicesSelectionForm.setSelectedServices(this.selectedServiceDescriptions);
        this.clinicalServicesSelectionForm.show();
    },    
    
    setSelectedServiceDescriptions: function(button) {
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }
        this.selectedServiceDescriptions = this.clinicalServicesSelectionForm.getSelectedServices();
        this.clinicalServicesSelectionForm.close();
    },
    
    cancelSelectServiceDescriptions: function(button) {
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }
        this.selectedServiceDescriptions = '';
        Ext.getCmp('printSelectedServiceDescriptions').setChecked(false);
        this.clinicalServicesSelectionForm.close();
    },
    
    selectRemoteMeds: function() {
        APP_GLOBAL.getController('Wizard').stopTimer();
        this.remoteMedsSelectionForm = this.getView('RemoteMedsSelectionForm').create(); 
        var remoteMedsJson;
        if (this.remoteVaMedsJson == null || this.remoteNonVaMedsJson == null) {       
            var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Remote Meds..."});
            mask.show();            
            var user = LLVA.AVS.Wizard.getParam('session').getUser();
            Ext.Ajax.request({
                url:  LLVA.AVS.Wizard.getUrl('getRemoteMeds'),                             
                scope: this,
                params: {
                    facilityNo: user.get('userLoginStationNumber'),
                    userDuz: user.get('userDuz'),
                    patientDfn: LLVA.AVS.Wizard.getParam('patientDfn')
                },        
                success: function (result, request) { 
                    mask.hide();
                    remoteMedsJson = Ext.JSON.decode(result.responseText);  
                    this.remoteVaMedsJson = [];
                    this.remoteNonVaMedsJson = [];
                    var i;
                    for (i = 0; i < remoteMedsJson.length; i++) {
                        if (remoteMedsJson[i].type == 'VA') {
                            this.remoteVaMedsJson.push(remoteMedsJson[i]);
                        } else {
                            this.remoteNonVaMedsJson.push(remoteMedsJson[i]);
                        }
                    }
                    this.remoteMedsSelectionForm.setRemoteMedsJson(remoteMedsJson);
                    this.remoteMedsSelectionForm.show();                            
                }
            });       
        } else {   
            var i;
            remoteMedsJson = [];
            for (i = 0; i < this.remoteVaMedsJson.length; i++) {
                remoteMedsJson.push(this.remoteVaMedsJson[i]);
            }
            for (i = 0; i < this.remoteNonVaMedsJson.length; i++) {
                remoteMedsJson.push(this.remoteNonVaMedsJson[i]);
            }    
            this.remoteMedsSelectionForm.setRemoteMedsJson(remoteMedsJson);
            this.remoteMedsSelectionForm.show();                    
        }
        
    },    
    
    setSelectedRemoteMeds: function() {
        if (!this.contentEdited) {
            APP_GLOBAL.getController('Wizard').startTimer();
        }
        
        var selRemoteMeds = this.remoteMedsSelectionForm.getSelectedRemoteMeds();
        var unselRemoteMeds = this.remoteMedsSelectionForm.getUnselectedRemoteMeds();
        this.remoteMedsSelectionForm.close();
        
        this.remoteVaMedicationsHtml = '';        
        this.remoteNonVaMedicationsHtml = '';    

        var numSelRemoteMeds = 0;
        var numUnselRemoteMeds = 0;       
        
        // display remote va meds        
        var i;
        for (i = 0; i < selRemoteMeds.length; i++) {
            if (selRemoteMeds[i].type == 'VA') {
                numSelRemoteMeds++;
            }
        }
        for (i = 0; i < unselRemoteMeds.length; i++) {
            if (unselRemoteMeds[i].type == 'VA') {
                numUnselRemoteMeds++;
            }
        }        
        for (i = 0; i < 2; i++) {
            var remoteMeds;
            if (i == 0) {
                remoteMeds = selRemoteMeds;
                if (numSelRemoteMeds > 0 && numUnselRemoteMeds > 0) {
                    this.remoteVaMedicationsHtml += "<div class=\"med-instructions\">";
                    this.remoteVaMedicationsHtml += "<u>" + this.medsTakingTitle + "</u>";
                    this.remoteVaMedicationsHtml += "</div>";
                }
            } else {
                remoteMeds = unselRemoteMeds;
                if (numUnselRemoteMeds > 0) {                    
                    this.remoteVaMedicationsHtml += "<br/><div class=\"med-instructions\">";
                    this.remoteVaMedicationsHtml += "<u>" + this.medsNotTakingTitle + "</u><br/><br/>";
                    this.remoteVaMedicationsHtml += this.remoteMedsNotTakingDisclaimer;                    
                    this.remoteVaMedicationsHtml += "</div>";
                 }
            }
            var j;
            for (j = 0; j < remoteMeds.length; j++) {            
                var med = remoteMeds[j];  
                if (med.type == 'VA') {                
                    this.remoteVaMedicationsHtml += "<div class=\"med-name\">" + med.name + "</div>";
                    this.remoteVaMedicationsHtml += "<div class=\"med-detail\">" + med.sig + "</div>";
                    this.remoteVaMedicationsHtml += "<div class=\"med-detail\">" + this.refillsTitle + ": " + med.refills;
                    if (med.dateLastReleased != '') {
                        this.remoteVaMedicationsHtml += "&nbsp;&nbsp;&nbsp;" + this.lastReleasedTitle + ": " + med.dateLastReleased;
                    }
                    if (med.dateExpires != '') {
                        this.remoteVaMedicationsHtml += "&nbsp;&nbsp;&nbsp;" + this.expiresTitle + ": " + med.dateExpires;
                    }            
                    this.remoteVaMedicationsHtml += "<br/>";
                    if (med.site != '') {
                        this.remoteVaMedicationsHtml += this.facilityTitle + ": " + med.site;
                    }              
                    if (med.provider != '') {
                        this.remoteVaMedicationsHtml += "&nbsp;&nbsp;&nbsp;" + this.providerTitle + ": " + med.provider;
                    }  
                    this.remoteVaMedicationsHtml += "</div>";
                    if (med.description != undefined && med.description != '') {
                        this.remoteVaMedicationsHtml += "<div class=\"med-detail\">" + this.descriptionTitle + ": " + med.description + "</div>";
                    }
                }
            }    
        }          
        if (this.remoteVaMedicationsHtml != null && this.remoteVaMedicationsHtml != '') {
           var node = Ext.DomQuery.selectNode('div[id=remote-va-medications-div]');
           if (node != null) {
                node.innerHTML = this.remoteVaMedicationsHtml;       
            }
            node = Ext.fly('section-remote-va_medications');
            if (node != null) {
                node.removeCls('section-hidden'); 
                node.addCls('section'); 
            }            
            var user = LLVA.AVS.Wizard.getParam('session').getUser();
            Ext.Ajax.request({
                url:  LLVA.AVS.Wizard.getUrl('setRemoteVAMedsHtml'),                             
                scope: this,
                params: {
                    facilityNo: user.get('userLoginStationNumber'),
                    userDuz: user.get('userDuz'),
                    patientDfn: LLVA.AVS.Wizard.getParam('patientDfn'),
                    datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                    locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                    locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                    remoteVaMedicationsHtml: this.remoteVaMedicationsHtml                
                },        
                success: function (result, request) {                                     
                }
            });           
        } else {
            var node = Ext.DomQuery.selectNode('div[id=remote-va-medications-div]');       
            if (node != null) {
                node.innerHTML = '';
            }
            node = Ext.fly('section-remote-va_medications');
            if (node != null) {
                node.removeCls('section'); 
                node.addCls('section-hidden');         
            }
        }     

        // display remote non-va meds      
        numSelRemoteMeds = 0;
        numUnselRemoteMeds = 0;                           
        var remoteNonVaMedsTaking = '';
        var nonVaMeds = Ext.DomQuery.selectNode('div[id=non-va-meds-div]').innerHTML;        
        Ext.DomQuery.selectNode('div[id=non-va-meds-div]').innerHTML = '';
        var i;
        for (i = 0; i < selRemoteMeds.length; i++) {
            if (selRemoteMeds[i].type == 'Non-VA') {
                numSelRemoteMeds++;
            }
        }
        for (i = 0; i < unselRemoteMeds.length; i++) {
            if (unselRemoteMeds[i].type == 'Non-VA') {
                numUnselRemoteMeds++;
            }
        }        
        for (i = 0; i <= 1; i++) {
            var remoteMeds;
            if (i == 0) {
                remoteMeds = selRemoteMeds;
                if (numSelRemoteMeds > 0 && numUnselRemoteMeds > 0) {
                    remoteNonVaMedsTaking += "<div class=\"med-instructions\">";
                    remoteNonVaMedsTaking += "<u>" + this.medsTakingTitle + "</u>";
                    remoteNonVaMedsTaking += "</div>";
                }
                remoteNonVaMedsTaking += nonVaMeds;
            } else {
                remoteMeds = unselRemoteMeds;
                if (numUnselRemoteMeds > 0) {                    
                    this.remoteNonVaMedicationsHtml += "<br/><div class=\"med-instructions\">";
                    this.remoteNonVaMedicationsHtml += "<u>" + this.medsNotTakingTitle + "</u><br/><br/>";
                    this.remoteNonVaMedicationsHtml += this.nonVaMedsNotTakingDisclaimer;                    
                    this.remoteNonVaMedicationsHtml += "</div>";
                 }                 
            }
            var j;
            for (j = 0; j < remoteMeds.length; j++) {            
                var med = remoteMeds[j];  
                if (med.type == 'Non-VA') {                
                    this.remoteNonVaMedicationsHtml += "<div class=\"med-name\">" + med.name + "</div>";
                    this.remoteNonVaMedicationsHtml += "<div class=\"med-detail\">" + med.sig + "</div>";                    
                    this.remoteNonVaMedicationsHtml += "<div class=\"med-detail\">";
                    if (med.startDate != 'N/A') {
                        this.remoteNonVaMedicationsHtml += this.startDateTitle + ": " + med.startDate;
                    }
                    if (med.stopDate != 'N/A') {
                        this.remoteNonVaMedicationsHtml += "&nbsp;&nbsp;&nbsp;" + this.stopDateTitle + ": " + med.stopDate;
                    }            
                    if (med.startDate != 'N/A' || med.stopDate != 'N/A') {
                        this.remoteNonVaMedicationsHtml += "<br/>";
                    }
                    if (med.site != '') {
                        this.remoteNonVaMedicationsHtml += this.documentingFacilityTitle + ": " + med.site;
                    }              
                    this.remoteNonVaMedicationsHtml += "</div>";
                }
            }    
        }            
        
        if (remoteNonVaMedsTaking == '' && this.remoteNonVaMedicationsHtml == '') {        
            var node = Ext.DomQuery.selectNode('div[id=remote-non-va-medications-div]');       
            if (node != null) {
                node.innerHTML = '';
            }
            node = Ext.fly('remote-non-va-medications-div');
            if (node != null) {
                node.removeCls('section'); 
                node.addCls('section-hidden');         
            }        
        } else {
            Ext.DomQuery.selectNode('div[id=remote-non-va-medications-div]').innerHTML = remoteNonVaMedsTaking + this.remoteNonVaMedicationsHtml;       
            Ext.fly('section-non-va_medications').removeCls('section-hidden');                
            Ext.fly('section-non-va_medications').addCls('section');
        }
        
        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        Ext.Ajax.request({
            url:  LLVA.AVS.Wizard.getUrl('setRemoteNonVAMedsHtml'),                             
            scope: this,
            params: {
                facilityNo: user.get('userLoginStationNumber'),
                userDuz: user.get('userDuz'),
                patientDfn: LLVA.AVS.Wizard.getParam('patientDfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIens'),
                locationNames: LLVA.AVS.Wizard.getParam('locationNames'),
                remoteNonVaMedicationsHtml: this.remoteNonVaMedicationsHtml                
            },        
            success: function (result, request) { 
            }
        });                 
    },
    
    printToPdf: function() {
        this.stopTimer();
        this.fadingWindow("Generating PDF", "Please wait while PDF is<br/>generated for the AVS.", 3000);
        var wizard = this;
        this.exportCharts(function(chartFilenames) { 
            this.chartFilenames = chartFilenames;
            var user = LLVA.AVS.Wizard.getParam('session').getUser();
            var url = Ext.String.format('{0}?facilityNo={1}&userDuz={2}&patientDfn={3}&datetimes={4}&locationIens={5}' + 
                '&locationNames={6}&fontClass={7}&labDateRange={8}&printAllServiceDescriptions={9}' + 
                '&selectedServiceDescriptions={10}&sections={11}&charts={12}&chartFilenames={13}&locked={14}' +
                '&language={15}&remoteVaMedicationsHtml={16}&remoteNonVaMedicationsHtml={17}',            
                LLVA.AVS.Wizard.getUrl('printPdf'),                             
                user.get('userLoginStationNumber'),
                user.get('userDuz'),
                LLVA.AVS.Wizard.getParam('demographics').get('dfn'),
                LLVA.AVS.Wizard.getParam('datetimes'),
                LLVA.AVS.Wizard.getParam('locationIens'),
                LLVA.AVS.Wizard.getParam('locationNames'),
                wizard.curFontClass,
                wizard.curLabDateRange, 
                wizard.printAllServiceDescriptions,
                wizard.selectedServiceDescriptions,
                wizard.getSectionsToDisplay(),
                wizard.charts,
                chartFilenames,
                wizard.locked,
                wizard.curLanguage);
            window.location = url;   
            wizard.startTimer();
        });            
    },
         
    pdf: function() {
        
        APP_GLOBAL.getController('Wizard').stopTimer();
        
        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        var comments = this.getFormattedComments();   
        var pdfObject;
        var onLoadInit = false;
        var wizard = this;
        this.exportCharts(function(chartFilenames) {              
            this.chartFilenames = chartFilenames;
            var pdfWin = new Ext.Window({
                layout:'fit',
                width:900,
                height:650,
                closeAction:'destroy',
                scope: this,
                autoScroll:false,
                collapsible: true,
                title:'Generating PDF...',
                modal:false,
                maximizable:true,
                wizard: this,
                html: '<div style="height:100%;width:100%;" id="pdf"></div>',
                buttons: [{                
                    text: wizard.defaultPrinter != null ? 'Default Printer (' + wizard.defaultPrinter.name + ')' : 'Default Printer',
                    icon: 'images/printer-share.png', 
                    scope: wizard,
                    handler: function() {      
                        pdfWin.close();     
                        if (wizard.defaultPrinter) {
                            wizard.printToVistaPrinter();                            
                        } else {
                            wizard.print('printAll');
                        }
                    }                    
                },{
                    text:'VistA Printer...',
                    scope: wizard,
                    icon: 'images/printer-network.png', 
                    handler: function() { 
                        pdfWin.close();   
                        wizard.vistaPrinterSelection();
                    }
                },{
                    text:'Windows Printer...',
                    scope: wizard,
                    icon: 'images/printer-plus.png', 
                    handler: function() {
                        pdfObject.printWithDialog();       
                    }
                },{                
                    text: 'Close',
                    scope: wizard,
                    handler: function() {
                        pdfWin.close();
                    }
                }],
                listeners: {
                
                    'afterrender': function() { 
                        var params = {  
                            id: 'pdfObject',
                            url: Ext.String.format('{0}?patientDfn={1}&datetimes={2}&locationIens={3}' + 
                            '&locationNames={4}&fontClass={5}&labDateRange={6}&printAllServiceDescriptions={7}' + 
                            '&selectedServiceDescriptions={8}&sections={9}&charts={10}'+ 
                            '&chartFilenames={11}&locked={12}&language={13}&print=false',
                                    LLVA.AVS.Wizard.getUrl('pdf'),
                                    LLVA.AVS.Wizard.getParam('patientDfn'),
                                    LLVA.AVS.Wizard.getParam('datetimes'),
                                    LLVA.AVS.Wizard.getParam('locationIens'),
                                    LLVA.AVS.Wizard.getParam('locationNames'),
                                    wizard.curFontClass,
                                    wizard.curLabDateRange,
                                    wizard.printAllServiceDescriptions,
                                    wizard.selectedServiceDescriptions ? wizard.selectedServiceDescriptions : '',
                                    wizard.getSectionsToDisplay(),
                                    wizard.charts ? wizard.charts : '',
                                    chartFilenames,
                                    wizard.locked,
                                    wizard.curLanguage)
                            ,                                        
                            pdfOpenParams: {
                                navpanes: 0,
                                toolbar: 0,
                                statusbar: 0,
                                view: "FitH"
                            }
                    
                        };
                        try { 
                            pdfObject = new PDFObject(params).embed("pdf"); 
                            if (navigator.userAgent.indexOf("MSIE") > -1 && !window.opera) {
                                pdfObject.onreadystatechange = function() {
                                    if (pdfObject.readyState == "4") {
                                        pdfWin.setTitle("PDF Viewer");
                                        pdfWin.setLoading(false);
                                    }
                                };
                            } else {
                                pdfObject.onload = function() {
                                    if (onLoadInit) {
                                        pdfWin.setTitle("PDF Viewer");
                                        pdfWin.setLoading(false);
                                    } else {
                                        onLoadInit = true;
                                    }
                                };
                            }   
                        } catch(err) {
                            alert(err.message);
                            LLVA.AVS.Wizard.handleAjaxError(err.message, 'pdf', this.pdf);
                        }                        
                    },
                    'show': function() {
                        this.setLoading(true);
                    },
                    'beforeclose': function() {
                        if (!this.contentEdited) {
                            APP_GLOBAL.getController('Wizard').startTimer();
                        }
                    }
                }    
            });
            pdfWin.show();        
        });
                         
    },    
    
    fontSize: function(cls) {        
        document.getElementById('sheet-header').className = cls;
        document.getElementById('sheet-contents').className = cls;
        document.getElementById('sheet-footer').className = cls;
        this.curFontClass = cls;
        var fontBtn = Ext.getCmp('fontBtn');
        fontBtn.menu.items.each(function(item) {
            item.setChecked(item.getId() == cls);             
        });
    },
    
    setLanguage: function(lang) {  
        /*    
        this.curLanguage = lang;
        var languageBtn = Ext.getCmp('languageBtn');
        languageBtn.menu.items.each(function(item) {
            item.setChecked(item.getId() == lang);             
        });
        */
    },    
    
    setLabDateRange: function(id, refresh) {    
        if (id == 'lab1Week') {
            this.curLabDateRange = 7;
            Ext.getCmp('labResults').setChecked(true);
        } else if (id == 'lab1Month') {
            this.curLabDateRange = 30;
            Ext.getCmp('labResults').setChecked(true);
        } else if (id == 'lab3Months') {
            this.curLabDateRange = 90;
            Ext.getCmp('labResults').setChecked(true);
        } else {
            this.curLabDateRange = -1;
            Ext.getCmp('labResults').setChecked(false);
        }
        var labDateRangeBtn = Ext.getCmp('labDateRangeBtn');
        labDateRangeBtn.menu.items.each(function(item) {
            item.setChecked(item.getId() == id);             
        });     
        if (refresh) {
            this.loadAVS();
        }        
    },
    
    setChartsDisplayed: function(id) {    
        var clinicalChartsBtn = Ext.getCmp('clinicalChartsBtn');
        if (id == 'chartsNoDisplay') {
            clinicalChartsBtn.menu.items.each(function(item) {
                if ((item.getId() != 'chartsNoDisplay') &&
                    (item.getId().lastIndexOf('menuseparator', 0) < 0)) {
                    item.setChecked(false);             
                }
            });       
            if (Ext.getCmp('chartsNoDisplay').checked) {
                this.charts = 'NONE';
                Ext.getCmp('clinicalCharts').setChecked(false);
            } else {
                this.charts = 'ALL';
                Ext.getCmp('chartsDisplayAll').setChecked(true);
                Ext.getCmp('clinicalCharts').setChecked(true);
            }            
        } else if (id == 'chartsDisplayAll') {
            clinicalChartsBtn.menu.items.each(function(item) {
                if ((item.getId() != 'chartsDisplayAll') &&
                    (item.getId().lastIndexOf('menuseparator', 0) < 0)) {
                    item.setChecked(false);             
                }
            });            
            if (Ext.getCmp('chartsDisplayAll').checked) {
                this.charts = 'ALL';
                Ext.getCmp('clinicalCharts').setChecked(true);
            } else {
                this.charts = 'NONE';
                Ext.getCmp('chartsNoDisplay').setChecked(true);
                Ext.getCmp('clinicalCharts').setChecked(false);
            }                        
        } else {
            var s = '';
            clinicalChartsBtn.menu.items.each(function(item) {
                if ((item.getId() != 'chartsNoDisplay') &&
                    (item.getId() != 'chartsDisplayAll') &&
                    (item.getId().lastIndexOf('menuseparator', 0) < 0) && 
                    (item.checked)) {  
                    s += item.getId() + ',';         
                }
            }); 
            this.charts = s;            
            if (this.charts == '') {
                 Ext.getCmp('chartsNoDisplay').setChecked(true); 
                 Ext.getCmp('clinicalCharts').setChecked(false);
            } else {
                Ext.getCmp('chartsNoDisplay').setChecked(false);
                Ext.getCmp('chartsDisplayAll').setChecked(false);
                Ext.getCmp('clinicalCharts').setChecked(true);
                this.charts = this.charts.substring(0, this.charts.length-1);
            }                        
        }
    },
    
    setLocked: function(val) {
        var menuitem = Ext.getCmp('lockSheet');
        menuitem.setChecked(val);
    },    
    
    setLastRefreshed: function(val) {
        Ext.getCmp('lastRefreshed').setValue("Last Refreshed " + val);
    },
    
    getSectionsToDisplay: function() {
        var sections = '';
        var sectionsBtn = Ext.getCmp('sectionsBtn');
        sectionsBtn.menu.items.each(function(item) {
            if (item.checked) {
                sections += item.getId() + ',';
            }
        });    
        if (sections.length > 0) {
            sections = sections.substring(0, sections.length-1);
        }
        return sections;
    },
    
    onClinicalChartsMenuChecked: function(checked) {
        var clinicalChartsBtn = Ext.getCmp('clinicalChartsBtn');
        clinicalChartsBtn.menu.items.each(function(item) {
            if (item.getId().lastIndexOf('menuseparator', 0) < 0) {  
                item.setChecked(false);        
            }
        });         
        if (checked) {
            Ext.getCmp('chartsDisplayAll').setChecked(true);
            this.charts = 'ALL';            
         } else {
             Ext.getCmp('chartsNoDisplay').setChecked(true);  
             this.charts = 'NONE';
         }
    },

    onLabResultsMenuChecked: function(checked) {
        var labDateRangeBtn = Ext.getCmp('labDateRangeBtn'); 
        labDateRangeBtn.menu.items.each(function(item) {
            item.setChecked(false);             
        });        
        if (checked) { 
            Ext.getCmp('lab1Week').setChecked(true);   
            this.curLabDateRange = 7;            
        } else {
            Ext.getCmp('labNoDisplay').setChecked(true);  
            this.curLabDateRange = -1;            
        }
    },    
    
    print: function(action, url) {                     
        
        APP_GLOBAL.getController('Wizard').stopTimer();
        
        var mask = new Ext.LoadMask(Ext.getBody(), {id: 'printMask', msg:"Preparing Document for Printing..."});
        mask.update('Preparing Document for Printing...');
        mask.show();               
        
        var user = LLVA.AVS.Wizard.getParam('session').getUser();
        var comments = Ext.DomQuery.selectNode('#preview #comments-area').innerHTML;        
        var wizard = this;        
        var callback = function(chartFilenames) {
            this.chartFilenames = chartFilenames;
            if (!url) {
                url = Ext.String.format('{0}?patientDfn={1}&datetimes={2}&locationIens={3}' + 
                '&locationNames={4}&fontClass={5}&labDateRange={6}&printAllServiceDescriptions={7}' + 
                '&selectedServiceDescriptions={8}&sections={9}&charts={10}' + 
                '&chartFilenames={11}&locked={12}&language={13}&print=true',
                        LLVA.AVS.Wizard.getUrl('pdf'),
                        LLVA.AVS.Wizard.getParam('patientDfn'),
                        LLVA.AVS.Wizard.getParam('datetimes'),
                        LLVA.AVS.Wizard.getParam('locationIens'),
                        LLVA.AVS.Wizard.getParam('locationNames'),
                        wizard.curFontClass,
                        wizard.curLabDateRange,
                        wizard.printAllServiceDescriptions,
                        wizard.selectedServiceDescriptions ? wizard.selectedServiceDescriptions : '',
                        wizard.getSectionsToDisplay(),
                        wizard.charts ? wizard.charts : '',
                        chartFilenames,
                        wizard.locked,
                        wizard.curLanguage)
            }
            var onLoadInit = false;
            var pdfObject;
            var doPrint = function() {
                Ext.getCmp('printMask').destroy();
                if (action == 'printAll') {
                    pdfObject.print();
                } else if (action == 'printToWindowsPrinter') {
                    pdfObject.printWithDialog();           
                }
                if (!this.contentEdited) {
                    APP_GLOBAL.getController('Wizard').startTimer();
                }
            };        

            var params = {  
                id: 'pdfObject',
                url: url,
                pdfOpenParams: {
                    navpanes: 0,
                    toolbar: 0,
                    statusbar: 0,
                    view: "FitH"
                }
        
            };
            var pdfDiv = document.getElementById('pdfDiv');
            if (pdfDiv == null) {
                pdfDiv = document.createElement('div');
                pdfDiv.id = "pdfDiv";
                pdfDiv.style.position = 'absolute';
                pdfDiv.style.top = '0px';
                pdfDiv.style.left = '0px';
                pdfDiv.style.height = '0px';
                pdfDiv.style.width = '0px';
                document.body.appendChild(pdfDiv);     
            }
            try {
                pdfObject = new PDFObject(params).embed("pdfDiv");    
                if (navigator.userAgent.indexOf("MSIE") > -1 && !window.opera){
                    pdfObject.onreadystatechange = function() {
                        if (pdfObject.readyState == "4") {
                            doPrint();
                        }
                    };
                } else {
                    pdfObject.onload = function() {
                        if (onLoadInit) {
                            doPrint();
                        } else {
                            onLoadInit = true;
                        }
                    };
                }        
            } catch(err) {
                LLVA.AVS.Wizard.handleAjaxError(err.message, 'print', this.print);
            }                 
        };
        
        if ((this.charts != 'NONE') && (document.getElementById('chartsFrame') != null)) {
            this.exportCharts(callback);
        } else {
            callback('');
        }                    
        
        return;
    },
    
    getElementsByClassName : function(node, classname) {
        var a = [];
        var re = new RegExp('(^| )'+classname+'( |$)');
        var els = node.getElementsByTagName("*");
        for(var i = 0, j=els.length; i < j; i++)
            if (re.test(els[i].className)) {
                a.push(els[i]);
            }
        return a;
    },    
    
    exportCharts : function(callback) {       
        var chartFilenames = '';    
        var chartsFrame = document.getElementById('chartsFrame');
        if (chartsFrame != null) {
            var innerDoc = chartsFrame.contentDocument || chartsFrame.contentWindow.document;  
            this.chartArray = this.getElementsByClassName(innerDoc.body, 'chart');
            if (this.chartArray.length > 0) {
                var iframe = chartsFrame.contentWindow || chartsFrame.contentDocument;
                var mask = new Ext.LoadMask(Ext.getBody(), {id: 'chartsMask', msg:"Exporting Charts..."});
                mask.show();    
                
                var wizard = this;
                function doExportChart(exportCallback) {
                    var chart = wizard.chartArray.shift();
                    innerDoc.getElementById(chart.id).scrollIntoView();                              
                    if (exportCallback) {
                        iframe.exportChart(chart.id, exportCallback);            
                    } else {
                        iframe.exportChart(chart.id); 
                    }
                }
                
                doExportChart(function lambda(numCharts, numExported, objRtn) {
                    if (objRtn) {
                        chartFilenames += objRtn.fileName + ',';
                    }                
                    if (numExported < numCharts) {
                        doExportChart(lambda);            
                    } else {
                        mask.hide();
                        callback(chartFilenames); 
                    }                
                });        
            } else {
                callback(chartFilenames);
            }
        } else {
            callback(chartFilenames);
        }        
    },    
    
    getFormattedComments : function() {
        var comments = '';
        var el = Ext.DomQuery.selectNode('#comments-div #comments-area');
        if (el) {
            comments = el.innerHTML;
            comments = Ext.String.trim(comments);     
            // remove zero-width space that htmleditor injects                         
            comments = comments.replace(/[\u200B-\u200D\uFEFF]/g, '');
            if (comments === 'None') {
                comments = '';
            }
        }  
        return comments;
    },
    
    fadingWindow : function(title, msg, delay) {
        if (!delay) {
            delay = 3000;
        }
        var win = new Ext.Window({
            id: 'win',
            title: title,
            html:'<p style="margin-top: 10px;font: 13px Tahoma,Verdana,Helvetica,sans-serif;text-align: center;">' + msg + '</p>',
            layout: {
                type: 'fit',
                align: 'center' 
            },
            height: 100,
            width: 250,
            closable: false                         
        });
        win.show();
        setTimeout(function() {
            win.close();
        }, delay);            
    }
    
});

