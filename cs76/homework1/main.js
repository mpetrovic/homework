window.onload=function() {
	var support = {
		store: null;
	},
	news_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%20%3D%20'http%3A%2F%2Fnews.google.com%2Fnews%3Fgeo%3D_ZIP_%26output%3Drss'&format=json&callback=newscb",
	weather_yql = ;
	
	try {
		support.store = 'localStorage' in window && window['localStorage'] !== null;
	} 
	catch(e) {
		support.store = false;
	}
	
	var input = $('#zip_entered')[0];
	$('#use')[0].addEventListner('click', function (e) {
		e.preventDefault();
		var zip = $('#zip_entered')[0].value;
		
		if (!isZIP(zip) {
			displayError(zip+' is not a valid ZIP code!');
			return;
		}
		
	});
	
	// loads data from
	function getData(zip) {
		// pull in data from external sources
		
	}
	
	function isZIP(zip) {
		return zip.search(/^\d{5}(-\d{4})?$/) != -1;
	}
	
	function saveZIP(zip) {
		var d, i, l;
		if (!support.store) {
			displayError('Your browser is missing key features for this app to work!');
			return;
		}
		
		// save the zip to localstorage
		d = localStorage['zip_list'];
		if (d == '')
			d = [];
		else 
			d = d.split(',');
			
		for (i=0,l=d.length; i<l; i++) {
			if (d[i] == zip) break;
		}
		if (i==l) {
			// we didn't find italics
			d.push(zip);
			localStorage['zip_list'] = d.join(',');
		}
	}
	
	function loadZIPs() {
		var d;
		if (!support.store) {
			displayError('Your browser is missing key features for this app to work!');
			return;
		}
		
		d = localStorage['zip_list'];
		if (d == '')
			return [];
		return d.split(',');
	}
	
	function displayError(err) {
	}
};

function $(css) {
	return document.querySelectorAll(css);
}

function newscb(obj) {
}