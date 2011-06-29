/**
 * The Canvas Artist paint-like web tool js source file.
 */

/**
 * The initialization function when triggers window is loaded.
 */
function init() {

	// first, decorate the page...
	decorate();

//	ctx = document.getElementById("canvas").getContext("2d");
//	ctx.fillStyle = "rgb(250,0,0)";
//	ctx.save();
//	ctx.translate(75, 75);
//	ctx.rotate(-Math.PI / 6);
//	ctx.translate(-75, -75);
//	ctx.fillRect(75, 75, 50, 50);
//	ctx.restore();
//	ctx.fillStyle = "rgb(0,0,250)";
//	ctx.fillRect(75, 75, 5, 5);
}

function decorate(){
	// get the center element...
	var centerElement = document.getElementById("centerElement");

	//decorate image selector according to browser type...
	decorateImageSelector(centerElement);
	
	decorateCanvas(centerElement);
	
//	decorateTools(centerElement);
}

function decorateImageSelector(rootElement){
	// decorate the IE browser...
	if(isIE()){
		// create the file input element...
		var fileInput = document.createElement("input");
		fileInput.setAttribute("id", "imageFile");
		fileInput.setAttribute("type", "file");
		fileInput.setAttribute("accept", "image/gif,image/png,image/jpg,image/jpeg");
		
		//create the image submitter element...
		var submitInput = document.createElement("input");
		submitInput.setAttribute("type", "submit");
		submitInput.setAttribute("value", "Draw on Canvas");
		
		// create the form element...
		var imgUploadForm = document.createElement("form");
		imgUploadForm.setAttribute("action", "javascript:handleImage(" + fileInput.value + ");");
		imgUploadForm.setAttribute("method", "post");
		imgUploadForm.setAttribute("enctype", "multipart/form-data");
		
		//append input types to form element.
		imgUploadForm.appendChild(fileInput);
		imgUploadForm.appendChild(submitInput);
		
		rootElement.appendChild(imgUploadForm);
	}else{
		//create a label, text input and button to add selected image...
		var imgSrcLabel = document.createElement("label");
		imgSrcLabel.setAttribute("for", "imgSrcInput");
		//create label's text node...
		imgSrcLabel.appendChild(document.createTextNode("Enter the image (Absolute) path: "));
		
		var imgSrcInput = document.createElement("input");
		imgSrcInput.setAttribute("type", "text");
		imgSrcInput.setAttribute("id", "imgSrcInput");
		
		var submitInput = document.createElement("input");
		submitInput.setAttribute("type", "submit");
		submitInput.setAttribute("value", "Draw on Canvas");
		submitInput.onclick = function() {handleImage(imgSrcInput.value)};

		//append created components to root element...
		rootElement.appendChild(imgSrcLabel);
		rootElement.appendChild(imgSrcInput);
		rootElement.appendChild(submitInput);
	}
}

function decorateCanvas(rootElement){
	//create the root canvas...
	var rootCanvas = document.createElement("canvas");
	rootCanvas.setAttribute("id", "canvas");
	rootCanvas.setAttribute("width", "800");
	rootCanvas.setAttribute("height", "600");
	//rootCanvas.setAttribute("class", "clear");
	rootCanvas.setAttribute("style", "border:1px dotted;float:center");
	
	//add root canvas to its root element...
	rootElement.appendChild(rootCanvas);
}

/**
 * Determines whether client-side browser is IE or not.
 * 
 * @returns {Boolean} the IE detector.
 */
function isIE() {
	// test for MSIE x.x;
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
		return true;
	} else {
		return false;
	}
}

function handleImage(imageSource) {
	var ctx = document.getElementById("canvas").getContext("2d");
	var newImage = new Image();
	newImage.src = imageSource;
	ctx.drawImage(newImage, 150, 150, 100, 100);
}