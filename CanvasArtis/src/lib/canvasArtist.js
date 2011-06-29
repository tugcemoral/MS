/**
 * The Canvas Artist paint-like web tool javascript source file.
 */

// The active tool instance.
var tool;
var tool_default = 'pencil';
// This object holds the implementation of each drawing tool.
var tools = {};

var currentSize = 3;
var size_default = "normal";

// the real canvas and its 2D context.
var canvas, context;

// temporaty canvas and its 2D context to store draw
var tmpCanvas, tmpContext;

var isMoveTool = false;

/**
 * The initialization function when triggers window is loaded.
 */
function init() {
	// first, decorate the page...
	decorate();
	constructTools();
	addListeners();
}

function decorate(){
	
	// get the canvas and its context.
	canvas = document.getElementById("rootCanvas");
	context = canvas.getContext("2d");
	
    // Add the temporary canvases.
    var container = canvas.parentNode;
    tmpCanvas = document.createElement("canvas");
    if (!tmpCanvas) {
      alert('Error: I cannot create a new canvas element!');
      return;
    }

    tmpCanvas.id = "imageTemp";
    tmpCanvas.width = canvas.width;
    tmpCanvas.height = canvas.height;
    container.appendChild(tmpCanvas);

    tmpContext = tmpCanvas.getContext("2d");
    
	// decorate image selector according to browser type...
	decorateImageSelector();
}

