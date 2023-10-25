var mysql = require('mysql2');
const bodyParser = require("body-parser");
const express = require('express');
var app = express();

var con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "root",
    database:"hr_v2",
    charset: 'utf8mb4'
  });

con.connect(function(err) {
    if (err) throw err;
    con.query("SELECT * FROM jobs", function(err,result, fields){
        if(err) throw err;
       // console.log(result);
    });
});

var port = process.env.PORT || 3010

app.get("/jobs", (req, res) => {
    con.query("SELECT * FROM jobs", function(err, result, fields){
        if(err) throw err;
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify(result));
    });
});

app.get("/trabajadorConTutoria", (req, res) => {
    let sql = "SELECT * FROM employees where meeting= 1"
    con.query(sql, function (err, result, fields) {
        if(err) throw err;
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify(result));
    })
});

app.get('/tutor/listaTrabajadores', function (req,res) {

    let manager_id = req.query.codigo;
    let sql = "SELECT * FROM hr_v2.employees where manager_id=?";
    let params = [manager_id];
    con.query(sql, params, function(err,results){
        if(err) throw err;
        res.json(results);
    });
});
// localhost:3010/buscarEmployee?id=5
app.get("/buscarEmployee", (req, res) => {
    let id = req.query.id;
    let sql = "SELECT * FROM employees where employee_id= ? ";
    let params = [id];
    con.query(sql, params, function (err, result) {
        if(err) throw err;
        res.setHeader('Content-Type', 'application/json');
        if(result.length>0){
            res.send({"result":result});
        }else{
            res.send({"result":result})
        }
        
    })
});

app.get("/trabajador/tutoria", (req, res) => {
    let id = req.query.codigo;
    let sql = "SELECT meeting_date FROM employees WHERE employee_id = ?";
    let params = [id];
    con.query(sql, params, function (err, result) {
        if(err) throw err;
        res.setHeader('Content-Type', 'application/json');
        if(result.length>0){
            res.send(result[0]);
        }else{
            res.send({"meeting_date": null})
        }
    })

});

app.post("/trabajador/feedback", bodyParser.json(), function (req, res) {
    let id = req.body.employee_id;
    let feedback = req.body.employee_feedback;
    let sql = "UPDATE employees set employee_feedback = ? where employee_id = ?"
    let params = [feedback, id];
    res.setHeader('Content-Type', 'application/json');
    con.query(sql, params, function (err, result) {
        if(err) throw err;
        if(result.affected_rows>0){
            res.send({"result":"Cambio exitoso"});
        }else{
            res.send({"result":"Error"})
        }
        
    })
});

app.post('/tutor/tutoria', function (req, res) {
    const manager_id = req.query.codigoTutor;
    const employee_id = req.query.codigoEmpleado;

    console.log("manager_id:", manager_id);
    console.log("employee_id:", employee_id);

    let sql = "SELECT * FROM hr_v2.employees where manager_id=? and employee_id=?";
    let params = [manager_id,employee_id];

    conn.query(sql, params, function(err,results){
        if(err) throw err;
        let respuesta;
        if (results.length !== 0) {
            if (results[0].meeting_date !== null) {
                respuesta = {
                    rpta: "El trabajador ya tiene una cita asignada. Elija otro trabajador"
                }
                res.json(respuesta)
            }else{
                let date_time = new Date();
                var day = 60 * 60 * 24 * 1000;
                date_time.setDate(date_time.getDate() + 1);
                date_time.setTime(date_time.getTime() - date_time.getTimezoneOffset() * 60 * 1000);

                let horarioTutoria = date_time.toISOString().slice(0, 19).replace('T', ' ');

                sql = "UPDATE hr_v2.employees set meeting_date = ? where employee_id=?";
                params = [horarioTutoria,employee_id];

                conn.query(sql,params,function (err, results) {
                    if(err) throw err;
                    respuesta = {
                        rpta: "Asignacion del trabajador correcta"
                    }
                    res.json(respuesta)
                });
            }
        }else{
            respuesta = {
                rpta: "El tutor no es manager del empleado",

            }


            res.json(respuesta)
        }
    });
});

// usar bodyParser.json() cuando se manda como raw -> json
// usar bodyParser.urlencoded({extended: true}) cuando se manda como x-www-form-unlencoded
/*
app.post("/validarPOST", bodyParser.json(), function (req, res) {
    let user = req.body.username;
    let password = req.body.password;
    let sql = "SELECT * FROM happyhierba.persona where (username= ? and contrasenia= ?)"
    let params = [user, password];
    res.setHeader('Content-Type', 'text/html; charset=utf-8');
    con.query(sql, params, function (err, result) {
        if(err) throw err;
        res.send(result);
    })
});

app.get("/validar/:user/:password", (req, res) => {
    let user = req.params.user;
    let password = req.params.password;
    let sql = "SELECT * FROM persona where (correo= ? and contrasenia= ?)"
    let params = [user, password];
    res.setHeader('Content-Type', 'text/html; charset=utf-8');
    con.query(sql, params, function (err, result) {
        if(err) throw err;
        res.send(result);
    })
});
*/

app.listen(port) 