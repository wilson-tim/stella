<%@ page errorPage="/ErrorPage.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/vikingtags.tld" prefix="viking" %>
<jsp:useBean	id="versionform"
							class="uk.co.firstchoice.viking.gui.VersionFormBean"
							scope="request"/>
<html:html>
<head>
<title>Version Maintenance</title>
<link href="<html:rewrite page="/css/viking.css"/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<html:rewrite page="/table.js"/>"></script>

<script type="text/javascript" src="<html:rewrite page="/validation.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/utils.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/datepicker.js"/>"></script>

<script>
var frm;

var versions = <viking:vikingSelector	selectionType="versionsArray" />

function doSeasonChange(which) {
	var seasonSel = eval('frm.'+which+'Season');
	var versionSel = eval('frm.'+which+'Version');
	var a = seasonSel.options[seasonSel.selectedIndex].value.split(";");
	versionSel.options.length = 0;
	if (which != "default") {
		versionSel.options[versionSel.options.length] = new Option();
	}
	for (var idx = 0; idx < versions.length; idx++) {
		if (versions[idx][0] == a[0]) {
			for (var x = 1; x < versions[idx].length; x++) {
				var arr = versions[idx][x].split(';');
				var selected = (which == "default" && arr[2] =="Y") || (which == "live" && arr[3] =="Y");
				versionSel.options[versionSel.options.length] = new Option(arr[1], arr[0], selected, selected);
			}
			break;
		}
	}
	if (which == "to") {
		frm.toVersionText.value = '';
	}
}


function versionExists(season, version) {
	var exists = false;
	version = version.toUpperCase();
	for (var idx = 0; idx < versions.length && !exists; idx++) {
		if (versions[idx][0] == season) {
			for (var x = 1; x < versions[idx].length && !exists; x++) {
				var arr = versions[idx][x].split(';');
				if (arr[1].toUpperCase() == version) {
					exists = true;
				}
			}
		}
	}
	return exists;
}


function doLive() {
	var sSel = frm.liveSeason;
	var vSel = frm.liveVersion;
	if (sSel.selectedIndex > -1 && sSel.options[sSel.selectedIndex].value != "" &&
			vSel.selectedIndex > -1 && vSel.options[vSel.selectedIndex].value != "" &&
			(!vSel.options[vSel.selectedIndex].defaultSelected ||
			!sSel.options[sSel.selectedIndex].defaultSelected)) {
		if (confirm("Are you sure wish change the live version?")) {
			frm.action.value = "live";
			frm.submit();
		}
	} else {
		alert("Please select a new live year and version.");
	}
}


function doDefault() {
	var sSel = frm.defaultSeason;
	var vSel = frm.defaultVersion;
	if (sSel.selectedIndex > -1 && sSel.options[sSel.selectedIndex].value != "" &&
			vSel.selectedIndex > -1 && vSel.options[vSel.selectedIndex].value != "" &&
			(!vSel.options[vSel.selectedIndex].defaultSelected ||
			!sSel.options[sSel.selectedIndex].defaultSelected)) {
		if (confirm("Are you sure wish change the default version?")) {
			frm.action.value = "default";
			frm.submit();
		}
	} else {
		alert("Please select a new default year and version.");
	}
}


function doCreate() {
	var sSel = frm.newSeason;
	var sVal = sSel.selectedIndex > -1?sSel.options[sSel.selectedIndex].text:"";
	frm.newVersion.value = trim(frm.newVersion.value);
	var version = frm.newVersion.value;
	if (sVal == "" ||	version == "") {
		alert("Please enter new version to create");
	} else {
		if (versionExists(sVal,version)) {
			alert("Season: "+sVal+", Version: "+version+" already exist!");
		} else {
			if (confirm("Continue with creating Season: '"+sVal+"', Version: '"+version+"' ?")) {
				frm.action.value = "create";
				frm.submit();
			} else {
				alert("Version NOT created.");
			}
		}
	}
}


