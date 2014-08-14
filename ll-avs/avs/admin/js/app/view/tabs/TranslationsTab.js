Ext.define('LLVA.AVS.Admin.view.tabs.TranslationsTab', {
    extend: 'Ext.grid.Panel',
    alias : 'widget.translationstab',
    
    title: 'Translations',
    id: 'translations-grid',
    
    disabled: true,    
    frame: false,
    
    store: 'Translations',
    disableSelection: true,    
    invalidateScrollerOnRefresh: false,    
    
    verticalScroller: {
        xtype: 'paginggridscroller'
    },
    
    viewConfig: {
        stripeRows: true,
        width: '100%',
        height: '93%'
    },
    
    columnLines: true,

    columns: [{
        header: 'ID',
        dataIndex: 'id',
        width: 50
    }, {
        header: 'Type',
        dataIndex: 'type',
        width: 130
    }, {
        header: 'Source String',
        dataIndex: 'source',
        flex: 0.5
    }, {
        header: 'Translation (Click a cell to edit it)',
        dataIndex: 'translation',
        flex: 1,
        editor: {
            xtype: 'textfield',
            allowBlank: true
        }
    }],
    selType: 'cellmodel',
    tbar: {
        margin: '0 0 3 0',
        ui: 'footer',
        items: [{
            xtype: 'tbtext', 
            style:'font-weight: bold;font-size: 12px;',
            text: 'Patient-Friendly Translations'
        }, 
        '->', 
        {
            xtype: 'tbtext',
            text: 'Search: '
        }, {
            xtype: 'textfield',
            width: 100,
            id: 'search-text'
        }, {
            text: 'Search',
            icon: 'images/magnifier.png',
            action: 'search'
        }, {
            text: 'Clear',
            action: 'reset'
        }]
    },
    plugins: [{
        ptype: 'cellediting',
        clicksToEdit: 1
    }]
});