function constructTools(){

	// initialize the color picker...
	$("#colorPicker").colorPicker();
	
	  // The drawing pencil.
	  tools.pencil = function () {
	    var tool = this;
	    this.started = false;

	    // This is called when you start holding down the mouse button.
	    // This starts the pencil drawing.
	    this.mousedown = function (ev) {
	    	if(!isMoveTool){
		    	tmpContext.strokeStyle = $("#colorPicker").val();
		    	tmpContext.beginPath();
		    	tmpContext.moveTo(ev._x, ev._y);
		        tool.started = true;
	    	}
	    };

	    // This function is called every time you move the mouse. Obviously, it
		// only draws if the tool.started state is set to true (when you are
		// holding down the mouse button).
	    this.mousemove = function (ev) {
	      if (tool.started
	    		  && !isMoveTool) {
	    	  // set the line width size...
	    	  tmpContext.lineWidth = currentSize;
	    	  tmpContext.lineTo(ev._x, ev._y);
	    	  tmpContext.stroke();
	      }
	    };

	    // This is called when you release the mouse button.
	    this.mouseup = function (ev) {
	      if (tool.started
	    		  && !isMoveTool) {
	        tool.mousemove(ev);
	        tool.started = false;
	        img_update();
	      }
	    };
	  };
	  
	  // The circle tool.
	  tools.circle = function(){
		
		  var tool = this;
		  this.started = false;
		  
		  this.mousedown = function(ev){
			  if(!isMoveTool){
				  tmpContext.strokeStyle = $("#colorPicker").val();
			      tool.started = true;
			      tool.x0 = ev._x;
			      tool.y0 = ev._y;
			  }
		  };
		  
		  this.mousemove = function(ev){
		      if (!tool.started || isMoveTool) {
			        return;
		      }
		      	
		      var centerX = Math.max(tool.x0, ev._x) - Math.abs(tool.x0 - ev._x) / 2;
		      var centerY = Math.max(tool.y0, ev._y) - Math.abs(tool.y0 - ev._y) / 2;
		      var distance = Math.sqrt(Math.pow(tool.x0 - ev._x, 2) + Math.pow(tool.y0 - ev._y, 2));
		      
		      tmpContext.lineWidth = currentSize;
		      tmpContext.beginPath();
		      // context.moveTo(centerX, centerY);
		      tmpContext.arc(tool.x0, tool.y0, distance, 0, Math.PI*2, false);
		      if($("#shapeFiller").is(":checked")){
		    	  tmpContext.fillStyle = $("#colorPicker").val();
		    	  tmpContext.fill();
		      } else {
		    	  tmpContext.strokeRect();
		      }
		  };
		  
		  this.mouseup = function(ev){
		      if (tool.started && !isMoveTool) {
			        tool.mousemove(ev);
			        tool.started = false;
			        img_update();
			        tmpContext.stroke();
			  }
		  };
		  
	  };

	  // The rectangle tool.
	  tools.rect = function () {
	    var tool = this;
	    this.started = false;

	    this.mousedown = function (ev) {
	    	if(!isMoveTool){
		      tmpContext.strokeStyle = $("#colorPicker").val();
		      tool.started = true;
		      tool.x0 = ev._x;
		      tool.y0 = ev._y;
	    	}
	    };

	    this.mousemove = function (ev) {
	      if (!tool.started || isMoveTool) {
	        return;
	      }

	      var x = Math.min(ev._x,  tool.x0),
	          y = Math.min(ev._y,  tool.y0),
	          w = Math.abs(ev._x - tool.x0),
	          h = Math.abs(ev._y - tool.y0);

	      tmpContext.clearRect(0, 0, tmpCanvas.width, tmpCanvas.height);

	      if (!w || !h) {
	        return;
	      }

	      tmpContext.lineWidth = currentSize;
	      if($("#shapeFiller").is(":checked")){
	    	  tmpContext.fillStyle = $("#colorPicker").val();
	    	  tmpContext.fillRect(x, y, w, h);
	      }else {
	    	  tmpContext.strokeRect(x, y, w, h);
	      }
	    };

	    this.mouseup = function (ev) {
	      if (tool.started && !isMoveTool) {
	        tool.mousemove(ev);
	        tool.started = false;
	        img_update();
	      }
	    };
	  };

	  // The line tool.
	  tools.line = function () {
	    var tool = this;
	    this.started = false;

	    this.mousedown = function (ev) {
	    	if(!isMoveTool){
		      tmpContext.strokeStyle = $("#colorPicker").val();
		      tool.started = true;
		      tool.x0 = ev._x;
		      tool.y0 = ev._y;
	    	}
	    };

	    this.mousemove = function (ev) {
    	  if (!tool.started || isMoveTool) {
    		  return;
    	  }

	      tmpContext.clearRect(0, 0, tmpCanvas.width, tmpCanvas.height);

	      // set the size of line.
	      tmpContext.lineWidth = currentSize;
	      tmpContext.beginPath();
	      tmpContext.moveTo(tool.x0, tool.y0);
	      tmpContext.lineTo(ev._x,   ev._y);
	      tmpContext.stroke();
	      tmpContext.closePath();
	    };

	    this.mouseup = function (ev) {
	      if (tool.started && !isMoveTool) {
	        tool.mousemove(ev);
	        tool.started = false;
	        img_update();
	      }
	    };
	  };
	  
	  // The eraser pencil
	  tools.eraser = function () {
	    var tool = this;
	    this.started = false;

	    // This is called when you start holding down the mouse button.
	    // This starts the pencil drawing.
	    this.mousedown = function (ev) {
	    	if(!isMoveTool){
		    	// set stroke style as white, eraser.
		    	tmpContext.strokeStyle = "#FFFFFF";
		    	tmpContext.beginPath();
		    	tmpContext.moveTo(ev._x, ev._y);
		        tool.started = true;
	    	}
	    };

	    // This function is called every time you move the mouse. Obviously, it
		// only draws if the tool.started state is set to true (when you are
		// holding down the mouse button).
	    this.mousemove = function (ev) {
	      if (!tool.started || isMoveTool) {
	    	  tmpContext.lineWidth = currentSize;
	    	  tmpContext.lineTo(ev._x, ev._y);
	    	  tmpContext.stroke();
	      }
	    };

	    // This is called when you release the mouse button.
	    this.mouseup = function (ev) {
	      if (tool.started && !isMoveTool) {
	        tool.mousemove(ev);
	        tool.started = false;
	        img_update();
	      }
	    };
	  };
}

// This function draws the #imageTemp canvas on top of #rootCanvas, after which
// #imageTemp is cleared. This function is called each time when the user
// completes a drawing operation.
function img_update () {
	context.drawImage(tmpCanvas, 0, 0);
	tmpContext.clearRect(0, 0, tmpCanvas.width, tmpCanvas.height);
}

