/*
validation function for pax edit screen

*/


	// function check to see if this is inital or updated
	// if updated and a fresh item, sync data
	function syncItems(element) {
		var name = element.name;
		var nameArray = name.split('.');							// get start of dom tree construct
		domRef = nameArray[0] + '.' + nameArray[1];					// construct dom ref to allocation object
		paxStatus = element.form.elements[domRef + '.paxStatus'];	// get pax status id

		if ( paxStatus.value != 2 ) {
			// we have an inital item
			// check to see if updated item needs updating!
			allocId = nameArray[1].charAt(16);

			updatedItemRefPrev = nameArray[0] + '.allocationItems[1].' + nameArray[2] + 'Prev';
			updatedItemRef = nameArray[0] + '.allocationItems[1].' + nameArray[2];

			if ( allocId == 0 ) {		// check to see we have the inital item of the array
				updatedStatusRef = nameArray[0] + '.allocationItems[1].paxStatus';
				initialStatusRef = nameArray[0] + '.allocationItems[0].paxStatus';

				// always update the updated if its previous value is zero
				if (element.form.elements[updatedItemRefPrev].value == 0 ) {
					element.form.elements[updatedItemRef].value = element.value;
					element.form.elements[initialStatusRef].value = 1;
					element.form.elements[updatedStatusRef].value = 2;
					// change the pax status flag to mark as saveable
					element.form.elements[nameArray[0]+'.statusCode'].value = 0;
				}
			}
			if ( allocId == 1 ) {
				updatedStatusRef = nameArray[0] + '.allocationItems[1].paxStatus';
				initialStatusRef = nameArray[0] + '.allocationItems[0].paxStatus'
				element.form.elements[initialStatusRef].value = 1;
				element.form.elements[updatedStatusRef].value = 2;
				// change the pax status flag to mark as saveable
				element.form.elements[nameArray[0]+'.statusCode'].value = 0;
				
				// change the colour of the item if it is diffrent from the inital value
				if ( element.form.elements[updatedItemRef].value != element.form.elements[updatedItemRef].value ) {
					element.form.elements[updatedItemRef].className = 'updatedField';
				}
			}
		}
	}

	// function to calculate a given allocation dynamically
	function getNewAllocation(element) {
		newAllocation = 0;

		var name = element.name;
		var nameArray = name.split('.');				// get start of dom tree construct
		domRef = nameArray[0] + '.' + nameArray[1];		// construct dom ref to allocation object

		// construct looping array
		var elementArray = new Array();
		elementArray[0] = domRef + '.inAdults' ;
		elementArray[1] = domRef + '.inChildren' ;
		elementArray[2] = domRef + '.inInfants' ; 
		
		// loop though array, 
		// getting values from dom tree and 
		// add to return var
   		for (var elementCounter = 0; elementCounter < elementArray.length; elementCounter++) {
			newAllocation = newAllocation + parseFloat(element.form.elements[elementArray[elementCounter]].value);
		}
		return newAllocation;
	}

	function checkValue(element) {
		message = '';
		
		syncItems(element); // check updated against inital

		// check value and allocation against the given allocation for item
		var name = element.name;
		var nameArray = name.split('.');						// get start of dom tree construct
		allocationRef = nameArray[0] + '.allocation';			// construct dom ref to allocation object
		var allocation = element.form.elements[allocationRef];	// get object ref
		newAllocation = getNewAllocation(element);

		if ( newAllocation > allocation.value ) {
			message = message + "\n* Warning: The initial figures exceeds allocation";
		}

		// check values & display warning if updated is greater than initial
		prevName = element.name + 'Prev';
		prevObject = element.form.elements[prevName];
		prevValue = prevObject.value;

		if ( element.value != prevValue && prevValue != 0 ) {
			message = message + "\n* Are you sure you want to change this figure?";
		}
		// display message
		if (message != '') { 
			alert(message);
		}
	}
	
	function processItem(element,id, url) {
		checked = document.forms[0].elements[id];
		//alert(checked);
		checked.checked=true;
		element.className='hilite';
		parent.frames['_edit'].location=url;
	}
