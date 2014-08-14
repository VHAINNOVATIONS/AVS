
function tomorrow(){
  var x = new Date();
  x.setDate(x.getDate() + 1);
  return x;
}

function nextYear(){
  var x = new Date();
  x.setDate(x.getDate() + 365);
  return x;
}

function addDay(d){
  var x = new Date();
  x.setDate(d.getDate() + 1);
  return x;
}

function sleep(delay){
  var start = new Date().getTime();
  while (new Date().getTime() < start + delay);
}

String.prototype.capitalize = function(){
  return this.replace( /(^|\s)([a-z])/g , function(m,p1,p2){ return p1+p2.toUpperCase(); } );
 };