function imgUpdate(tmpCanvas, tmpContext){
	context.drawImage(tmpCanvas, 0, 0);
	tmpContext.clearRect(0, 0, tmpCanvas.width, tmpCanvas.height);
	tmpCanvas.parentNode.removeChild(tmpCanvas);
}

function addListeners(){
    // Get the tool select input.
    var tool_select = document.getElementById('dtool');
    if (!tool_select) {
      alert('Error: failed to get the dtool element!');
      return;
    }
    tool_select.addEventListener('change', ev_tool_change, false);

    // Activate the default tool.
    if (tools[tool_default]) {
      tool = new tools[tool_default]();
      tool_select.value = tool_default;
    }
    
    // Attach the mousedown, mousemove and mouseup event listeners to canvas.
    tmpCanvas.addEventListener('mousedown', ev_canvas, false);
    tmpCanvas.addEventListener('mousemove', ev_canvas, false);
    tmpCanvas.addEventListener('mouseup',   ev_canvas, false);
    
}

// The event handler for any changes made to the tool selector.
function ev_tool_change (ev) {
  if (tools[this.value]) {
    tool = new tools[this.value]();
  }
}

// The general-purpose event handler. This function just determines the mouse
// position relative to the canvas element.
function ev_canvas (ev) {
  if (ev.layerX || ev.layerX == 0) { // Firefox
    ev._x = ev.layerX;
    ev._y = ev.layerY;
  } else if (ev.offsetX || ev.offsetX == 0) { // Opera
    ev._x = ev.offsetX;
    ev._y = ev.offsetY;
  }

  // Call the event handler of the tool.
  var func = tool[ev.type];
  if (func) {
    func(ev);
  }
}

function sizeChanged(){
	var value = $("#sizeSelect").val();
	
	switch (value) {
	case "small":
		currentSize = 1;
		break;
	case "normal":
		currentSize = 3;
		break;
	case "large":
		currentSize = 8;
		break;
	case "huge":
		currentSize = 15;
		break;
	}
}

function operationToolChanged(){
	// get the selected tool name...
	var toolName = $("#oTool").val();
	// switch move tool flag.
	if(toolName == "move"){
		isMoveTool = true;
		//set draw on canvas as input submit enabled.
		//$("#drawOnCanvasSubmit").attr("disabled", "false");
		document.getElementById("drawOnCanvasSubmit").disabled = false;
	} else{
		// toolName == "draw"
		isMoveTool = false;
		document.getElementById("drawOnCanvasSubmit").disabled = true;
		
		var tmpImageCanvas = document.getElementById("imageTemp2");
		if(tmpImageCanvas != null){
			var tmpImageContext = tmpImageCanvas.getContext("2d");
			
			imgUpdate(tmpImageCanvas, tmpImageContext);
		}
	}
}

function decorateImageSelector(){
	var rootElement = document.getElementById("imageInputRow");
	
	// decorate the IE browser...
	if(isIE()){
		// create the file input element...
		var fileInput = document.createElement("input");
		fileInput.setAttribute("id", "imageFile");
		fileInput.setAttribute("type", "file");
		fileInput.setAttribute("accept", "image/gif,image/png,image/jpg,image/jpeg");
		
		// create the image submitter element...
		var submitInput = document.createElement("input");
		submitInput.setAttribute("type", "submit");
		submitInput.setAttribute("value", "Draw on Canvas");
		submitInput.setAttribute("id", "drawOnCanvasSubmit");
		submitInput.disabled = true;
		
		// create the form element...
		var imgUploadForm = document.createElement("form");
		imgUploadForm.setAttribute("action", "javascript:handleImage(" + fileInput.value + ");");
		imgUploadForm.setAttribute("method", "post");
		imgUploadForm.setAttribute("enctype", "multipart/form-data");
		
		// append input types to form element.
		imgUploadForm.appendChild(fileInput);
		imgUploadForm.appendChild(submitInput);
		
		rootElement.appendChild(imgUploadForm);
	}else{
		// create a label, text input and button to add selected image...
		var imgSrcLabel = document.createElement("label");
		imgSrcLabel.setAttribute("for", "imgSrcInput");
		// create label's text node...
		imgSrcLabel.appendChild(document.createTextNode("Enter the image (Absolute) path: "));
		
		var imgSrcInput = document.createElement("input");
		imgSrcInput.setAttribute("type", "text");
		imgSrcInput.setAttribute("id", "imgSrcInput");
		
		var submitInput = document.createElement("input");
		submitInput.setAttribute("id", "drawOnCanvasSubmit");
		submitInput.setAttribute("type", "submit");
		submitInput.setAttribute("value", "Draw on Canvas");
		submitInput.disabled = true;
		submitInput.onclick = function() {handleImage(imgSrcInput.value)};

		// append created components to root element...
		rootElement.appendChild(imgSrcLabel);
		rootElement.appendChild(imgSrcInput);
		rootElement.appendChild(submitInput);
	}
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
	
	var tmpImageCanvas = document.getElementById("imageTemp2");
	if(tmpImageCanvas != null){
		var tmpImageContext = tmpImageCanvas.getContext("2d");
		imgUpdate(tmpImageCanvas, tmpImageContext);
	}
	
	var newImage = new Image();
	newImage.src = imageSource;
    // load image
    newImage.onload = function(){
    	drawImage(newImage);
    };
}

