const Koa = require('koa')
const Router = require('koa-router')
const logger = require('koa-logger');
const bodyParser = require('koa-bodyparser');

const properties = require('./properties')
const routes = require('./routes.js');

const app = new Koa()
const router = new Router();

router
  .get('/', routes.addMeeting)
  .get('/meetings', routes.meetings)
  .get('/users/:id/meetings', routes.meetingsByUser)
  .delete('/meetings', routes.clearMeetings )
  .post('/meetings', routes.addMeeting)
  .put('/meetings', routes.updateMeeting)
  .get('/users', routes.users)
  .post('/users', routes.addUser)
  .delete('/users', routes.clearUsers)

app
  .use(logger())
  .use(bodyParser())
  .use(router.routes())
  .use(router.allowedMethods());


app.listen(properties.port, () => {
  console.log('listening on ' + properties.port)
})

// MongoClient.connect(properties.mongoUrl, (err, client) => {
//   if (err) return console.log(err)
//   db = client.db(properties.mongoDbName) // whatever your database name is

//   app.use(ctx => {
//     db.collection('meetings').save({ XD: "XD" }, (err, result) => {
//       if (err) return console.log(err)

//       console.log(result)
//       ctx.body = "OK"
//     })
//   });



//   app.listen(properties.port, () => {
//     console.log('listening on ' + properties.port)
//   })

//   db.collection('meetings').find().toArray((err, res) => {
//     console.log(res)
//   })
// })