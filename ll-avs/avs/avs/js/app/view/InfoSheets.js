Ext.define('LLVA.AVS.Wizard.view.InfoSheets', {
    extend: 'Ext.window.Window',
    alias: 'widget.infosheets',        
    id: 'infosheets',  
    width: 900,
    height: 600,    
    title: 'Krames StayWell HealthSheets',
    modal: false,    
    shim : false,
    frame : false,
    collapsible: true,
    closeAction: 'close',
    searchResultsStore: undefined,   
    contentIframe: undefined,
    printIframe: undefined,
    numSearchResults: 0,
    contentTitle: undefined,
    selMenuContext: undefined,
    isCbSelection: false,
    savedSelection: undefined,
    diagnosisCodes: '',
    
    initComponent: function() {
        var infoSheets = this;
        
        var renderResult = function(value, p, record) {
            return Ext.String.format(
                '<b>{0}</b><br/>Language: {1}',
                record.data.title,
                record.data.language
            );
        };        
        this.searchResultsStore = Ext.create('LLVA.AVS.Wizard.store.SearchResults');  
        this.contentIframe = Ext.create('LLVA.AVS.Wizard.view.IFrame', {   
            id: 'contentIframe',        
            src: LLVA.AVS.Wizard.getUrl('tips'),
            width: '100%',
            height: '100%'
        });
        this.printIframe = Ext.create('LLVA.AVS.Wizard.view.IFrame', {   
            id: 'printIframe',
            src: ''
        });        
        var cbSm = Ext.create('Ext.selection.CheckboxModel', {
            checkOnly: true,
            mode: 'MULTI'
        });
        Ext.apply(this, {                   
            layout: {
                align: 'stretchmax',
                type: 'hbox'
            },
            unselectable: 'on',
            items: [
                {
                    xtype: 'panel',
                    height: 566,
                    width: 262,
                    id: 'SearchResultsPanel',                    
                    layout: 'fit',
                    title: 'Search Results',
                    frame: false,
                    dockedItems: [{
                        xtype: 'toolbar',
                        id: 'SearchResultsTopToolbar',
                        dock: 'top',
                        items: [{ 
                                xtype: 'button',
                                id: 'NewSearchBtn',
                                text: 'New Search...',
                                icon: 'images/search.png',
                                scope: infoSheets,
                                handler: function() {
                                    this.fireEvent('newSearch');
                                }
                            },{ 
                                xtype: 'tbseparator'
                            },{ 
                                xtype: 'button',
                                id: 'PrintSelectionsBtn',
                                text: 'Print Selected Articles',
                                icon: 'images/folderoptions.png',
                                scope: infoSheets,
                                disabled: true,
                                handler: function() {
                                    this.printSelectedDocs();
                                }
                            }]
                    },{
                        xtype: 'toolbar',
                        id: 'SearchResultsToolbar',
                        dock: 'bottom',
                        items: [{
                                xtype: 'label',
                                id: 'StatusLabel',
                                html: ''
                            }]                       
                    }],
                    items: [
                        { 
                            xtype: 'grid',
                            id: 'SearchResultsGridPanel',
                            store: this.searchResultsStore,
                            selModel: cbSm,
                            viewConfig: {
                                id: 'gv',
                                trackOver: true,
                                stripeRows: false,
                                plugins: [{
                                    ptype: 'preview',
                                    bodyField: 'blurb',
                                    expanded: true,
                                    pluginId: 'preview'
                                }]
                            },      
                            frame: false,
                            hideHeaders: true,
                            loadMask: true,   
                            columns:  [{
                                id: 'result',
                                text: 'Search Results',
                                dataIndex: 'title',
                                flex: 1,
                                sortable: false,
                                renderer: renderResult
                            }],                            
                            listeners: {
                                scope: this, 
                                itemclick: function(view, rec, item, index) {
                                    if (!this.isCbSelection) {
                                        this.getContent(rec.get('contentTypeId'),
                                                        rec.get('contentId'),
                                                        rec.get('title'));
                                    }
                                    this.isCbSelection = false;
                                },
                                selectionchange: function(sm, sel, opts) { 
                                    this.isCbSelection = true;
                                    Ext.getCmp('PrintSelectionsBtn').setDisabled(sel.length == 0);
                                }
                            }     
                        }
                    ]
                },                                
                {
                    xtype: 'panel',
                    height: 566,
                    width: 630,
                    id: 'ContentsPanel',                    
                    layout: {
                        type: 'anchor'
                    },
                    autoScroll: true,
                    title: 'Content',
                    selection: undefined,
                    dockedItems: [{
                        xtype: 'toolbar',
                        id: 'ContentsToolbar',
                        dock: 'top',
                        items: [
                            { xtype: 'tbspacer' },
                            {
                                xtype: 'label',
                                html: '<span style="padding-bottom: 7px;">Print:</span>'
                            },
                            { xtype: 'tbspacer' },
                            {
                                xtype: 'button',
                                id: 'PrintContentBtn',
                                itemId: 'printContent',
                                text: 'Document',
                                icon: 'images/printer.png',
                                disabled: true,
                                scope: this,                             
                                handler: function() {
                                    this.printContent();        
                                }
                            },{
                                xtype: 'button',
                                id: 'PrintSelectionBtn',
                                itemId: 'printSelection',
                                text: 'Selection',
                                icon: 'images/printer.png',
                                disabled: true,
                                scope: this,                     
                                handler: function() {
                                    this.printContentSel();        
                                }
                            },
                            { xtype: 'tbspacer' },
                            { xtype: 'tbspacer' },
                            { xtype: 'tbseparator' },
                            { xtype: 'tbspacer' },
                            { xtype: 'tbspacer' },
                            { xtype: 'tbspacer' },
                            {
                                xtype: 'label',
                                html: '<span style="padding-bottom: 7px;">Patient Instructions:</span>'
                            },
                            { xtype: 'tbspacer' },
                            {
                                id: 'InsertContentBtn',
                                itemId: 'insertContent',
                                xtype:'button',
                                icon: 'images/note_go.png',
                                text: 'Insert Document',
                                disabled: true,
                                scope: this,
                                handler: function() {
                                    this.insertContent();        
                                }      
                            },{
                                id: 'InsertSelectionBtn',
                                itemId: 'insertSelection',
                                xtype:'button',
                                text: 'Insert Selection',
                                icon: 'images/note_go.png',
                                disabled: true,
                                scope: this,
                                handler: function() {
                                    this.fireEvent('insertSel', this.getSelectedContent());      
                                }            
                            },                      
                            '->',
                            {
                                xtype: 'button',
                                id: 'CloseBtn',
                                itemId: 'closeBtn',
                                text: 'Close',
                                icon: 'images/close.png',
                                disabled: false,
                                scope: this,
                                handler: function() {
                                    this.close();    
                                }
                            }                        ]
                    }],
                    items : [this.contentIframe]
                }
            ],
            listeners: {
                render: {
                    fn: this.onRenderHandler  
                }, 
                show: {
                    fn: this.onShowHandler
                },
                beforeclose: {
                    fn: this.onCloseHandler
                }               
            }          
        });

        this.callParent(arguments);
    },
    
    onRenderHandler: function(cmp, eOpts) {
    },
    
    onShowHandler: function() {
        if (this.numSearchResults == 0) {
            this.doSearch();
        }
    },
    
    onCloseHandler: function() {
        APP_GLOBAL.getController('Wizard').startTimer();
    },
    
    setDiagnosisCodes: function(diagnosisCodes) {
        this.diagnosisCodes = diagnosisCodes;
    },
    
    doSearch: function(keywords, logicalOperator, meshCodes, icdCodes, cptCodes, language) {
        this.setLoading('<b>Searching Krames...</b>');
                
        if ((keywords == undefined || keywords == '') &&
           (meshCodes == undefined || meshCodes == '') &&
           (icdCodes == undefined || icdCodes == '') && 
           (cptCodes == undefined || cptCodes == '')) {
            icdCodes = this.diagnosisCodes;
        }
        
        this.searchResultsStore.load({
            scope : this,
            url: LLVA.AVS.Wizard.getUrl('kramesSearch'),
            callback : function(records, operation, success) {
                if (success) {
                  this.numSearchResults = records.length;
                  this.setStatus('# Results: ' + records.length);
                } else {
                  this.numSearchResults = 0;
                  this.setStatus('No results returned.');                
                  this.fireEvent('newSearch');
                }
            },            
            params: {
                stationNo: LLVA.AVS.Wizard.getParam('stationNo'),
                userDuz: LLVA.AVS.Wizard.getParam('userDuz'),
                patientDfn: LLVA.AVS.Wizard.getParam('demographics').get('dfn'),
                datetimes: LLVA.AVS.Wizard.getParam('datetimes'),
                locationIens: LLVA.AVS.Wizard.getParam('locationIen'),
                fullText: keywords,
                logicalOperator: logicalOperator,
                meshCodes: meshCodes,
                icd9Codes: icdCodes,
                cptCodes: cptCodes,
                language: language
            }
        });
    
    },
        
    getContent: function(contentTypeId, contentId, title) {
        this.contentTitle = title;
        var url = LLVA.AVS.Wizard.getUrl('kramesContent') + 
                    '?contentTypeId=' + contentTypeId + 
                    '&contentId=' + contentId +
                    '&documentType=html';
        this.contentIframe.load(url); 
        this.contentIframe.setContentEditable(true);
        Ext.getCmp('PrintContentBtn').setDisabled(false);
        Ext.getCmp('PrintSelectionBtn').setDisabled(false);
        Ext.getCmp('InsertContentBtn').setDisabled(false);
        Ext.getCmp('InsertSelectionBtn').setDisabled(false);
    },
            
    printContent: function() {
        this.contentIframe.focus();
        if (Ext.isIE) {            
            this.contentIframe.getDoc().execCommand('print', false, null);
        } else {
            this.contentIframe.getWin().print();
        }
        this.fireEvent('insertKramesTitles', this.contentTitle);
    },        
    
    printSelectedDocs: function() {
        var s = Ext.getCmp('SearchResultsGridPanel').getSelectionModel().getSelection();
        var  contentTitles = '';
        var contentTypeIds = '';
        var contentIds = '';
        Ext.each(s, function (item) {
            contentTitles += item.data.title + '^';
            contentTypeIds += item.data.contentTypeId + ',';
            contentIds += item.data.contentId + ',';
        });    
        contentTitles = contentTitles.substring(0, contentTitles.length-1);
        contentTypeIds = contentTypeIds.substring(0, contentTypeIds.length-1);
        contentIds = contentIds.substring(0, contentIds.length-1);
        var url = LLVA.AVS.Wizard.getUrl('kramesContent') + 
          '?contentTypeId=' + contentTypeIds + '&contentId=' + contentIds + '&documentType=pdf';
        this.fireEvent('printDocuments', url);  
        this.fireEvent('insertKramesTitles', contentTitles);        
    },
        
    setLoading: function(text) {
        Ext.getCmp('StatusLabel').update('<img src="images/loader.gif" align="top" />&nbsp;&nbsp;&nbsp;' + text);
    },
    
    setStatus: function(status) {
        Ext.getCmp('StatusLabel').update(status);
    },
        
    getSelectedContent: function() {
    
        if (typeof console === "undefined") { 
            console = { 
                log: function () { }, 
                info: function () { }, 
                warn: function () { }, 
                error: function () { } 
            }; 
        }    
    
        try {       
            var content = '';        
            if (!Ext.isIE && this.contentIframe.getWin().getSelection) {
                var sel = this.contentIframe.getWin().getSelection().getRangeAt(0);
                var nodes = sel.cloneContents();
                content = this.serializeDoc(nodes); 
            } else if (this.contentIframe.getDoc().selection) {
                var range = this.contentIframe.getDoc().selection.createRange();            
                content = range.htmlText;
            }     
        } catch(e) {
        }       
        // filter out undesired control characters
        return content.replace(/[^\w\s\.~`!@#\$%\^&\*\(\)\-_\+=\[\]\{}'";:\?/,<>]/gi, " ");
    },
    
    insertContent: function() {
        var doc = this.contentIframe.getWin().document;
        // just grab everything inside the body tags
        var selection = doc.getElementsByTagName('body')[0].innerHTML;
        this.fireEvent('insertSel', selection);
    },
    
    printContentSel: function() {
        var selection = this.getSelectedContent();                             
        if ((selection == null) || Ext.String.trim(selection).length == 0) {                                    
            alert('No content selected.');
            return;
        }
        var html = "<html><head><title>" + this.title + "</title>" +
               "<style type='text/css' media='print'>\n" +
               "@page port {size: portrait;}" +
               "@page land {size: landscape;}" +
               ".portrait {page: port;}" +
               ".landscape {page: land;}" +
               "</style>" +
               "<script type='text/javascript'>" + 
               "function setTimer() { setTimeout('doPrint()', 10); } " + 
               "function doPrint() { window.print(); window.close(); } " +
               "</scr" + "ipt></head>\n" +
               "<body style='padding-left:20px;font-family:arial,helvetica,sans-serif;font-size: 12pt;' " +
               "onload='javascript:setTimer();'>" +
               "<h2 style='color:#000000;font-size: 14px;margin-bottom: 10px;'>" + this.contentTitle + "</h2><br>" +
               selection + "\n" +
               "</body></html>";
        var childWin = window.open("about:blank","childWin","resizable=1, scrollbars=1, toolbar=0, statusbar=0, " + 
                                                 "width=800,height=600,location=no,menubar=no,toolbar=no,status=no,directories=no");
        childWin.document.open();
        childWin.document.write(html);
        childWin.document.close();
    },
        
    serializeDoc: function(doc) {
        if (window.XMLSerializer) { // all browsers, except IE before version 9
            var serializer = new XMLSerializer();
            // the serializeToString method raises an exception in IE9
            try {
                return serializer.serializeToString(doc);
            } catch (e) {
                return doc.xml;
            }
        } else {                                                        
           return doc.xml;
        }    
    }
            
});