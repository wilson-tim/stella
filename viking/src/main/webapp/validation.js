function leftPad(zero, num, digits) {
	var str;
	if (zero)
		str = "000000000000000"+num;
	else
		str = "               "+num;
	return str.substr(str.length-digits);
}


/*
Ensure that the dateField field contains a valid date in one of the following formats:
'dd.mm.yy' 'dd/mm/yy' 'dd mmm yy' and optionally is on or after okFrom, and further optionally if on or before okTo
okFrom and okTo should both be Date objects. BlankOK indicates whether an empty field should raise an error
syntax: validDate(dateFiled [,blankOK [, okFrom [ , okTo]] )
*/
function validDate(dateValue, okFrom, okTo) {
	ok = false;
	if (dateValue != "") {
		var dateArr = dateValue.split(/[\s+\.\/-]/);
		if (dateArr.length == 1) {			// It was one long string - eg. 101202 or 10mar02 - split as ddmmyy[yy]
			dateArr = dateValue.match(/(\d{2})(\d{2}|[a-zA-Z]{3})(\d{4}|\d{2})/);
			if (dateArr)					// did it match the pattern
				if (dateArr.length == 4) {	// Non-global search returns the full string in arr[0] - move them down
					for (var i=0; i<3; i++) {
						dateArr[i] = dateArr[i+1];
					}			
					dateArr.length = 3;
				}
		}
		if (dateArr)
			if (dateArr.length == 3) {
				if (dateArr[1].length == 3) {	//It's a alpha month (eg. may or dec) - convert to number
					dateArr[1] = "jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|".indexOf(dateArr[1].toLowerCase()+"|")/4+1;
				}
				var year = parseInt(dateArr[2], 10);
				var month = (parseInt(dateArr[1], 10)-1);
				var day = parseInt(dateArr[0], 10);
				if (year<1000)
					year = year+(year<50?2000:1900);
				var dt = new Date(year, month, day);
				if ((dt.getFullYear() == year) &&		// Make sure it was a valid date - Date will accept 321302
					(dt.getMonth() == month) &&	// and convert to 010203
					(dt.getDate() == day)) {
					ok = true;
					if (okTo)
						if (dt.getTime() > okTo.getTime())
							ok = false;
					if (okFrom)
						if (dt.getTime() < okFrom.getTime())
							ok = false;
					if (ok) {
						return leftPad(true, dt.getDate(), 2)+"/"+
								leftPad(true, dt.getMonth()+1, 2)+"/"+dt.getFullYear();
					}
				}
			}
	}
	return "";
}


function validDateField(dateField, blankOK, okFrom, okTo) {
	var ok = true;
	if (dateField) {
		var dateValue = dateField.value;
		var newDateValue = validDate(dateValue, okFrom, okTo);
		ok = (newDateValue != "");
		var okFromStr = (null==okFrom ? "" : ""+okFrom.getDate()+"/"+(okFrom.getMonth()+1)+"/"+okFrom.getFullYear());
		var okToStr = (null==okTo ? "" : ""+okTo.getDate()+"/"+(okTo.getMonth()+1)+"/"+okTo.getFullYear());
		if (((dateValue == "" && !blankOK) || (dateValue != "")) && !ok) {
			var error = "Please enter a valid date in the format DD/MM/YY. ";
			if (okTo)
				error += "Date must be between "+okFromStr+" and "+okToStr;
			else if (okFrom)
				error += "Date must be after "+okFromStr;
			alert(error);
			dateField.focus();
		} else {
			dateField.value = newDateValue;
		}
	}
	return ok;
}


/*
Convert a time string in the format hh:mm or hh:mm:ss to seconds
*/
function timeToSeconds(timeValue) {
  var timeArr = timeValue.split(/[\.\:]/);
  if (timeArr.length < 2 || timeArr.length > 3) {        // invalid format
    return 0;
  } else {
    return parseInt(timeArr[0],10)*3600 + parseInt(timeArr[1],10)*60 + (timeArr.length == 3?parseInt(timeArr[2],10):0);
  }
}


