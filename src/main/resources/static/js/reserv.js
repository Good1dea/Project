/*start Set place ID*/
const urlParams = new URLSearchParams(window.location.search);
const placeID = urlParams.get('place_id');
if(placeID != null && placeID != 0){
	document.getElementById("parking_place").value=placeID;
}
/*end Set place ID*/