function doCopyRename(action) {
	var fsSel = eval("frm."+action+"FromSeason");
	var fsVal = fsSel.selectedIndex > -1?fsSel.options[fsSel.selectedIndex].text:"";
	var fvSel = eval("frm."+action+"FromVersion");
	var fvVal = fvSel.selectedIndex > -1?fvSel.options[fvSel.selectedIndex].text:"";
	var tsSel = eval("frm."+action+"ToSeason");
	var tsVal = tsSel.selectedIndex > -1?tsSel.options[tsSel.selectedIndex].text:"";
	var tvInp = eval("frm."+action+"ToVersion");
	tvInp.value = trim(tvInp.value);
	var tvVal = tvInp.value;
	if (fsVal == "" || fvVal == "") {
		alert("Please select season/version to rename from");
	} else {
		if (tsVal == "" || tvVal == "") {
			alert("Please enter season/version to rename to");
		} else {
			if (versionExists(tsVal, tvVal)) {
				alert(	"Error - cannot "+action+" season "+fsVal+", version "+fvVal+
								" to season "+tsVal+", version "+tvVal+" as it already exist.");
			} else {
				if (confirm(	"Are you sure you want to "+action+" season "+fsVal+", version "+fvVal+
											" to season "+tsVal+", version "+tvVal+"?")) {
					frm.action.value = action;
					frm.submit();
				}
			}
		}
	}
}


function doDelete() {
	var dsSel = frm.deleteSeason;
	var dsVal = dsSel.selectedIndex > -1?dsSel.options[dsSel.selectedIndex].text:"";
	var dvSel = frm.deleteVersion;
	var dvVal = dvSel.selectedIndex > -1?dvSel.options[dvSel.selectedIndex].text:"";
	if (dsVal == "" || dvVal == "") {
		alert("Please select season/version to delete");
	} else {
		if (confirm("WARNING!! You are about to delete season '"+dsVal+"', version '"+dvVal+
								"' INCLUDING ALL FLIGHT AND ALLOCATION INFORMATION - do you wish to continue?") &&
				!confirm("2nd Warning. Would you like to cancel the delete of season '"+dsVal+"', version '"+dvVal+"'?") &&
				confirm("3rd and final warning. Pressing Yes now will completely delete season '"+dsVal+"', version '"+dvVal+"' - continue?")) {
			frm.action.value = 'delete';
			frm.submit();
		} else {
			alert("Version delete cancelled.");
		}
	}
}


function doOnLoad() {
	frm=document.forms[0];
	doSeasonChange('live');
	doSeasonChange('default');
	doSeasonChange('copyFrom');
	doSeasonChange('renameFrom');
	doSeasonChange('delete');
}

</script>

<style>
table { width: 100%; }
td { text-align: left; vertical-align: middle; }
th { text-align: right; vertical-align: middle; color: darkBlue; background-color: transparent; }
th.header { font-size: 12pt; font-weight: bold; text-align: center; }
body { text-align: center; }
</style>
</head>

