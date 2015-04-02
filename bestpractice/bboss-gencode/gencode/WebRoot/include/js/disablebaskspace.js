$(document).ready(function(){
	
	document.onkeydown =function(e){
		
	
		
		var event = e || window.event;
		
		if(event.keyCode==8){
		
				
				var elem = event.target || event.srcElement;
				
				
				var name = elem.nodeName;
				
				if(name!='INPUT' && name!='TEXTAREA'){
					return _stopIt(event);
				}
				var type_e = elem.type.toUpperCase();
				if(name=='INPUT' && (type_e!='TEXT' && type_e!='TEXTAREA' && type_e!='PASSWORD' && type_e!='FILE')){
						return _stopIt(event);
				}
				if(name=='INPUT' && (elem.readOnly==true || elem.disabled ==true)){
						return _stopIt(event);
				}
			}
		}
	}
)
function _stopIt(e){
		if(e.returnValue){
			e.returnValue = false ;
		}
		if(e.preventDefault ){
			e.preventDefault();
		}				

		return false;
}