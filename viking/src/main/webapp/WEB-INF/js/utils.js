var lotsOfBlanks = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

function trim(value) {
   var temp = value;
   var obj = /^(\s*)([\W\w]*)(\b\s*$)/;
   if (obj.test(temp)) { temp = temp.replace(obj, '$2'); }
   var obj = / +/g;
   temp = temp.replace(obj, " ");
   if (temp == " ") { temp = ""; }
   return temp;
}


function leftBlankPad(st, le) {
//	if (st.length >= le) {
//		return st.substring(st.length-le, st.length);
//	} else {
//		return lotsOfBlanks.substr(0, (le-st.length)*6)+st;
//	}
}


function centerBlankPad(st, le) {

}


function addTableRow(tableId) {
    var tbody = document.getElementById(tableId).getElementsByTagName("tbody")[0];
	var row = document.createElement("tr");
	row.className = '';
	for (var i=1; i<arguments.length; i++) {
		var td1 = document.createElement("td");
		if (typeof arguments[i] == 'object') {
			td1.appendChild(arguments[i]);
		} else {
			var strArr = (""+arguments[i]).split("<br>");
			for (var j=0; j<(strArr.length-1); j++) {
				td1.appendChild(document.createTextNode(strArr[j]));
				td1.appendChild(document.createElement("br"));
			}
			td1.appendChild(document.createTextNode(strArr[strArr.length-1]));
		}
		row.appendChild(td1);
	}
    tbody.appendChild(row);
	return row;
}


function deleteTableRow(tableId, rowNum) {
	var table = document.getElementById(tableId);
	table.deleteRow(rowNum);
}


function deleteAllTableRows(tId) {
	var trsLen = document.getElementById(tId).getElementsByTagName('tbody')[0].getElementsByTagName('tr').length;
	for (var i=1; i<trsLen; i++) {
		deleteTableRow(tId, 1);
	}
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


function dateToString(date) {
	return	leftPad(true, date.getDate(), 2)+'/'+
			leftPad(true, date.getMonth()+1, 2)+'/'+
			leftPad(true, date.getFullYear(), 4);
}


function strToDate(dateStr) {
	var arr = dateStr.split('/');
	return new Date(parseInt(arr[2],10), parseInt(arr[1],10)-1, parseInt(arr[0],10));
}



function buildDateSelector(	selector,	//<%-- the selector to populate with the date options --%>
							startDate,	//<%-- the first date to use. Date object. --%>
							endDate,	//<%-- the last date to use. Date object. --%>
							startDay,	//<%-- the start day of week or -1. startDate is adjusted to this DOW. 0=Sunday, 6=Saturday --%>
							frequency,	//<%-- the nubmer of days between each date --%>
							selectedDate) {	//<%-- the date to default to (if any) --%>

	if (
		arguments.length >= 5 &&
		selector && selector.options && selector.options.length &&
		(typeof startDate == 'object' || typeof startDate == 'string') &&
		(typeof endDate == 'object' || typeof endDate == 'string') &&
		frequency > 0
		) {

		var oneDay = 24*60*60*1000;		//<%-- number of millisecs in a day --%>
		selector.options.length = 0;
		if (typeof startDate == 'string') {
			var sa = validDate(startDate).split('/');
			if (sa.length != 3) {
				return;
			}
			startDate = new Date(sa[2], parseInt(sa[1],10)-1, sa[0]);
		}
		if (typeof endDate == 'string') {
			var sa = validDate(endDate).split('/');
			if (sa.length != 3) {
				return;
			}
			endDate = new Date(sa[2], parseInt(sa[1],10)-1, sa[0]);
		}
		if (startDay > -1) {
			var adjust = startDay - startDate.getDay();
			adjust = adjust < 0?adjust+7:adjust;
			startDate.setMilliseconds(startDate.getMilliseconds()+oneDay*adjust);
		}
		var dateStr;
//		selector.options[selector.options.length] = new Option(); //<%-- empty option --%>
		while (startDate <= endDate) {
			var dStr = dateToString(startDate);
			var op;
			if (dStr == selectedDate) {
				op = new Option(dStr, dStr, true, true);
			} else {
				op = new Option(dStr, dStr, false, false);
			}
			selector.options[selector.options.length] = op;
			startDate.setMilliseconds(startDate.getMilliseconds()+oneDay*frequency);
		}
	}
}


function dateBefore(date, ref) {
	if (arguments.length != 2) return false;
	if (typeof date == 'string') date = strToDate(date);
	if (typeof ref == 'string') ref = strToDate(ref);
	return (date < ref);
}


function dateAfter(date, ref) {
	if (arguments.length != 2) return false;
	if (typeof date == 'string') date = strToDate(date);
	if (typeof ref == 'string') ref = strToDate(ref);
	return (date > ref);
}


function dateBetween(date, from, to) {
	return (!dateBefore(date, from) && !dateAfter(date, to));
}
