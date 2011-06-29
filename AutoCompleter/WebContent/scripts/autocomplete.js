var isIE;
var completeTable;
var completeField;
var autorow;
var counties;

// the request initializer function called on body onload...
function initRequest() {
	completeField = document.getElementById("complete-field");
	counties = document.getElementById("counties");
	var menu = document.getElementById("auto-row");
	autorow = document.getElementById("menu-popup");
	autorow.style.top = (findPos(completeField)[1] + 22) + "px";
	autorow.style.left = findPos(completeField)[0] + "px";
	completeTable = document.getElementById("completeTable");
	completeTable.setAttribute("bordercolor", "white");
}

function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	return [ curleft, curtop ];
}

function getElementY(element) {
	var targetTop = 0;
	if (element.offsetParent) {
		while (element.offsetParent) {
			targetTop += element.offsetTop;
			element = element.offsetParent;
		}
	} else if (element.y) {
		targetTop += element.y;
	}
	return targetTop;
}

function doCompletion() {
	if (trim(completeField.value)== "") {
		clearTable();
	} else {
		var url = "autocomplete?type=completion&name=" + escape(encodeURI(completeField.value));
		establishAJAXInteraction(url, "completion");
	}
}

function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}

function establishAJAXInteraction(url, type) {
	// get xml http request object.
	req = getXmlHttpObject();
	// set async state change method...
	if(type == "completion")
		req.onreadystatechange = processRequest;
	else {
		req.onreadystatechange = processFillRequest;
	}
	// open the request to given url...
	req.open("GET", url, true);
	req.send(null);
}

function getXmlHttpObject() {
	var xmlHttp = null;
	try {
		// Internet Explorer
		isIE = true;
		xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
		// Firefox, Opera 8.0+ or Safari
		try {
			isIE = false;
			xmlHttp = new XMLHttpRequest();
		} catch (e) {
			isIE = true;
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	return xmlHttp;
}

function processRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			postProcess(req.responseXML);
		}
	}
}

function processFillRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			postFillProcess(req.responseXML);
		}
	}
}

function postFillProcess(responseXML){
	var cities = responseXML.getElementsByTagName("cities")[0];
	for (loop = 0; loop < cities.childNodes.length; loop++) {
		var city = cities.childNodes[loop];
		//get the counties node list...
		var countiesNodeList = city.childNodes;
		for(k = 0; k < countiesNodeList.length; k++){
			appendToCountiesCombobox(countiesNodeList[k].childNodes[0].nodeValue);
		}
	}
}

function appendToCountiesCombobox(countyName) {
	//create an option and append county name into it...
	var option = document.createElement("option");
	option.appendChild(document.createTextNode(countyName));
	
	//add created option to counties select...
	counties.appendChild(option);
}

function postProcess(responseXML) {
	clearTable();

	var cities = responseXML.getElementsByTagName("cities")[0];
	if (cities.childNodes.length > 0) {
//		completeTable.setAttribute("bordercolor", "black");
//		completeTable.setAttribute("border", "1px");
	} else {
		clearTable();
	}
	for (loop = 0; loop < cities.childNodes.length; loop++) {
		var city = cities.childNodes[loop];
		var cityName = city.getAttribute("name");
//		var firstName = city.getElementsByTagName("firstName")[0];
		appendCity(cityName);
	}
}

function appendCity(cityName) {
	var row;
	var nameCell;
	if (isIE) {
		row = completeTable.insertRow(completeTable.rows.length);
		nameCell = row.insertCell(0);
	} else {
		row = document.createElement("tr");
		nameCell = document.createElement("td");
		row.appendChild(nameCell);
		completeTable.appendChild(row);
	}
	row.className = "popupRow";
	nameCell.setAttribute("color", "#CCFFFF;");
	nameCell.setAttribute("width", "100%");

	var linkElement = document.createElement("a");
	linkElement.className = "popupItem";
	linkElement.setAttribute("href", "javascript:enter(' " + cityName + "')");
	linkElement.appendChild(document.createTextNode(cityName));
	nameCell.appendChild(linkElement);
}

function enter(name) {
	//document.getElementById("autofillform").id.value = name;
	completeField.value = name;
	clearTable();
	clearCombobox();
	
	//create a new ajax request to get country names according to selected city...
	//create the url
	var url = "autocomplete?type=fill&name=" + escape(encodeURI(name));
	establishAJAXInteraction(url, "fill");
}

function clearTable() {
	if (completeTable) {
		completeTable.setAttribute("bordercolor", "white");
		completeTable.setAttribute("border", "0");
		completeTable.style.visible = false;
		for (loop = completeTable.childNodes.length - 1; loop >= 0; loop--) {
			completeTable.removeChild(completeTable.childNodes[loop]);
		}
	}
}

function clearCombobox(){
	if(counties){
		for (loop = counties.childNodes.length - 1; loop >= 0; loop--) {
			counties.removeChild(counties.childNodes[loop]);
		}
	}
}
