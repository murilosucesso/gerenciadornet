Array.prototype.reduce = undefined;
google.load('visualization', '1', {packages: ['corechart']});
	      
function drawVisualization() {
    // Create and populate the data table.
    var data = new google.visualization.DataTable();
    var raw_data = [['Vendido', 1336060, 1538156, 1576579, 1600652, 1968113, 1901067],
                      ['Lucro', 3817614, 3968305, 4063225, 4604684, 4013653, 6792087]];
    
    var years = ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'];
                    
    data.addColumn('string', 'Mes');
    for (var i = 0; i  < raw_data.length; ++i) {
      data.addColumn('number', raw_data[i][0]);    
    }
    data.addRows(years.length);
  
    for (var j = 0; j < years.length; ++j) {    
      data.setValue(j, 0, years[j].toString());    
    }
    for (var i = 0; i  < raw_data.length; ++i) {
      for (var j = 1; j  < raw_data[i].length; ++j) {
        data.setValue(j-1, i+1, raw_data[i][j]);    
      }
    }
    
    // Create and draw the visualization.
    new google.visualization.ColumnChart(document.getElementById('visualization')).
        draw(data,
             {title:"Relatorio Venda",
              width:600, height:400,
              hAxis: {title: "Mes"}}
        );
  }
google.setOnLoadCallback(drawVisualization);