package com.mstar.frontend

import android.R
import android.content.DialogInterface
import android.R.string.cancel
import android.app.AlertDialog
import android.app.Dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.ArrayAdapter
import com.mstar.frontend.services.MeetingService
import com.mstar.frontend.services.UserService
import android.text.InputType
import android.support.v4.widget.SearchViewCompat.setInputType
import android.widget.EditText
import com.mstar.frontend.domain.Punishment


class PunishmentsDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        val id = arguments.getInt("id")
        val meeting = MeetingService.meetings.first { it.id == id }
        Log.i("items", meeting.punishment.toString())
        builder.setItems(meeting.punishment.map { getUserName(it.userId) + " " + it.text }.toTypedArray(),
                DialogInterface.OnClickListener { dialog, id -> })

        val input = EditText(activity)

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        var m_Text = "XD"
        // Set up the buttons
        builder.setPositiveButton("OK") { dialog,
                                          which -> m_Text = input.text.toString()
        meeting.punishment.add(Punishment(0,m_Text))}
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        return builder.create()
    }

    fun getUserName(id: Int) : String {
        return UserService.users.first { it.id == id }.name
    }
}