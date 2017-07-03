const express = require('express')
var request = require('superagent');
var bodyParser = require('body-parser');
var querystring = require('querystring');

const app = express()
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.get('/mailchimp/auth/callback', function(req, res) {
console.log("code = "+ req.query.code);
  request.post('https://login.mailchimp.com/oauth2/token')
         .send(querystring.stringify({
            'grant_type': 'authorization_code',
            'client_id': '******',
            'client_secret': '********',
            'redirect_uri': 'http://127.0.0.1:8880/firebaseAuth.html',
            'code': req.query.code
          }))
            .end((err, result) => {
                if (err) {
                console.log(err);
                    res.send('An unexpected error occured while trying to perform MailChimp oAuth' + err);
                } else {
                    res.send(result);
                }
            });
});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!')
})