/*
Ensure that the timeField field contains a valid time in one of the following formats:
'hh.mm' 'hh:mm' 'hhmm'
*/
function validTime(timeValue) {
	ok = false;
	if (timeValue != "") {
		var timeArr = timeValue.split(/[\.\:]/);
		if (timeArr.length == 1) {			// It was one long string - eg. 1012
			if (timeValue.length == 3)		// if only 3 digits - eg. 725
				timeValue = '0'+timeValue;	// convert to 0725
			timeArr = timeValue.match(/(\d{2})(\d{2})/);
			if (timeArr) {					// did it match the pattern
				if (timeArr.length == 3) {	// Non-global search returns the full string in arr[0] - move them down
					for (var i=0; i<2; i++) {
						timeArr[i] = timeArr[i+1];
					}			
					timeArr.length = 2;		// [0]="07" [1]="25"
				}
			}
		}
		if (timeArr)
			if (timeArr.length == 2) {
				var hour = parseInt(timeArr[0], 10);
				var minute = parseInt(timeArr[1], 10);
				if (hour < 24 && hour > -1 && minute < 60 && minute > -1) {
					ok = true;
					if (ok) {
						return leftPad(true, hour, 2)+":"+leftPad(true, minute, 2);
					}
				}
			}
	}
	return "";
}


function validTimeField(timeField, blankOK) {
	var ok = true;
	if (timeField) {
		var timeValue = timeField.value;
		var newTimeValue = validTime(timeValue);
		ok = (newTimeValue != "");
		if (((timeValue == "" && !blankOK) || (timeValue != "")) && !ok) {
			var error = "Please enter a valid time in the format HH:MM ";
			alert(error);
			timeField.focus();
		} else {
			timeField.value = newTimeValue;
		}
	}
	return ok;
}


function floatToString(num, decimals) {
	if (!decimals) decimals = 2;
	var sign = num<0?"-":"";
	num = Math.abs(num);
	return sign+Math.floor(num)+"."+leftPad(true, Math.round((num-Math.floor(num))*Math.pow(10,decimals)), decimals);
}


function validAmount(amt, okFrom, okTo) {
	var amount = parseFloat(amt.value);
	var ok = false;
	if (!isNaN(amount)) {
		ok = true;
		var error = "Input must be in the format 99.99 ";
		if (arguments.length > 1) {
			if (amount < okFrom) {
				ok = false;
				error += "and must be above "+okFrom+" ";
			}
		}	
		if (arguments.length > 2) {
			if (amount > okTo) {
				ok = false;
				error += "and must be below "+okTo+" ";
			}
		}	
		if (ok) {
			return "";
		} else
			return error;
	} else
		return "Please enter a valid number in the format 99.99";
}


function validAmountField(amountField, okFrom, okTo) {
	if (amountField) {
		var errors = validAmount(amountField.value, okFrom, okTo);
		if (errors == "") {
			amountField.value = floatToString(parseFloat(amountField.value));
			return true;
		} else {
			alert(errors);
			return false;
		}
	}
	return false;
}


function validNumber(numberString, okFrom, okTo) {
	ok = false;
	var number = parseInt(numberString, 10);
	if (!isNaN(number)) {
		var ns = leftPad(true, number, numberString.length);
		if (numberString == ns) {		//parseInt will interpet 123abc45 as 123 - not ok here
			ok = true;
			if (arguments.length > 1)
				if (number < okFrom)
					ok = false;
			if (arguments.length > 2)
				if (number > okTo)
					ok = false;
		}
	}
	return ok;
}


function validNumberField(numberField, blankOk, okFrom, okTo) {
	if (numberField)
		if (numberField.value != "" || !blankOk)
			if (!validNumber(numberField.value, okFrom, okTo)) {
				alert(	"Please enter a valid number "+
						(arguments.length>2?"between "+okFrom+" and "+okTo:arguments.length>1?"greater than "+okFrom:""));
				numberField.focus();
				return false;
		}
	return true;
}


function validText(textField, minLength, maxLength) {
	var ok = true;
	if (textField) {
		var textValue = textField.value;
		var error = "Please enter text "+
						(arguments.length>2?"(between "+minLength+" and "+maxLength+" characters long)":
						arguments.length>1?" (minimum "+minLength+" characters long)":"");
		if (arguments.length > 1)
			if (textValue.length < minLength)
				ok = false;
		if (arguments.length > 2)
			if (textValue.length > maxLength)
				ok = false;
		if (!ok) {
			alert(error);
			textField.focus();
		}
	}
	return ok;
}

/* Added by DSB for fixing currency bug dated 01/03/05 */
function displayMessage(amt) {
  var yesno;
  yesno = confirm('This exchange rate is not in the currency table, are you sure you wish to make this change?');
  if (!(yesno)) {
  	 amt.focus();
  	 return false;
  }	 
  return true;
}


function validDisplyErrorAmount(amt) {
    var error = validAmount(amt, 0.0);
	if (error == "")  { 
	    amt.value=floatToString(parseFloat(amt.value), 4); 
    	return true ;
	} else  {
	   alert(error);
	   //this.focus();
	   return false;
	}   
}