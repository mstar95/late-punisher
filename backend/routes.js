const db = require('monk')('localhost/late-punisher')

const meetings = db.get('meetings')
const meetings = db.get('users')

exports.meetings = async ctx => {
    ctx.body = await meetings.find({})
}

exports.addMeeting = async ctx => {
    const meeting = ctx.request.body
    ctx.body = await meetings.insert(meeting)
}

exports.clearMeetings = async ctx => {
    await meetings.remove({})
}

exports.meetingsByUser = async ctx => {
    const id = parseInt(ctx.params.id)
    const conditions = {
        $or: [
            { organizerId: id },
            { users: id }
        ]
    }
    ctx.body = await meetings.find(conditions)
}

exports.clearUsers = async ctx => {
    await users.remove({})
}

exports.addUser = async ctx => {
    const user = ctx.request.body
    const users = await users.find({id: user.id})
    if (users.length == 0) {
        users.insert(users)
    }
}

exports.users = async ctx => {
    ctx.body = await users.find({})
}
