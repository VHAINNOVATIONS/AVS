var charts = [], numCharts = 0, numExported = 0, exportCallback;

function FC_Exported(objRtn) { 
    if (objRtn.statusCode == '1') {
        numExported++;      
        exportCallback(numCharts, numExported, objRtn); 
    } else { 
        alert('Chart ' + objRtn.DOMId + ' could not be saved on server.'); 
        exportCallback(0, 0, objRtn); 
    } 
} 

var fsCallback = function(chart, divId) { 
    numCharts++; 
    charts[divId] = chart.id; 
};

function exportChart(divId, cb) { 
    exportCallback = cb; 
    FusionCharts(charts[divId]).exportChart({ exportFormat: 'JPG' });
}