<body onLoad="doOnLoad();">
<h6><%= versionform.getError() %></h6>
<p/>
<h3>Version Maintenance</h3>
<html:form action="versionsmaint.do">
<input type="hidden" name="action"/>
<p/>
<table border="0" cellspacing="0" style="width: auto;">
	<tr>
		<td style="text-align: center; border: 1px solid darkBlue;">
			<table cellpadding="5">
				<tr>
					<th colspan="5" class="header">Change live version</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="liveSeason"
																		value="<%= versionform.getLiveSeason() %>"
																		onChange="doSeasonChange('live');" />
					</td>
					<th>Version:</th>
					<td>
						<html:select name="versionform" property="liveVersion"></html:select>
					</td>
					<th>
						<button onClick="doLive();">Change</button>
					</th>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td style="text-align: center; border: 1px solid darkBlue;">
			<table cellpadding="5">
				<tr>
					<th colspan="5" class="header">Change default version</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="defaultSeason"
																		value="<%= versionform.getDefaultSeason() %>"
																		onChange="doSeasonChange('default');" />
					</td>
					<th>Version:</th>
					<td>
						<html:select name="versionform" property="defaultVersion"></html:select>
					</td>
					<th>
						<button onClick="doDefault();">Change</button>
					</th>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td style="text-align: center; border: 1px solid darkBlue;">
			<table cellpadding="5">
				<tr>
					<th colspan="5" class="header">Create new empty version</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="newSeason"
																		value="<%= versionform.getNewSeason() %>" />
					</td>
					<th>New version name:</th>
					<td>
						<html:text	property="newVersion"
												name="versionform"
												size="20"
												maxlength="10" />
					</td>
					<th>
						<button onClick="doCreate();">Create</button>
					</th>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td style="text-align: center; border: 1px solid darkBlue;">
			<table cellpadding="5">
				<tr>
					<th colspan="6" class="header">Rename existing version</th>
				</tr>
				<tr>
					<th colspan="6" style="font-size: 10pt; text-align: center;">-= RENAME FROM =-</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="renameFromSeason"
																		value="<%= versionform.getRenameFromSeason() %>"
																		onChange="doSeasonChange('renameFrom');" />
					</td>
					<th>Version:</th>
					<td>
						<html:select name="versionform" property="renameFromVersion"></html:select>
					</td>
				</tr>
				<tr>
					<th colspan="6" style="font-size: 10pt; text-align: center;">-= RENAME TO =-</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="renameToSeason"
																		value="<%= versionform.getRenameToSeason() %>" />
					</td>
					<th>Version:</th>
					<td>
						<html:text	property="renameToVersion"
												name="versionform"
												size="20"
												maxlength="10" />
					</td>
					<th>
						<button onClick="doCopyRename('rename');">Rename</button>
					</th>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td style="text-align: center; border: 1px solid darkBlue;">
			<table cellpadding="5">
				<tr>
					<th colspan="6" class="header">Copy existing version</th>
				</tr>
				<tr>
					<th colspan="6" style="font-size: 10pt; text-align: center;">-= COPY FROM =-</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="copyFromSeason"
																		value="<%= versionform.getCopyFromSeason() %>"
																		onChange="doSeasonChange('copyFrom');" />
					</td>
					<th>Version:</th>
					<td>
						<html:select name="versionform" property="copyFromVersion"></html:select>
					</td>
				</tr>
				<tr>
					<th colspan="6" style="font-size: 10pt; text-align: center;">-= COPY TO =-</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="copyToSeason"
																		value="<%= versionform.getCopyToSeason() %>"
																		onChange="doSeasonChange('copyTo');" />
					</td>
					<th>Version:</th>
					<td>
						<html:text	property="copyToVersion"
												name="versionform"
												size="20"
												maxlength="10" />
					</td>
					<th>
						<button onClick="doCopyRename('copy');">Copy</button>
					</th>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td style="text-align: center; border: 1px solid darkBlue;">
			<table cellpadding="5">
				<tr>
					<th colspan="5" class="header">Delete existing version</th>
				</tr>
				<tr>
					<th>Season:</th>
					<td>
						<viking:vikingSelector	selectionType="seasonSelector"
																		name="deleteSeason"
																		value="<%= versionform.getDeleteSeason() %>"
																		onChange="doSeasonChange('delete');" />
					</td>
					<th>Version:</th>
					<td>
						<html:select name="versionform" property="deleteVersion"></html:select>
					</td>
					<th>
						<button onClick="doDelete();" style="color: red;">Delete</button>
					</th>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td style="text-align: center;">
			<button	id="cancelBtn"
							accesskey="C"
							onclick="location.href='menu.do';"><u>C</u>lose</button>
		</td>
	</tr>
</table>
</html:form>
</body>
</html:html>