function trim(value) {
   var temp = value;
   var obj = /^(\s*)([\W\w]*)(\b\s*$)/;
   if (obj.test(temp)) { temp = temp.replace(obj, '$2'); }
   var obj = / +/g;
   temp = temp.replace(obj, " ");
   if (temp == " ") { temp = ""; }
   return temp;
}


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


function floatToString(num) {
	var sign = num<0?"-":"";
	num = Math.abs(num);
	return sign+Math.floor(num)+"."+leftPad(true, Math.round((num-Math.floor(num))*100), 2);
}


function validAmount(amountField, okFrom, okTo) {
	var ok = true;
	if (amountField) {
		ok = false;
		var amount = parseFloat(amountField.value);
		if (!isNaN(amount)) {
			ok = true;
			var error = "Input must be in the format 99999.99 ";
			if (arguments.length > 1)
				if (amount < okFrom) {
					ok = false;
					error += "and must be above "+okFrom+" ";
				}
			if (arguments.length > 2)
				if (amount > okTo) {
					ok = false;
					error += "and must be below "+okTo+" ";
				}
			if (ok) {
				amountField.value = floatToString(amount);
//				var sign = amount<0?"-":"";
//				amount = Math.abs(amount);
//				amountField.value = sign+Math.floor(amount)+"."+
//									leftPad(true, Math.round((amount-Math.floor(amount))*100), 2);
			} else
				alert(error);
		} else
			alert("Please enter a valid amount in the format 99999.99");
		if (!ok)
			amountField.focus();
	}
	return ok;
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


function radioSelected(radio) {
	if (radio) {				// there is such a radio on the form
		if (radio.length) {		// there are more than one radio in the array
			for (var i=0; i<radio.length; i++) {
				if (radio[i].checked)
					return radio[i].value;
			}
			return "";
		} else {
			return radio.checked?radio.value:"";
		}
	} else {
		return "";
	}
}
