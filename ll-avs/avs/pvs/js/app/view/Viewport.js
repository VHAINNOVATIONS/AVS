Ext.define('LLVA.PVS.view.Viewport', {
    extend: 'Ext.container.Viewport',    
    alias : 'widget.viewport',
    layout: 'fit',
    frame: false,
    items: [{
        id: 'frame',
        xtype: 'panel',
        layout: 'fit',
        frame: true,
        style: 'margin: 5px 5px 5px 5px;',
        items: [{
            id: 'preview',
            xtype: 'panel',
            layout: 'fit',
            frame: false,
            html: '',
            autoScroll: true
        }]
    }]
});
