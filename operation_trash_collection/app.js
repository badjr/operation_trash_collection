var bodyParser = require('body-parser');
var express = require('express');
var app = express();
var fs = require("fs");
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

var user = {
   "user4" : {
      "name" : "mohit",
      "password" : "password4",
      "profession" : "teacher",
      "id": 4
   }
}

app.post('/addUser', function (req, res) {
   // First read existing users.
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
       data = JSON.parse( data );
       data["user4"] = user["user4"];
       console.log( data );
       res.end( JSON.stringify(data));
   });
})

//curl -H "Content-Type: application/json" --data @test_json.json http://localhost:8081/hello
app.post('/hello', function(req, res) {
	console.log(req.body);
	res.setHeader('Content-Type', 'application/json');
	res.send( req.body );
	//res.send(JSON.stringify({ a: 1 }));
	//res.pipe(req);
	//res.sendStatus(200);
	console.log("Hello, world!");
});

//curl -X POST -d @test_json.json localhost:8081/reportTrash --header "Content-Type:application/json"
app.post('/reportTrash', function(req, res) {
	var mysql      = require('mysql');
	var connection = mysql.createConnection({
		host     : 'localhost',
		user     : 'trash_collector',
		password : '123',
		database : 'operation_trash_collection'
	});

	connection.connect();
	
	var lat = req.body.latitude;
	var long = req.body.longitude;
	connection.query('insert into trash_reports (latitude, longitude) values (' + lat + ', ' + long + ')', function(err, rows, fields) {
		if (!err) {
			console.log('The solution is: ', rows);
		}
		else {
			console.log('Error while performing Query.');
		}
	});

	connection.end();
});

app.get('/getTrashReports', function(req, res) {
	var mysql      = require('mysql');
	var connection = mysql.createConnection({
		host     : 'localhost',
		user     : 'trash_collector',
		password : '123',
		database : 'operation_trash_collection'
	});

	connection.connect();
	
	var lat = req.body.latitude;
	var long = req.body.longitude;
	connection.query('select * from trash_reports;', function(err, rows, fields) {
		if (!err) {
			console.log('The solution is: ', rows);
			res.setHeader('Content-Type', 'application/json');
			res.send( rows );
			//res.status(200);
		}
		else {
			console.log('Error while performing Query.');
			res.status(500);
		}
	});

	connection.end();

});

//var express = require('express');
//var app = express();

//nodemon app.js

app.use("/", express.static(__dirname));

var server = app.listen(8081, function () {

  //var host = server.address().address
  //var port = server.address().port
	var host = "localhost";
	var port = "8081";
  console.log("Example app listening at http://%s:%s", host, port)

});
