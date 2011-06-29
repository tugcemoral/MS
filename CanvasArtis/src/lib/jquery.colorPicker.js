/**
 * Really Simple Color Picker in jQuery
 * 
 * Copyright (c) 2008 Lakshan Perera (www.laktek.com)
 * Licensed under the MIT (MIT-LICENSE.txt)  licenses.
 * 
 */

(function($){
  $.fn.colorPicker = function(){    
    if(this.length > 0) buildSelector();
    return this.each(function(i) { 
      buildPicker(this)}); 
  };
  
  var selectorOwner;
  var selectorShowing = false;
  
  buildPicker = function(element){
    //build color picker
    control = $("<div class='color_picker'>&nbsp;</div>")
    control.css('background-color', $(element).val());
    
    //bind click event to color picker
    control.bind("click", toggleSelector);
    
    //add the color picker section
    $(element).after(control);

    //add even listener to input box
    $(element).bind("change", function() {
      selectedValue = toHex($(element).val());
      $(element).next(".color_picker").css("background-color", selectedValue);
    });
    
    //hide the input box
    $(element).hide();

  };
  
  buildSelector = function(){
    selector = $("<div id='color_selector'></div>");

     //add color pallete
     $.each($.fn.colorPicker.defaultColors, function(i){
      swatch = $("<div class='color_swatch'>&nbsp;</div>")
      swatch.css("background-color", "#" + this);
      swatch.bind("click", function(e){ changeColor($(this).css("background-color")) });
      swatch.bind("mouseover", function(e){ 
        $(this).css("border-color", "#598FEF"); 
        $("input#color_value").val(toHex($(this).css("background-color")));    
        }); 
      swatch.bind("mouseout", function(e){ 
        $(this).css("border-color", "#000");
        $("input#color_value").val(toHex($(selectorOwner).css("background-color")));
        });
      
     swatch.appendTo(selector);
     });
  
     //add HEX value field
     hex_field = $("<label for='color_value'>Hex</label><input type='text' size='8' id='color_value'/>");
     hex_field.bind("keydown", function(event){
      if(event.keyCode == 13) {changeColor($(this).val());}
      if(event.keyCode == 27) {toggleSelector()}
     });
     
     $("<div id='color_custom'></div>").append(hex_field).appendTo(selector);

     $("body").append(selector); 
     selector.hide();
  };
  
  checkMouse = function(event){
    //check the click was on selector itself or on selectorOwner
    var selector = "div#color_selector";
    var selectorParent = $(event.target).parents(selector).length;
    if(event.target == $(selector)[0] || event.target == selectorOwner || selectorParent > 0) return
    
    hideSelector();   
  }
  
  hideSelector = function(){
    var selector = $("div#color_selector");
    
    $(document).unbind("mousedown", checkMouse);
    selector.hide();
    selectorShowing = false
  }
  
  showSelector = function(){
    var selector = $("div#color_selector");
    
    //alert($(selectorOwner).offset().top);
    
    selector.css({
      top: $(selectorOwner).offset().top + ($(selectorOwner).outerHeight()),
      left: $(selectorOwner).offset().left
    }); 
    hexColor = $(selectorOwner).prev("input").val();
    $("input#color_value").val(hexColor);
    selector.show();
    
    //bind close event handler
    $(document).bind("mousedown", checkMouse);
    selectorShowing = true 
   }
  
  toggleSelector = function(event){
    selectorOwner = this; 
    selectorShowing ? hideSelector() : showSelector();
  }
  
  changeColor = function(value){
    if(selectedValue = toHex(value)){
      $(selectorOwner).css("background-color", selectedValue);
      $(selectorOwner).prev("input").val(selectedValue).change();
    
      //close the selector
      hideSelector();    
    }
  };
  
  //converts RGB string to HEX - inspired by http://code.google.com/p/jquery-color-utils
  toHex = function(color){
    //valid HEX code is entered
    if(color.match(/[0-9a-fA-F]{3}$/) || color.match(/[0-9a-fA-F]{6}$/)){
      color = (color.charAt(0) == "#") ? color : ("#" + color);
    }
    //rgb color value is entered (by selecting a swatch)
    else if(color.match(/^rgb\(([0-9]|[1-9][0-9]|[1][0-9]{2}|[2][0-4][0-9]|[2][5][0-5]),[ ]{0,1}([0-9]|[1-9][0-9]|[1][0-9]{2}|[2][0-4][0-9]|[2][5][0-5]),[ ]{0,1}([0-9]|[1-9][0-9]|[1][0-9]{2}|[2][0-4][0-9]|[2][5][0-5])\)$/)){
      var c = ([parseInt(RegExp.$1),parseInt(RegExp.$2),parseInt(RegExp.$3)]);
      
      var pad = function(str){
            if(str.length < 2){
              for(var i = 0,len = 2 - str.length ; i<len ; i++){
                str = '0'+str;
              }
            }
            return str;
      }

      if(c.length == 3){
        var r = pad(c[0].toString(16)),g = pad(c[1].toString(16)),b= pad(c[2].toString(16));
        color = '#' + r + g + b;
      }
    }
    else color = false;
    
    return color
  }

  
  //public methods
  $.fn.colorPicker.addColors = function(colorArray){
    $.fn.colorPicker.defaultColors = $.fn.colorPicker.defaultColors.concat(colorArray);
  };
  
  $.fn.colorPicker.defaultColors = 
	['990033','ff3366','cc0033','ff0033','ff9999','cc3366','ffccff','cc6699',
		'993366','660033','cc3399','ff99cc','ff66cc','ff99ff','ff6699','cc0066',
		'ff0066','ff3399','ff0099','ff33cc','ff00cc','ff66ff','ff33ff','ff00ff',
		'cc0099','990066','cc66cc','cc33cc','cc99ff','cc66ff','cc33ff','993399',
		'cc00cc','cc00ff','9900cc','990099','cc99cc','996699','663366','660099',
		'9933cc','660066','9900ff','9933ff','9966cc','330033','663399','6633cc',
		'6600cc','9966ff','330066','6600ff','6633ff','ccccff','9999ff','9999cc',
		'6666cc','6666ff','666699','333366','333399','330099','3300cc','3300ff',
		'3333ff','3333cc','0066ff','0033ff','3366ff','3366cc','000066','000033',
		'0000ff','000099','0033cc','0000cc','336699','0066cc','99ccff','6699ff',
		'003366','6699cc','006699','3399cc','0099cc','66ccff','3399ff','003399',
		'0099ff','33ccff','00ccff','99ffff','66ffff','33ffff','00ffff','00cccc',
		'009999','669999','99cccc','ccffff','33cccc','66cccc','339999','336666',
		'006666','003333','00ffcc','33ffcc','33cc99','00cc99','66ffcc','99ffcc',
		'00ff99','339966','006633','336633','669966','66cc66','99ff99','66ff66',
		'339933','99cc99','66ff99','33ff99','33cc66','00cc66','66cc99','009966',
		'009933','33ff66','00ff66','ccffcc','ccff99','99ff66','99ff33','00ff33',
		'33ff33','00cc33','33cc33','66ff33','00ff00','66cc33','006600','003300',
		'009900','33ff00','66ff00','99ff00','66cc00','00cc00','33cc00','339900',
		'99cc66','669933','99cc33','336600','669900','99cc00','ccff66','ccff33',
		'ccff00','999900','cccc00','cccc33','333300','666600','999933','cccc66',
		'666633','999966','cccc99','ffffcc','ffff99','ffff66','ffff33','ffff00',
		'ffcc00','ffcc66','ffcc33','cc9933','996600','cc9900','ff9900','cc6600',
		'993300','cc6633','663300','ff9966','ff6633','ff9933','ff6600','cc3300',
		'996633','330000','663333','996666','cc9999','993333','cc6666','ffcccc',
		'ff3333','cc3333','ff6666','660000','990000','cc0000','ff0000','ff3300',
		'cc9966','ffcc99','ffffff','cccccc','999999','666666','333333','000000',
		'000000','000000','000000','000000','000000','000000','000000','000000'];  
})(jQuery);