function drawImage(img){
	var draggingRect = false;
    var draggingRectOffsetX = 0;
    var draggingRectOffsetY = 0;
	
    // Add the temporary canvases.
    var container = canvas.parentNode;
    var tmpCanvas = document.createElement("canvas");
    if (!tmpCanvas) {
      alert('Error: I cannot create a new canvas element!');
      return;
    }

    tmpCanvas.id = "imageTemp2";
    tmpCanvas.width = canvas.width;
    tmpCanvas.height = canvas.height;
    container.appendChild(tmpCanvas);

    var tmpContext = tmpCanvas.getContext("2d");
    
    var canvasImg = tmpCanvas;
    var contextImg = tmpContext;
    
    var rectX = canvasImg.width / 2 - 200 / 2;
    var rectY = canvasImg.height / 2 - 137 / 2 + 10;
    
    var myStage = new Kinetic.Stage(canvasImg);
    
    myStage.setDrawStage(function(){
        var mousePos = myStage.getMousePos();
        
        if (draggingRect) {
            rectX = mousePos.x - draggingRectOffsetX;
            rectY = mousePos.y - draggingRectOffsetY;
        }
        
        myStage.beginRegion();
        
        contextImg.drawImage(img, rectX, rectY, 200, 137);
        // draw rectangular region for image
        contextImg.beginPath();
        contextImg.rect(rectX, rectY, 200, 137);
        contextImg.closePath();
        
        
        myStage.addRegionEventListener("onmousedown", function(){
        	if(isMoveTool){
	            draggingRect = true;
	            var mousePos = myStage.getMousePos();
	            
	            draggingRectOffsetX = mousePos.x - rectX;
	            draggingRectOffsetY = mousePos.y - rectY;
        	}
        });
        myStage.addRegionEventListener("onmouseup", function(){
        	if(isMoveTool){
        		draggingRect = false;
        	}
        });
        myStage.addRegionEventListener("onmouseover", function(){
        	if(isMoveTool){
        		document.body.style.cursor = "pointer";
        	}
        });
        myStage.addRegionEventListener("onmouseout", function(){
        	if(isMoveTool){
        		document.body.style.cursor = "default";
        	}
        });
        
        myStage.closeRegion();
    });
}

function saveCanvas(){
	// save the canvas as png...
	Canvas2Image.saveAsPNG(canvas);
	//window.open(canvas.toDataURL("image/png", 1.0));
}

function clearCanvas() {
	context.clearRect(0, 0, canvas.width, canvas.height);
	tmpContext.clearRect(0, 0, tmpCanvas.width, tmpCanvas.height);
	
	var tmpImageCanvas = document.getElementById("imageTemp2");
	if(tmpImageCanvas != null)
		tmpImageCanvas.getContext("2d").clearRect(0, 0, tmpImageCanvas.width, tmpImageCanvas.height);
}

// wipes the canvas context
function clear(c) {
  c.clearRect(0, 0, c.width, c.height);
}
