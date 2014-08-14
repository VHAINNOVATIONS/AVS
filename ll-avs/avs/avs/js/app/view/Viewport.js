Ext.define('LLVA.AVS.Wizard.view.Viewport', {
    extend: 'Ext.container.Viewport',    
    alias : 'widget.wizardviewport',
    layout: 'fit',
    frame: false,
    items: [{
        id: 'frame',
        xtype: 'panel',
        layout: 'fit',
        frame: true,
        style: 'margin: 5px 5px 5px 5px;',
        tools: [{
            type:'help',
            tooltip: 'Get Help',
            handler: function(event, toolEl, panel) {
                window.open(LLVA.AVS.Wizard.getUrl('help'));
            }
        },{
            type:'gear',
            tooltip: 'Admin',
            handler: function(event, toolEl, panel) {            
                var url = Ext.String.format('{0}?stationNo={1}&userDuz={2}',
                              LLVA.AVS.Wizard.getUrl('admin'),
                              LLVA.AVS.Wizard.getParam('stationNo'),
                              LLVA.AVS.Wizard.getParam('userDuz'));                              
                window.open(url, '', 'width=890,height=650');  
            }
        
        }],
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
