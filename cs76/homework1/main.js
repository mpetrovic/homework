window.onload=function() {
	var support = {
		store: null;
	},
	zips = loadZIPs(),
	zip_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20json%20where%20url%20%3D%20'http%3A%2F%2Fmaps.googleapis.com%2Fmaps%2Fapi%2Fgeocode%2Fjson%3Flatlng%3D__LAT__%2C__LONG__%26sensor%3Dtrue'&format=json&diagnostics=true&callback=zipfunc",
	news_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%20%3D%20'http%3A%2F%2Fnews.google.com%2Fnews%3Fgeo%3D__ZIP__%26output%3Drss'&format=json&callback=newsfunc",
	woeid_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.places%20where%20text%3D'__ZIP__'%20limit%201&format=json&diagnostics=true&callback=woeidfunc",
	weather_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%3D'http%3A%2F%2Fweather.yahooapis.com%2Fforecastrss%3Fw%3D__WOEID__'&format=json&diagnostics=true&callback=weatherfunc";
	
	try {
		support.store = 'localStorage' in window && window['localStorage'] !== null;
	} 
	catch(e) {
		support.store = false;
	}
	
	var input = $('#zip_entered')[0];
	$('#use')[0].addEventListener('click', function (e) {
		e.preventDefault();
		var zip = $('#zip_entered')[0].value;
		
		if (!isZIP(zip) {
			displayError(zip+' is not a valid ZIP code!');
			return;
		}
		
		getData(zip);
		
	});
	
	$('#current')[0].addEventListener('click', function (e) {
		// get lat/long from geolocation
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function (pos) {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', zip_yql.replace('__LAT__',pos.coords.latitude).replace('__LONG__', pos.coords.longtitude));
				xhr.onreadystatechange = parseResponse;
				xhr.send();
			}, displayError);
		}
	});
	
	// loads data from
	function getData(zip) {
		// pull in data from external sources
		var news_xhr = new XMLHttpRequest(),
			weather_xhr = new XMLHttpRequest();
		
		// get news
		news_xhr.open('GET', news_yql.replace('__ZIP__', zip));
		news_xhr.onreadystatechange = parseResponse;
		news_xhr.send();
		
		// get woeid if we need it
		if (typeof zips[zip] == 'undefined') {
			weather_xhr.open('GET', woeid_yql.replace('__ZIP__', zip));
			weather_xhr.onreadystatechange = parseResponse;
			weather_xhr.send();
		}
		else {
			// get weather if we don't
			weather_xhr.open('GET', weather_yql.replace('__WOEID__', zips[zip]));
			weather_xhr.onreadystatechange = parseResponse;
			weather_xhr.send();
		}
	}
	
	// get the zip code from googleapis
	function zipfunc(obj) {
		var i, l, j, k, addr,
			res = obj.query.results.json.results,
			zip = FALSE;
		
		for (i=0,l=res.length;i<l;i++) {
			addr = res[i].address_components;
			for (j=0,k=addr.length;j<k;j++) {
				if (addr[j].types == 'postal_code') {
					zip = parseInt(addr[j].short_name):
					break;
				}
			}
			if (zip) break;
		}
		
		if (zip) {
			getData(zip);
		}
		else {
			displayError("Your position does not map to a US ZIP code.");
		}
	}
	
	// get news from googleapis
	function newsfunc(obj) {
	}
	
	// get the woeid from yahoo apis
	function woeidfunc(obj) {
		// pull the woeid out of the object
		var zip, woeid, i, l, j, k,
			xhr = new XMLHttpRequest(),
			res = obj.query.results.json.results,
			address;
				
		
		saveZIP(zip, woeid);
		
		// get the weather
		xhr.open('GET', weather_yql.replace('__WOEID__', woeid));
		xhr.onreadystatechange = parseResponse;
		xhr.send();
	}
	
	function weatherfunc(obj) {
	}
	
	function parseResponse(tr) {
		if (tr.readyState == 4) {
			if (tr.status == 200 || tr.status == 302) {
				eval(tr.responseText);
			}
		}
	}
	
	function isZIP(zip) {
		return zip.search(/^\d{5}(-\d{4})?$/) != -1;
	}
	
	function saveZIP(zip, woeid) {
		var d, i, l;
		if (!support.store) {
			displayError('Your browser is missing key features for this app to work!');
			return;
		}
		
		// save the zip to localstorage
		d = localStorage['zip_list'];
		if (d == '')
			d = {};
		else 
			d = JSON.parse(d);
			
		for (i in d) {
			if (d[zip] == zip) break;
		}
		if (i==l) {
			// we didn't find italics
			d[zip] = woeid;
			localStorage['zip_list'] = JSON.stringify(d);
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
			return {};
		return JSON.parse(d);
	}
	
	function displayError(err) {
		$('#error')[0].innerHTML = err;
	}
};

function $(css) {
	return document.querySelectorAll(css);
}