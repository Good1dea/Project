let i = 0;
let price = 0;
let currPos = 0;
let elements = document.getElementsByName("place");
while (i < elements.length) {
    let reserved = elements[i].dataset.reserve;
    if(reserved == "true"){
        elements[i].style.backgroundColor = "red";
    }
    i++;
}

function clickHandler(id)
{
    let color = document.getElementById(id).style.backgroundColor;
    let pos = document.getElementById(id).getAttribute("id");
    price = document.getElementById(id).dataset.price;

    if(color === "red"){
    	return;
    }

    if(currPos != 0){
        document.getElementById(currPos.toString()).style.backgroundColor = "green";
    	document.getElementById(currPos.toString()).innerHTML = currPos.toString() + "\nFree";
    }
    if(color === "yellow"){
        price = 0;
    	document.getElementById(id).style.backgroundColor = "green";
    	document.getElementById(id).innerHTML = pos + "\nFree";

    	document.getElementById("id").value = "";
        document.getElementById("pricePerDay").value = "";
    }
    else{
        document.getElementById("id").value = pos.toString();
        document.getElementById("pricePerDay").value = price.toString();

    	document.getElementById(id).style.backgroundColor = "yellow";
    	document.getElementById(id).innerHTML = pos + "\nOrder";
    }

    if(currPos != Number(pos)){
        currPos = Number(pos);
    } else {
        currPos = 0;
    }
}
