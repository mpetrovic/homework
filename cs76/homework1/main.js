/**
	Matt Petrovic
	mpetrovic@iq.harvard.edu
	208228130
 */
window.onload=function() {
	var support = {
		store: null,
	},
	zips,
	zip_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20json%20where%20url%20%3D%20'http%3A%2F%2Fmaps.googleapis.com%2Fmaps%2Fapi%2Fgeocode%2Fjson%3Flatlng%3D__LAT__%2C__LONG__%26sensor%3Dtrue'&format=json&diagnostics=true&callback=zipfunc",
	news_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%20%3D%20'http%3A%2F%2Fnews.google.com%2Fnews%3Fgeo%3D__ZIP__%26output%3Drss'&format=json&callback=newsfunc",
	woeid_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20woeid%2Cname%20from%20geo.places%20where%20text%3D%22__ZIP__%22%20and%20country.code%20%3D%20%27US%27&format=json&diagnostics=true&callback=woeidfunc",
	weather_yql = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss%20where%20url%3D'http%3A%2F%2Fweather.yahooapis.com%2Fforecastrss%3Fw%3D__WOEID__'&format=json&diagnostics=true&callback=weatherfunc";
	
	try {
		support.store = 'localStorage' in window && window['localStorage'] !== null;
	} 
	catch(e) {
		support.store = false;
	}
	
	zips = loadZIPs();
	
	/**
	 * CLICK HANDLERS
	 */
	$('#use a')[0].addEventListener('click', function (e) {
		e.preventDefault();
		displayError('');
		var zip = $('#zip_entered')[0].value;
		
		if (!isZIP(zip)) {
			displayError(zip+' is not a valid ZIP code!');
			return;
		}
		
		getData(zip);
		toggle($('#use')[0], 'show');
		
	});
	
	$('#current')[0].addEventListener('click', function (e) {
		e.preventDefault();
		displayError('');
		$('#use')[0].className = '';
		$('#recent')[0].className = '';
		// get lat/long from geolocation
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function (pos) {
				getExternal(zip_yql.replace('__LAT__',pos.coords.latitude).replace('__LONG__', pos.coords.longitude));
			}, function() {
				displayError('Error getting your position.');
			});
		}
	});
	
	$('#history')[0].addEventListener('click', function (e) {
		e.preventDefault();
		displayError('');
		$('#use')[0].className = '';
		// collect a list of recently used zips and
		// put them in a div and display it
		var keys = Object.keys(zips),
			i, num = 5, str = 'Recent Searches:<br>';
		for (i=0; i<num; i++) {
			if (keys[i])
				str += '<a href="#" data-zip="'+keys[i]+'">'+keys[i]+'</a>';
		}
		
		var recent = $('#recent')[0];
		recent.innerHTML = str;
		toggle(recent, 'show');
		
	});
	
	$('#recent')[0].addEventListener('click', function (e) {
		e.preventDefault();
		displayError('');
		
		var zip = e.target.getAttribute('data-zip');
		if (zip) {
			getData(zip);
			toggle(e.currentTarget, 'show');
		}
	});
	
	$('#enter')[0].addEventListener('click', function (e) {
		toggle($('#use')[0], 'show');
		$('#recent')[0].className = '';
		displayError('');
	});
	
	// loads data from external sources
	function getData(zip) {
		// pull in data from external sources
		// reset everything
		var panel = $('#panel')[0];
		panel.className = '';
		
		$('#news')[0].innerHTML = '';
		$('#weather')[0].innerHTML = '';
		
		// get news
		getExternal(news_yql.replace('__ZIP__', zip));
		
		// get woeid if we need it
		if (typeof zips[zip] == 'undefined') {
			getExternal(woeid_yql.replace('__ZIP__', zip));
		}
		else {
			// get weather if we don't
			getExternal(weather_yql.replace('__WOEID__', zips[zip]));
		}
	}
	
	/**
	 * YQL CALLBACKS
	 */
	 
	// get the zip code from googleapis
	window.zipfunc = function(obj) {
		var i, l, j, k, addr,
			res, zip = false;
		
		try {
			res = obj.query.results.json.results;
			for (i=0,l=res.length;i<l;i++) {
				addr = res[i].address_components;
				for (j=0,k=addr.length;j<k;j++) {
					if (addr[j].types == 'postal_code') {
						zip = addr[j]['short_name'];
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
		catch (e) {
			console.log(e);
			displayError('Error reading response from server. Please try again later.');
		}
	}
	
	// get news from googleapis
	window.newsfunc = function(obj) {
		var items, elem,
			i, l, src = '';
		
		try {
			items = obj.query.results.item;
		}
		catch (e) {
			displayError('There was an error reading results from our news provider. Please try again later.');
			return;
		}
		
		elem = $('#news')[0];
		elem.innerHTML = '';
		for (i=0,l=items.length;i<l;i++) {
			// add descriptions as straight html?
			src += items[i].description;
		}
		elem.innerHTML = src.replace(/ size=\"-1\"/g, '');
		
		var panel = $('#panel')[0];
		if (panel.className.indexOf('loading') > -1) {
			panel.className = 'loaded';
		}
		else {
			panel.className = 'loading';
		}
	}
	
	// get the woeid from yahoo apis
	window.woeidfunc = function(obj) {
		// pull the woeid out of the object - 12759094
		var zip, woeid;
		
		try {
			woeid = obj.query.results.place.woeid;
			zip = obj.query.results.place.name;
			
			saveZIP(zip, woeid);			
			// get the weather
			getExternal(weather_yql.replace('__WOEID__', woeid));
		}
		catch (e) {
			displayError('There was a problem reading data from our weather provider. Please try again later.');
		}
	}
	
	window.weatherfunc = function(obj) {
		var forecast;
		try {
			forecast = obj.query.results.item.description;
		}
		catch (e) {
			displayError('There was a problem reading data from our weather provider. Please try again later.');
			return;
		}
		
		$('#weather')[0].innerHTML = forecast;
	
		var panel = $('#panel')[0];
		if (panel.className.indexOf('loading') > -1) {
			panel.className = 'loaded';
		}
		else {
			panel.className = 'loading';
		}
	}
	
	function getExternal(url) {
		var script = document.createElement('script');
		script.src = url;
		$('head')[0].appendChild(script);
	}
	
	function isZIP(zip) {
		return zip.search(/^\d{5}(-\d{4})?$/) != -1;
	}
	
	function saveZIP(zip, woeid) {
		var d, i, found = false;
		if (!support.store) {
			displayError('Your browser is missing key features for this app to work!');
			return;
		}
		
		// save the zip to localstorage
		d = zips;
			
		for (i in d) {
			if (d[zip] == woeid) found = true;
		}
		if (!found) {
			// we didn't find the woeid
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
		if (typeof d != 'string')
			return {};
		return JSON.parse(d);
	}
	
	function displayError(err) {
		$('#error')[0].innerHTML = err;
	}
	
	// assumes there's only one class ever
	// because that's all I used on these
	function toggle(elem, classStr) {
		if (elem.className.indexOf(classStr) > -1) {
			elem.className = '';
		}
		else {
			elem.className = classStr;
		}
	}
};

function $(css, ctx) {
	if (ctx && typeof ctx == 'object' && 'querySelectorAll' in ctx)
		return ctx.querySelectorAll(css);
	return document.querySelectorAll(css);
}