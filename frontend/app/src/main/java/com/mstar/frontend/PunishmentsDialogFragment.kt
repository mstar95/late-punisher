package com.mstar.frontend

import android.content.DialogInterface
import android.app.AlertDialog
import android.app.Dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import com.mstar.frontend.services.MeetingService
import com.mstar.frontend.services.UserService
import android.text.InputType
import android.widget.EditText
import com.mstar.frontend.domain.Punishment


class PunishmentsDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        val id = arguments.get("_id")
        val meeting = MeetingService.meetings.first { it._id == id }
        Log.i("items", meeting.punishment.toString())
        builder.setItems(meeting.punishment.map { getUserName(it.userId) + " " + it.text }.toTypedArray(),
                { _, _ -> })

        val input = EditText(activity)

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        var m_Text = "XD"
        // Set up the buttons
        builder.setPositiveButton("OK") { dialog,
                                          which ->
            m_Text = input.text.toString()
            // if (!meeting.punishment.map { it.userId }.contains(UserService.userId)) {
            if (m_Text != "") {
                meeting.punishment.add(Punishment(UserService.userId, m_Text))
                MeetingService.updateMeeting(meeting)
            }
        }
    builder.setNegativeButton("Cancel")
    { dialog, _ -> dialog.cancel() }
    return builder.create()
}

private fun getUserName(id: String): String {
    return UserService.users.first { it.id == id }.name
}
}