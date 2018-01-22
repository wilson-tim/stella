var sortString = "";

function sortTable(sFieldName) {

	var oDSO = document.forms[0].dsoSearch; 

	var idx = sortString.indexOf(sFieldName);
	if (idx == -1) {
		sortString += "+"+sFieldName+";";
	} else {
		var direction = sortString.charAt(idx-1);
		if (direction == '+') {
			sortString = sortString.replace('+'+sFieldName, '-'+sFieldName);
		} else {
			sortString = sortString.replace('-'+sFieldName+';', '');
		}
	}
	if (sortString == "") {
		window.document.all["sortId"].innerText = "-NONE-";
	} else {
		window.document.all["sortId"].innerText = sortString.substring(0,sortString.length-1);
	}
	oDSO.Sort = sortString;
	oDSO.reset();
}


function resetSort() {
	var oDSO = document.forms[0].dsoSearch;
	window.document.all["sortId"].innerText = "-NONE-";
	sortString = "";
	oDSO.Sort = sortString;
	oDSO.reset();
}


function showDetails(data) {
	event.returnValue = false;
	seriesNo = data.split(";")[0];
	type = data.split(";")[1].split("-")[0];
	var width = window.screen.width*40/100;
	var height = window.screen.height*80/100;
	var leftPoint = parseInt(window.screen.width / 2) - parseInt(width / 2);
	var topPoint = parseInt(window.screen.height / 2) - parseInt(height / 2);
	window.showModalDialog('capacitydetailspopup.do?seriesNo='+seriesNo+'&type='+escape(type), '', 'dialogHeight:'+height+'px;dialogWidth:'+
									width+'px;help:no;scroll:yes;status:no');
}


function doOpenSeries(no) {
	event.returnValue = false;
	var maxWidth = parseInt(window.screen.width*1);
	
	
	var win = window.open("editseries.do?seriesNo="+no,'',
				'top=0,left=0,width='+window.screen.width+',height='+window.screen.height+
				',scrollbars=yes,menubar=yes,location=yes,status=yes,toolbar=no,resizable=yes',false);
	win.moveTo(-5, -5);
	win.resizeTo(win.screen.availWidth+10, win.screen.availHeight+9);
	win.focus();
}