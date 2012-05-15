var mapWidth = 512;
var mapHeight = 320;
var mapType;
var center_lat = 0;
var center_lng = 0;
var marker_lat = 0;
var marker_lng = 0;
var setZoom = 3;

var map,marker;
function initMap(zoom)
{
	var mapOptions={zoom: 3,streetViewControl: false,scaleControl: true,mapTypeId: google.maps.MapTypeId.ROADMAP};
	map = new google.maps.Map($('#mapArea')[0], mapOptions);
	google.maps.event.addListener(map, 'maptypeid_changed', function(event) {
		mapType=map.getMapTypeId();
	});	
	google.maps.event.addListener(map, 'tilesloaded', function(event) {
		center_lat = map.getCenter().lat();
		center_lng = map.getCenter().lng();
		setZoom = map.getZoom();
	});		
	google.maps.event.addListener(map, 'center_changed', function(event) {
		center_lat = map.getCenter().lat();
		center_lng = map.getCenter().lng();
		setZoom = map.getZoom();
	});
	marker = new google.maps.Marker({map:map,draggable:true});
	google.maps.event.addListener(marker, 'dragend', function(event) {
		marker_lat = marker.getPosition().lat(); 
		marker_lng = marker.getPosition().lng(); 		
	});	
	searchMap();
}
function searchMap(zoom){
	var address = $('#address').val();
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode( { 'address': address}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			var tlatlng=results[0].geometry.location;
			if(zoom)map.setZoom(zoom);
			map.setCenter(tlatlng);
			marker.setPosition(tlatlng);
			marker.setTitle(address);
		}
		else alert(address + " 地址错误，未找到当前地址");
	});
}
function pasteMap()
{
	if (marker_lat == 0) marker_lat = center_lat;
	if (marker_lng == 0) marker_lng = center_lng;
	callback("http://maps.google.com/maps/api/staticmap?center=" + center_lat + ',' + center_lng + "&zoom=" + setZoom + "&size=" + mapWidth + 'x' + mapHeight + "&maptype=" + mapType + "&markers=" + marker_lat + ',' + marker_lng + "&sensor=false");
}
function pageInit()
{
	$('#address').keypress(function(ev){if(ev.which==13)searchMap(10);});
	$('#mapsearch').click(function(){searchMap(10);});
	$('#addMap').click(pasteMap);
}
$(pageInit);