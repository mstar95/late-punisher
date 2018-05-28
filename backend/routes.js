const db = require('monk')('localhost/late-punisher')

const meetings = db.get('meetings')
const users = db.get('users')

exports.meetings = async ctx => {
    ctx.body = await meetings.find({})
    console.log(ctx.body)
}

exports.addMeeting = async ctx => {
    const meeting = ctx.request.body
    ctx.body = await meetings.insert(meeting)
}

exports.updateMeeting = async ctx => {
    const meeting = ctx.request.body
    ctx.body = await meetings.update({ _id: meeting._id }, meeting)
}

exports.clearMeetings = async ctx => {
    meetings.remove({})
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
    const result = await users.find({ id: parseInt(user.id) })
    console.log(user, result)

    if (result.length == 0) {
        ctx.body = await users.insert(user)
    } else {
        ctx.body = result[0]
    }
}

exports.users = async ctx => {
    ctx.body = await users.find({})
}
