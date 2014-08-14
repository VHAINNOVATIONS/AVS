Ext.define('LLVA.AVS.Wizard.view.EncountersTree', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.encounterstree',    
    border: true,    
    id: 'encounters-tree',  
    data: undefined,
    selectedNode: undefined,
    store: Ext.data.StoreManager.lookup('Encounters'),          
    rootVisible: false,
    useArrows: true,
    animate: true,
    scroll: 'vertical',
    hidden: true,
    initialized: false,
    hideHeaders : true, 
    floating: true,
    mask: undefined,
    cls: 'no-leaf-icons',
    viewConfig: {
        style: { 
            overflow: 'auto', 
            overflowX: 'hidden' 
        }
    }, 
    frame: false,
    dockedItems: [{
        xtype: 'toolbar',
        items: [{
            xtype: 'label',
            html: 'Encounters (Last 60 Days)',
            cls: 'treepanel-title'   
        }]                             
    }],
    listeners: {
        itemclick: {
            fn: function(view, rec, item, index, event, opts) {
                if (rec.get('checked') != undefined) { 
                    rec.set('checked', !rec.get('checked'));
                }
                this.selectedNode = (this.getSelectionModel().getLastSelected()) ? this.getSelectionModel().getLastSelected() : this.root;
                if (this.selectedNode.parentNode.parentNode != null) {
                    var selParentNode = this.selectedNode.parentNode;
                    var checkedRecs = this.getChecked();
                    Ext.Array.each(checkedRecs, function(rec) {
                        if (rec.parentNode.get('id') != selParentNode.get('id')) {
                            rec.parentNode.eachChild(function(childRec) {
                                childRec.set('checked', false);
                            });   
                        }
                    });
                    this.fireEvent('selectedEncounters', this.getSelectedEncounters());                                                        
                }
            }
        }
    },      
    
    getSelectedEncounters: function() {
        var selEnc = [];
        var map = new Ext.util.HashMap();
        var sel = this.getChecked();
        if (sel.length > 0) {
          var i;
          for (i=0; i < sel.length; i++) {
              map.add(sel[i].get('id'), sel);
          }
        }
        Ext.each(this.data, function(parent, pIndex) {            
            Ext.each(parent.children, function(encounter, cIndex) {
                if (map.get(encounter.id) != null) {
                    selEnc.push(encounter);
                }
            });
        });
        
        return selEnc;        
    },    
        
    loadData: function(data) {
        // make a copy of the json object before appending to tree root
        this.data = Ext.JSON.decode(Ext.JSON.encode(data));
        this.store.getRootNode().appendChild(data);
    },
    
    expandFirstNode: function() {
        if (!this.initialized) {
            if (this.store.getRootNode().hasChildNodes()) {
                this.expandNode(this.store.getRootNode().getChildAt(0));
            }    
        }
        this.initialized = true;
    },

    getData: function() {
        return this.data;
    }    
});
