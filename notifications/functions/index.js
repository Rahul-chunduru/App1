
const functions = require('firebase-functions');
const admin = require('firebase-admin')
admin.initializeApp(functions.config().firebase);

/**
Triggers when a new event is posted
*/

exports.event_notifications = functions.database.ref('/Events/{event_num}')
.onWrite(event => {

	//Store the datasnapshot value in request variable
	const request = event.data.val();

	//get the list of device notification tokens	
	const getDeviceTokensPromise = admin.database().ref('/User_keys').once('value');
	
	return Promise.all([getDeviceTokensPromise]).then(results =>{
		const snap = results[0];
		const payload = {

			//Notification details
	  	data :{

	  		title: request.title,
	  		body: request.text,
	  		sender : request.By,
	  		post_type : request.type,
	  		Type:"event"
	  	}	  	 
	  };

	  //listing all the tokens
	  const tokens = Object.keys(snap.val());


	  //Send Notifications to all devices
	  return admin.messaging().sendToDevice(tokens,payload)
	  .then(function(response){
	  		console.log("event_Success", response);
	  	})
	  	.catch(function(error){
	  		console.log("event_Error", error);
	  	})
	});
});


/**
Triggers when a new assignment is added to the MA108 course website
*/
exports.website_notifications = functions.database.ref('/grps/MA108')
.onWrite(event => {
	//const name = event.params.grp_name;

	//get the list of device notification tokens	
	const getDeviceTokensPromise = admin.database().ref('/Groups/MA108/token').once('value');
	
	return Promise.all([getDeviceTokensPromise]).then(results =>{
				const snap = results[0];


		const payload = {
	  	data :{

	  		title: 'MA108',
	  		text: "A new assignment was posted",
	  		Type: "website",
	  		grp_name:'MA108'
	  	}
	  	 
	  };

	  const tokens = Object.keys(snap.val());


	  //Send Notifications to respective group members
	  return admin.messaging().sendToDevice(tokens,payload)
	  .then(function(response){
	  		console.log("website_Success", response);
	  	})
	  	.catch(function(error){
	  		console.log("website_Error", error);
	  	})
	});
});


/**
Triggers when a new assignment is added to the CS215 course website
*/
exports.website_notifications2 = functions.database.ref('/grps/CS215')
.onWrite(event => {
	//const name = event.params.grp_name;

	//get the list of device notification tokens	
	const getDeviceTokensPromise = admin.database().ref('/Groups/CS207/token').once('value');
	//console.log('one');
	return Promise.all([getDeviceTokensPromise]).then(results =>{
				const snap = results[0];


		const payload = {
	  	data :{

	  		title: 'CS215',
	  		text: "A new assignment was posted",
	  		Type: "website",
	  		grp_name:'CS215'
	  	}
	  	 //console.log('three');
	  };

	  //listing all the tokens
	  const tokens = Object.keys(snap.val());

	  	  //Send Notifications to respective group members
	  return admin.messaging().sendToDevice(tokens,payload)
	  .then(function(response){
	  		console.log("website_Success", response);
	  	})
	  	.catch(function(error){
	  		console.log("website_Error", error);
	  	})
	});
});



exports.nonCourse_notif = functions.database.ref('/Non_course/{mssg_num}')
.onWrite(event => {
	const request = event.data.val();
	

	//get the list of device notification tokens
	const getDeviceTokensPromise = admin.database().ref('/User_keys').once('value');
	//console.log('one');
	return Promise.all([getDeviceTokensPromise]).then(results =>{
		const snap = results[0];
		const payload = {
	  	data :{

	  		title: "MyCseApp",
	  		body: request.a,	  		
	  		Type:"non_grp"
	  	}
	  	 //console.log('three');
	  };

	  //listing all the tokens
	  const tokens = Object.keys(snap.val());

	  	  //Send Notifications to respective group members
	  return admin.messaging().sendToDevice(tokens,payload)
	  .then(function(response){
	  		console.log("noncourse_Success", response);
	  	})
	  	.catch(function(error){
	  		console.log("noncourse_Error", error);
	  	})
	});
});

