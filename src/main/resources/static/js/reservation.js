/*start Set place ID*/
const urlParams = new URLSearchParams(window.location.search);
const placeID = urlParams.get('place_id');
if(placeID != null && placeID != 0){
	document.getElementById("placeId").value=placeID;
}
/*end Set place ID*/