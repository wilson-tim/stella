
// -------------------------------------------------------------------
// TabNext()
// Function to auto-tab field
// Arguments:
//   obj :  The input object (this)
//   event: Either 'up' or 'down' depending on the keypress event
//   len  : Max length of field - tab when input reaches this length
//   next_field: input object to get focus after this one
// -------------------------------------------------------------------
var phone_field_length=0;
function TabNext(obj,event,len,next_field) {
	if (event == "down") {
		phone_field_length=obj.value.length;
		}
	else if (event == "up") {
		if (obj.value.length != phone_field_length) {
			phone_field_length=obj.value.length;
			if (phone_field_length == len) {
				next_field.focus();
				}
			}
		}
	}


// -------------------------------------------------------------------
// onchangeTab()
// Function to auto-tab field when a field has changed value
// For use on text disabled fields (such as calendar inputs and select lists
//
// Arguments:
//   next_field: input object to get focus after this one
// -------------------------------------------------------------------
function onchangeTab(next_field) {
	next_field.focus();
}